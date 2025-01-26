package ru.sberSchool.tasks.task14.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.sberSchool.tasks.Constants;
import ru.sberSchool.tasks.task14.service.DataSource;
import ru.sberSchool.tasks.utils.PropertiesConfigUtil;

import java.sql.*;

/**
 * Implementation of the {@link DataSource} interface that uses PostgreSQL as the underlying data store.
 * This class provides methods for initializing the database, saving key-value pairs to a cache table,
 * and retrieving data associated with a key.
 *
 * @see DataSource
 * @author Elland Ilia
 */
@Slf4j
public class PostgreSQLDataSource implements DataSource {

    @Override
    public void init() {
        try (Connection connection = DriverManager.getConnection(PropertiesConfigUtil.getProperty(Constants.DB_URL), PropertiesConfigUtil.getProperty(Constants.DB_USER), PropertiesConfigUtil.getProperty(Constants.DB_PASSWORD));
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cache (key VARCHAR(255) PRIMARY KEY, value TEXT)");
            log.debug("init[0]: Initialized PostgreSQL database.");
        } catch (SQLException e) {
            log.error("Error initializing database: {}", e.getMessage(), e);
        }
    }

    @Override
    public void save(String key, String value) {
        try (Connection connection = DriverManager.getConnection(PropertiesConfigUtil.getProperty(Constants.DB_URL), PropertiesConfigUtil.getProperty(Constants.DB_USER), PropertiesConfigUtil.getProperty(Constants.DB_PASSWORD));
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO cache (key, value) VALUES (?, ?) ON CONFLICT (key) DO UPDATE SET value = EXCLUDED.value")
        ) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
            log.debug("save[0]: Saved data to cache with key = {}", key);
        } catch (SQLException e) {
            log.error("Error saving data to database: {}", e.getMessage(), e);
        }
    }

    @Override
    public String get(String key) {
        try (Connection connection = DriverManager.getConnection(PropertiesConfigUtil.getProperty(Constants.DB_URL), PropertiesConfigUtil.getProperty(Constants.DB_USER), PropertiesConfigUtil.getProperty(Constants.DB_PASSWORD));
             PreparedStatement pstmt = connection.prepareStatement("SELECT value FROM cache WHERE key = ?")
        ) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String value = rs.getString(1);
                log.debug("get[0]: Retrieved data from cache with key = {}", key);
                return value;
            }
        } catch (SQLException e) {
            log.error("get[0]: Error retrieving data from database: {}", e.getMessage(), e);
        }
        return null;
    }
}
