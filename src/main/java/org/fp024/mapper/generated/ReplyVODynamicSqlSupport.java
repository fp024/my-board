package org.fp024.mapper.generated;

import java.sql.JDBCType;
import java.time.LocalDateTime;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class ReplyVODynamicSqlSupport {
    public static final ReplyVO replyVO = new ReplyVO();

    public static final SqlColumn<Long> rno = replyVO.rno;

    public static final SqlColumn<Long> bno = replyVO.bno;

    public static final SqlColumn<String> reply = replyVO.reply;

    public static final SqlColumn<String> replyer = replyVO.replyer;

    public static final SqlColumn<LocalDateTime> replyDate = replyVO.replyDate;

    public static final SqlColumn<LocalDateTime> updateDate = replyVO.updateDate;

    public static final class ReplyVO extends AliasableSqlTable<ReplyVO> {
        public final SqlColumn<Long> rno = column("RNO", JDBCType.NUMERIC).withJavaProperty("rno");

        public final SqlColumn<Long> bno = column("BNO", JDBCType.NUMERIC).withJavaProperty("bno");

        public final SqlColumn<String> reply = column("REPLY", JDBCType.VARCHAR).withJavaProperty("reply");

        public final SqlColumn<String> replyer = column("REPLYER", JDBCType.VARCHAR).withJavaProperty("replyer");

        public final SqlColumn<LocalDateTime> replyDate = column("REPLYDATE", JDBCType.TIMESTAMP).withJavaProperty("replyDate");

        public final SqlColumn<LocalDateTime> updateDate = column("UPDATEDATE", JDBCType.TIMESTAMP).withJavaProperty("updateDate");

        public ReplyVO() {
            super("TBL_REPLY", ReplyVO::new);
        }
    }
}