<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page pageTitle="learning_cards_setup">
	<jsp:attribute name="navBar">
        <t:navBar></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>
    <jsp:attribute name="jsIncludes">
        <script src="/js/lib/jquery-ui.js"></script>
        <script src="/js/lib/jquery.ui.touch-punch.js"></script>
    </jsp:attribute>

    <jsp:body>

        <script>
            var LearningCardsSetup = (function() {

                return {
                    start: function () {
                        var categoryIds = [];
                        $('#sortable2 li').each(function () {
                            categoryIds.push($(this).attr('id'));
                        });
                        if (categoryIds.length === 0) {
                            bootbox.alert({
                                message: "Please, choose at least one category."
                            });
                        } else {
                            $('#categoryIds').val(categoryIds);
                            $('#startLearningForm').submit();
                        }
                    }
                };
            })();
            
            $(document).ready(function () {
                $("#sortable1, #sortable2").sortable({
                    connectWith: ".connectedSortable"
                }).disableSelection();
            });
        </script>
        
        <style>
            #sortable1, #sortable2 {
                border: 1px solid #eee;
                min-height: 20px;
                list-style-type: none;
                margin: 0;
                padding: 5px 0 0 0;
                float: left;
            }
            #sortable1 li, #sortable2 li {
                margin: 0 5px 5px 5px;
                padding: 5px;
                font-size: 1.2em;
            }
        </style>
        
        <div>
        
        <form method="POST" action="/learning/cards/start" id="startLearningForm">
            <input id="categoryIds" name="categoryIds[]" type="hidden" />
        </form>
        
        <div class="panel panel-primary" style="width: auto;margin-top: 25px;">

            <div class="light-font">
                <ol class="breadcrumb primary-color">
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="home" /></a></li>
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="activities" /></a></li>
                    <li class="breadcrumb-item active"><spring:message code="learning_cards_setup" /></li>
                </ol>
            </div>
            
            <hr>
            
            <div class="card">
                <h4 class="card-header primary-color white-text">
                    <spring:message code="selecting_categories" />
                </h4>
                <div class="card-body">

                    <c:if test="${empty categories}">
                        <div class="card">
                            <div class="card-header danger-color white-text">
                                <spring:message code="warning" />
                            </div>
                            <div class="card-body">
                                <spring:message code="no_available_categories" />
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty categories}">
                        <h5 style="text-align: center;">
                            <spring:message code="drag_drop_categories" />
                        </h5>
                        <div class="d-flex" style="flex-direction: row; justify-content: space-evenly; flex-wrap: wrap;">
                            <div class="card w-100" style="margin-top: 15px;">
                                <h5 class="card-header primary-color white-text">
                                    <spring:message code="available_categories" />
                                </h5>
                                <ul id="sortable1" class="connectedSortable" style="margin-left: 20px;">
                                    <c:forEach items="${categories}" var="category">
                                        <li id="${category.id}" class="ui-state-default" style="list-style-type: decimal;">${category.name}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div class="card w-100" style="margin-top: 15px;">
                                <h5 class="card-header primary-color white-text">
                                    <spring:message code="selected_categories" />
                                </h5>
                                <ul id="sortable2" class="connectedSortable" style="margin-left: 20px; list-style-type: decimal;">
            
                                </ul>
                            </div>
                        </div>
                        <div class="d-flex" style="justify-content: flex-end;margin-top: 15px;">
                            <button class="btn btn-primary" onclick="LearningCardsSetup.start();">
                                <spring:message code="start" />
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </jsp:body>
</t:page>