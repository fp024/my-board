package org.fp024.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PageDTOTest {
  @Test
  void testPage1OfTotalPage11() {
    long pageNum = 1;
    long total = PageDTO.PAGE_SIZE * 11;

    Criteria criteria = new Criteria(pageNum);
    PageDTO p = new PageDTO(criteria, total);

    // 현재 페이지 기준으로 페이지 네비게이터에 보이는 마지막 페이지
    assertEquals(10, p.getEndPage());
    // 현재 페이지 기준으로 페이지 네비게이터에 보이는 시작 페이지
    assertEquals(1, p.getStartPage());
    assertFalse(p.isPrev());
    assertTrue(p.isNext());
  }

  @Test
  void testPage11OfTotalPage20() {
    long pageNum = 11;
    long total = PageDTO.PAGE_SIZE * 20;

    Criteria criteria = new Criteria(pageNum);
    PageDTO p = new PageDTO(criteria, total);

    assertEquals(20, p.getEndPage());
    assertEquals(11, p.getStartPage());
    assertTrue(p.isPrev());
    assertFalse(p.isNext());
  }

  @Test
  void testPage30OfTotalPage30() {
    long pageNum = 30;
    long total = PageDTO.PAGE_SIZE * 30;

    Criteria criteria = new Criteria(pageNum);
    PageDTO p = new PageDTO(criteria, total);

    assertEquals(30, p.getEndPage());
    assertEquals(21, p.getStartPage());
    assertTrue(p.isPrev());
    assertFalse(p.isNext());
  }

  @Test
  void testPage5fTotalPage10() {
    long pageNum = 5;
    long total = PageDTO.PAGE_SIZE * 10;

    Criteria criteria = new Criteria(pageNum);
    PageDTO p = new PageDTO(criteria, total);

    assertEquals(10, p.getEndPage());
    assertEquals(1, p.getStartPage());
    assertFalse(p.isPrev());
    assertFalse(p.isNext());
  }
}
