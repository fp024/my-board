package org.fp024.config;

import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.fp024.security.CustomUserDetailsService;
import org.fp024.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final DataSource dataSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(
        formLoginConfigurer ->
            formLoginConfigurer
                .loginPage("/customLogin")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/board/list"));

    http.logout(
        logoutConfigurer ->
            logoutConfigurer
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .logoutSuccessUrl("/board/list"));

    http.rememberMe(
        rememberMeConfigurer ->
            rememberMeConfigurer
                .key("fp024")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7)));

    return http.build();
  }

  @Bean
  public UserDetailsService customUserDetailsService(MemberService memberService) {
    return new CustomUserDetailsService(memberService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
    // 💡 Spring JDBC에서는 JdbcDaoSupport를 정리할 예정이였는데,
    //     Spring Security의 JdbcTokenRepositoryImpl가 여전히 사용해서
    //     아직 즉시 지워지진 않은 것 같다.
    jdbcTokenRepository.setDataSource(dataSource);
    return jdbcTokenRepository;
  }
}
