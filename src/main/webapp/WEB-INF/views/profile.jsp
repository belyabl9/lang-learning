<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<t:page pageTitle="profile">
	<jsp:attribute name="navBar">
        <t:navBar></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>
    <jsp:attribute name="jsIncludes">
     
    </jsp:attribute>

    <jsp:body>

        <script>
            $(document).ready(function () {
                $('#new_password').on('input', function() {
                    var self = $(this);
                    if (self.val().length === 0) {
                        $("#changePasswordBtn").prop('disabled', true);
                    } else if ($("#confirm_password").val().length !== 0) {
                        $("#changePasswordBtn").prop('disabled', false);
                    }
                });
                $('#confirm_password').on('input', function() {
                    var self = $(this);
                    if (self.val().length === 0) {
                        $("#changePasswordBtn").prop('disabled', true);
                    } else if ($("#new_password").val().length !== 0) {
                        $("#changePasswordBtn").prop('disabled', false);
                    }
                });
                
                $("#changePasswordForm").submit(function(e) {
                    e.preventDefault();

                    if ($('#new_password').val() !== $('#confirm_password').val()) {
                        bootbox.alert({
                            message: "Passwords must match."
                        });
                        return;
                    }
                    
                    var formData = new FormData(this);
                    
                    $.ajax({
                        url: "/user/password/change",
                        type: 'POST',
                        data: formData,
                        success: function (data) {
                            if (data.success) {
                                bootbox.alert({
                                    message: "The password has been successfully changed.",
                                });
                            } else {
                                bootbox.alert({
                                    message: "Can not change the password: " + data.message,
                                });
                            }
                        },
                        error : function(e) {
                            bootbox.alert({
                                message: "Can not change the password: " + e,
                            });
                        },
                        cache: false,
                        contentType: false,
                        processData: false
                    });
                    
                    $('#new_password,#confirm_password').val('');
                });
            });
        </script>

        <div style="margin-top: 25px;">
            <h1><spring:message code="profile" /></h1>

            <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="pills-profile-info-tab" data-toggle="pill" href="#pills-profile-info" role="tab" aria-controls="pills-profile-info" aria-selected="true"><spring:message code="profile_info_tab" /></a>
                </li>
                <c:if test="${user.getClass().getSimpleName() eq 'InternalUser'}">
                    <li class="nav-item">
                        <a class="nav-link" id="pills-change-password-tab" data-toggle="pill" href="#pills-change-password" role="tab" aria-controls="pills-change-password" aria-selected="false"><spring:message code="change_password_tab" /></a>
                    </li>
                </c:if>
            </ul>
            <hr />
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade show active" id="pills-profile-info" role="tabpanel" aria-labelledby="pills-profile-info-tab">
                    <div>
                        <label style="width: 100px;">
                            <spring:message code="reg_form.full_name" />
                        </label>
                        <input value="${user.name}" readonly="readonly" />
                    </div>
                    <c:if test="${not empty user.email}">
                        <div>
                            <label style="width: 100px;">
                                <spring:message code="reg_form.email" />
                            </label>
                            <input value="${user.email}" readonly="readonly" />
                        </div>
                    </c:if>
                </div>
                <c:if test="${user.getClass().getSimpleName() eq 'InternalUser'}">
                    <div class="tab-pane fade" id="pills-change-password" role="tabpanel" aria-labelledby="pills-change-password-tab">
                        <form id="changePasswordForm" method="POST" action="/user/password/change">
                            <div>
                                <label style="width: 150px;">
                                    <spring:message code="new_password" />
                                </label>
                                <input type="password" id="new_password" name="new_password" />
                            </div>
                            <div>
                                <label style="width: 150px;">
                                    <spring:message code="confirm_password" />
                                </label>
                                <input type="password" id="confirm_password" name="confirm_password" />
                            </div>
                            <div style="display: flex;">
                                <button id="changePasswordBtn" class="btn btn-primary" disabled>
                                    <spring:message code="save" />
                                </button>
                            </div>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
    </jsp:body>
</t:page>