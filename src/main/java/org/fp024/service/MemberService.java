package org.fp024.service;


import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.on;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.fp024.domain.MemberDTO;
import org.fp024.mapper.MemberQueryMapper;
import org.fp024.mapper.generated.AuthVODynamicSqlSupport;
import org.fp024.mapper.generated.MemberVODynamicSqlSupport;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Service;

/** 회원 서비스 */
@RequiredArgsConstructor
@Service
public class MemberService {
  private final MemberQueryMapper memberQueryMapper;

  public Optional<MemberDTO> read(String userId) {
    return memberQueryMapper.selectOne(
        select(
                MemberVODynamicSqlSupport.userId,
                MemberVODynamicSqlSupport.userPassword,
                MemberVODynamicSqlSupport.userName,
                MemberVODynamicSqlSupport.enabled,
                MemberVODynamicSqlSupport.registerDate,
                MemberVODynamicSqlSupport.updateDate,
                AuthVODynamicSqlSupport.auth)
            .from(MemberVODynamicSqlSupport.memberVO, "m")
            .leftJoin(
                AuthVODynamicSqlSupport.authVO,
                "a",
                on(MemberVODynamicSqlSupport.userId, isEqualTo(AuthVODynamicSqlSupport.userId)))
            .where(MemberVODynamicSqlSupport.userId, isEqualTo(userId))
            .build()
            .render(RenderingStrategies.MYBATIS3));
  }
}
