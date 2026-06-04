package org.fp024.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"org.fp024.service", "org.fp024.task"})
@MapperScan(basePackages = {"org.fp024.mapper"})
@PropertySource({"classpath:database.properties"})
@EnableScheduling
@EnableTransactionManagement
public class RootConfig {

  @Bean(destroyMethod = "close")
  public HikariDataSource dataSource(
      @Value("${jdbc.driver}") String driverClassName,
      @Value("${jdbc.url}") String url,
      @Value("${jdbc.username}") String userName,
      @Value("${jdbc.password}") String password) {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(driverClassName);
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setUsername(userName);
    hikariConfig.setPassword(password);

    return new HikariDataSource(hikariConfig);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    sqlSessionFactoryBean.setTypeAliasesPackage("org.fp024.domain.generated,org.fp024.domain");
    return sqlSessionFactoryBean.getObject();
  }

  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
