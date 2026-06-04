package org.fp024.mapper.generated;

import static org.fp024.mapper.generated.BoardVODynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.fp024.domain.generated.BoardVO;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.dsl.CountDSLCompleter;
import org.mybatis.dynamic.sql.dsl.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.dsl.SelectDSLCompleter;
import org.mybatis.dynamic.sql.dsl.UpdateDSL;
import org.mybatis.dynamic.sql.dsl.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface BoardMapper extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    BasicColumn[] selectList = BasicColumn.columnList(bno, title, writer, regdate, updateDate, replyCount, content);

    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT seq_board.nextval FROM DUAL", keyProperty="row.bno", before=true, resultType=Long.class)
    int insert(InsertStatementProvider<BoardVO> insertStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="BoardVOResult", value = {
        @Result(column="BNO", property="bno", jdbcType=JdbcType.NUMERIC, id=true),
        @Result(column="TITLE", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="WRITER", property="writer", jdbcType=JdbcType.VARCHAR),
        @Result(column="REGDATE", property="regdate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATEDATE", property="updateDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="REPLYCNT", property="replyCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="CONTENT", property="content", jdbcType=JdbcType.CLOB)
    })
    List<BoardVO> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("BoardVOResult")
    Optional<BoardVO> selectOne(SelectStatementProvider selectStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, boardVO, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, boardVO, completer);
    }

    default int deleteByPrimaryKey(Long bno_) {
        return delete(c -> 
            c.where(bno, isEqualTo(bno_))
        );
    }

    default int insert(BoardVO row) {
        return MyBatis3Utils.insert(this::insert, row, boardVO, c ->
            c.withMappedColumn(bno)
            .withMappedColumn(title)
            .withMappedColumn(writer)
            .withMappedColumn(regdate)
            .withMappedColumn(updateDate)
            .withMappedColumn(replyCount)
            .withMappedColumn(content)
        );
    }

    default int insertSelective(BoardVO row) {
        return MyBatis3Utils.insert(this::insert, row, boardVO, c ->
            c.withMappedColumn(bno)
            .withMappedColumnWhenPresent(title, row::getTitle)
            .withMappedColumnWhenPresent(writer, row::getWriter)
            .withMappedColumnWhenPresent(regdate, row::getRegdate)
            .withMappedColumnWhenPresent(updateDate, row::getUpdateDate)
            .withMappedColumnWhenPresent(replyCount, row::getReplyCount)
            .withMappedColumnWhenPresent(content, row::getContent)
        );
    }

    default Optional<BoardVO> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, boardVO, completer);
    }

    default List<BoardVO> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, boardVO, completer);
    }

    default List<BoardVO> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, boardVO, completer);
    }

    default Optional<BoardVO> selectByPrimaryKey(Long bno_) {
        return selectOne(c ->
            c.where(bno, isEqualTo(bno_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, boardVO, completer);
    }

    static UpdateDSL updateAllColumns(BoardVO row, UpdateDSL dsl) {
        return dsl.set(bno).equalTo(row::getBno)
                .set(title).equalTo(row::getTitle)
                .set(writer).equalTo(row::getWriter)
                .set(regdate).equalTo(row::getRegdate)
                .set(updateDate).equalTo(row::getUpdateDate)
                .set(replyCount).equalTo(row::getReplyCount)
                .set(content).equalTo(row::getContent);
    }

    static UpdateDSL updateSelectiveColumns(BoardVO row, UpdateDSL dsl) {
        return dsl.set(bno).equalToWhenPresent(row::getBno)
                .set(title).equalToWhenPresent(row::getTitle)
                .set(writer).equalToWhenPresent(row::getWriter)
                .set(regdate).equalToWhenPresent(row::getRegdate)
                .set(updateDate).equalToWhenPresent(row::getUpdateDate)
                .set(replyCount).equalToWhenPresent(row::getReplyCount)
                .set(content).equalToWhenPresent(row::getContent);
    }

    default int updateByPrimaryKey(BoardVO row) {
        return update(c ->
            c.set(title).equalTo(row::getTitle)
            .set(writer).equalTo(row::getWriter)
            .set(regdate).equalTo(row::getRegdate)
            .set(updateDate).equalTo(row::getUpdateDate)
            .set(replyCount).equalTo(row::getReplyCount)
            .set(content).equalTo(row::getContent)
            .where(bno, isEqualTo(row::getBno))
        );
    }

    default int updateByPrimaryKeySelective(BoardVO row) {
        return update(c ->
            c.set(title).equalToWhenPresent(row::getTitle)
            .set(writer).equalToWhenPresent(row::getWriter)
            .set(regdate).equalToWhenPresent(row::getRegdate)
            .set(updateDate).equalToWhenPresent(row::getUpdateDate)
            .set(replyCount).equalToWhenPresent(row::getReplyCount)
            .set(content).equalToWhenPresent(row::getContent)
            .where(bno, isEqualTo(row::getBno))
        );
    }
}