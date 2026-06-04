package org.fp024.service;

import static org.mybatis.dynamic.sql.SqlBuilder.isLikeWhenPresent;
import static org.mybatis.dynamic.sql.SqlBuilder.or;

import java.util.ArrayList;
import java.util.List;
import org.fp024.domain.Criteria;
import org.fp024.domain.SearchType;
import org.mybatis.dynamic.sql.AndOrCriteriaGroup;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectModel;
import org.springframework.stereotype.Component;

/**
 * 검색 조건 만드는 복잡한 부분을 외부 클래스로 분리했다.
 *
 * <p>예전에 private 로 BoardServiceImpl에 내장된 메서드였는데, ReflectionTestUtils로 테스트가 잘안된다. ㅠㅠ <br>
 * 복잡함을 조금이나마 줄이고 싶어서, 별도 클래스로 분리를 했다.
 */
@Component
public class BoardMapperSupport {
  public QueryExpressionDSL<SelectModel> addSearchWhereClause(
      QueryExpressionDSL<SelectModel> selectDSL, Criteria criteria) {
    List<SearchType> searchTypeList = criteria.getSearchTypeSet().stream().toList();
    List<AndOrCriteriaGroup> subCriteriaList = new ArrayList<>();

    for (int i = 0; i < searchTypeList.size(); i++) {
      if (i > 0) {
        subCriteriaList.add(
            or(
                searchTypeList.get(i).getColumn(),
                isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards)));
      }
    }
    if (searchTypeList.size() == 1) {
      selectDSL.where(
          searchTypeList.getFirst().getColumn(),
          isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards));
    } else if (searchTypeList.size() > 1) {
      selectDSL.where(
          searchTypeList.getFirst().getColumn(),
          isLikeWhenPresent(criteria.getKeyword()).map(this::addWildcards),
          subCriteriaList);
    }

    return selectDSL;
  }

  private String addWildcards(String keyword) {
    return "%" + keyword + "%";
  }
}
