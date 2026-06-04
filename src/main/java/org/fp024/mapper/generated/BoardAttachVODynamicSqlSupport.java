package org.fp024.mapper.generated;

import java.sql.JDBCType;
import org.fp024.domain.FileType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class BoardAttachVODynamicSqlSupport {
    public static final BoardAttachVO boardAttachVO = new BoardAttachVO();

    public static final SqlColumn<String> uuid = boardAttachVO.uuid;

    public static final SqlColumn<String> uploadPath = boardAttachVO.uploadPath;

    public static final SqlColumn<String> fileName = boardAttachVO.fileName;

    public static final SqlColumn<FileType> fileType = boardAttachVO.fileType;

    public static final SqlColumn<Long> bno = boardAttachVO.bno;

    public static final class BoardAttachVO extends AliasableSqlTable<BoardAttachVO> {
        public final SqlColumn<String> uuid = column("UUID", JDBCType.VARCHAR).withJavaProperty("uuid");

        public final SqlColumn<String> uploadPath = column("UPLOADPATH", JDBCType.VARCHAR).withJavaProperty("uploadPath");

        public final SqlColumn<String> fileName = column("FILENAME", JDBCType.VARCHAR).withJavaProperty("fileName");

        public final SqlColumn<FileType> fileType = column("FILETYPE", JDBCType.CHAR, "org.fp024.typehandler.CustomEnumTypeHandler").withJavaProperty("fileType");

        public final SqlColumn<Long> bno = column("BNO", JDBCType.NUMERIC).withJavaProperty("bno");

        public BoardAttachVO() {
            super("TBL_ATTACH", BoardAttachVO::new);
        }
    }
}