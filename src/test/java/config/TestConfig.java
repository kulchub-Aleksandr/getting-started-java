package config;

import java.util.Optional;

public final class TestConfig {
    private static final String DEFAULT_BASE_URL = "https://github.com";
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_HEADLESS = "false";
    private static final String DEFAULT_BROWSER_SIZE = "1920x1080";
    private static final long DEFAULT_TIMEOUT_MILLIS = 10_000L;

    private TestConfig() {
    }

    public static String baseUrl() {
        return resolve("baseUrl", "selenide.baseUrl", "BASE_URL", DEFAULT_BASE_URL);
    }

    public static String browser() {
        return resolve("browser", "selenide.browser", "BROWSER", DEFAULT_BROWSER);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(resolve("headless", "selenide.headless", "HEADLESS", DEFAULT_HEADLESS));
    }

    public static String browserSize() {
        return resolve("browserSize", "selenide.browserSize", "BROWSER_SIZE", DEFAULT_BROWSER_SIZE);
    }

    public static long timeoutMillis() {
        String timeout = System.getProperty("timeout");
        if (timeout != null && !timeout.isBlank()) {
            return parseDuration(timeout.trim());
        }

        String selenideTimeout = System.getProperty("selenide.timeout");
        if (selenideTimeout != null && !selenideTimeout.isBlank()) {
            return Long.parseLong(selenideTimeout.trim());
        }

        String envTimeout = System.getenv("TIMEOUT");
        if (envTimeout != null && !envTimeout.isBlank()) {
            return parseDuration(envTimeout.trim());
        }

        return DEFAULT_TIMEOUT_MILLIS;
    }

    public static Optional<String> remote() {
        return Optional.ofNullable(resolve("remote", "selenide.remote", "REMOTE", null));
    }

    private static String resolve(String key, String selenideKey, String envName, String defaultValue) {
        String value = firstNonBlank(
                System.getProperty(key),
                selenideKey == null ? null : System.getProperty(selenideKey),
                System.getenv(envName));
        return value == null ? defaultValue : value;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private static long parseDuration(String value) {
        String duration = value.toLowerCase();
        if (duration.endsWith("ms")) {
            return Long.parseLong(duration.substring(0, duration.length() - 2).trim());
        }
        if (duration.endsWith("s")) {
            return Long.parseLong(duration.substring(0, duration.length() - 1).trim()) * 1000;
        }
        return Long.parseLong(duration) * 1000;
    }
}
