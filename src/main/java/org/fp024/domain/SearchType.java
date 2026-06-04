package org.fp024.domain;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.content;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.title;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.writer;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import lombok.Getter;
import org.mybatis.dynamic.sql.SqlColumn;

/** 검색 타입 */
public enum SearchType {
  TITLE("T", title, "제목"),
  CONTENT("C", content, "내용"),
  WRITER("W", writer, "작성자");

  @Getter private final String code;
  @Getter private final SqlColumn<String> column;
  @Getter private final String description;

  private static final SearchType DEFAULT_SEARCH_TYPE = TITLE;

  SearchType(String code, SqlColumn<String> column, String description) {
    this.code = code;
    this.column = column;
    this.description = description;
  }

  public static Set<SearchType> allSearchTypeSet() {
    return EnumSet.allOf(SearchType.class);
  }

  public static SearchType searchTypeOf(String code) {
    return Arrays.stream(SearchType.values())
        .filter(s -> s.getCode().equals(code))
        .findAny()
        .orElse(DEFAULT_SEARCH_TYPE);
  }

  public static Set<SearchType> searchTypeSetOf(String... codes) {
    EnumSet<SearchType> searchTypeSet = EnumSet.noneOf(SearchType.class);
    Arrays.stream(codes).forEach(code -> searchTypeSet.add(searchTypeOf(code)));
    return searchTypeSet;
  }

  public String getName() {
    return name();
  }
}
