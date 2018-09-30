<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>
        <spring:message code="reg_form.title" />
    </title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script type="text/javascript" charset="utf8" src="/webjars/jquery/3.3.1/jquery.min.js"></script>
    <script src="/js/lib/bootstrap-3.3.7.min.js"></script>
    <script type="text/javascript" charset="utf8" src="/webjars/bootbox/4.4.0/bootbox.js"></script>
    
    <link href="/css/bootstrap-3.3.7.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/login/util.css">
    <link rel="stylesheet" type="text/css" href="/css/login/main.css">

    <link rel="stylesheet" href="/css/font-awesome.min.css">

</head>
<body style="background-color: #666666;">

<script>
    <c:if test="${not empty message}">
        bootbox.alert({
            message: "${message}"
        });
    </c:if>
    
    $( document ).ready(function() {
        $('.input100').each(function () {
            $(this).on('blur', function () {
                if ($(this).val().trim() != "") {
                    $(this).addClass('has-val');
                }
                else {
                    $(this).removeClass('has-val');
                }
            });
        })
    });
</script>
<div class="limiter">
    <div class="container-login100">
        <div class="wrap-login100">
            <form class="login100-form" method="POST" action="/register">
                <div style="height: 175px; width:200px; margin: 10px auto;">
                    <div style="position:relative; top:1px; left:1px;">
                        <div class="d-flex" style="height: 100%;align-items: center;border: 1px solid white;border-radius: 20px;font-size: 15px;padding: 5px;color: white;background-color: #6675df;">
                            <a href="<t:replaceParam name='lang' value='ua' />" style="color:white;">Українська</a>&nbsp;|&nbsp;
                            <a href="<t:replaceParam name='lang' value='ru' />" style="color:white;">Русский</a>&nbsp;|&nbsp;
                            <a href="<t:replaceParam name='lang' value='en' />" style="color:white;">English</a>
                        </div>
                    </div>
                </div>
                <span class="login100-form-title p-b-43">
                    <spring:message code="reg_form.title" />
                </span>

                <div class="wrap-input100">
                    <input class="input100" id="name" name="name" required>
                    <span class="focus-input100"></span>
                    <span class="label-input100">
                        <spring:message code="reg_form.full_name" />
                    </span>
                </div>

                <div class="wrap-input100">
                    <input class="input100" id="email" name="email" required>
                    <span class="focus-input100"></span>
                    <span class="label-input100">
                        <spring:message code="reg_form.email" />
                    </span>
                </div>

                <div class="wrap-input100">
                    <input class="input100" id="login" name="login" required>
                    <span class="focus-input100"></span>
                    <span class="label-input100">
                        <spring:message code="reg_form.login" />
                    </span>
                </div>


                <div class="wrap-input100">
                    <input class="input100" type="password" id="password" name="password" required>
                    <span class="focus-input100"></span>
                    <span class="label-input100">
                        <spring:message code="reg_form.password" />
                    </span>
                </div>

                <div class="container-login100-form-btn">
                    <button class="login100-form-btn" id="registerBtn">
                        <spring:message code="reg_form.register" />
                    </button>
                </div>
                <div class="container-login100-form-btn" style="margin-top: 25px;">
                    <a href="/login" class="login100-form-btn" style="background-color: #16b34d;">
                        <spring:message code="reg_form.back_to_login" />
                    </a>
                </div>
            </form>

            <div id="carouselExampleIndicators" class="carousel slide login100-more" data-ride="carousel">
                <ol class="carousel-indicators">
                    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                </ol>
                <div class="carousel-inner h-100 w-100">
                    <div class="item active h-100 w-100">
                        <img class="d-block h-100 w-100" src="/images/intro1.jpg" alt="First slide">
                    </div>
                    <div class="item h-100 w-100">
                        <img class="d-block h-100 w-100" src="/images/intro2.jpg" alt="Second slide">
                    </div>
                    <div class="item h-100 w-100">
                        <img class="d-block h-100 w-100" src="/images/intro3.jpg" alt="Third slide">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>