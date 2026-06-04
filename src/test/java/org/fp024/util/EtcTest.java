package org.fp024.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/** 여러가지 테스트 모음 */
@Slf4j
class EtcTest {
  /** TimeUnit으로 7일에 대한 초(Second) 계산을 코드로서 처리할 수 있다. */
  @Test
  void testTimeUnit() {
    int seconds = (int) TimeUnit.DAYS.toSeconds(7);
    assertThat(seconds, is(60 * 60 * 24 * 7));
  }
}
