package org.fp024.service;

import org.fp024.domain.Criteria;
import org.fp024.domain.ReplyPageDTO;
import org.fp024.domain.generated.ReplyVO;

public interface ReplyService {
  void register(ReplyVO vo);

  ReplyVO get(Long rno);

  int modify(ReplyVO vo);

  int remove(Long rno);

  ReplyPageDTO getListPage(Criteria cri, Long bno);
}
