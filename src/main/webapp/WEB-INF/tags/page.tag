<%@ tag description="Generic Page" pageEncoding="UTF-8" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="navBar" required="true" fragment="true" %>
<%@ attribute name="bodyFooter" fragment="true" %>
<%@ attribute name="jsIncludes" fragment="true" %>
<%@ attribute name="cssFile" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="${pageTitle}" /></title>

    <!--  <link rel="shortcut icon" href="/favicon.png" /> -->

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="/css/styles.css">
    
    <link rel="stylesheet" type="text/css" href="/webjars/datatables/1.10.16/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="/webjars/datatables/1.10.16/css/dataTables.bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/css/responsive.dataTables.min.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.css">
    <!-- Bootstrap core CSS -->
    <link href="/webjars/bootstrap/4.0.0/css/bootstrap.css" rel="stylesheet">
    <!-- Material Design Bootstrap -->
    <link href="/webjars/mdbootstrap-bootstrap-material-design/4.5.3/css/mdb.css" rel="stylesheet">

    <c:if test="${not empty cssFile}">
        <link rel="stylesheet" type="text/css" href="/css/${cssFile}">
    </c:if>

    <script type="text/javascript" charset="utf8" src="/webjars/jquery/3.3.1/jquery.min.js"></script>

    <script type="text/javascript" charset="utf8" src="/webjars/bootbox/4.4.0/bootbox.js"></script>

    <script type="text/javascript" charset="utf8" src="/webjars/datatables/1.10.16/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src="/js/lib/dataTables.responsive.min.js"></script>

    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="/webjars/popper.js/1.14.1/umd/popper.js"></script>
    
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="/webjars/bootstrap/4.0.0/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/css/datatables-adj.css">
    
    <jsp:invoke fragment="jsIncludes" />
</head>
<body>
    <div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-header">
            <h1>Processing...</h1>
        </div>
        <div class="modal-body">
            <div class="progress progress-striped active">
                <div class="bar" style="width: 100%;"></div>
            </div>
        </div>
    </div>
    <jsp:invoke fragment="navBar" />
    <main id="mainContent">
        <div class="container">
            <jsp:doBody />
        </div>
    </main>
    <%--<jsp:invoke fragment="bodyFooter" />--%>
</body>
</html>