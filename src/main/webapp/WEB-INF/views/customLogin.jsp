<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">

<%@include file="includes/header.jsp" %>

<body class="bg-gradient-primary">

<style>
  /* SB Admin2 로그인 페이지의 기본 멍멍이 사진이 슬퍼보여서 바꿈... */
  .custom-bg-login-image {
    background: url(https://avatars.githubusercontent.com/u/42864970?v=4) center;
    background-size: cover;
  }
</style>
<div class="container">

  <!-- Outer Row -->
  <div class="row justify-content-center">

    <div class="col-xl-10 col-lg-12 col-md-9">

      <div class="card o-hidden border-0 shadow-lg my-5">
        <div class="card-body p-0">
          <!-- Nested Row within Card Body -->
          <div class="row">
            <div class="col-lg-6 d-none d-lg-block custom-bg-login-image"></div>
            <div class="col-lg-6">
              <div class="p-5">
                <div class="text-center">
                  <h1 class="h4 text-gray-900 mb-4">게시판 로그인</h1>
                </div>
                <form class="user" method="post" action="/login">
                  <div class="form-group">
                    <input name="username" type="email" class="form-control form-control-user"
                           id="exampleInputEmail" aria-describedby="emailHelp"
                           placeholder="ID를 입력해주세요..." autofocus>
                  </div>
                  <div class="form-group">
                    <input name="password" type="password" class="form-control form-control-user"
                           id="exampleInputPassword" placeholder="암호">
                  </div>
                  <div class="form-group">
                    <div class="custom-control custom-checkbox small">
                      <input name="remember-me" type="checkbox" class="custom-control-input"
                             id="customCheck">
                      <label class="custom-control-label" for="customCheck">자동 로그인</label>
                    </div>
                  </div>
                  <a href="index.html" class="btn btn-primary btn-user btn-block">
                    로그인
                  </a>
                  <!-- Google, Facebook 로그인 연동은 기회될 때 해야겠다. 틀은 주석으로 남겨두자. -->
                  <!--
                  <hr>
                  <a href="index.html" class="btn btn-google btn-user btn-block">
                    <i class="fab fa-google fa-fw"></i> Login with Google
                  </a>
                  <a href="index.html" class="btn btn-facebook btn-user btn-block">
                    <i class="fab fa-facebook-f fa-fw"></i> Login with Facebook
                  </a>
                  -->
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                </form>

                <!-- 아직 회원 가입이나 암호찾기 기능은 없으므로, 이것 주석 ㅠㅠ -->
                <!--
                <hr>
                <div class="text-center">
                  <a class="small" href="forgot-password.html">Forgot Password?</a>
                </div>
                <div class="text-center">
                  <a class="small" href="register.html">Create an Account!</a>
                </div>
                -->
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>

  </div>

  <%@include file="includes/footer.jsp" %>
</div>

<%@include file="includes/scripts.jsp" %>

<script>
  $(".btn-user").on("click", function (e) {
    e.preventDefault();
    $("form").submit();
  })
</script>

</body>

</html>
