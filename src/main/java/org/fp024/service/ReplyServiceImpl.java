package org.fp024.service;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.bno;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.reply;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.replyDate;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.replyVO;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.replyer;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.rno;
import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.updateDate;
import static org.mybatis.dynamic.sql.SqlBuilder.count;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThanOrEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.Criteria;
import org.fp024.domain.ReplyPageDTO;
import org.fp024.domain.generated.ReplyVO;
import org.fp024.mapper.generated.ReplyMapper;
import org.mybatis.dynamic.sql.Constant;
import org.mybatis.dynamic.sql.DerivedColumn;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyServiceImpl implements ReplyService {

  private final ReplyMapper replyMapper;

  private final BoardService boardService;

  @Transactional
  @Override
  public void register(ReplyVO vo) {
    LOGGER.info("register.....{}", vo);
    // 테이블의 댓글 등록일시, 수정일시 기본 값이 SYSDATE 이다.
    vo.setReplyDate(null);
    vo.setUpdateDate(null);

    boardService.updateReplyCount(vo.getBno(), 1);

    replyMapper.insertSelective(vo);
  }

  @Override
  public ReplyVO get(Long rno) {
    LOGGER.info("get.....{}", rno);
    return replyMapper.selectByPrimaryKey(rno).orElse(null);
  }

  @Override
  public int modify(ReplyVO vo) {
    LOGGER.info("modify.....{}", vo);
    return replyMapper.update(
        c ->
            c.set(reply)
                .equalTo(vo.getReply())
                .set(updateDate)
                .equalTo(LocalDateTime.now())
                .where(rno, isEqualTo(vo.getRno())));
  }

  @Transactional
  @Override
  public int remove(Long rno) {
    LOGGER.info("remove.....{}", rno);

    Optional<ReplyVO> optional = replyMapper.selectByPrimaryKey(rno);

    if (optional.isEmpty()) {
      return 0;
    }

    boardService.updateReplyCount(optional.get().getBno(), -1);

    return replyMapper.deleteByPrimaryKey(rno);
  }

  @Override
  public ReplyPageDTO getListPage(Criteria cri, Long boardNo) {
    LOGGER.info("get Reply List of a board {}", boardNo);

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");
    Constant<String> hint = Constant.of("/*+INDEX(tbl_reply idx_reply) */ 'dummy'");

    List<ReplyVO> list =
        replyMapper.selectMany(
            select(ReplyMapper.selectList)
                .from(
                    select(hint, rn, rno, bno, reply, replyer, replyDate, updateDate)
                        .from(replyVO)
                        .where(bno, isEqualTo(boardNo))
                        .and(rno, isGreaterThan(0L))
                        .and(rownum, isLessThanOrEqualTo(cri.getPageNum() * cri.getAmount())))
                .where(
                    rn, isGreaterThan((cri.getPageNum() - 1) * cri.getAmount()))
                .orderBy(bno.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3));

    return new ReplyPageDTO(cri.getAmount(), getCount(boardNo), list);
  }

  private int getCount(Long boardNo) {
    return (int)
        replyMapper.count(
            select(count())
                .from(replyVO)
                .where(bno, isEqualTo(boardNo))
                .build()
                .render(RenderingStrategies.MYBATIS3));
  }
}
