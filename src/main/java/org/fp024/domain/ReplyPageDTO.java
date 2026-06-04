package org.fp024.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.fp024.domain.generated.ReplyVO;

import java.util.List;

/** Gson의 경우 필드기반 직렬화를 하여, getPageNavigationSize()를 직렬화하지 않는다. */
@RequiredArgsConstructor
@Getter
@ToString
public class ReplyPageDTO {
  /** 댓글 페이지 네비게이터에 표시할 페이지 인덱스 수 */
  public final int pageNavigationSize = 3;

  private final int pageSize;
  private final int replyCount;
  private final List<ReplyVO> list;
}
