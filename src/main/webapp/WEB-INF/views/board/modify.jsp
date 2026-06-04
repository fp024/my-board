<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../includes/header.jsp"%>
<link href="/resources/css/upload-ajax.css" rel="stylesheet">
<%@include file="../includes/tui-editor-css.jsp" %>

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
        <h1 class="h3 mb-2 text-gray-800">Board Modify Page</h1>


        <!-- 게시물 수정 폼 영역 -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Board Modify Page</h6>
          </div>
          <div class="card-body">
            <form id="modify-form" role="form" action="/board/modify" method="post">
              <input type="hidden" name="bno" value="<c:out value="${board.bno}"/>">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
              <input type="hidden" name="pageNum" value="<c:out value="${criteria.pageNum}"/>">
              <input type="hidden" name="amount" value="<c:out value="${criteria.amount}"/>">
              <input type="hidden" name="searchCodes" value="<c:out value="${criteria.searchCodesWithJoined}"/>">
              <input type="hidden" name="keyword" value="<c:out value="${criteria.keyword}"/>">

              <div class="form-group">
                <label>Bno</label> <input class="form-control" name="boardVO.bno" value="<c:out value='${board.bno}'/>" readonly="readonly">
              </div>
              <div class="form-group">
                <label>Title</label> <input class="form-control" name="boardVO.title" value="<c:out value='${board.title}'/>">
              </div>
              <div class="form-group">
                <label>Editor area</label>
                <%-- <textarea class="form-control" rows="5" name="boardVO.content"><c:out value='${board.content}' /></textarea>--%>
                <input type="hidden" name="boardVO.content">
                <div id="editor"></div>
              </div>
              <div class="form-group">
                <label>Writer</label> <input name="boardVO.writer" class="form-control" readonly="readonly" value="<c:out value='${board.writer}'/>">
              </div>
              <sec:authentication property="principal" var="pinfo" />
              <sec:authorize access="isAuthenticated()">
                <c:if test="${pinfo.username eq board.writer}">
                  <button type="submit" data-oper="modify" class="btn btn-warning">Modify</button>
                  <button type="submit" data-oper="remove" class="btn btn-danger">Remove</button>
                </c:if>
              </sec:authorize>
              <button type="submit" data-oper="list" class="btn btn-secondary">List</button>
            </form>
          </div>
        </div>

        <!-- 첨부파일 수정 영역 -->
        <!-- 첨부파일 표시 영역 -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <div>
              <span class="font-weight-bold text-primary"><i class="fas fa-paperclip"></i>Files</span>
            </div>
          </div>
          <div class="card-body">
            <div class="form-group uploadDiv">
              <input type="file" name="uploadFile" multiple>
            </div>

            <div class="uploadResult">
              <ul>
              </ul>
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

