package org.fp024.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.time.LocalDateTime;

/**
 * Gson도 ObjectMapper처럼 싱글톤으로 써보자!<br>
 * Gson instances are Thread-safe so you can reuse them freely across multiple threads.
 */
public class GsonHelper {
  public static Gson gson() {
    return GsonHolder.INSTANCE;
  }

  /**
   * Gson 메시지 컨버터도 Holder 패턴을 사용해서 싱글톤으로 생성되도록 했다.
   *
   * @return Gson 메시지 컨버터
   */
  public static GsonHttpMessageConverter gsonHttpMessageConverter() {
    return GsonHttpMessageConverterHolder.INSTANCE;
  }

  private static class GsonHolder {
    private static final Gson INSTANCE =
        new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonSerializer())
            .create();
  }

  /**
   * 특정 객체의 JSON 문자열을 반환
   *
   * @param object 객체
   * @return JSON 문자열
   */
  public static String toJson(Object object) {
    return GsonHolder.INSTANCE.toJson(object);
  }

  private static class GsonHttpMessageConverterHolder {
    private static final GsonHttpMessageConverter INSTANCE = new GsonHttpMessageConverter(gson());
  }
}
