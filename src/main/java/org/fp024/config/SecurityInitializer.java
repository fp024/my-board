package org.fp024.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import java.nio.charset.StandardCharsets;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
  // JSP 예외 페이지에서 JSP의 내용중 한글이 깨져보여서 넣어보긴했는데 결과는 같았다.
  // 그런데, 예외 스택 추적 로그 상으로 봤을 때.. WebConfig에 선언했을 때보다 Spring Security 필더보다 먼저 선언되는 것이 보였다.
  // 일단 이렇게 써보자!
  //
  // 참조: 2번째 답변이 제일나은 것 같다.
  // https://stackoverflow.com/questions/20863489/characterencodingfilter-dont-work-together-with-spring-security-3-2-0
  @Override
  protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
    FilterRegistration.Dynamic encodingFilter =
        servletContext.addFilter(
            "encodingFilter", new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
    encodingFilter.addMappingForUrlPatterns(
        null, false, "/*"); // 첫번째 인자 dispatcherTypes를 null로 두면 REQUEST로 인식 한다고 함.
  }
}
