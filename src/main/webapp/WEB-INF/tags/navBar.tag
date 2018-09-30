<%@ tag description="Navigation bar component" pageEncoding="UTF-8" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="activeMenuItem" %>
<%@ attribute name="hideLangPanel" type="java.lang.Boolean" %>

<header>
    <nav class="navbar navbar-expand-lg navbar-dark indigo">

        <a class="navbar-brand" style="border: 1px solid white;border-radius: 25px;padding: 10px;font-weight: bold;background-color: #6675df;" href="/">Language learning</a>

        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#basicExampleNav" aria-controls="basicExampleNav"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="basicExampleNav">

            <ul class="navbar-nav mr-auto">
                <li class="nav-item ${activeMenuItem eq 'categories' ? "active" : ""}">
                    <a id="categoriesLink" class="nav-link" href="/categories">
                        <spring:message code="categories" />
                    </a>
                </li>
                <li class="nav-item ${activeMenuItem eq 'activities' ? "active" : ""}">
                    <a id="activitiesLink" class="nav-link" href="/activities">
                        <spring:message code="activities" />
                    </a>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <c:if test="${not hideLangPanel}">
                    <li style="margin: 10px 15px 10px 0;">
                        <div class="d-flex" style="height: 100%;align-items: center;border: 1px solid #6675df;border-radius: 20px;font-size: 15px;padding: 5px;color: white;background-color: #6675df;">
                            <a href="<t:replaceParam name='lang' value='ua' />" style="color:white;">Українська</a>&nbsp;|&nbsp;
                            <a href="<t:replaceParam name='lang' value='ru' />" style="color:white;">Русский</a>&nbsp;|&nbsp;
                            <a href="<t:replaceParam name='lang' value='en' />" style="color:white;">English</a>
                        </div>
                    </li>
                </c:if>
                <li class="nav-item dropdown d-flex" style="align-items: center;">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ${user.name}
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="/user/${user.id}/profile">
                            <spring:message code="profile" />
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="#" onclick="$('#logoutForm').submit();">
                            <spring:message code="sign_out" />
                        </a>
                    </div>
                </li>
            
                <li class="nav-item">

                </li>
            </ul>
        </div>

    </nav>
</header>

<form id="logoutForm" method="POST" action="/logout">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>