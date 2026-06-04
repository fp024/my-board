package org.fp024.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import java.io.File;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@Slf4j
class CommonUtilTest {
  @Test
  void testGetFolder() {
    // Mock 적용 외부에서 미리 기대 날짜를 만들기.
    LocalDateTime currentDateTime = LocalDateTime.of(2022, 4, 20, 0, 0, 0, 0);

    try (MockedStatic<LocalDateTime> mockedLocalDateTime =
        mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
      mockedLocalDateTime.when(LocalDateTime::now).thenReturn(currentDateTime);

      assertEquals("2022" + File.separator + "04" + File.separator + "20", CommonUtil.getFolder());
    }
  }

  @Test
  void testGetUUID() {
    // IntelliJ에서 JUnit으로 실행할 때, ${project.basedir}/.mvn/jvm.config 의 설정을 사용하는 것 같지는 않다.
    IntStream.rangeClosed(1, 10).forEach(i -> LOGGER.info("UUID: {}", CommonUtil.getUUID()));
  }
}
