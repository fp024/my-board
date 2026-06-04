package org.fp024.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.generated.ReplyVO;
import org.junit.jupiter.api.Test;

@Slf4j
class GsonHelperTest {

  @Test
  void test() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonSerializer())
            .create();

    assertThat(
        gson.toJson(LocalDateTime.of(2022, 1, 2, 3, 4, 5, 0)), //
        is("[2022,1,2,3,4,5]"));

    ReplyVO vo = new ReplyVO();

    vo.setReplyDate(LocalDateTime.of(2022, 1, 2, 3, 4, 5, 0));

    LOGGER.info(gson.toJson(vo));
  }
}
