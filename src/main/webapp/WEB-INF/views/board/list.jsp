<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../includes/header.jsp"%>

<body id="page-top">

  <!-- Page Wrapper -->
  <div id="wrapper">
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content">

        <!-- topbar도 별도 파일에 분리하자! -->
        <%@include file="../includes/topbar.jsp"%>

        <!-- Begin Page Content -->
        <div class="container-fluid">

          <!-- Page Heading -->
          <h1 class="h3 mb-2 text-gray-800">Tables</h1>

          <!-- DataTales Example -->
          <div class="card shadow mb-4">
            <div class="card-header py-3">
              <div class="form-row float-left">
                <div class="font-weight-bold text-primary">Board List Page</div>
              </div>
              <div class="form-row float-right">
                <button id="regBtn" type="button" class="btn btn-outline-primary btn-sm btn-block">Register New Board</button>
              </div>
            </div>
            <div class="card-body">
              <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                  <thead>
                    <tr>
                      <th>#번호</th>
                      <th>제목</th>
                      <th>작성자</th>
                      <th>작성일</th>
                      <th>수정일</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${list}" var="board">
                      <tr>
                        <td><c:out value="${board.bno}" /></td>
                        <td><a class="move" href="${board.bno}"><c:out value="${board.title}" /></a> <c:if test="${board.replyCount gt 0}"><span class="badge badge-info badge-counter"><c:out value="${board.replyCount}" /></span></c:if></td>
                        <td><c:out value="${board.writer}" /></td>
                        <%-- https://github.com/sargue/java-time-jsptags --%>
                        <td><javatime:format value="${board.regdate}" pattern="yyyy-MM-dd"></javatime:format></td>
                        <td><javatime:format value="${board.updateDate}" pattern="yyyy-MM-dd"></javatime:format></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>

                <div class="col-lg-12">
                  <form id="searchForm" action="/board/list" method="get">
                    <input type="hidden" name="searchCodes" value="${pageMaker.searchCodesWithJoined}">
                    <c:forEach var="searchType" items="${allSearchTypeSet}">                                                      
                    <div class="form-check form-check-inline">                    
                      <input class="form-check-input" type="checkbox" id="SEARCH_TYPE_${searchType.name}" value="${searchType.code}" 
                          <c:if test="${fn:contains(pageMaker.searchCodes,searchType.code)}">checked="checked"</c:if>>
                      <label class="form-check-label" for="SEARCH_TYPE_${searchType.name}">${searchType.description}</label>
                    </div>
                    </c:forEach>
                    <input type="text" name="keyword" value="${pageMaker.keyword}">                  
                    <input type="hidden" name="pageNum" value="${pageMaker.pageNum}">
                    <input type="hidden" name="amount" value="${pageMaker.amount}">
                    <button type="button" class="btn btn-outline-primary">Search</button>
                  </form>
                </div>

                <!-- 
                  BootStrap 버전이 달라서 교제와 마크업이 다르다, 아래 공식 페이지의 내용을 참조해서 넣도록 하자!
                  https://getbootstrap.com/docs/4.0/components/pagination/
                 -->
                <ul class="pagination justify-content-end">
                  <c:if test="${pageMaker.prev}">
                    <li class="page-item"><a class="page-link" href="${pageMaker.startPage - 1}">Previous</a></li>
                  </c:if>
                  <c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage}">
                    <li class="page-item ${pageMaker.pageNum eq num ? 'active':''}"><a class="page-link" href="${num}">${num}</a></li>
                  </c:forEach>
                  <c:if test="${pageMaker.next}">
                    <li class="page-item"><a class="page-link" href="${pageMaker.endPage + 1}">Next</a></li>
                  </c:if>
                </ul>

                <form id="actionForm" action="/board/list" method="get">
                  <input type="hidden" name="pageNum" value="${pageMaker.pageNum}">
                  <input type="hidden" name="amount" value="${pageMaker.amount}">
                  <input type="hidden" name="searchCodes" value="<c:out value="${pageMaker.searchCodesWithJoined}"/>">
                  <input type="hidden" name="keyword" value="<c:out value="${pageMaker.keyword}"/>">
                </form>
              </div>
            </div>
          </div>

        </div>
        <!-- /.container-fluid -->

      </div>
      <!-- End of Main Content -->


      <!-- Footer -->
      <%@include file="../includes/footer.jsp"%>
      <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

  </div>
  <!-- End of Page Wrapper -->
  <%@include file="../includes/dialogAndScript.jsp"%>
  <script type="text/javascript">
  $(document).ready(function() {
    var submitProcess = function(e) {
      // 검색조건 체크 박스에서 체크된 코드를 ,로 join하여 하나의 문자열로 만든다.
      var selectedSearchCodes = $searchForm.find("input[id^='SEARCH_TYPE_']:checked").map(function() {
        return $(this).val();  
      }).get().join();
    
      if (!selectedSearchCodes) {
        alert("검색 체크 박스를 하나 이상 선택해주세요!");
        return;
      }
    
      if (!$searchForm.find("input[name='keyword']").val()) {
        alert("검색 키워드를 입력해주세요!");
        return;
      }

      $searchForm.find("input[name='searchCodes']").val(selectedSearchCodes);
      $searchForm.find("input[name='pageNum']").val(1);

      e.preventDefault();
      $searchForm.submit();
    }; 
        
    var $searchForm = $('#searchForm');
    
    // form에 input box가 한개이면 엔터를 누르면 submit이 일어난다. 이와 관련해서 처리부분을 함수로 분리하여 실행되게한다.
    $('input[name="keyword"]').keydown(function(e) {
        if (event.keyCode === 13) {
          submitProcess(e);
        }
    });
    
    $("#searchForm button").on("click", function(e){
      submitProcess(e);
    });
  });
  </script>
</body>

</html>