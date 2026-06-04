package org.fp024.mapper.generated;

import static org.fp024.mapper.generated.AuthVODynamicSqlSupport.*;
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
import org.fp024.domain.MemberAuthType;
import org.fp024.domain.generated.AuthVO;
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
public interface AuthMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<AuthVO>, CommonUpdateMapper {
    BasicColumn[] selectList = BasicColumn.columnList(userId, auth);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthVOResult", value = {
        @Result(column="USERID", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="AUTH", property="auth", typeHandler=CustomEnumTypeHandler.class, jdbcType=JdbcType.VARCHAR, id=true)
    })
    List<AuthVO> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthVOResult")
    Optional<AuthVO> selectOne(SelectStatementProvider selectStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authVO, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authVO, completer);
    }

    default int deleteByPrimaryKey(String userId_, MemberAuthType auth_) {
        return delete(c -> 
            c.where(userId, isEqualTo(userId_))
            .and(auth, isEqualTo(auth_))
        );
    }

    default int insert(AuthVO row) {
        return MyBatis3Utils.insert(this::insert, row, authVO, c ->
            c.withMappedColumn(userId)
            .withMappedColumn(auth)
        );
    }

    default int insertMultiple(Collection<AuthVO> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, authVO, c ->
            c.withMappedColumn(userId)
            .withMappedColumn(auth)
        );
    }

    default int insertSelective(AuthVO row) {
        return MyBatis3Utils.insert(this::insert, row, authVO, c ->
            c.withMappedColumnWhenPresent(userId, row::getUserId)
            .withMappedColumnWhenPresent(auth, row::getAuth)
        );
    }

    default Optional<AuthVO> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authVO, completer);
    }

    default List<AuthVO> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authVO, completer);
    }

    default List<AuthVO> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authVO, completer);
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authVO, completer);
    }

    static UpdateDSL updateAllColumns(AuthVO row, UpdateDSL dsl) {
        return dsl.set(userId).equalTo(row::getUserId)
                .set(auth).equalTo(row::getAuth);
    }

    static UpdateDSL updateSelectiveColumns(AuthVO row, UpdateDSL dsl) {
        return dsl.set(userId).equalToWhenPresent(row::getUserId)
                .set(auth).equalToWhenPresent(row::getAuth);
    }
}