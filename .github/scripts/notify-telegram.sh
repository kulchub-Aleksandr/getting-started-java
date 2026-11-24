#!/usr/bin/env bash
set -uo pipefail

notice() { printf '::notice::%s\n' "$1"; }

if [ -z "${TELEGRAM_BOT_TOKEN:-}" ] || [ -z "${TELEGRAM_CHAT_ID:-}" ]; then
  notice "Telegram credentials are not configured, notification skipped"
  exit 0
fi

STATUS="${STATUS:-failure}"
BRANCH="${BRANCH:-unknown}"
COMMIT_SHA="${COMMIT_SHA:-unknown}"
COMMIT_NAME="${COMMIT_NAME:-}"
RUN_URL="${RUN_URL:-}"
REPORT_URL="${REPORT_URL:-}"
ARTIFACTS_URL="${ARTIFACTS_URL:-}"
REPORT_PUBLISHED="${REPORT_PUBLISHED:-false}"
RESULTS_DIR="${RESULTS_DIR:-build/test-results/test}"
MAX_TESTS="${MAX_TESTS:-10}"
DRY_RUN="${DRY_RUN:-0}"

case "$STATUS" in
  cancelled)
    notice "Run was cancelled, notification skipped"
    exit 0
    ;;
  success) headline="UI tests: SUCCESS" ;;
  failure) headline="UI tests: FAILURE" ;;
  *) headline="UI tests: ${STATUS^^}" ;;
esac

short_sha="${COMMIT_SHA:0:7}"

xml_unescape() {
  sed -e 's/&lt;/</g' -e 's/&gt;/>/g' -e 's/&quot;/"/g' -e "s/&apos;/'/g" -e 's/&amp;/\&/g'
}

html_escape() {
  sed -e 's/&/\&amp;/g' -e 's/</\&lt;/g' -e 's/>/\&gt;/g'
}

commit_name="${COMMIT_NAME%%$'\n'*}"
commit_name="${commit_name#"${commit_name%%[![:space:]]*}"}"
commit_name="${commit_name%"${commit_name##*[![:space:]]}"}"
if [ "${#commit_name}" -gt 120 ]; then
  commit_name="${commit_name:0:117}..."
fi
if [ -n "$commit_name" ]; then
  commit_line="Commit: $(printf '%s' "$commit_name" | html_escape)"
else
  commit_line="Commit: <code>${short_sha}</code>"
fi

total_tests=0
failed_tests=()

if [ -d "$RESULTS_DIR" ]; then
  for file in "$RESULTS_DIR"/*.xml; do
    [ -f "$file" ] || continue
    class=""
    name=""
    while IFS= read -r line || [ -n "$line" ]; do
      if [[ $line =~ \<testcase[[:space:]] ]]; then
        total_tests=$((total_tests + 1))
        class=""
        name=""
        [[ $line =~ (^|[[:space:]])classname=\"([^\"]*)\" ]] && class="${BASH_REMATCH[2]}"
        [[ $line =~ (^|[[:space:]])name=\"([^\"]*)\" ]] && name="${BASH_REMATCH[2]}"
      fi
      if [[ $line =~ \<(failure|error)([[:space:]\>]) ]] && [ -n "$name" ]; then
        failed_tests+=("${class}|${name}")
        name=""
      fi
    done < "$file"
  done
fi

failed_count=${#failed_tests[@]}
failed_lines=""
shown=0

if [ "$failed_count" -gt 0 ]; then
  for entry in "${failed_tests[@]}"; do
    [ "$shown" -lt "$MAX_TESTS" ] || break
    cls="${entry%%|*}"
    nm="${entry#*|}"
    if [ "${#nm}" -gt 160 ]; then
      nm="${nm:0:157}..."
    fi
    cls=$(printf '%s' "$cls" | xml_unescape | html_escape)
    nm=$(printf '%s' "$nm" | xml_unescape | html_escape)
    short_cls="${cls##*.}"
    shown=$((shown + 1))
    if [ -n "$short_cls" ]; then
      failed_lines+="${shown}. <b>${short_cls}</b> — ${nm}"$'\n'
    else
      failed_lines+="${shown}. ${nm}"$'\n'
    fi
  done
  if [ "$failed_count" -gt "$shown" ]; then
    failed_lines+="… and $((failed_count - shown)) more"$'\n'
  fi
fi

message="<b>${headline}</b>"$'\n\n'
message+="Branch: <code>$(printf '%s' "$BRANCH" | html_escape)</code>"$'\n'
message+="${commit_line}"$'\n'
if [ -n "$RUN_URL" ]; then
  message+="Run: <a href=\"${RUN_URL}\">run details</a>"$'\n'
fi
if [ "$REPORT_PUBLISHED" = "true" ] && [ -n "$REPORT_URL" ]; then
  message+="Allure report: <a href=\"${REPORT_URL}\">open report</a>"$'\n'
elif [ -n "$ARTIFACTS_URL" ]; then
  message+="Allure report: not published, <a href=\"${ARTIFACTS_URL}\">run artifacts</a>"$'\n'
else
  message+="Allure report: not published"$'\n'
fi
if [ "$total_tests" -gt 0 ]; then
  message+="Tests: ${failed_count} failed of ${total_tests}"$'\n'
else
  message+="Tests: ${failed_count} failed"$'\n'
fi
if [ -n "$failed_lines" ]; then
  message+=$'\n'"${failed_lines}"
fi

if [ "$DRY_RUN" = "1" ]; then
  printf '%s\n' "$message"
  exit 0
fi

body_file=$(mktemp)
error_file=$(mktemp)
trap 'rm -f "$body_file" "$error_file"' EXIT

api_url="https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage"
attempt=1
max_attempts=3
http_code="000"

while [ "$attempt" -le "$max_attempts" ]; do
  http_code=$(curl --silent --show-error --output "$body_file" --write-out '%{http_code}' \
    --max-time 20 \
    --data-urlencode "chat_id=${TELEGRAM_CHAT_ID}" \
    --data-urlencode "text=${message}" \
    --data-urlencode "parse_mode=HTML" \
    --data-urlencode "disable_web_page_preview=true" \
    "$api_url" 2>"$error_file") || http_code="000"

  if [ "$http_code" = "200" ]; then
    notice "Telegram notification sent (${STATUS})"
    exit 0
  fi

  attempt=$((attempt + 1))
  if [ "$attempt" -le "$max_attempts" ]; then
    sleep $((attempt * 2))
  fi
done

reason=$(grep -o '"description":"[^"]*"' "$body_file" 2>/dev/null | head -n 1 | sed -e 's/.*"description":"//' -e 's/"$//')
if [ -z "$reason" ]; then
  reason=$(head -c 200 "$error_file" | tr '\n' ' ')
fi
if [ -z "$reason" ]; then
  reason="no response"
fi
printf '::warning::Telegram notification failed after %s attempts (HTTP %s): %s\n' "$max_attempts" "$http_code" "$reason"
exit 0
