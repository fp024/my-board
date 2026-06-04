package org.fp024.mapper.generated;

import static org.fp024.mapper.generated.ReplyVODynamicSqlSupport.*;
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
import org.fp024.domain.generated.ReplyVO;
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
public interface ReplyMapper extends CommonCountMapper, CommonDeleteMapper, CommonUpdateMapper {
    BasicColumn[] selectList = BasicColumn.columnList(rno, bno, reply, replyer, replyDate, updateDate);

    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT seq_reply.nextval FROM DUAL", keyProperty="row.rno", before=true, resultType=Long.class)
    int insert(InsertStatementProvider<ReplyVO> insertStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="ReplyVOResult", value = {
        @Result(column="RNO", property="rno", jdbcType=JdbcType.NUMERIC, id=true),
        @Result(column="BNO", property="bno", jdbcType=JdbcType.NUMERIC),
        @Result(column="REPLY", property="reply", jdbcType=JdbcType.VARCHAR),
        @Result(column="REPLYER", property="replyer", jdbcType=JdbcType.VARCHAR),
        @Result(column="REPLYDATE", property="replyDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATEDATE", property="updateDate", jdbcType=JdbcType.TIMESTAMP)
    })
    List<ReplyVO> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("ReplyVOResult")
    Optional<ReplyVO> selectOne(SelectStatementProvider selectStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, replyVO, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, replyVO, completer);
    }

    default int deleteByPrimaryKey(Long rno_) {
        return delete(c -> 
            c.where(rno, isEqualTo(rno_))
        );
    }

    default int insert(ReplyVO row) {
        return MyBatis3Utils.insert(this::insert, row, replyVO, c ->
            c.withMappedColumn(rno)
            .withMappedColumn(bno)
            .withMappedColumn(reply)
            .withMappedColumn(replyer)
            .withMappedColumn(replyDate)
            .withMappedColumn(updateDate)
        );
    }

    default int insertSelective(ReplyVO row) {
        return MyBatis3Utils.insert(this::insert, row, replyVO, c ->
            c.withMappedColumn(rno)
            .withMappedColumnWhenPresent(bno, row::getBno)
            .withMappedColumnWhenPresent(reply, row::getReply)
            .withMappedColumnWhenPresent(replyer, row::getReplyer)
            .withMappedColumnWhenPresent(replyDate, row::getReplyDate)
            .withMappedColumnWhenPresent(updateDate, row::getUpdateDate)
        );
    }

    default Optional<ReplyVO> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, replyVO, completer);
    }

    default List<ReplyVO> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, replyVO, completer);
    }

    default List<ReplyVO> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, replyVO, completer);
    }

    default Optional<ReplyVO> selectByPrimaryKey(Long rno_) {
        return selectOne(c ->
            c.where(rno, isEqualTo(rno_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, replyVO, completer);
    }

    static UpdateDSL updateAllColumns(ReplyVO row, UpdateDSL dsl) {
        return dsl.set(rno).equalTo(row::getRno)
                .set(bno).equalTo(row::getBno)
                .set(reply).equalTo(row::getReply)
                .set(replyer).equalTo(row::getReplyer)
                .set(replyDate).equalTo(row::getReplyDate)
                .set(updateDate).equalTo(row::getUpdateDate);
    }

    static UpdateDSL updateSelectiveColumns(ReplyVO row, UpdateDSL dsl) {
        return dsl.set(rno).equalToWhenPresent(row::getRno)
                .set(bno).equalToWhenPresent(row::getBno)
                .set(reply).equalToWhenPresent(row::getReply)
                .set(replyer).equalToWhenPresent(row::getReplyer)
                .set(replyDate).equalToWhenPresent(row::getReplyDate)
                .set(updateDate).equalToWhenPresent(row::getUpdateDate);
    }

    default int updateByPrimaryKey(ReplyVO row) {
        return update(c ->
            c.set(bno).equalTo(row::getBno)
            .set(reply).equalTo(row::getReply)
            .set(replyer).equalTo(row::getReplyer)
            .set(replyDate).equalTo(row::getReplyDate)
            .set(updateDate).equalTo(row::getUpdateDate)
            .where(rno, isEqualTo(row::getRno))
        );
    }

    default int updateByPrimaryKeySelective(ReplyVO row) {
        return update(c ->
            c.set(bno).equalToWhenPresent(row::getBno)
            .set(reply).equalToWhenPresent(row::getReply)
            .set(replyer).equalToWhenPresent(row::getReplyer)
            .set(replyDate).equalToWhenPresent(row::getReplyDate)
            .set(updateDate).equalToWhenPresent(row::getUpdateDate)
            .where(rno, isEqualTo(row::getRno))
        );
    }
}