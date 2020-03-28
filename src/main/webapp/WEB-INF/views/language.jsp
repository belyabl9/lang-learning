<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:page pageTitle="language">
	<jsp:attribute name="navBar">
        <t:navBar activeMenuItem="language" />
    </jsp:attribute>

    <jsp:body>
        <div style="margin-top: 25px;">

            <div class="light-font">
                <ol class="breadcrumb primary-color">
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="home" /></a></li>
                    <li class="breadcrumb-item active"><spring:message code="language" /></li>
                </ol>
            </div>
            
            <hr>
            
            <div>
                <form method="POST" action="/language" style="width: 450px;">
                    <h3>Please, select a language to learn</h3>
                    <select class="langSelect browser-default custom-select" name="language">
                        <option disabled>Select a language to learn</option>
                        <c:forEach items="${languages}" var="language">
                            <option ${language.getCode().equals(lang) ? 'selected' : ''} value="${language.getCode()}">${fn:toLowerCase(language.name())}</option>
                        </c:forEach>
                    </select>
                    <button id="changeLangBtn" class="btn btn-primary">
                        <spring:message code="select" />
                    </button>
                </form>
            </div>
            
        </div>
    </jsp:body>
</t:page>