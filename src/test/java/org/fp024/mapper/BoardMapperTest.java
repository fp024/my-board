package org.fp024.mapper;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.bno;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.content;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.regdate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.replyCount;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.title;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.updateDate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.writer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mybatis.dynamic.sql.SqlBuilder.count;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThanOrEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;
import static org.mybatis.dynamic.sql.SqlBuilder.or;
import static org.mybatis.dynamic.sql.SqlBuilder.select;
import static org.mybatis.dynamic.sql.SqlBuilder.update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.fp024.config.RootConfig;
import org.fp024.domain.Criteria;
import org.fp024.domain.SearchType;
import org.fp024.domain.generated.BoardVO;
import org.fp024.mapper.generated.BoardMapper;
import org.fp024.mapper.generated.BoardVODynamicSqlSupport;
import org.junit.jupiter.api.Test;
import org.mybatis.dynamic.sql.AndOrCriteriaGroup;
import org.mybatis.dynamic.sql.Constant;
import org.mybatis.dynamic.sql.DerivedColumn;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * mybatis-dynamic-sql 으로 만들어진 mapper를 쓸 때,<br>
 * QueryDSL 같이 사용한 부분은 별도 클래스로 분리해서 그것이 mapper클래스를 사용하게 해야할 것 같다.
 *
 * <p>참조:
 *
 * <p>WHERE 절 지원 https://mybatis.org/mybatis-dynamic-sql/docs/whereClauses.html
 *
 * <p>SELECT 문장 https://mybatis.org/mybatis-dynamic-sql/docs/select.html
 *
 * <p>테스트 코드 참고:
 * https://github.com/mybatis/mybatis-dynamic-sql/blob/b1fa3a6562c3ccf6798a64651def0e5019d5ac8d/src/test/java/examples/groupby/GroupByTest.java
 *
 * @author fp024
 */
@SpringJUnitConfig(classes = {RootConfig.class})
@Slf4j
class BoardMapperTest {
  @Autowired private BoardMapper mapper;

  // order by를 사용한 페이지 쿼리의 코드화
  //     SELECT bno
  //          , title
  //          , content
  //          , writer
  //          , regdate AS regDate
  //          , updatedate AS updateDate
  //       FROM (SELECT rownum AS rn, bno, title, content, writer, regdate, updatedate
  //               FROM tbl_board
  //              WHERE rownum <= #{pageNum} * #{amount}
  //              ORDER BY bno DESC
  //              )
  //      WHERE rn > (#{pageNum} - 1) * #{amount}
  @Test
  void testGetListWithPaging() {
    DerivedColumn<Long> ROWNUM = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = ROWNUM.as("rn");

    List<BoardVO> boardList =
        mapper.selectMany(
            select(BoardMapper.selectList)
                .from(
                    select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
                        .from(BoardVODynamicSqlSupport.boardVO)
                        .where(ROWNUM, isLessThanOrEqualTo(10L))
                        .orderBy(bno.descending()))
                .where(rn, isGreaterThan(0L))
                .orderBy(bno.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3));

    boardList.forEach(b -> LOGGER.info("{}", b.toString()));
  }

  /*
   * 어떻게든 Hint를 쿼리문에 집어넣을 수는 있는데... 쉼표(,)를 무조건 붙이기 때문에
   * dummy 컬럼을 노출해줘야하는 문제가 있다.
   */
  // 만들고 싶은 쿼리
  //     SELECT bno
  //          , title
  //          , content
  //          , writer
  //          , regdate AS regDate
  //          , updatedate AS updateDate
  //       FROM (SELECT /*+ INDEX_DESC(tbl_board pk_board) */
  //                    rownum AS rn, bno, title, content, writer, regdate, updatedate
  //               FROM tbl_board
  //              WHERE rownum <= #{pageNum} * #{amount})
  //      WHERE rn > (#{pageNum} - 1) * #{amount}
  // 어쩔 수 없이 dummy가 붙은  쿼리 (select()에 Constant를 넣을 때, 쉼표를 자동으로 붙여서 어쩔 수 없다.)
  //     SELECT bno
  //          , title
  //          , content
  //          , writer
  //          , regdate AS regDate
  //          , updatedate AS updateDate
  //       FROM (SELECT /*+ INDEX_DESC(tbl_board pk_board) */ 'dummy',
  //               rownum AS rn, bno, title, content, writer, regdate, updatedate
  //               FROM tbl_board
  //              WHERE rownum <= #{pageNum} * #{amount})
  //      WHERE rn > (#{pageNum} - 1) * #{amount}
  @Test
  void testGetListWithPaging_Hint() {
    DerivedColumn<Long> ROWNUM = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = ROWNUM.as("rn");

    Constant<String> hint = Constant.of("/*+ INDEX_DESC(tbl_board pk_board) */ 'dummy'");

    List<BoardVO> boardList =
        mapper.selectMany(
            select(BoardMapper.selectList)
                .from(
                    select(hint, rn, bno, title, content, writer, regdate, updateDate, replyCount)
                        .from(BoardVODynamicSqlSupport.boardVO)
                        .where(ROWNUM, isLessThanOrEqualTo(10L)))
                .where(rn, isGreaterThan(0L))
                .orderBy(bno.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3));

    boardList.forEach(b -> LOGGER.info("{}", b.toString()));
  }

