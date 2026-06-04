package org.fp024.servlet.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class JDBCDriverCleaner implements ServletContextListener {
  protected void deregisterJdbcDrivers(ServletContext servletContext) {
    ClassLoader contextClassLoader = servletContext.getClassLoader();
    ClassLoader parentClassLoader =
        contextClassLoader != null ? contextClassLoader.getParent() : null;
    LOGGER.debug(
        "### 현재 서블릿 컨텍스트의 클래스 로더: {}",
        contextClassLoader != null
            ? contextClassLoader.getClass().getName()
            : "Bootstrap ClassLoader");
    LOGGER.debug(
        "### 현재 서블릿 컨텍스트의 부모 클래스 로더: {}",
        parentClassLoader != null
            ? parentClassLoader.getClass().getName()
            : "Bootstrap ClassLoader");

    Collections.list(DriverManager.getDrivers())
        .forEach(
            driver -> {
              ClassLoader driverClassLoader = driver.getClass().getClassLoader();
              LOGGER.debug(
                  "### {} 드라이버의 클래스 로더: {}",
                  driver.getClass().getName(),
                  driverClassLoader != null
                      ? driverClassLoader.getClass().getName()
                      : "Bootstrap ClassLoader");

              if (driverClassLoader == contextClassLoader) {
                try {
                  DriverManager.deregisterDriver(driver);
                  LOGGER.info("### {} 드라이버 등록 해제", driver.getClass().getName());
                } catch (SQLException ex) {
                  LOGGER.error("### {} 드라이버 등록 해제 실패", driver.getClass().getName(), ex);
                }
              } else {
                LOGGER.warn(
                    "### {} 드라이버 skip (웹앱/서블릿 컨텍스트 클래스 로더가 로드한 드라이버가 아님)",
                    driver.getClass().getName());
              }
            });
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    LOGGER.trace("contextDestroyed() 실행...");
    deregisterJdbcDrivers(event.getServletContext());
  }
}
