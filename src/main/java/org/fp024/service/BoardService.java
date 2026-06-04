package org.fp024.service;

import java.util.List;
import org.fp024.domain.BoardDTO;
import org.fp024.domain.Criteria;
import org.fp024.domain.generated.BoardAttachVO;
import org.fp024.domain.generated.BoardVO;

public interface BoardService {
  void register(BoardDTO board);

  BoardVO get(Long bno);

  boolean modify(BoardDTO board);

  boolean remove(Long bno);

  List<BoardVO> getList(Criteria criteria);

  long getTotal(Criteria criteria);

  void updateReplyCount(Long bno, int amount);

  List<BoardAttachVO> getAttachList(Long bno);
}
