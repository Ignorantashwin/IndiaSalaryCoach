package com.indiasalarycoach.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Slf4j
public class DataSourceConfig {

    /**
     * Converts Replit's DATABASE_URL (postgresql://user:pass@host/db?params)
     * to JDBC format using java.net.URI for safe parsing (handles special
     * characters in passwords, avoids manual string splitting bugs).
     */
    @Bean
    @Primary
    public DataSource dataSource(
   @Value("${DATABASE_URL}")
    String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException(
                "DATABASE_URL environment variable is not set. " );
        }
        String jdbcUrl = toJdbcUrl(databaseUrl);
        log.info("Connecting to PostgreSQL database (credentials redacted)");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

    /**
     * Converts postgresql://user:pass@host/db?params to
     * jdbc:postgresql://host/db?user=...&password=...
     * Uses java.net.URI for safe parsing — handles special chars in passwords.
     */
    private String toJdbcUrl(String url) {
        // Already in JDBC format
        if (url.startsWith("jdbc:")) return url;

        // Normalize postgres:// → postgresql://
        String normalized = url.replaceFirst("^postgres://", "postgresql://");

        try {
            URI uri = new URI(normalized);
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath(); // e.g. /mydb
            String rawQuery = uri.getRawQuery(); // existing query params

            StringBuilder jdbc = new StringBuilder("jdbc:postgresql://");
            jdbc.append(host);
            if (port > 0) jdbc.append(":").append(port);
            jdbc.append(path);

            // Parse user:password from userInfo
            String userInfo = uri.getUserInfo();
            StringBuilder params = new StringBuilder(rawQuery != null ? rawQuery : "");

            if (userInfo != null && !userInfo.isBlank()) {
                int colon = userInfo.indexOf(':');
                String user = colon >= 0 ? userInfo.substring(0, colon) : userInfo;
                String pass = colon >= 0 ? userInfo.substring(colon + 1) : null;

                if (!user.isBlank()) {
                    if (params.length() > 0) params.append('&');
                    params.append("user=").append(user);
                }
                if (pass != null && !pass.isBlank()) {
                    if (params.length() > 0) params.append('&');
                    params.append("password=").append(pass);
                }
            }

            // Local PostgreSQL does not need SSL
if (!params.toString().contains("sslmode")) {
    if (params.length() > 0) params.append('&');
    params.append("sslmode=disable");
}

            if (params.length() > 0) {
                jdbc.append('?').append(params);
            }

            return jdbc.toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(
                "Cannot parse DATABASE_URL: " + e.getMessage(), e);
        }
    }
}
