<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page pageTitle="words">
	<jsp:attribute name="navBar">
        <t:navBar></t:navBar>
    </jsp:attribute>
    <jsp:attribute name="bodyFooter">
	    <t:footer></t:footer>
    </jsp:attribute>

    <jsp:body>
        <div style="word-break: break-word; margin-top: 15px; margin-left: 0;">
            
            <div class="light-font">
                <ol class="breadcrumb primary-color">
                    <li class="breadcrumb-item"><a class="white-text" href="/"><spring:message code="home" /></a></li>
                    <li class="breadcrumb-item"><a class="white-text" href="/categories"><spring:message code="categories" /></a></li>
                    <li class="breadcrumb-item active"><spring:message code="category" /> "${category.name}"</li>
                </ol>
            </div>
            
            <hr>
            
            <c:if test="${shared}">
                <div class="d-flex">
                    <button class="btn btn-primary" style="margin-left: 0px;" data-toggle="modal" data-target="#cloneCategoryFormContainer">
                        <i class="fa fa-plus-square-o"></i> <spring:message code="clone" />
                    </button>
                </div>
            </c:if>
            
            <c:if test="${not shared and (user.isAdmin() or not category.isBuiltIn())}">
                <div class="d-flex">
                    <button class="btn btn-primary" style="margin-left: 0px; min-width: 200px;" data-toggle="modal" data-target="#newWordForm">
                        <i class="fa fa-plus-square-o"></i> <spring:message code="word.add" />
                    </button>
                    <div id="actionsOnWordsContainer" class="d-none">
                        <button class="btn btn-primary" data-toggle="modal" data-target="#moveWordFormContainer">
                            <spring:message code="move" />
                        </button>
                        <button class="btn btn-primary" data-toggle="modal" data-target="#copyWordFormContainer">
                            <spring:message code="copy" />
                        </button>
                    </div>
                </div>
                <c:if test="${fn:length(category.words) gt 0}">
                    <div>
                        <button id="exportCategoryBtn" class="btn btn-primary" style="margin-left: 0; min-width: 200px;" data-toggle="modal">
                            <i class="fa fa-plus-square-o"></i> <spring:message code="export" />
                        </button>
                    </div>
                </c:if>
                <div id="accordion" class="accordion">
                    <div class="card mb-0" style="margin-top:15px; background-color: #4285f4; color:white;">
                        <div class="card-header collapsed" data-toggle="collapse" href="#collapseOne">
                            <a class="card-title">
                                <spring:message code="import" />
                            </a>
                        </div>
                        <div id="collapseOne" class="card-body collapse" data-parent="#accordion" style="background-color: #a3cefb;">
                            <form id="importForm" action="/categories/${category.id}/words/import" method="POST" enctype="multipart/form-data">
                                <fieldset style="width: 100%; margin-top: 15px">
                                    <legend>
                                        <spring:message code="import_words_from_file" />
                                    </legend>
                                    <hr>
                                    <div>
                                        <div><spring:message code="expected_format" />:</div>
                                        <textarea readonly="true" rows="4" cols="15" style="resize: none; white-space: pre-line; background-color: #d8edf7;">
                                            word1 ; translation1
                                            word2 ; translation2
                                            ...
                                            wordN ; translationN
                                        </textarea>
                                    </div>
                                    <div class="form-group">
                                        <label for="duplicateActionSelect">
                                            <spring:message code="action_on_duplicate" />
                                        </label>
                                        <select class="form-control" id="duplicateActionSelect" name="duplicateAction">
                                            <option value="REPLACE"><spring:message code="replace" /></option>
                                            <option value="SKIP"><spring:message code="skip" /></option>
                                            <option value="ERROR_ROLLBACK"><spring:message code="duplicate_error_rollback_descr" /></option>
                                            <option value="ERROR"><spring:message code="duplicate_error_descr" /></option>
                                        </select>
                                    </div>
                                    <div class="d-flex" style="border: 1px solid #034bec; border-radius: 15px; padding: 15px 15px; justify-content: space-between; flex-wrap: wrap;">
                                        <div>
                                            <input type="file" name="wordImportFile" />
                                        </div>
                                        <div>
                                            <button class="btn btn-primary"><spring:message code="import" /></button>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>
            
            <div style="margin-top: 15px;">
                <table id="wordsTable" class="cell-border hover w-100 display nowrap">
                    <thead style="background-color: #78bbf5;">
                        <c:if test="${not shared and (user.isAdmin() or not category.isBuiltIn())}">
                            <th style="width: 15px; padding: 0;">
                                <input type="checkbox" name="select_all" id="select-all" style="margin-left: 10px;">
                            </th>
                        </c:if>
                        <th class="w-25"><spring:message code="word" /></th>
                        <th class="w-25"><spring:message code="translation" /></th>
                        <th><spring:message code="usage_examples" /></th>
                        <c:if test="${ not shared and (user.isAdmin() || !category.isBuiltIn()) }">
                            <th style="width: 80px;">
                                <spring:message code="actions" />
                            </th>
                        </c:if>
                    </thead>
                    <c:forEach items="${category.words}" var="word">
                        <tr>
                            <c:if test="${not shared and (user.isAdmin() or not category.isBuiltIn())}">
                                <td class="text-center">
                                    <input type="checkbox" class="wordSelector" value="${word.id}">
                                </td>
                            </c:if>
                            <td>${word.original}</td>
                            <td>${word.translation}
                            <td>
                                <ol style="list-style: decimal; margin-left: 20px;">
                                    <c:forEach items="${word.sentenceExamples}" var="sentence">
                                        <li>${sentence}</li>
                                    </c:forEach>
                                </ol>
                            </td>
                            <c:if test="${ (user.isAdmin() || !category.isBuiltIn()) && !shared }">
                                <td class="text-center">
                                    <div class="dropdown">
                                        <button class="btn dropdown-toggle" style="background-color: #3f51b5; padding: 5px 30px;" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">

                                        </button>
                                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                            <a class="dropdown-item" style="margin-right: 10px;  cursor: pointer;" onclick="Category.showUpdateForm(${word.id});">
                                                <img onclick="Category.showUpdateForm(${word.id});" style="width: 16px; height: 16px;" src="/images/edit.png" />
                                                <spring:message code="edit" />
                                            </a>
                                            <a class="dropdown-item" style="margin-right: 10px;  cursor: pointer;" onclick="Category.removeWord(${word.id});">
                                                <img onclick="Category.removeWord(${word.id});" style="width: 16px; height: 16px;" src="/images/delete.png" />
                                                <spring:message code="delete" />
                                            </a>
                                        </div>
                                    </div>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>

        <div class="modal fade" id="newWordForm" tabindex="-1" role="dialog">
            <form id="addWordForm">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header text-center">
                            <h4 class="modal-title w-100 font-weight-bold"><spring:message code="word.add" /></h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="md-form mb-4">
                                <input type="text" id="original" name="original" class="form-control validate" required>
                                <label id="originalLbl" data-error="wrong" data-success="right" for="original">
                                    <spring:message code="word" />
                                </label>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="md-form mb-4">
                                <input type="text" id="translation" name="translation" class="form-control validate" required>
                                <label id="translationLbl" data-error="wrong" data-success="right" for="translation">
                                    <spring:message code="translation" />
                                </label>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="form-group">
                                <label id="usageExamplesLbl" data-error="wrong" data-success="right" for="usageExamples">
                                    <spring:message code="usage_examples" />:
                                </label>
                                <textarea class="form-control rounded-0" id="usageExamples" name="usageExamples" rows="3"></textarea>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="form-group">
                                <label id="associationImgLbl" data-error="wrong" data-success="right" for="associationImg">
                                    <spring:message code="association_img" />
                                </label>
                                <input type="file" id="associationImg" name="associationImg" class="form-control validate">
                                <input type="hidden" id="associationImgBase64" name="associationImgBase64" />
                                <div class="association-img-container">
                                    <img id="associationImgPreview" class="association-img" />
                                    <span id="associationImgBtn" class="btn-primary association-img-btn d-none" onclick="removeWordAssociationImg('associationImg');">
                                        <spring:message code="delete" />
                                    </span>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="categoryId" value="${category.id}" />
                        <div class="modal-footer d-flex justify-content-center">
                            <button class="btn btn-primary">
                                <spring:message code="add" />
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <div class="modal fade" id="updateWordFormContainer" tabindex="-1" role="dialog">
            <form id="updateWordForm">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header text-center">
                            <h4 class="modal-title w-100 font-weight-bold">
                                <spring:message code="word.update" />
                            </h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="md-form mb-4">
                                <input type="text" id="updatedOriginal" name="original" class="form-control validate" required>
                                <label id="updatedOriginalLbl" data-error="wrong" data-success="right" for="updatedOriginal">
                                    <spring:message code="word" />
                                </label>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="md-form mb-4">
                                <input type="text" id="updatedTranslation" name="translation" class="form-control validate" required>
                                <label id="updatedTranslationLbl" data-error="wrong" data-success="right" for="updatedTranslation">
                                    <spring:message code="translation" />
                                </label>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="form-group">
                                <label id="updatedUsageExamplesLbl" data-error="wrong" data-success="right" for="updatedUsageExamples">
                                    <spring:message code="usage_examples" />:
                                </label>
                                <textarea class="form-control rounded-0" id="updatedUsageExamples" name="usageExamples" rows="3"></textarea>
                            </div>
                        </div>
                        <div class="modal-body mx-3">
                            <div class="form-group">
                                <label id="updatedAssociationImgLbl" data-error="wrong" data-success="right" for="updatedAssociationImg">
                                    <spring:message code="association_img" />:
                                </label>
                                <input type="file" id="updatedAssociationImg" name="updatedAssociationImg" class="form-control validate">
                                <input type="hidden" id="updatedAssociationImgBase64" name="associationImgBase64" />
                                <div class="association-img-container">
                                    <img id="updatedAssociationImgPreview" class="association-img" />
                                    <span id="updatedAssociationImgBtn" class="btn-primary association-img-btn d-none" onclick="removeWordAssociationImg('updatedAssociationImg');">
                                        <spring:message code="delete" />
                                    </span>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" id="wordId" name="id" />
                        <div class="modal-footer d-flex justify-content-center">
                            <button class="btn btn-primary">
                                <spring:message code="update" />
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <div class="modal fade" id="copyWordFormContainer" tabindex="-1" role="dialog">
            <form id="copyWordForm">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header text-center">
                            <h4 class="modal-title w-100 font-weight-bold">
                                <spring:message code="copy_words" />
                            </h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body mx-3">
                            <div>
                                <label data-error="wrong" data-success="right" for="copyWordsCategorySelect">
                                    <spring:message code="category" />:
                                </label>
                                <select id="copyWordsCategorySelect" name="categoryId" class="form-control validate" required>
                                    <option value="" disabled selected>
                                        <spring:message code="choose_category" />
                                    </option>
                                    <c:forEach items="${categories}" var="category">
                                        <option value="${category.id}">${category.name}${category.isBuiltIn() ? ' (built-in)' : ''}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <input type="hidden" id="copiedWordsIds" name="wordsIds" />
                        <div class="modal-footer d-flex justify-content-center">
                            <button class="btn btn-primary">
                                <spring:message code="copy" />
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <div class="modal fade" id="moveWordFormContainer" tabindex="-1" role="dialog">
            <form id="moveWordForm">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header text-center">
                            <h4 class="modal-title w-100 font-weight-bold">
                                <spring:message code="move_words" />
                            </h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body mx-3">
                            <div>
                                <label data-error="wrong" data-success="right" for="moveWordsCategorySelect">
                                    <spring:message code="category" />:
                                </label>
                                <select id="moveWordsCategorySelect" name="categoryId" class="form-control validate" required>
                                    <option value="" disabled selected>
                                        <spring:message code="choose_category" />
                                    </option>
                                    <c:forEach items="${categories}" var="category">
                                        <option value="${category.id}">${category.name}${category.isBuiltIn() ? ' (built-in)' : ''}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <input type="hidden" id="movedWordsIds" name="wordsIds" />
                        <div class="modal-footer d-flex justify-content-center">
                            <button class="btn btn-primary">
                                <spring:message code="move" />
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <c:if test="${shared}">
            <t:cloneCategoryForm categoryId="${category.id}" />
        </c:if>
        
        <script>
            function removeWordAssociationImg(imgId) {
                $('#' + imgId + "Base64").val("");
                $('#' + imgId).val("");
                $('#' + imgId + "Preview").prop('src', '');
                $('#' + imgId + "Btn").addClass('d-none');
            }
            
            function setBase64Result(fileObj, destinationId) {
                if (!fileObj) {
                    removeWordAssociationImg(destinationId);
                    return;
                }
                
                var reader = new FileReader();
                reader.readAsDataURL(fileObj);
                reader.onload = function () {
                    $('#' + destinationId + "Base64").val(reader.result.split(/,/)[1]);
                    $('#' + destinationId + "Preview").prop('src', reader.result);
                    $('#' + destinationId + "Btn").removeClass('d-none');
                    
                };
                reader.onerror = function (error) {
                    console.log('Error: ', error);
                };
            }
            
            var Category = (function() {

                return {

                    showUpdateForm: function (id) {
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/word/find",
                            data : JSON.stringify({ id: id }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                if (!data.success) {
                                    bootbox.alert({
                                        message: "Can not find a word. " + data.message,
                                        callback: function () {
                                            $('#updateWordFormContainer').modal('hide');
                                        }
                                    });
                                } else {
                                    $('#wordId').val(data.word.id);
                                    $('#updatedOriginal').val(data.word.original);
                                    $('#updatedOriginalLbl').addClass("active");

                                    $('#updatedTranslation').val(data.word.translation);
                                    $('#updatedTranslationLbl').addClass("active");

                                    $('#updatedUsageExamples').val(data.word.usageExamples.join("\n"));
                                    
                                    if (data.word.associationImgUrl) {
                                        $('#updatedAssociationImgPreview').prop('src', data.word.associationImgUrl);
                                    } else {
                                        removeWordAssociationImg('updatedAssociationImg');
                                    }

                                    $('#updateWordFormContainer').modal('show');
                                }
                            },
                            error : function(e) {
                                bootbox.alert({
                                    message: "Can not find a word. " + e,
                                    callback: function () {
                                        $('#updateWordFormContainer').modal('hide');
                                    }
                                });
                            }
                        });
                    },

                    removeWord: function (id) {
                        $.ajax({
                            type : "POST",
                            contentType : "application/json",
                            url : "/word/delete",
                            data : JSON.stringify({ id: id }),
                            dataType : 'json',
                            timeout : 100000,
                            success : function(data) {
                                if (!data.success) {
                                    bootbox.alert({
                                        message: "Can not delete a word. " + data.message,
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
                                    message: "Can not delete a word. " + e,
                                    callback: function () {
                                        location.reload();
                                    }
                                });
                            }
                        });

                    },
                    
                    prepareCopyingForm: function() {
                        var ids = [];
                        $.each($('.wordSelector'), function (index, val) {
                            ids.push(val.value);
                        });
                        $('#copiedWordsIds').val(ids);
                    },
                    prepareMovingForm: function() {
                        var ids = [];
                        $.each($('.wordSelector'), function (index, val) {
                            ids.push(val.value);
                        });
                        $('#movedWordsIds').val(ids);
                    }
                };
            })();

            $(document).ready(function () {
                
                var table = $('#wordsTable').DataTable({
                    "bLengthChange": false,
                    responsive: true,
                    columnDefs: [
                        { responsivePriority: 1, targets: 0, sortable: false },
                        { responsivePriority: 2, targets: -1, sortable: false }
                    ],
                    "order": [[1, 'asc']],
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

                $("#associationImg").change(function () {
                    setBase64Result($(this)[0].files[0], 'associationImg');
                });

                $('#associationImgPreview').on('load', function () {
                    $('#associationImgBtn').removeClass('d-none');
                });
                
                $("#updatedAssociationImg").change(function () {
                    setBase64Result($(this)[0].files[0], 'updatedAssociationImg');
                });
                $('#updatedAssociationImgPreview').on('load', function () {
                    $('#updatedAssociationImgBtn').removeClass('d-none');
                });

                $("#importForm").submit(function(e) {
                    e.preventDefault();
                    var formData = new FormData(this);

                    $.ajax({
                        url: "/categories/${categoryId}/words/import",
                        type: 'POST',
                        data: formData,
                        success: function (data) {
                            bootbox.alert({
                                message: data.message,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        },
                        cache: false,
                        contentType: false,
                        processData: false
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
                                    message: "<spring:message code='can_not_clone_category' /> " + data.message,
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
                                message: "<spring:message code='can_not_clone_category' /> " + e,
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

                labelWorkaroundFunc("original", "originalLbl");
                labelWorkaroundFunc("updatedOriginal", "updatedOriginalLbl");
                labelWorkaroundFunc("translation", "translationLbl");
                labelWorkaroundFunc("updatedTranslation", "updatedTranslationLbl");
                labelWorkaroundFunc("clonedCategoryName", "clonedCategoryNameLbl");

                $('.wordSelector,#select-all').change(function() {
                    if ($('.wordSelector:checked').length > 0) {
                        $('#actionsOnWordsContainer').removeClass("d-none");
                    } else {
                        $('#actionsOnWordsContainer').addClass("d-none");
                    }
                });

                $("#addWordForm").on("submit", function(event) {
                    event.preventDefault();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });
                    
                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/word/add",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            if (!data.success) {
                                bootbox.alert({
                                    message: "Can not add a word. " + data.message,
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
                                message: "Can not add a word. " + e,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });

                $("#updateWordForm").on("submit", function(event) {
                    event.preventDefault();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/word/update",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            console.log(data);
                            if (!data.success) {
                                bootbox.alert({
                                    message: "Can not update the word. " + data.message,
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
                                message: "Can not update the word. " + e,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });

                $("#copyWordForm").on("submit", function(event) {
                    event.preventDefault();

                    Category.prepareCopyingForm();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/words/copy",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            if (!data.success) {
                                bootbox.alert({
                                    message: "Can not copy a word. " + data.message,
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
                                message: "Can not copy a word. " + e,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });

                $("#moveWordForm").on("submit", function(event) {
                    event.preventDefault();

                    Category.prepareMovingForm();

                    var data = {};
                    $.each($( this ).serializeArray(), function (index, fieldObj) {
                        data[fieldObj.name] = fieldObj.value;
                    });

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/words/move",
                        data : JSON.stringify(data),
                        dataType : 'json',
                        timeout : 100000,
                        success : function(data) {
                            if (!data.success) {
                                bootbox.alert({
                                    message: "Can not move a word. " + data.message,
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
                                message: "Can not move a word. " + data.message,
                                callback: function () {
                                    location.reload();
                                }
                            });
                        }
                    });
                });
                
                var triggerDownload = function(response, fileName, fileType) {
                    var blob = new Blob([response], { type: fileType });
                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(blob);
                    link.download = fileName;

                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);  
                };
                
                $('#exportCategoryBtn').on('click', function () {
                    var data = {
                        categoryId: ${categoryId}
                    };
                    $.ajax({
                        type : "POST",
                        url : "/categories/export",
                        data : data,
                        timeout : 100000,
                        success : function(data) {
                            triggerDownload(data, '${category.name}.txt', 'application/txt');
                        },
                        error : function(e) {
                            console.error("Could not download a file: ", e);
                        }
                    });
                });

                $('#select-all').on('click', function() {
                    // Get all rows with search applied
                    var rows = table.rows({ 'search': 'applied' }).nodes();
                    // Check/uncheck checkboxes for all rows in the table
                    $('input[type="checkbox"]', rows).prop('checked', this.checked).trigger("change");
                });

                $('#wordsTable tbody').on('change', 'input[type="checkbox"]', function(){
                    // If checkbox is not checked
                    if(!this.checked){
                        var el = $('#select-all').get(0);
                        // If "Select all" control is checked and has 'indeterminate' property
                        if(el && el.checked && ('indeterminate' in el)){
                            // Set visual state of "Select all" control
                            // as 'indeterminate'
                            el.indeterminate = true;
                        }
                    }
                });
            });
        </script>
    </jsp:body>
</t:page>