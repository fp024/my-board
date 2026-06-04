package org.fp024.mapper.generated;

import static org.fp024.mapper.generated.BoardAttachVODynamicSqlSupport.*;
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
import org.fp024.domain.generated.BoardAttachVO;
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
public interface BoardAttachMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<BoardAttachVO>, CommonUpdateMapper {
    BasicColumn[] selectList = BasicColumn.columnList(uuid, uploadPath, fileName, fileType, bno);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="BoardAttachVOResult", value = {
        @Result(column="UUID", property="uuid", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="UPLOADPATH", property="uploadPath", jdbcType=JdbcType.VARCHAR),
        @Result(column="FILENAME", property="fileName", jdbcType=JdbcType.VARCHAR),
        @Result(column="FILETYPE", property="fileType", typeHandler=CustomEnumTypeHandler.class, jdbcType=JdbcType.CHAR),
        @Result(column="BNO", property="bno", jdbcType=JdbcType.NUMERIC)
    })
    List<BoardAttachVO> selectMany(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("BoardAttachVOResult")
    Optional<BoardAttachVO> selectOne(SelectStatementProvider selectStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, boardAttachVO, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, boardAttachVO, completer);
    }

    default int deleteByPrimaryKey(String uuid_) {
        return delete(c -> 
            c.where(uuid, isEqualTo(uuid_))
        );
    }

    default int insert(BoardAttachVO row) {
        return MyBatis3Utils.insert(this::insert, row, boardAttachVO, c ->
            c.withMappedColumn(uuid)
            .withMappedColumn(uploadPath)
            .withMappedColumn(fileName)
            .withMappedColumn(fileType)
            .withMappedColumn(bno)
        );
    }

    default int insertMultiple(Collection<BoardAttachVO> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, boardAttachVO, c ->
            c.withMappedColumn(uuid)
            .withMappedColumn(uploadPath)
            .withMappedColumn(fileName)
            .withMappedColumn(fileType)
            .withMappedColumn(bno)
        );
    }

    default int insertSelective(BoardAttachVO row) {
        return MyBatis3Utils.insert(this::insert, row, boardAttachVO, c ->
            c.withMappedColumnWhenPresent(uuid, row::getUuid)
            .withMappedColumnWhenPresent(uploadPath, row::getUploadPath)
            .withMappedColumnWhenPresent(fileName, row::getFileName)
            .withMappedColumnWhenPresent(fileType, row::getFileType)
            .withMappedColumnWhenPresent(bno, row::getBno)
        );
    }

    default Optional<BoardAttachVO> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, boardAttachVO, completer);
    }

    default List<BoardAttachVO> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, boardAttachVO, completer);
    }

    default List<BoardAttachVO> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, boardAttachVO, completer);
    }

    default Optional<BoardAttachVO> selectByPrimaryKey(String uuid_) {
        return selectOne(c ->
            c.where(uuid, isEqualTo(uuid_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, boardAttachVO, completer);
    }

    static UpdateDSL updateAllColumns(BoardAttachVO row, UpdateDSL dsl) {
        return dsl.set(uuid).equalTo(row::getUuid)
                .set(uploadPath).equalTo(row::getUploadPath)
                .set(fileName).equalTo(row::getFileName)
                .set(fileType).equalTo(row::getFileType)
                .set(bno).equalTo(row::getBno);
    }

    static UpdateDSL updateSelectiveColumns(BoardAttachVO row, UpdateDSL dsl) {
        return dsl.set(uuid).equalToWhenPresent(row::getUuid)
                .set(uploadPath).equalToWhenPresent(row::getUploadPath)
                .set(fileName).equalToWhenPresent(row::getFileName)
                .set(fileType).equalToWhenPresent(row::getFileType)
                .set(bno).equalToWhenPresent(row::getBno);
    }

    default int updateByPrimaryKey(BoardAttachVO row) {
        return update(c ->
            c.set(uploadPath).equalTo(row::getUploadPath)
            .set(fileName).equalTo(row::getFileName)
            .set(fileType).equalTo(row::getFileType)
            .set(bno).equalTo(row::getBno)
            .where(uuid, isEqualTo(row::getUuid))
        );
    }

    default int updateByPrimaryKeySelective(BoardAttachVO row) {
        return update(c ->
            c.set(uploadPath).equalToWhenPresent(row::getUploadPath)
            .set(fileName).equalToWhenPresent(row::getFileName)
            .set(fileType).equalToWhenPresent(row::getFileType)
            .set(bno).equalToWhenPresent(row::getBno)
            .where(uuid, isEqualTo(row::getUuid))
        );
    }
}