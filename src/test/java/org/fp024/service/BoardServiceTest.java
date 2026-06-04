package org.fp024.service;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.bno;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.content;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.regdate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.replyCount;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.title;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.updateDate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.writer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mybatis.dynamic.sql.SqlBuilder.count;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThanOrEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.fp024.config.RootConfig;
import org.fp024.domain.BoardDTO;
import org.fp024.domain.Criteria;
import org.fp024.domain.PageSize;
import org.fp024.domain.generated.BoardVO;
import org.fp024.mapper.generated.BoardMapper;
import org.fp024.mapper.generated.BoardVODynamicSqlSupport;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.Constant;
import org.mybatis.dynamic.sql.DerivedColumn;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {RootConfig.class})
@Slf4j
class BoardServiceTest {
  @Autowired private BoardService service;

  @Autowired private BoardMapperSupport boardMapperSupport;

  @Test
  void testExist() {
    assertNotNull(service);
    LOGGER.info(service.toString());
  }

  @Test
  void testRegister() {
    BoardDTO boardDTO = new BoardDTO();

    BoardVO board = new BoardVO();
    board.setTitle("1새로 작성하는 글");
    board.setContent("새로 작성하는 내용");
    board.setWriter("newbie");

    boardDTO.setBoardVO(board);

    service.register(boardDTO);

    LOGGER.info("생성된 게시물 번호: {}", board.getBno());
  }

  // 쿼리에 INDEX 힌트가 있어서 한줄 주석을 사용했다.
  //   select BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE
  //     from
  //           (select /*+ INDEX_DESC(tbl_board pk_board) */ 'dummy'
  //                , ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE from TBL_BOARD
  //            where (TITLE like ? or CONTENT like ? or WRITER like ?)
  //              and ROWNUM <= ?
  //           )
  //    where rn > ? order by BNO DESC
  @Test
  void testGetList() {
    Criteria criteria = new Criteria(1, PageSize.SIZE_10);
    criteria.setSearchCodes(Arrays.asList("T", "C", "W"));
    criteria.setKeyword("newbie");
    service.getList(criteria).forEach(board -> LOGGER.info(board.toString()));
  }

  @Test
  void testGet() {
    LOGGER.info("{}", service.get(1L));
  }

  @Test
  void testDelete() {
    // 게시물 존재 여부에 따라 결과가 달라짐
    LOGGER.info("REMOVE RESULT: {}", service.remove(122L));
  }

  @Test
  void testUpdate() {
    BoardVO board = service.get(1L);

    if (board == null) {
      return;
    }
    board.setTitle("제목 수정합니다. - " + LocalDateTime.now().getSecond());

    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setBoardVO(board);

    LOGGER.info("MODIFY RESULT: {}", service.modify(boardDTO));
  }

  /*
    select count(*)
      from TBL_BOARD
     where (TITLE like ? or CONTENT like ? or WRITER like ?)
       and BNO > ?
  */
  @Test
  void testGetCount() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(Arrays.asList("T", "W"));
    criteria.setKeyword("검색어");
    service.getTotal(criteria);
  }

  @Test
  void testAddSearchWhereClause_getListQuery() {
    Criteria criteria = new Criteria(1, PageSize.SIZE_10);
    criteria.setSearchCodes(Arrays.asList("T", "C", "W"));
    criteria.setKeyword("newbie");

    SelectStatementProvider selectStatementProvider = getListSqlDSL(criteria);
    assertEquals(
        "select BNO, TITLE, WRITER, REGDATE, UPDATEDATE, REPLYCNT, CONTENT from (select /*+"
            + " INDEX_DESC(tbl_board pk_board) */ 'dummy', ROWNUM as rn, BNO, TITLE, CONTENT,"
            + " WRITER, REGDATE, UPDATEDATE, REPLYCNT from TBL_BOARD where (TITLE like"
            + " #{parameters.p1,jdbcType=VARCHAR} or CONTENT like #{parameters.p2,jdbcType=CLOB}"
            + " or WRITER like #{parameters.p3,jdbcType=VARCHAR}) and ROWNUM <= #{parameters.p4})"
            + " where rn > #{parameters.p5} order by BNO DESC",
        selectStatementProvider.getSelectStatement());
    assertEquals(
        "{p1=%newbie%, p2=%newbie%, p3=%newbie%, p4=10, p5=0}",
        selectStatementProvider.getParameters().toString());
  }

  private SelectStatementProvider getListSqlDSL(Criteria criteria) {
    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    Constant<String> hint = Constant.of("/*+ INDEX_DESC(tbl_board pk_board) */ 'dummy'");

    QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder selectDSL;

    selectDSL =
        boardMapperSupport
            .addSearchWhereClause(
                select(hint, rn, bno, title, content, writer, regdate, updateDate, replyCount)
                    .from(BoardVODynamicSqlSupport.boardVO),
                criteria)
            .where()
            .and(rownum, isLessThanOrEqualTo(criteria.getPageNum() * criteria.getAmount()));

    return select(BoardMapper.selectList)
        .from(selectDSL)
        .where(rn, isGreaterThan((criteria.getPageNum() - 1) * criteria.getAmount()))
        .orderBy(bno.descending())
        .build()
        .render(RenderingStrategies.MYBATIS3);
  }

  @Test
  void testAddSearchWhereClause_getTotalCountQuery() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(Arrays.asList("T", "W"));
    criteria.setKeyword("검색어");
    SelectStatementProvider selectStatementProvider = getTotalCountSqlDSL(criteria);

    assertEquals(
        "select count(*) from TBL_BOARD where (TITLE like #{parameters.p1,jdbcType=VARCHAR} or"
            + " WRITER like #{parameters.p2,jdbcType=VARCHAR}) and BNO >"
            + " #{parameters.p3,jdbcType=NUMERIC}",
        selectStatementProvider.getSelectStatement());
    assertEquals("{p1=%검색어%, p2=%검색어%, p3=0}", selectStatementProvider.getParameters().toString());
  }

  private SelectStatementProvider getTotalCountSqlDSL(Criteria criteria) {
    QueryExpressionDSL<SelectModel> selectDSL =
        select(count()).from(BoardVODynamicSqlSupport.boardVO);

    QueryExpressionDSL<SelectModel> addedSearchWhereClause =
        boardMapperSupport.addSearchWhereClause(selectDSL, criteria);
    return Objects.requireNonNull(addedSearchWhereClause)
        .where()
        .and(bno, isGreaterThan(0L))
        .build()
        .render(RenderingStrategies.MYBATIS3);
  }

  @Test
  void testUpdateReplyCount() {
    service.updateReplyCount(10001041L, 0);
  }
}
