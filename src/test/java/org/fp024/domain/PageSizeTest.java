package org.fp024.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PageSizeTest {
  @Test
  void testValueOfAmountAllowed() {
    assertEquals(PageSize.SIZE_10, PageSize.valueOfAmount(10));
    assertEquals(PageSize.SIZE_20, PageSize.valueOfAmount(20));
    assertEquals(PageSize.SIZE_50, PageSize.valueOfAmount(50));
  }

  @Test
  void testValueOfAmountNotAllowed() {
    assertEquals(PageSize.SIZE_10, PageSize.valueOfAmount(15));
    assertEquals(PageSize.SIZE_10, PageSize.valueOfAmount(25));
    assertEquals(PageSize.SIZE_10, PageSize.valueOfAmount(55));
  }
}
