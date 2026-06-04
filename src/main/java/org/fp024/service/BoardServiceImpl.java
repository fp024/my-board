package org.fp024.service;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.bno;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.content;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.regdate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.replyCount;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.title;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.updateDate;
import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.writer;
import static org.fp024.util.CommonUtil.currentSystemPathToUnixPath;
import static org.mybatis.dynamic.sql.SqlBuilder.count;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThanOrEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fp024.domain.BoardDTO;
import org.fp024.domain.Criteria;
import org.fp024.domain.generated.BoardAttachVO;
import org.fp024.domain.generated.BoardVO;
import org.fp024.mapper.generated.BoardAttachMapper;
import org.fp024.mapper.generated.BoardAttachVODynamicSqlSupport;
import org.fp024.mapper.generated.BoardMapper;
import org.fp024.mapper.generated.BoardVODynamicSqlSupport;
import org.mybatis.dynamic.sql.Constant;
import org.mybatis.dynamic.sql.DerivedColumn;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 쿼리 DSL 작성 참조 링크
 *
 * <p>SELECT: <a href="https://mybatis.org/mybatis-dynamic-sql/docs/select.html">SELECT</a>
 *
 * <p>UPDATE: <a href="https://mybatis.org/mybatis-dynamic-sql/docs/update.html">UPDATE</a>
 *
 * <p>INSERT: <a href="https://mybatis.org/mybatis-dynamic-sql/docs/insert.html">INSERT</a>
 *
 * <p>DELETE: <a href="https://mybatis.org/mybatis-dynamic-sql/docs/delete.html">DELETE</a>
 *
 * <p>예제:<a href="https://github.com/mybatis/mybatis-dynamic-sql/tree/master/src/test/java/examples">예제</a>
 *
 * <p>SqlBuilder 클래스는 import static 해주는게 낫겠다. 이래야 보기가 편함.
 *
 * @author fp024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
  // Spring 4.3이상에서 자동처리 (단일 파라미터 생성자에 대해서는 자동 주입)
  // 모든 인자에 대한 생성자를 자동으로 만들도록 lombok에서 정의했음.
  private final BoardMapper mapper;

  private final BoardMapperSupport boardMapperSupport;

  private final BoardAttachMapper attachMapper;

  @Override
  public void register(BoardDTO boardDTO) {
    LOGGER.info("register..... {}", boardDTO);
    BoardVO board = boardDTO.getBoardVO();
    // insertSelectKey와 insert를 따로 분리해서 만들지 않았다, 항상 Key 프로퍼티를 설정해서 반환한다.
    // 바로 모델을 insert를 하면 board 모델의 등록/수정일시가 null일 경우 null로 업데이트를 할 수 있으므로 명시적으로 정해준다.
    // 테이블 생성시 SYSDATE 기본값이 동작하도록 명시적으로 지정해줄 필요가 있었다.
    //
    // bno도 시퀀스로 얻은 값이 insert에 포함되도록 지정해줘야한다.
    /*
    mapper.insert(
        SqlBuilder.insert(board)
            .map(BoardVODynamicSqlSupport.bno)
            .toProperty(BoardVODynamicSqlSupport.bno.name())
            .map(BoardVODynamicSqlSupport.title)
            .toProperty(BoardVODynamicSqlSupport.title.name())
            .map(BoardVODynamicSqlSupport.content)
            .toProperty(BoardVODynamicSqlSupport.content.name())
            .map(BoardVODynamicSqlSupport.writer)
            .toProperty(BoardVODynamicSqlSupport.writer.name())
            .build()
            .render(RenderingStrategies.MYBATIS3));
    */
    // 위의 코드보다 INSERT 직전 입력하지 않을 값을 명확하게하고 선택적 INSERT하는게 더 관리에 나을 수 있어보인다.
    board.setRegdate(null);
    board.setUpdateDate(null);
    mapper.insertSelective(board);

    if (boardDTO.getAttachList() == null || boardDTO.getAttachList().isEmpty()) {
      return;
    }

    boardDTO
        .getAttachList()
        .forEach(
            attach -> {
              // 신규 등록시는 최초에 bno가 없지만 insert 이후로 board에다 bno를 MyBatis가 넣어줄 것 임.
              attach.setBno(board.getBno());
              // 업로드 경로를 DB에 저장을 할 때만, Unix 경로로 사용해보자!,
              // 요즘 윈도우에서는 Unix 경로를 쓰더라도 잘 될 것 같은데... 조회 추가해보면서 확인해보자.
              attach.setUploadPath(currentSystemPathToUnixPath(attach.getUploadPath()));
              attachMapper.insert(attach);
            });
  }

  @Override
  public BoardVO get(Long bno) {
    LOGGER.info("get..... {}", bno);
    return mapper.selectByPrimaryKey(bno).orElse(null);
  }

  @Transactional
  @Override
  public boolean modify(BoardDTO boardDTO) {
    LOGGER.info("modify..... {}", boardDTO);
    BoardVO board = boardDTO.getBoardVO();

    attachMapper.delete(
        c -> c.where(BoardAttachVODynamicSqlSupport.bno, isEqualTo(board.getBno())));

    boolean modifyResult =
        mapper.update(
                c ->
                    c.set(BoardVODynamicSqlSupport.title)
                        .equalTo(board.getTitle())
                        .set(BoardVODynamicSqlSupport.content)
                        .equalTo(board.getContent())
                        .set(BoardVODynamicSqlSupport.updateDate)
                        .equalTo(LocalDateTime.now())
                        .where(BoardVODynamicSqlSupport.bno, isEqualTo(board.getBno())))
            == 1;

    if (modifyResult && boardDTO.getAttachList() != null && !boardDTO.getAttachList().isEmpty()) {
      boardDTO
          .getAttachList()
          .forEach(
              attach -> {
                attach.setBno(board.getBno());
                attach.setUploadPath(currentSystemPathToUnixPath(attach.getUploadPath()));
                attachMapper.insert(attach);
              });
    }

    return modifyResult;
  }

  @Transactional
  @Override
  public boolean remove(Long bno) {
    LOGGER.info("remove..... {}", bno);
    attachMapper.delete(c -> c.where(BoardAttachVODynamicSqlSupport.bno, isEqualTo(bno)));
    return mapper.deleteByPrimaryKey(bno) == 1;
  }

  /**
   * ORDER BY는 메서드를 지원해서 그것을 사용해도 되는데, <br>
   * 어떻게든 Hint를 포함해보았다. 자세한 참고는 BoardMapperTest를 확인해볼 것!
   *
   * @see "org.fp024.mapper.BoardMapperTest"
   */
  @Override
  public List<BoardVO> getList(Criteria criteria) {
    LOGGER.info("get List with criteria: {}", criteria);
    DerivedColumn<Long> rownum = DerivedColumn.of("ROWNUM");
    DerivedColumn<Long> rn = rownum.as("rn");

    Constant<String> hint = Constant.of("/*+ INDEX_DESC(tbl_board pk_board) */ 'dummy'");

    QueryExpressionDSL<SelectModel>.QueryExpressionWhereBuilder select =
        boardMapperSupport
            .addSearchWhereClause(
                select(hint, rn, bno, title, content, writer, regdate, updateDate, replyCount)
                    .from(BoardVODynamicSqlSupport.boardVO),
                criteria)
            .where()
            .and(rownum, isLessThanOrEqualTo(criteria.getPageNum() * criteria.getAmount()));

    return mapper.selectMany(
        select(BoardMapper.selectList)
            .from(select)
            .where(
                rn,
                isGreaterThan((criteria.getPageNum() - 1) * criteria.getAmount()))
            .orderBy(bno.descending())
            .build()
            .render(RenderingStrategies.MYBATIS3));
  }

  @Override
  public long getTotal(Criteria criteria) {
    QueryExpressionDSL<SelectModel> selectDSL =
        select(count()).from(BoardVODynamicSqlSupport.boardVO);

    QueryExpressionDSL<SelectModel> addedSearchWhereClause =
        boardMapperSupport.addSearchWhereClause(selectDSL, criteria);

    return mapper.count(
        addedSearchWhereClause
            .where()
            .and(bno, isGreaterThan(0L))
            .build()
            .render(RenderingStrategies.MYBATIS3));
  }

  @Override
  public void updateReplyCount(Long bno, int amount) {
    mapper.update(
        c ->
            c.set(BoardVODynamicSqlSupport.replyCount)
                .equalToConstant(
                    String.format("%s + %d", BoardVODynamicSqlSupport.replyCount.name(), amount))
                .where(BoardVODynamicSqlSupport.bno, isEqualTo(bno)));
  }

  @Override
  public List<BoardAttachVO> getAttachList(Long bno) {
    LOGGER.info("get Attach list by bno {}", bno);
    return attachMapper.select(c -> c.where(BoardAttachVODynamicSqlSupport.bno, isEqualTo(bno)));
  }
}
