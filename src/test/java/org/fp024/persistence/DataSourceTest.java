package org.fp024.persistence;

import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fp024.config.RootConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {RootConfig.class})
@Slf4j
class DataSourceTest {
  @Autowired private DataSource dataSource;

  @Autowired private SqlSessionFactory sqlSessionFactory;

  @Test
  void testConnection() {
    // oracle.jdbc.driver.OracleDriver 을 사용할 때의 경고 로그
    // WARN : com.zaxxer.hikari.util.DriverDataSource -
    // Registered driver with driverClassName=oracle.jdbc.driver.OracleDriver was
    // not found,
    // trying direct instantiation.

    // Oracle 9 이후로...
    // oracle.jdbc.driver.OracleDriver 대신에 oracle.jdbc.OracleDriver를 사용해야한다고 함.
    try (Connection con = dataSource.getConnection()) {
      LOGGER.info("{}", con);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testMyBatis() {
    try (SqlSession session = sqlSessionFactory.openSession();
        Connection con = session.getConnection(); ) {
      LOGGER.info("{}", session);
      LOGGER.info("{}", con);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
