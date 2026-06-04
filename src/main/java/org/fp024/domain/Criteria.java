package org.fp024.domain;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.type.Alias;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Alias("criteria")
public class Criteria {
  @Getter private long pageNum;
  private PageSize pageSize;

  @Getter private final Set<SearchType> searchTypeSet = EnumSet.noneOf(SearchType.class);

  @Getter @Setter private String keyword;

  public Criteria() {
    this(1, PageSize.SIZE_10);
  }

  public Criteria(long pageNum) {
    this(pageNum, PageSize.SIZE_10);
  }

  public Criteria(long pageNum, PageSize pageSize) {
    this.pageNum = pageNum;
    this.pageSize = pageSize;
  }

  public void setPageNum(long pageNum) {
    if (pageNum < 1) {
      pageNum = 1;
    }
    this.pageNum = pageNum;
  }

  public void setAmount(int amount) {
    this.pageSize = PageSize.valueOfAmount(amount);
  }

  public int getAmount() {
    return pageSize.getSize();
  }

  public void setSearchCodes(List<String> codes) {
    codes.forEach(c -> searchTypeSet.add(SearchType.searchTypeOf(c)));
  }

  public List<String> getSearchCodes() {
    return searchTypeSet.stream().map(SearchType::getCode).collect(Collectors.toList());
  }

  /**
   * 체크박스를 원본 그대로 쓰면, searchCode=T&searchCode=C&searchCode=W 이런식으로 URL이 만들어진다. <br>
   * 그래서 searchCodes=T,C,W 로 처리될 수 있도록 URL에는 이런 값이 들어가도록 뷰에서 처리를 했다.
   */
  public String getSearchCodesWithJoined() {
    return searchTypeSet.stream().map(SearchType::getCode).collect(Collectors.joining(","));
  }

  public String getLink() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromPath("")
            .queryParam("pageNum", this.pageNum)
            .queryParam("amount", this.getAmount())
            .queryParam("searchCodes", getSearchCodesWithJoined())
            .queryParam("keyword", this.keyword);

    return builder.toUriString();
  }
}
