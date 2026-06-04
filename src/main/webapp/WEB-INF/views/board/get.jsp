<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../includes/header.jsp" %>
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
      <%@include file="../includes/topbar.jsp" %>

      <!-- Begin Page Content -->
      <div class="container-fluid">

        <!-- Page Heading -->
        <h1 class="h3 mb-2 text-gray-800">Board Read Page</h1>


        <!-- DataTales Example -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Board Read Page</h6>
          </div>
          <div id="board-read-body" class="card-body">
            <div class="form-group">
              <label>Bno</label> <input class="form-control" name="bno" value="<c:out value='${board.bno}'/>"
                                        readonly="readonly">
            </div>
            <div class="form-group">
              <label>Title</label> <input class="form-control" name="title" value="<c:out value='${board.title}'/>"
                                          readonly="readonly">
            </div>
            <div class="form-group">
              <label>Viewer Area</label>
              <%-- <textarea class="form-control" rows="5" name="content" readonly="readonly"><c:out value=''/></textarea> --%>
              <div class="border rounded" style="min-height: 500px">
                <div id="viewer"></div>
              </div>
            </div>
            <div class="form-group">
              <label>Writer</label> <input class="form-control" name="writer" readonly="readonly"
                                           value="<c:out value='${board.writer}'/>">
            </div>
            <div class="form-group">
              <label>Register Date</label> <input class="form-control" name="regDate" readonly="readonly"
                                                  value="<javatime:format value="${board.regdate}" pattern="yyyy-MM-dd HH:mm:ss" />">
            </div>
            <div class="form-group">
              <label>Update Date</label> <input class="form-control" name="regDate" readonly="readonly"
                                                value="<javatime:format value="${board.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" />">
            </div>
            <sec:authentication property="principal" var="pinfo" />
            <sec:authorize access="isAuthenticated()">
              <c:if test="${pinfo.username eq board.writer}">
                <button data-oper="modify" class="btn btn-primary">Modify</button>
              </c:if>
            </sec:authorize>
            <button data-oper="list" class="btn btn-secondary">List</button>

            <form id="operForm" action="/board/modify" method="get">
              <input type="hidden" id="bno" name="bno" value="<c:out value="${board.bno}"/>">
              <input type="hidden" name="pageNum" value="<c:out value="${criteria.pageNum}"/>">
              <input type="hidden" name="amount" value="<c:out value="${criteria.amount}"/>">
              <input type="hidden" name="searchCodes" value="<c:out value="${criteria.searchCodesWithJoined}"/>">
              <input type="hidden" name="keyword" value="<c:out value="${criteria.keyword}"/>">
            </form>
          </div>
        </div>

        <!-- 첨부파일이 이미지일 때 크게 보여줄 Wrapper 영역 -->
        <div class="bigPictureWrapper">
          <div class="bigPicture">
          </div>
        </div>

        <!-- 첨부파일 표시 영역 -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <div>
              <span class="font-weight-bold text-primary"><i class="fas fa-paperclip"></i>Files</span>
            </div>
          </div>
          <div class="card-body">
            <div class="uploadResult">
              <ul>
              </ul>
            </div>
          </div>
        </div>

        <!-- 댓글 영역 -->
        <div class="card shadow mb-4">
          <div class="card-header py-3">
            <div>
              <span class="font-weight-bold text-primary"><i class="fa fa-comments fa-fw"></i>Reply</span>
              <sec:authorize access="isAuthenticated()">
                <button id="addReplyBtn" class="btn btn-primary btn-sm float-md-right">New Reply</button>
              </sec:authorize>
            </div>
          </div>
          <div class="card-body">
            <ul id="replyUL" class="list-group list-group-flush">
              <!-- start reply -->
              <li class="list-group-item left clearfix">댓글이 없습니다.</li>
              <!-- end reply -->
            </ul>
          </div>
          <div id="reply-page-navigation" class="card-footer">
          </div>
        </div>

      </div>
      <!-- /.container-fluid -->


    </div>
    <!-- End of Main Content -->


    <!-- Footer -->
    <%@include file="../includes/footer.jsp" %>
    <!-- End of Footer -->

  </div>
  <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->
<%@include file="../includes/dialogAndScript.jsp" %>

<!-- 댓글 등록 Modal-->
<div class="modal fade" id="replyModal" tabindex="-1" role="dialog" aria-labelledby="replyModalLabel"
     aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="replyModalLabel">REPLY MODAL</h5>
        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label>Reply</label>
          <input class="form-control" name="reply" value="New Reply!!!!!">
        </div>
        <div class="form-group">
          <label>Replyer</label>
          <input class="form-control" name="replyer" value="replyer" readonly="readonly">
        </div>
        <div class="form-group">
          <label>Reply Date</label>
          <input class="form-control" name="replyDate" value="">
        </div>
      </div>
      <div class="modal-footer">
        <button id="modalModBtn" class="btn btn-warning" type="button" data-dismiss="modal">Modify</button>
        <button id="modalRemoveBtn" class="btn btn-danger" type="button" data-dismiss="modal">Remove</button>
        <button id="modalRegisterBtn" class="btn btn-primary" type="button" data-dismiss="modal">Register</button>
        <button id="modalCloseBtn" class="btn btn-secondary" type="button" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!-- 첨부파일 Ajax 처리 -->
<script>
  $(document).ready(function () {
    var bno = '<c:out value="${board.bno}" />'

    $.getJSON("/board/getAttachList", {bno: bno}, function (arr) {
      console.log(arr);

      var str = "";

      $(arr).each(function (i, attach) {
        if (attach.fileType === 'IMAGE') {
          var fileCallPath = encodeURIComponent(
              attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
          var originPath =
              attach.uploadPath + "/" + attach.uuid + "_"
              + attach.fileName;
          console.log(originPath);
          str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid
              + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType
              + "'><div>"
              + "<img src='/display?fileName=" + fileCallPath + "'></a>"
              + "</div></li>";
        } else {
          var fileCallPath = encodeURIComponent(
              attach.uploadPath + "/" + attach.uuid + "_" + attach.fileName);
          str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid
              + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType
              + "'><div>"
              + "<span>" + attach.fileName + "</span><br>"
              + "<a href='/download?fileName=" + fileCallPath
              + "'><img src='/resources/img/attach.png'></a>"
              + "</div></li>";
        }
      });
      $(".uploadResult ul").append(str);
    });

    $(".uploadResult").on("click", "li", function (e) {
      console.log("view image");

      var liObj = $(this);

      var path = encodeURIComponent(
          liObj.data("path") + "/" + liObj.data("uuid") + "_" + liObj.data("filename"));

      if (liObj.data("type") === "IMAGE") {
        showImage(path.replace(new RegExp(/\\/g), "/"));
      } else {
        self.location = "/download?fileName=" + path;
      }

    });

    function showImage(fileCallPath) {
      console.log(fileCallPath);
      $(".bigPictureWrapper").css("display", "flex").show();
      $(".bigPicture")
      .html("<img src='/display?fileName=" + fileCallPath + "'>")
      .animate({width: '100%', height: '100%'}, 1000);
    }

    $(".bigPictureWrapper").on("click", function (e) {
      $(".bigPicture").animate({width: '0%', height: '0%'}, 1000);
      setTimeout(function () {
        $(".bigPictureWrapper").hide();
      }, 1000);
    });

  });
</script>

<!-- 댓글 Ajax 처리 -->
<script type="text/javascript" src="/resources/js/reply.js"></script>
<script type="text/javascript">
  var bnoValue = '<c:out value="${board.bno}"/>';
  var replyUL = $('#replyUL');
  var replyPageFooter = $('#reply-page-navigation');
  var pageNum = 1;

  /** 페이지 네비게이션 그리기 */
  function showReplyPage(pageNum, pageSize, pageNavigationSize, replyCount) {
    var endNum = Math.ceil(pageNum / pageNavigationSize) * pageNavigationSize;
    var startNum = endNum - (pageNavigationSize - 1);

    var prev = startNum != 1;
    var next = false;

    if (endNum * pageSize >= replyCount) {
      endNum = Math.ceil(replyCount / pageSize);
    }

    if (endNum * pageSize < replyCount) {
      next = true;
    }

    var str = "<ul class='pagination pull-right'>";

    if (prev) {
      str += "<li class='page-item'><a class='page-link' href='" + (startNum - 1) + "'>Previous</a></li>"
    }

    for (var i = startNum; i <= endNum; i++) {
      var active = pageNum == i ? "active" : "";
      str += "<li class='page-item " + active + "'><a class='page-link' href='" + i + "'>" + i + "</a></li>"
    }

    if (next) {
      str += "<li class='page-item'><a class='page-link' href='" + (endNum + 1) + "'>Next</a></li>"
    }

    str += "</ul></div>";

    console.log("page-navigation" + str);

    replyPageFooter.html(str);
  }


  function showList(page) {
    console.log("show list page: " + page);
    pageNum = page; // 전역변수 변경, 함수 내에서 페이지번호를 조정할 수 있는 부분이 있어서 변경해줘야했다.

    replyService.getList({bno: bnoValue, page: page || 1}, function (pageSize, pageNavigationSize, replyCount, list) {
      console.log("replyCount: " + replyCount);
      console.log("list: " + list);
      console.log(list);

      if (page == -1) {
        showList(Math.ceil(replyCount / pageSize)); // // 페이지 사이즈를 서버의 데이터로 받아왔다., Javascript는 정수 나눗셈이 이나여서 실수를 곱해주지 않아도 되겠다.
        return;
      }

      var str = "";
      if (list == null || list.length == 0) {
        if (page > 1) {
          showList(page - 1);
        } else {
          replyUL.html('<li class="list-group-item left clearfix">댓글이 없습니다.</li>');
        }
        return;
      }

      for (var i = 0, len = list.length || 0; i < len; i++) {
        str += '<li class="reply-item list-group-item left clearfix" data-rno=' + list[i].rno + '>';
        str += '<div>';
        str += '<div class="header">';
        str += '<strong class="font-weight-bold text-primary">' + list[i].replyer + '</strong>';
        str += '<small class="float-right text-muted">' + replyService.displayTime(list[i].replyDate) + '</small>';
        str += '</div>'
        str += '<p>' + list[i].reply + '</p>'
        str += '</div>'
        str += '</li>'
      }
      replyUL.html(str);
      showReplyPage(page, pageSize, pageNavigationSize, replyCount);
    });
  }

  showList(pageNum);

  var replyModal = $("#replyModal");
  var modalInputReply = replyModal.find("input[name='reply']");
  var modalInputReplyer = replyModal.find("input[name='replyer']");
  var modalInputReplyDate = replyModal.find("input[name='replyDate']");

  var modalModBtn = $("#modalModBtn");
  var modalRemoveBtn = $("#modalRemoveBtn");
  var modalRegisterBtn = $("#modalRegisterBtn");

  var replyer = null;

  <sec:authorize access="isAuthenticated()">
  replyer = '<sec:authentication property="principal.username"/>';
  </sec:authorize>

  var csrfHeaderName = "${_csrf.headerName}";
  var csrfTokenValue = "${_csrf.token}";

  $("#addReplyBtn").on("click", function (e) {
    replyModal.find("input").val("");
    replyModal.find("input[name='replyer']").val(replyer);
    modalInputReplyDate.closest("div").hide();
    replyModal.find("button[id != 'modalCloseBtn']").hide();

    modalRegisterBtn.show();

    replyModal.modal("show");
  });

  // Ajax String Security Header
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
  });

  modalRegisterBtn.on("click", function (e) {
    var reply = {
      reply: modalInputReply.val(),
      replyer: modalInputReplyer.val(),
      bno: bnoValue
    }

    replyService.add(reply, function (result) {
      alert(result);

      replyModal.find("input").val("");
      replyModal.modal("hide");

      showList(-1);
    });
  })

  replyUL.on("click", "li.reply-item", function (e) {
    var rno = $(this).data("rno");
    replyService.get(rno, function (reply) {
      modalInputReply.val(reply.reply);
      modalInputReplyer.val(reply.replyer);
      modalInputReplyDate.val(replyService.displayTime(reply.replyDate)).attr("readonly", "readonly");
      replyModal.data("rno", reply.rno);

      replyModal.find("button[id != 'modalCloseBtn']").hide();

      modalModBtn.show();
      modalRemoveBtn.show();

      replyModal.modal("show");
    });
  });

  modalModBtn.on("click", function (e) {
    var originalReplyer = modalInputReplyer.val();

    var reply = {
      rno: replyModal.data("rno"),
      reply: modalInputReply.val(),
      replyer: originalReplyer
    };

    if (!replyer) {
      alert("로그인 후 수정이 가능합니다.");
      replyModal.modal("hide");
      return;
    }

    console.log("Original Replyer: " + originalReplyer); // 댓글 원래 작성자

    if (replyer != originalReplyer) {
      alert("자신이 작성한 댓글만 삭제가 가능합니다.");
      replyModal.modal("hide");
      return;
    }

    replyService.update(reply, function (result) {
      alert(result);
      replyModal.modal("hide");
      showList(pageNum);
    });
  });

  modalRemoveBtn.on("click", function (e) {
    var rno = replyModal.data("rno");

    console.log("RNO: " + rno);
    console.log("REPLYER: " + replyer);

    if (!replyer) {
      alert("로그인 후 삭제가 가능합니다.");
      replyModal.modal("hide");
      return;
    }

    var originalReplyer = modalInputReplyer.val();
    console.log("Original Replyer: " + originalReplyer); // 댓글 원래 작성자

    if (replyer != originalReplyer) {
      alert("자신이 작성한 댓글만 삭제가 가능합니다.");
      replyModal.modal("hide");
      return;
    }

    replyService.remove(rno, originalReplyer, function (result) {
      alert(result);
      replyModal.modal("hide");
      showList(pageNum);
    });
  });

  replyPageFooter.on("click", "li a", function (e) {
    e.preventDefault();
    console.log("page click");

    var targetPageNum = $(this).attr("href");
    console.log("targetPageNum: " + targetPageNum);
    pageNum = targetPageNum;
    showList(pageNum);
  });

</script>

<script type="text/javascript">
  $(document).ready(function () {
    var $operForm = $("#operForm");

    $("#board-read-body button[data-oper='modify']").on("click", function (e) {
      $operForm.attr("action", "/board/modify").submit();
    });

    // 빈 폼으로 Submit을 해버리면 결과 URL 끝에 ?가 붙는다.
    $("#board-read-body button[data-oper='list']").on("click", function (e) {
      $operForm.find("#bno").remove();
      $operForm.attr("action", "/board/list").submit();
      $operForm.submit();
    });
  });
</script>
<%@include file="../includes/tui-editor-js.jsp" %>
<script>
  const { Editor } = toastui;
  const { codeSyntaxHighlight } = Editor.plugin;

  const editor = new Editor.factory({
    el: document.querySelector('#viewer'),
    viewer: true,
    height: '600px',
    initialEditType: 'markdown',
    plugins: [codeSyntaxHighlight],
    initialValue: ${boardContentJson}
  });
</script>
</body>

</html>