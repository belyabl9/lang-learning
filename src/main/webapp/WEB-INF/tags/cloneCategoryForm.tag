<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="categoryId" type="java.lang.Long" %>

<div class="modal fade" id="cloneCategoryFormContainer" tabindex="-1" role="dialog">
    <form id="cloneCategoryForm">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold">
                        <spring:message code="category.clone" />
                    </h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body mx-3">
                    <div class="md-form mb-4">
                        <input type="text" id="clonedCategoryName" name="categoryName" class="form-control" required>
                        <label id="clonedCategoryNameLbl" data-error="wrong" data-success="right" for="clonedCategoryName">
                            <spring:message code="category.name" />
                        </label>
                    </div>
                </div>
                <input type="hidden" id="clonedCategoryId" name="id" value="${categoryId}">
                <div class="modal-footer d-flex justify-content-center">
                    <button class="btn btn-primary">
                        <spring:message code="clone" />
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>