package org.fp024.domain;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Arrays;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

class CriteriaTest {
  @Test
  void testCodes() {
    Criteria c = new Criteria();
    c.setSearchCodes(Arrays.asList("T"));
    assertIterableEquals(EnumSet.of(SearchType.TITLE), c.getSearchTypeSet());

    c = new Criteria();
    c.setSearchCodes(Arrays.asList("T", "C"));
    assertIterableEquals(EnumSet.of(SearchType.TITLE, SearchType.CONTENT), c.getSearchTypeSet());

    c = new Criteria();
    c.setSearchCodes(Arrays.asList("T", "C", "W"));
    assertIterableEquals(
        EnumSet.of(SearchType.TITLE, SearchType.CONTENT, SearchType.WRITER), c.getSearchTypeSet());
  }
}
