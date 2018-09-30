<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="categories">
	<jsp:attribute name="navBar">
        <t:navBar activeMenuItem="categories" />
    </jsp:attribute>

    <jsp:body>
        <div style="margin-top: 25px;">

            <div class="light-font">
                <ol class="breadcrumb primary-color">
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="home" /></a></li>
                    <li class="breadcrumb-item active"><spring:message code="categories" /></li>
                </ol>
            </div>
            
            <hr>
            
            <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="pills-user-categories-tab" data-toggle="pill" href="#pills-user-categories" role="tab" aria-controls="pills-user-categories" aria-selected="true"><spring:message code="user_categories" /></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="pills-suggested-categories-tab" data-toggle="pill" href="#pills-suggested-categories" role="tab" aria-controls="pills-suggested-categories" aria-selected="false"><spring:message code="suggested_categories" /></a>
                </li>
            </ul>
            <hr />
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade show active" id="pills-user-categories" role="tabpanel" aria-labelledby="pills-user-categories-tab">
                    <div>
                        <button id="addCategoryBtn" class="btn btn-primary" style="margin-left: 0px;" onclick="Categories.showAddForm(${user.id});">
                            <i class="fa fa-plus-square-o"></i> <spring:message code="category.add" />
                        </button>
                    </div>
                    <t:userCategories categories="${userCategories}" />
                </div>
                <div class="tab-pane fade" id="pills-suggested-categories" role="tabpanel" aria-labelledby="pills-suggested-categories-tab">
                    <c:if test="${user.isAdmin()}">
                        <div>
                            <button class="btn btn-primary" onclick="Categories.showAddForm(null);">
                                <i class="fa fa-plus-square-o"></i> <spring:message code="category.add" />
                            </button>
                        </div>
                    </c:if>

                    <div style="margin-top: 15px;">
                        <t:suggestedCategories categories="${builtInCategories}" />
                    </div>
                </div>
            </div>
        </div>

        <%-- Modal form for creating a new category --%>
        <t:newCategoryForm />
        <%-- *** --%>

        <%-- Modal form for updating the category --%>
        <t:updateCategoryForm />
        <%-- *** --%>

        <%-- Modal form for cloning the category --%>
        <t:cloneCategoryForm />
        <%-- *** --%>
        
        <script>
            var Categories = (function() {

                return {

                    showAddForm: function (userId) {
                        $('#newCategoryUserId').val(userId);
                        $('#newCategoryForm').modal('show');
                    },

                    showUpdateForm: function (id) {
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/category/find",
                            data : JSON.stringify({ id: id }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                if (!data.success) {
                                    bootbox.alert({
                                        message: "<spring:message code='can_not_find_category' /> " + data.message,
                                        callback: function () {
                                            $('#updateCategoryFormContainer').modal('hide');
                                        }
                                    });
                                } else {
                                    $('#updatedCategoryId').val(data.category.id);
                                    $('#categoryNameToUpdate').val(data.category.name);
                                    $('#categoryNameToUpdateLbl').addClass("active");
                                    $('#updateCategoryFormContainer').modal('show');
                                }
                            },
                            error : function(e) {
                                bootbox.alert({
                                    message: "<spring:message code='can_not_find_category' /> " + e,
                                    callback: function () {
                                        $('#updateCategoryFormContainer').modal('hide');
                                    }
                                });
                            }
                        });
                    },

                    removeCategory: function (id) {
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/category/delete",
                            data : JSON.stringify({ id: id }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                if (data.success) {
                                    location.reload(true);
                                } else {
                                    bootbox.alert({
                                        message: "<spring:message code='can_not_remove_category' />"
                                    });
                                }
                            },
                            error : function(e) {
                                console.log("ERROR: ", e);
                                bootbox.alert({
                                    message: "<spring:message code='can_not_remove_category' />"
                                });
                            }
                        });
                    },

                    shareCategory: function (checkbox, id) {
                        if (checkbox.checked) {
                            $.ajax({
                                type: "POST",
                                url: "/categories/share",
                                data: {categoryId: id},
                                dataType: 'json',
                                timeout: 100000,
                                success: function (data) {
                                    if (data.success) {
                                        window.prompt(
                                            '<spring:message code="share_msg" />',
                                            window.location.origin + "/categories/ref/" + data.reference);
                                    } else {
                                        bootbox.alert({
                                            message: "<spring:message code='can_not_share_category' />"
                                        });
                                    }
                                },
                                error: function (e) {
                                    console.log("ERROR: ", e);
                                    bootbox.alert({
                                        message: "<spring:message code='can_not_share_category' />"
                                    });
                                }
                            });
                        } else {
                            $.ajax({
                                type: "POST",
                                url: "/categories/unshare",
                                data: {categoryId: id},
                                dataType: 'json',
                                timeout: 100000,
                                success: function (data) {
                                    if (!data.success) {
                                        bootbox.alert({
                                            message: "<spring:message code='can_not_unshare_category' />"
                                        });
                                    }
                                },
                                error: function (e) {
                                    console.log("ERROR: ", e);
                                    bootbox.alert({
                                        message: "<spring:message code='can_not_unshare_category' />"
                                    });
                                }
                            });
                        }
                    },

                    prepareCloningForm: function(categoryName, categoryId) {
                        $('#clonedCategoryName').val(categoryName);
                        $('#clonedCategoryNameLbl').addClass('active');
                        $('#clonedCategoryId').val(categoryId);
                    }
                };
            })();

            $(document).ready( function () {
                var suggestedCategoriesTable = $('#suggestedCategoriesTable').DataTable({
                    "columnDefs": [
                        {
                            "targets": 0,
                            "orderable": true
                        },
                        {
                            "targets": 1,
                            "width": '50px',
                            "orderable": false
                        }
                    ],
                    "columns": [

                    ],
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

                var userCategories = $('#categoriesTable').DataTable({
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

                $("#addCategoryForm").on("submit", function(event) {
                    event.preventDefault();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/category/add",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            if (!data.success) {
                                bootbox.alert({
                                    message: "<spring:message code='can_not_add_category' />" + data.message,
                                    callback: function () {
                                        location.reload();
                                    }
                                });
                            } else {
                                location.reload();
                            }
                        },
                        error : function(e) {
                            bootbox.alert({
                                message: "<spring:message code='can_not_add_category' />" + e,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });

                $("#updateCategoryForm").on("submit", function(event) {
                    event.preventDefault();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });
                });

                $("#cloneCategoryForm").on("submit", function(event) {
                    event.preventDefault();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/category/clone",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            if (!data.success) {
                                bootbox.alert({
                                    message: "<spring:message code='can_not_clone_category' />" + data.message,
                                    callback: function () {
                                        location.reload();
                                    }
                                });
                            } else {
                                window.location = "/categories";
                            }
                        },
                        error : function(e) {
                            bootbox.alert({
                                message: "<spring:message code='can_not_clone_category' />" + e,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });

                var labelWorkaroundFunc = function (input, label) {
                    $("#" + input).focus(function() {
                        $("#" + label).addClass("active");
                    });
                    $("#" + input).focusout(function() {
                        if ($("#" + input).val().length === 0) {
                            $("#" + label).removeClass("active");
                        }
                    });
                };

                labelWorkaroundFunc("categoryNameToAdd", "categoryNameToAddLbl");
                labelWorkaroundFunc("categoryNameToUpdate", "categoryNameToUpdateLbl");
                labelWorkaroundFunc("clonedCategoryName", "clonedCategoryNameLbl");
            } );
        </script>
    </jsp:body>
</t:page>