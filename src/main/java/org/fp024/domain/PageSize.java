package org.fp024.domain;

import lombok.Getter;

/**
 * 한 페이지의 크기를 정의하는 타입 <br>
 * 한 페이지의 크기를 외부로부터 그대로 입력받게하지 말고 범위를 정해주자!<br>
 */
public enum PageSize {
  SIZE_10(10),
  SIZE_20(20),
  SIZE_50(50);

  @Getter private final int size;

  public static final PageSize DEFAULT_PAGE_SIZE = SIZE_10;

  private static final PageSize[] VALUES = PageSize.values();

  PageSize(int size) {
    this.size = size;
  }

  /**
   * amount 값을 알맞은 PageSize 타입으로 변경하여 반환한다.<br>
   * PageSize 타입에서 허용한 페이지 단위만 사용가능하다. <br>
   * 허용되지 않는 사이즈가 들어올경우 기본 10 페이지 단위로 사용하게된다.
   */
  public static PageSize valueOfAmount(int amount) {
    for (PageSize pageSize : VALUES) {
      if (pageSize.getSize() == amount) {
        return pageSize;
      }
    }
    return DEFAULT_PAGE_SIZE;
  }
}
