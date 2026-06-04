package org.fp024.persistence;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class JDBCTest {
  private static final Properties DATABASE_PROPERTIES = getDBProperties();

  public static Properties getDBProperties() {
    Properties properties = new Properties();
    try (InputStream reader = JDBCTest.class.getClassLoader().getResourceAsStream("database.properties")) {
      properties.load(reader);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return properties;
  }

  static {
    try {
      Class.forName(DATABASE_PROPERTIES.get("jdbc.driver").toString());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  @Test
  void testConnection() {
    final String url = DATABASE_PROPERTIES.get("jdbc.url").toString();
    final String user = DATABASE_PROPERTIES.get("jdbc.username").toString();
    final String password = DATABASE_PROPERTIES.get("jdbc.password").toString();
    try (Connection con = DriverManager.getConnection(url, user, password)) {
      LOGGER.info("{}", con);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
