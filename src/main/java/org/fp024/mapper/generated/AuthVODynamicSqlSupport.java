package org.fp024.mapper.generated;

import java.sql.JDBCType;
import org.fp024.domain.MemberAuthType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class AuthVODynamicSqlSupport {
    public static final AuthVO authVO = new AuthVO();

    public static final SqlColumn<String> userId = authVO.userId;

    public static final SqlColumn<MemberAuthType> auth = authVO.auth;

    public static final class AuthVO extends AliasableSqlTable<AuthVO> {
        public final SqlColumn<String> userId = column("USERID", JDBCType.VARCHAR).withJavaProperty("userId");

        public final SqlColumn<MemberAuthType> auth = column("AUTH", JDBCType.VARCHAR, "org.fp024.typehandler.CustomEnumTypeHandler").withJavaProperty("auth");

        public AuthVO() {
            super("TBL_MEMBER_AUTH", AuthVO::new);
        }
    }
}