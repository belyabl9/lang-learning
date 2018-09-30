<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade" id="newCategoryForm" tabindex="-1" role="dialog">
    <form id="addCategoryForm">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold">
                        <spring:message code="category.add" />
                    </h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body mx-3">
                    <div class="md-form mb-4">
                        <input type="text" id="categoryNameToAdd" name="categoryName" class="form-control" required>
                        <label id="categoryNameToAddLbl" data-error="wrong" data-success="right" for="categoryNameToAdd">
                            <spring:message code="category.name" />
                        </label>
                    </div>
                </div>
                <input type="hidden" id="newCategoryUserId" name="userId" />
                <div class="modal-footer d-flex justify-content-center">
                    <button id="addCategoryForm_addBtn" class="btn btn-primary">
                        <spring:message code="add" />
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>