  /** 검색조건 처리를 위해 where 절을 동적으로 만드는 부분을 별도 메서드로 빼야할 것 같다. */
  @Test
  void testGetListWithPaging_Hint_with_SearchCondition() {
    DerivedColumn<Long> ROWNUM = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = ROWNUM.as("rn");

    Constant<String> hint = Constant.of("/*+ INDEX_DESC(tbl_board pk_board) */ 'dummy'");

    List<BoardVO> boardList =
        mapper.selectMany(
            select(BoardMapper.selectList)
                .from(
                    select(hint, rn, bno, title, content, writer, regdate, updateDate, replyCount)
                        .from(BoardVODynamicSqlSupport.boardVO)
                        .where(ROWNUM, isLessThanOrEqualTo(10L)))
                .where(rn, isGreaterThan(0L))
                .orderBy(bno.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3));

    boardList.forEach(b -> LOGGER.info("{}", b.toString()));
  }

  @Test
  void testCreateSearchWhereClause_T_C_W() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(Arrays.asList("T", "C", "W"));
    criteria.setKeyword("검색어");

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    SelectStatementProvider selectStatement =
        addSearchWhereClause(innerSql, criteria).build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where (TITLE like #{parameters.p1,jdbcType=VARCHAR}"
            + " or CONTENT like #{parameters.p2,jdbcType=CLOB}"
            + " or WRITER like #{parameters.p3,jdbcType=VARCHAR})"
            + " and ROWNUM <= #{parameters.p4}",
        selectStatement.getSelectStatement());

