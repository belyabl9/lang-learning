<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="activities">
	<jsp:attribute name="navBar">
        <t:navBar activeMenuItem="activities"></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>
    <jsp:attribute name="jsIncludes">
     
    </jsp:attribute>

    <jsp:body>
        
        <script>
            $(document).ready( function () {
                var activitiesTable = $('#activitiesTable').DataTable({
                    "columnDefs": [
                        {
                            "targets": 0,
                            "orderable": true
                        },
                        {
                            "targets": 1,
                            "orderable": false
                        }
                    ],
                    "columns": [],
                    "bLengthChange": false,
                    responsive: true,
                    "language": {
                        <c:if test="${pageContext.response.locale eq 'ru'}">
                            "url": "/translations/Russian.json"
                        </c:if>
                        <c:if test="${pageContext.response.locale eq 'ua'}">
                            "url": "/translations/Ukrainian.json"
                        </c:if>
                        <c:if test="${pageContext.response.locale eq 'en'}">
                            "url": "/translations/English.json"
                        </c:if>
                    }
                });
            });
        </script>

        <div style="margin-top: 15px;">

            <div class="light-font">
                <ol class="breadcrumb primary-color">
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="home" /></a></li>
                    <li class="breadcrumb-item active"><spring:message code="activities" /></li>
                </ol>
            </div>

            <hr>
            
            <table id="activitiesTable" class="cell-border hover">
                <thead style="background-color: #78bbf5;">
                    <th><spring:message code="name" /></th>
                    <th><spring:message code="description" /></th>
                </thead>
                <tbody>
                    <tr>
                        <td>
                            <a class="btn" href="/learning/cards/setup" style="background-color: #78bbf5;">
                                <spring:message code="learning-cards-activity-name" />
                            </a>
                        </td>
                        <td style="vertical-align: top;">
                            <div class="d-flex" style="flex-wrap: wrap;">
                                <div>
                                    <spring:message code="learning-cards-activity-descr" />
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a class="btn" href="/learning/quiz/setup" style="background-color: #78bbf5;">
                                <spring:message code="learning-quiz-activity-name" />
                            </a>
                        </td>
                        <td style="vertical-align: top;">
                            <div class="d-flex" style="flex-wrap: wrap;">
                                <div>
                                    <spring:message code="learning-quiz-activity-descr" />
                                </div>
                                <div>
                                    <img src="/images/word-learning-scheme.png" style="width: 100%;" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </jsp:body>
</t:page>