package org.fp024.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.fp024.domain.CodeOperation;

/**
 * 아래 클래스 보고 참조해서 수정했다. 일반 enum도 잘 처리할 수 있지만, 일반 enum에 대한 처리는 EnumTypeHandler으로 기본 등록이 되어있는데, 이것을
 * 바꾸거나 제거하는 방법은 잘 모르겠다.
 *
 * @see org.apache.ibatis.type.EnumTypeHandler
 */
@MappedTypes(CodeOperation.class)
public class CustomEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
  private final Class<E> type;

  public CustomEnumTypeHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    String saveData = getSaveData(parameter);
    if (jdbcType == null) {
      ps.setString(i, saveData);
    } else {
      ps.setObject(i, saveData, jdbcType.TYPE_CODE); // see r3589
    }
  }

  /** CodeOperation 을 구현한 Enum이면 code()값을 반환 그렇지 않으면 name() 값을 반환<br> */
  private String getSaveData(E parameter) {
    if (parameter instanceof CodeOperation) {
      return ((CodeOperation) parameter).getCode();
    } else {
      return parameter.name();
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String s = rs.getString(columnName);
    return s == null ? null : getEnum(s);
  }

  /** 저장소의 문자열로 알맞은 Enum 인스턴스를 반환 */
  private E getEnum(String s) {
    try {
      E[] values = type.getEnumConstants();
      if (values[0] instanceof CodeOperation) {
        for (E e : values) {
          if (((CodeOperation) e).getCode().equals(s)) {
            return e;
          }
        }
        throw new IllegalStateException("저장소 enum code가 잘못되었는지 확인 필요");
      }
      return Enum.valueOf(type, s);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String s = rs.getString(columnIndex);
    return s == null ? null : getEnum(s);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String s = cs.getString(columnIndex);
    return s == null ? null : getEnum(s);
  }
}
