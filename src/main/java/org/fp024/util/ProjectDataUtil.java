package org.fp024.util;

import java.io.IOException;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Project Data 프러퍼티 관리 유틸리티
 *
 * <p>원래는 mybatis에서 가져온 Resource를 사용했었지만, <br>
 * Spring-core에 존재하는 PropertiesLoaderUtils.loadProperties()를 사용해서 동일하게 구현가능하므로 <br>
 * 이제는 그것을 사용하자!
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectDataUtil {
  /** 프로퍼티 파일은 한번만 로드 */
  private static final Properties PROPERTIES = ProjectDataUtilsHolder.PROPERTIES;

  /** Properties 를 외부로 공개하지 말고, 키로 값을 가져올 수 있는 getProperty 메서드만 추가해주자! */
  public static String getProperty(String key) {
    return PROPERTIES.getProperty(key);
  }

  public static int getIntProperty(String key) {
    return Integer.parseInt(getProperty(key));
  }

  public static long getLongProperty(String key) {
    return Long.parseLong(getProperty(key));
  }

  private static class ProjectDataUtilsHolder {
    private static final String PROPERTIES_FILENAME = "project-data.properties";

    private static final Properties PROPERTIES;

    static {
      try {
        PROPERTIES =
            PropertiesLoaderUtils.loadProperties(new ClassPathResource(PROPERTIES_FILENAME));
        LOGGER.info("### {} 프로퍼티 파일 로드 성공!!! ^^", PROPERTIES_FILENAME);
      } catch (IOException e) {
        throw new IllegalStateException("### " + PROPERTIES_FILENAME + " 프로퍼티 파일 로드 실패 ㅠㅠ", e);
      }
    }
  }
}
