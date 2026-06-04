package org.fp024.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SearchTypeTest {
  @Test
  void testAllSearchTypeSet() {
    Set<SearchType> allSearchTypeSet = SearchType.allSearchTypeSet();
    assertEquals(
        EnumSet.of(SearchType.TITLE, SearchType.CONTENT, SearchType.WRITER), allSearchTypeSet);
  }

  @Test
  void testSearchTypeOf() {
    assertSame(SearchType.TITLE, SearchType.searchTypeOf("T"));
    assertSame(SearchType.CONTENT, SearchType.searchTypeOf("C"));
    assertSame(SearchType.WRITER, SearchType.searchTypeOf("W"));

    assertSame(SearchType.TITLE, SearchType.searchTypeOf("없는_코드"));
  }

  @Test
  void testSearchTypeSetOf() {
    assertEquals(
        EnumSet.of(SearchType.TITLE, SearchType.CONTENT), SearchType.searchTypeSetOf("T", "C"));
    assertEquals(
        EnumSet.of(SearchType.TITLE, SearchType.WRITER), SearchType.searchTypeSetOf("T", "W"));
    assertEquals(
        EnumSet.of(SearchType.CONTENT, SearchType.WRITER), SearchType.searchTypeSetOf("C", "W"));
    assertEquals(
        EnumSet.of(SearchType.TITLE, SearchType.CONTENT, SearchType.WRITER),
        SearchType.searchTypeSetOf("T", "C", "W"));
  }
}
