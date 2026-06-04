package org.fp024.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.fp024.domain.MemberDTO;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface MemberQueryMapper {
  /**
   * 회원과 권한이 1:N이여서 ResultMap을 꼭 구성해야했따. Mapper XML에 정의했다. <br>
   * 가이드에서도 ResultMap을 사용하라고 했다. <br>
   * 기본 쿼리문에서 없으면 없는대로 null로 들어가니 메서드를 따로 만들 필요는 없고 selectOne에만 추가해주면 되겠다.<br>
   * <a
   * href="https://mybatis.org/mybatis-dynamic-sql/docs/select.html#xml-mapper-for-join-statements">XML
   * Mapper for Join Statements</a>
   */
  @SelectProvider(type = SqlProviderAdapter.class, method = "select")
  @ResultMap("MemberResultMap")
  Optional<MemberDTO> selectOne(SelectStatementProvider selectStatement);
}