    assertEquals(
        "{p1=%검색어%" + ", p2=%검색어%" + ", p3=%검색어%" + ", p4=10}",
        selectStatement.getParameters().toString());
  }

  @Test
  void testCreateSearchWhereClause_T_C() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(Arrays.asList("T", "C"));
    criteria.setKeyword("검색어");

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    SelectStatementProvider selectStatement =
        addSearchWhereClause(innerSql, criteria).build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where (TITLE like #{parameters.p1,jdbcType=VARCHAR}"
            + " or CONTENT like #{parameters.p2,jdbcType=CLOB})"
            + " and ROWNUM <= #{parameters.p3}",
        selectStatement.getSelectStatement());

    assertEquals(
        "{p1=%검색어%" + ", p2=%검색어%" + ", p3=10}", selectStatement.getParameters().toString());
  }

  @Test
  void testCreateSearchWhereClause_T_W() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(Arrays.asList("T", "W"));
    criteria.setKeyword("검색어");

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    SelectStatementProvider selectStatement =
        addSearchWhereClause(innerSql, criteria).build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where (TITLE like #{parameters.p1,jdbcType=VARCHAR}"
            + " or WRITER like #{parameters.p2,jdbcType=VARCHAR})"
            + " and ROWNUM <= #{parameters.p3}",
        selectStatement.getSelectStatement());

    assertEquals(
        "{p1=%검색어%" + ", p2=%검색어%" + ", p3=10}", selectStatement.getParameters().toString());
  }

  @Test
  void testCreateSearchWhereClause_C() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(List.of("C"));
    criteria.setKeyword("검색어");

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    SelectStatementProvider selectStatement =
        addSearchWhereClause(innerSql, criteria).build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where CONTENT like #{parameters.p1,jdbcType=CLOB}"
            + " and ROWNUM <= #{parameters.p2}",
        selectStatement.getSelectStatement());

    assertEquals("{p1=%검색어%" + ", p2=10}", selectStatement.getParameters().toString());
  }

  @Test
  void testCreateSearchWhereClause_W() {
    Criteria criteria = new Criteria();
    criteria.setSearchCodes(List.of("W"));
    criteria.setKeyword("검색어");

    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    SelectStatementProvider selectStatement =
        addSearchWhereClause(innerSql, criteria).build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where WRITER like #{parameters.p1,jdbcType=VARCHAR}"
            + " and ROWNUM <= #{parameters.p2}",
        selectStatement.getSelectStatement());

    assertEquals("{p1=%검색어%" + ", p2=10}", selectStatement.getParameters().toString());
  }

  /** 쿼리문에 검색 조건 WHERE 절 붙임 */
  QueryExpressionDSL<SelectModel> addSearchWhereClause(
      QueryExpressionDSL<SelectModel> innerSql, Criteria criteria) {
    List<SearchType> searchTypeList = criteria.getSearchTypeSet().stream().toList();
    List<AndOrCriteriaGroup> subCriteriaList = new ArrayList<>();

    for (int i = 0; i < searchTypeList.size(); i++) {
      if (i > 0) {
        subCriteriaList.add(
            or(
                searchTypeList.get(i).getColumn(),
                isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards)));
      }
    }
    if (searchTypeList.size() == 1) {
      innerSql
          .where(
              searchTypeList.get(0).getColumn(),
              isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards))
          .and(
              DerivedColumn.of("ROWNUM"),
              isLessThanOrEqualTo(criteria.getPageNum() * criteria.getAmount()));
    } else if (searchTypeList.size() > 1) {
      innerSql
          .where(
              searchTypeList.get(0).getColumn(),
              isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards),
              subCriteriaList)
          .and(
              DerivedColumn.of("ROWNUM"),
              isLessThanOrEqualTo(criteria.getPageNum() * criteria.getAmount()));
    }
    return innerSql;
  }

  /**
   * OR우선순위 지정을 위해 소괄호를 어떻게 넣어야할지 모르겠어서, 라이브러리 깃 허브에 질문글 올림.ㅠㅠ<br>
   * https://github.com/mybatis/mybatis-dynamic-sql/issues/415
   */
  @Test
  void testWhere() {
    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    innerSql
        .where(title, isLikeWhenPresent("keyword").map(this::addWildcards))
        .or(content, isLikeWhenPresent("keyword").map(this::addWildcards))
        .or(writer, isLikeWhenPresent("keyword").map(this::addWildcards))
        .and(rownum, isLessThanOrEqualTo(10L));

    SelectStatementProvider selectStatement = innerSql.build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where TITLE like #{parameters.p1,jdbcType=VARCHAR}"
            + " or CONTENT like #{parameters.p2,jdbcType=CLOB}"
            + " or WRITER like #{parameters.p3,jdbcType=VARCHAR}"
            + " and ROWNUM <= #{parameters.p4}",
        selectStatement.getSelectStatement());

    assertEquals(
        "{p1=%keyword%" + ", p2=%keyword%" + ", p3=%keyword%" + ", p4=10}",
        selectStatement.getParameters().toString());
  }

  /**
   * 소괄호로 감싸는 것에 대한 답변 받음<br>
   * https://github.com/mybatis/mybatis-dynamic-sql/issues/415<br>
   * org.mybatis.dynamic.sql.SqlBuilder.or 를 사용해서 아래와 같이 쓸 수 있음<br>
   * import static을 사용하지 않다보니까? 메서드를 찾기 힘들때가 있다. 왠만하면 추가해두자!
   */
  @Test
  void testWhereWithParentheses() {
    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    QueryExpressionDSL<SelectModel> innerSql =
        select(rn, bno, title, content, writer, regdate, updateDate, replyCount)
            .from(BoardVODynamicSqlSupport.boardVO);

    innerSql
        .where()
        .or(
            title,
            isLikeWhenPresent("keyword").map(this::addWildcards),
            or(content, isLikeWhenPresent("keyword").map(this::addWildcards)),
            or(writer, isLikeWhenPresent("keyword").map(this::addWildcards)))
        .and(rownum, isLessThanOrEqualTo(10L));

    SelectStatementProvider selectStatement = innerSql.build().render(RenderingStrategies.MYBATIS3);

    assertEquals(
        "select ROWNUM as rn, BNO, TITLE, CONTENT, WRITER, REGDATE, UPDATEDATE, REPLYCNT"
            + " from TBL_BOARD"
            + " where (TITLE like #{parameters.p1,jdbcType=VARCHAR}"
            + " or CONTENT like #{parameters.p2,jdbcType=CLOB}"
            + " or WRITER like #{parameters.p3,jdbcType=VARCHAR})"
            + " and ROWNUM <= #{parameters.p4}",
        selectStatement.getSelectStatement());

    assertEquals(
        "{p1=%keyword%" + ", p2=%keyword%" + ", p3=%keyword%" + ", p4=10}",
        selectStatement.getParameters().toString());
  }

  String addWildcards(String keyword) {
    return "%" + keyword + "%";
  }

  /**
   * 시퀀스를 generatorConfig.xml에서 정해주면 insert() 함수에 아래 어노테이션을 붙여줘서 실행하게되는데, 일부러 해당 값을 반환받지 않게 하기도
   * 애매하고, 굳이 안받는 메서드를 따로 만들 필요는 없을 것 같다., 필요시 호출 후 bno를 null로 바꿔주면 될 것 같다. @SelectKey(statement =
   * "SELECT seq_board.nextval FROM DUAL" , keyProperty = "record.bno", before = true, resultType =
   * Long.class)
   */
  @Test
  void testInsertSelectKey() {
    BoardVO board = new BoardVO();
    board.setTitle("새로 작성하는 글 select key");
    board.setContent("새로 작성하는 내용 select key");
    board.setWriter("newbie");

    // 테이블 기본값으로 SYSDATE로 처리되는 일자시간 컬럼
    board.setRegdate(null);
    board.setUpdateDate(null);

    mapper.insertSelective(board);
    LOGGER.info(board.toString());
  }

  /** selectByPrimaryKey 가 Optional<T> 로 반환해준다. */
  @Test
  void testRead() {
    // 존재하는 게시물 번호로 테스트
    Optional<BoardVO> board = mapper.selectByPrimaryKey(1L);

    if (board.isEmpty()) {
      throw new IllegalStateException("1번 게시물 없음");
    }

    LOGGER.info(board.get().toString());
  }

  @Test
  void testDelete() {
    LOGGER.info("DELETE COUNT: {}", mapper.deleteByPrimaryKey(3L));
  }

  @Test
  void testUpdate() {
    BoardVO board = new BoardVO();

    board.setBno(28L);
    board.setTitle("수정된 제목 - jex02");
    board.setContent("수정된 내용 - jex02");
    board.setWriter("user00 - jex02");
    board.setUpdateDate(LocalDateTime.now());

    // Selective로 끝나는 메서드를 사용하면, null로 설정된 값은 업데이트하지 않는다.
    int count = mapper.updateByPrimaryKeySelective(board);
    LOGGER.info("UPDATE COUNT: {}", count);
  }

  @Test
  void testUpdateWithDSL() {
    BoardVO board = new BoardVO();

    board.setBno(28L);
    board.setTitle("수정된 제목 - jex02 1");
    board.setContent("수정된 내용 - jex02 1");
    board.setWriter("user00 - jex02 1");
    board.setUpdateDate(LocalDateTime.now());

    UpdateStatementProvider updateStatement =
        update(BoardVODynamicSqlSupport.boardVO)
            .set(BoardVODynamicSqlSupport.title)
            .equalTo(board.getTitle())
            .set(BoardVODynamicSqlSupport.content)
            .equalTo(board.getContent())
            .set(BoardVODynamicSqlSupport.writer)
            .equalTo(board.getWriter())
            .set(BoardVODynamicSqlSupport.updateDate)
            .equalTo(board.getUpdateDate())
            .where(BoardVODynamicSqlSupport.bno, isEqualTo(board.getBno()))
            .build()
            .render(RenderingStrategies.MYBATIS3);

    // Selective로 끝나는 메서드를 사용하면, null로 설정된 값은 업데이트하지 않는다.
    int count = mapper.update(updateStatement);
    LOGGER.info("UPDATE COUNT: {}", count);
  }

  /*
    아래에 대응되는 쿼리를 만든다., 아직 검색 조건을 넣지 않았기 때문에, criteria를 실제 쿼리에서 활용하지 않는다.
     <resultMap type="map" id="CountResultMap">
       <result property="count" column="count" javaType="long" jdbcType="BIGINT" />
     </resultMap>
     <select id="getTotalCount" parameterType="criteria" resultMap="CountResultMap">
       SELECT COUNT(*) AS count
         FROM tbl_board
        WHERE bno > 0
     </select>

     mybatis-dymanic-sql 모듈 사용할 때는 count의 반환형이 long으로 되어있어 resultMap으로 바꿔줘야하는 고려가 없어도 된다.
  */
  @Test
  void testBoardCount() {
    long totalCount =
        mapper.count(
            select(count())
                .from(BoardVODynamicSqlSupport.boardVO)
                .where(bno, isGreaterThan(0L))
                .build()
                .render(RenderingStrategies.MYBATIS3));

    LOGGER.info("totalCount: {}", totalCount);
  }
}
