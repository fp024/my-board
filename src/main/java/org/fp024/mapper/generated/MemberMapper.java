package org.fp024.mapper.generated;

import static org.fp024.mapper.generated.MemberVODynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.fp024.domain.generated.MemberVO;
import org.fp024.typehandler.CustomEnumTypeHandler;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.dsl.CountDSLCompleter;
import org.mybatis.dynamic.sql.dsl.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.dsl.SelectDSLCompleter;
import org.mybatis.dynamic.sql.dsl.UpdateDSL;
import org.mybatis.dynamic.sql.dsl.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface MemberMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<MemberVO>, CommonUpdateMapper {
    BasicColumn[] selectList = BasicColumn.columnList(userId, userPassword, userName, registerDate, updateDate, enabled);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="MemberVOResult", value = {
        @Result(column="USERID", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="USERPW", property="userPassword", jdbcType=JdbcType.VARCHAR),
        @Result(column="USERNAME", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="REGDATE", property="registerDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATEDATE", property="updateDate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="ENABLED", property="enabled", typeHandler=CustomEnumTypeHandler.class, jdbcType=JdbcType.CHAR)
    })
    List<MemberVO> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("MemberVOResult")
    Optional<MemberVO> selectOne(SelectStatementProvider selectStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, memberVO, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, memberVO, completer);
    }

    default int deleteByPrimaryKey(String userId_) {
        return delete(c -> 
            c.where(userId, isEqualTo(userId_))
        );
    }

    default int insert(MemberVO row) {
        return MyBatis3Utils.insert(this::insert, row, memberVO, c ->
            c.withMappedColumn(userId)
            .withMappedColumn(userPassword)
            .withMappedColumn(userName)
            .withMappedColumn(registerDate)
            .withMappedColumn(updateDate)
            .withMappedColumn(enabled)
        );
    }

    default int insertMultiple(Collection<MemberVO> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, memberVO, c ->
            c.withMappedColumn(userId)
            .withMappedColumn(userPassword)
            .withMappedColumn(userName)
            .withMappedColumn(registerDate)
            .withMappedColumn(updateDate)
            .withMappedColumn(enabled)
        );
    }

    default int insertSelective(MemberVO row) {
        return MyBatis3Utils.insert(this::insert, row, memberVO, c ->
            c.withMappedColumnWhenPresent(userId, row::getUserId)
            .withMappedColumnWhenPresent(userPassword, row::getUserPassword)
            .withMappedColumnWhenPresent(userName, row::getUserName)
            .withMappedColumnWhenPresent(registerDate, row::getRegisterDate)
            .withMappedColumnWhenPresent(updateDate, row::getUpdateDate)
            .withMappedColumnWhenPresent(enabled, row::getEnabled)
        );
    }

    default Optional<MemberVO> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, memberVO, completer);
    }

    default List<MemberVO> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, memberVO, completer);
    }

    default List<MemberVO> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, memberVO, completer);
    }

    default Optional<MemberVO> selectByPrimaryKey(String userId_) {
        return selectOne(c ->
            c.where(userId, isEqualTo(userId_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, memberVO, completer);
    }

    static UpdateDSL updateAllColumns(MemberVO row, UpdateDSL dsl) {
        return dsl.set(userId).equalTo(row::getUserId)
                .set(userPassword).equalTo(row::getUserPassword)
                .set(userName).equalTo(row::getUserName)
                .set(registerDate).equalTo(row::getRegisterDate)
                .set(updateDate).equalTo(row::getUpdateDate)
                .set(enabled).equalTo(row::getEnabled);
    }

    static UpdateDSL updateSelectiveColumns(MemberVO row, UpdateDSL dsl) {
        return dsl.set(userId).equalToWhenPresent(row::getUserId)
                .set(userPassword).equalToWhenPresent(row::getUserPassword)
                .set(userName).equalToWhenPresent(row::getUserName)
                .set(registerDate).equalToWhenPresent(row::getRegisterDate)
                .set(updateDate).equalToWhenPresent(row::getUpdateDate)
                .set(enabled).equalToWhenPresent(row::getEnabled);
    }

    default int updateByPrimaryKey(MemberVO row) {
        return update(c ->
            c.set(userPassword).equalTo(row::getUserPassword)
            .set(userName).equalTo(row::getUserName)
            .set(registerDate).equalTo(row::getRegisterDate)
            .set(updateDate).equalTo(row::getUpdateDate)
            .set(enabled).equalTo(row::getEnabled)
            .where(userId, isEqualTo(row::getUserId))
        );
    }

    default int updateByPrimaryKeySelective(MemberVO row) {
        return update(c ->
            c.set(userPassword).equalToWhenPresent(row::getUserPassword)
            .set(userName).equalToWhenPresent(row::getUserName)
            .set(registerDate).equalToWhenPresent(row::getRegisterDate)
            .set(updateDate).equalToWhenPresent(row::getUpdateDate)
            .set(enabled).equalToWhenPresent(row::getEnabled)
            .where(userId, isEqualTo(row::getUserId))
        );
    }
}