package org.fp024.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Gson도 기본형식으로 날짜를 반환해주긴 하는데, <br>
 * ex03과 일관성을 위해, 배열 형식으로 직렬화 하게 했다.
 */
public class LocalDateTimeJsonSerializer implements JsonSerializer<LocalDateTime> {
  @Override
  public JsonElement serialize(
      LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray jsonArray = new JsonArray(6);
    jsonArray.add(src.getYear());
    jsonArray.add(src.getMonthValue());
    jsonArray.add(src.getDayOfMonth());
    jsonArray.add(src.getHour());
    jsonArray.add(src.getMinute());
    jsonArray.add(src.getSecond());
    return jsonArray;
  }
}
