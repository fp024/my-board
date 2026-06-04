package org.fp024.domain;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

/** 필드들을 불변으로 만들어도 괜찮을 것 같아서 약간 수정했다. */
@Getter
@ToString
public class PageDTO {
  /** 한 페이지의 게시물 노출 수 */
  public static final int PAGE_SIZE = 10;
  /** 페이지 네비게이터에 표시할 페이지 인덱스 수 */
  public static final int PAGE_NAVIGATION_SIZE = 10;

  private final long pageNum;
  private final long startPage;
  private final long endPage;
  private final boolean prev;
  private final boolean next;
  private final long total;
  private final int amount;
  private final String keyword;
  private final List<String> searchCodes;
  private final String searchCodesWithJoined;

  public PageDTO(Criteria criteria, long total) {
    this.pageNum = criteria.getPageNum();
    this.total = total;
    this.amount = criteria.getAmount();
    this.keyword = criteria.getKeyword();
    this.searchCodes = criteria.getSearchCodes();
    this.searchCodesWithJoined = criteria.getSearchCodesWithJoined();

    long endPageNum =
        (long) Math.ceil(((double) pageNum) / PAGE_NAVIGATION_SIZE) * PAGE_NAVIGATION_SIZE;
    this.startPage = endPageNum - (PAGE_NAVIGATION_SIZE - 1);

    long realEnd = (long) (Math.ceil(((double) total) / amount));

    if (realEnd < endPageNum) {
      this.endPage = realEnd;
    } else {
      this.endPage = endPageNum;
    }

    this.prev = this.startPage > 1;

    this.next = endPageNum < realEnd;
  }
}
