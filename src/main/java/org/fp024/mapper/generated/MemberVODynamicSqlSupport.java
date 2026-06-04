package org.fp024.mapper.generated;

import java.sql.JDBCType;
import java.time.LocalDateTime;
import org.fp024.domain.EnabledType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class MemberVODynamicSqlSupport {
    public static final MemberVO memberVO = new MemberVO();

    public static final SqlColumn<String> userId = memberVO.userId;

    public static final SqlColumn<String> userPassword = memberVO.userPassword;

    public static final SqlColumn<String> userName = memberVO.userName;

    public static final SqlColumn<LocalDateTime> registerDate = memberVO.registerDate;

    public static final SqlColumn<LocalDateTime> updateDate = memberVO.updateDate;

    public static final SqlColumn<EnabledType> enabled = memberVO.enabled;

    public static final class MemberVO extends AliasableSqlTable<MemberVO> {
        public final SqlColumn<String> userId = column("USERID", JDBCType.VARCHAR).withJavaProperty("userId");

        public final SqlColumn<String> userPassword = column("USERPW", JDBCType.VARCHAR).withJavaProperty("userPassword");

        public final SqlColumn<String> userName = column("USERNAME", JDBCType.VARCHAR).withJavaProperty("userName");

        public final SqlColumn<LocalDateTime> registerDate = column("REGDATE", JDBCType.TIMESTAMP).withJavaProperty("registerDate");

        public final SqlColumn<LocalDateTime> updateDate = column("UPDATEDATE", JDBCType.TIMESTAMP).withJavaProperty("updateDate");

        public final SqlColumn<EnabledType> enabled = column("ENABLED", JDBCType.CHAR, "org.fp024.typehandler.CustomEnumTypeHandler").withJavaProperty("enabled");

        public MemberVO() {
            super("TBL_MEMBER", MemberVO::new);
        }
    }
}