<!-- 첨부파일 보기/추가/삭제 -->
<script type="text/javascript">
  $(document).ready(function () {
    var bno = '<c:out value="${board.bno}"/>';

    function showUploadResult(attachList) {
      var str = "";
      $(attachList).each(function (i, attach) {
        if (attach.fileType === 'IMAGE') {
          var fileCallPath = encodeURIComponent(
              attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
          var originPath =
              attach.uploadPath + "/" + attach.uuid + "_" + attach.fileName;
          console.log(originPath);
          str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid
              + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType
              + "'><div>"
              + "<span>" + attach.fileName + "</span>"
              + "<button type='button' data-file=\'" + fileCallPath
              + "\' data-type='IMAGE' class='btn btn-warning btn-circle'><i class='fas fa-times'></i></button><br>"
              + "<img src='/display?fileName=" + fileCallPath + "'></a>"
              + "</div></li>";
        } else {
          var fileCallPath = encodeURIComponent(
              attach.uploadPath + "/" + attach.uuid + "_" + attach.fileName);
          str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid
              + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType
              + "'><div>"
              + "<span>" + attach.fileName + "</span>"
              + "<button type='button' data-file=\'" + fileCallPath
              + "\' data-type='NORMAL' class='btn btn-warning btn-circle'><i class='fas fa-times'></i></button><br>"
              + "<img src='/resources/img/attach.png'>"
              + "</div></li>";
        }
      });
      $(".uploadResult ul").append(str);
    }

    $.getJSON("/board/getAttachList", {bno, bno}, function (arr) {
      console.log(arr);
      showUploadResult(arr);
    });

    // 삭제 버튼 이벤트, 화면에서만 삭제함.
    $(".uploadResult").on("click", "button", function (e) {
      console.log("delete file");
      if (confirm("이 파일을 삭제하시겠습니까?")) {
        var targetLi = $(this).closest("li");
        targetLi.remove();
      }
    });

    // 첨부 파일 추가
    var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
    var maxSize = 5242880;

    function checkExtension(fileName, fileSize) {
      if (fileSize > maxSize) {
        alert("파일 사이즈 초과");
        return false;
      }

      if (regex.test(fileName)) {
        alert("해당 종류의 파일은 업로드할 수 없습니다.");
        return false;
      }
      return true;
    }

    var csrfHeaderName = "${_csrf.headerName}";
    var csrfTokenValue = "${_csrf.token}";

    $("input[type='file']").change(function () {
      var formData = new FormData();
      var inputFile = $("input[name='uploadFile']");
      var files = inputFile[0].files;

      for (var i = 0; i < files.length; i++) {
        if (!checkExtension(files[i].name, files[i].size)) {
          return false;
        }
        formData.append("uploadFile", files[i]);
      }

      $.ajax({
        url: '/uploadAjaxAction',
        processData: false,
        contentType: false,
        data: formData,
        type: 'POST',
        beforeSend: function (xhr) {
          xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
        },
        dataType: 'json',
        success: function (result) {
          console.log(result);
          showUploadResult(result);
        }
      });
    });
  });
</script>

<!-- 게시물 폼 등록 처리 -->
<script type="text/javascript">
  $(document).ready(function () {
    var $formObj = $("#modify-form");

    $formObj.find(".btn").on("click", function (e) {
      e.preventDefault();

      var operation = $(this).data("oper"); // data-oper 속성을 이렇게 읽는구나?

      console.log(operation);

      if (operation === "remove") {
        $formObj.attr("action", "/board/remove");

      } else if (operation === "list") {
        $formObj.attr("action", "/board/list").attr("method", "get");
        var pageNumTag = $("input[name='pageNum']").clone();
        var amountTag = $("input[name='amount']").clone();
        var searchCodes = $("input[name='searchCodes']").clone();
        var keyword = $("input[name='keyword']").clone();

        $formObj.empty();
        $formObj.append(pageNumTag);
        $formObj.append(amountTag);
        $formObj.append(searchCodes);
        $formObj.append(keyword);
      } else if (operation === "modify") {
        console.log("submit clicked");

        $formObj.find("input[name='boardVO.content']").val(editor.getMarkdown());
        var str = "";

        $(".uploadResult ul li").each(function (i, obj) {
          var jobj = $(obj);
          console.dir(jobj);
          str += "<input type='hidden' name='attachList[" + i + "].fileName' value='" + jobj.data(
              "filename") + "'>";
          str += "<input type='hidden' name='attachList[" + i + "].uuid' value='" + jobj.data(
              "uuid") + "'>";
          str += "<input type='hidden' name='attachList[" + i + "].uploadPath' value='"
              + jobj.data("path") + "'>";
          str += "<input type='hidden' name='attachList[" + i + "].fileType' value='" + jobj.data(
              "type") + "'>";
        });
        $formObj.append(str).submit();
      }
      $formObj.submit();
    });
  });
</script>
<%@include file="../includes/tui-editor-js.jsp" %>
<script>
  const { Editor } = toastui;
  const { codeSyntaxHighlight } = Editor.plugin;

  const editor = new Editor({
    el: document.querySelector('#editor'),
    height: '600px',
    initialEditType: 'markdown',
    plugins: [codeSyntaxHighlight],
    previewStyle: 'vertical',
    initialValue: ${boardContentJson}
  });
</script>
</body>

</html>