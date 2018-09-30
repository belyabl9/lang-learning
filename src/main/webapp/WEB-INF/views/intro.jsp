<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="intro" cssFile="intro.css">
	<jsp:attribute name="navBar">
        <t:navBar></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>
    <jsp:attribute name="jsIncludes">
     
    </jsp:attribute>

    <jsp:body>
        
        <div style="text-align: center; color: #3f51b5; margin-top: 35px;text-decoration: underline;text-underline-position: under;">
            <h1><spring:message code="intro.welcome" /></h1>
        </div>
        
        <div style="text-align: center;">
            <div style="margin-top: 35px;color: #3f51b5; text-align: center; display: inline-block;border: 1px solid;padding: 25px;border-radius: 25px;">
                <div style="text-align: center;text-decoration: underline;text-underline-position: under;">
                    <h3><spring:message code="intro.why_special_title" />:</h3>
                </div>
                <ul class="features">
                    <li class="features"><spring:message code="intro.why_special_1" /></li>
                    <li class="features"><spring:message code="intro.why_special_2" /></li>
                    <li class="features"><spring:message code="intro.why_special_3" /></li>
                </ul>
            </div>
        </div>
    </jsp:body>
</t:page>