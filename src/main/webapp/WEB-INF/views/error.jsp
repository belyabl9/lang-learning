<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="error">
	<jsp:attribute name="navBar">
        <t:navBar></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>

    <jsp:body>
        <div class="card card-danger" style="margin-top: 25px;">
            <div class="card-header bg-danger">
                <h2 style="text-align: center;"><spring:message code="error" />:</h2>
            </div>
            <div class="card-block" style="padding: 15px 0px;">
                <h3 style="text-align: center;">${reason}</h3>
            </div>
        </div>
    </jsp:body>
</t:page>