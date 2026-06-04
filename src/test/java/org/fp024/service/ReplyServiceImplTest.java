package org.fp024.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.fp024.config.RootConfig;
import org.fp024.domain.Criteria;
import org.fp024.domain.PageSize;
import org.fp024.domain.ReplyPageDTO;
import org.fp024.domain.generated.BoardVO;
import org.fp024.domain.generated.ReplyVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(classes = {RootConfig.class})
@Slf4j
class ReplyServiceImplTest {

  @Autowired private ReplyService replyService;

  @Autowired private BoardService boardService;

  List<Long> latestBoardNoList() {
    Criteria criteria = new Criteria();
    criteria.setAmount(5);
    criteria.setPageNum(1L);
    List<BoardVO> boardList = boardService.getList(criteria);
    return boardList.stream().map(BoardVO::getBno).collect(Collectors.toList());
  }

  @Transactional
  @Test
  void testCreate() {
    List<Long> latestBnoList = latestBoardNoList();
    LOGGER.info("latestBnoList: {}", latestBnoList);

    IntStream.rangeClosed(1, 10)
        .forEach(
            i -> {
              ReplyVO vo = new ReplyVO();

              // 게시물의 번호
              vo.setBno(latestBnoList.get(i % latestBnoList.size()));
              vo.setReply("댓글 테스트 " + i);
              vo.setReplyer("replayer" + i);

              replyService.register(vo);
            });
  }

  @Test
  void testRead() {
    long targetRno = 100L;
    ReplyVO vo = replyService.get(targetRno);
    LOGGER.info("{}", vo);
  }

  @Transactional
  @Test
  void testDelete() {
    long targetRno = 1L;
    replyService.remove(targetRno);
  }

  @Transactional
  @Test
  void testUpdate() {
    long targetRno = 2L;

    ReplyVO vo = replyService.get(targetRno);

    vo.setReply("Update Reply ");

    int count = replyService.modify(vo);

    LOGGER.info("UPDATE COUNT: {}", count);
  }

  // 페이징 실행 쿼리
  //    select RNO, BNO, REPLY, REPLYER, REPLYDATE, UPDATEDATE
  //     from (select /*+INDEX(tbl_reply idx_reply) */ 'dummy'
  //                 , ROWNUM as rn, RNO, BNO, REPLY, REPLYER, REPLYDATE, UPDATEDATE
  //             from TBL_REPLY
  //            where BNO = 10000521
  //              and RNO > 0
  //              and ROWNUM <= 40)
  //      where rn > 30 order by BNO DESC
  @Test
  void testListPaging() {
    Criteria cri = new Criteria(4, PageSize.SIZE_10);
    ReplyPageDTO replyPageDTO = replyService.getListPage(cri, 100L);
    replyPageDTO.getList().forEach(reply -> LOGGER.info("reply: {}", reply));
  }
}
