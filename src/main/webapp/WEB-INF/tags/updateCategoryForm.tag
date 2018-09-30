<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade" id="updateCategoryFormContainer" tabindex="-1" role="dialog">
    <form id="updateCategoryForm">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold">
                        <spring:message code="category.update" />
                    </h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body mx-3">
                    <div class="md-form mb-4">
                        <input type="text" id="categoryNameToUpdate" name="categoryName" class="form-control" required>
                        <label id="categoryNameToUpdateLbl" data-error="wrong" data-success="right" for="categoryNameToUpdate">
                            <spring:message code="category.name" />
                        </label>
                    </div>
                </div>
                <input type="hidden" id="updatedCategoryId" name="id">
                <div class="modal-footer d-flex justify-content-center">
                    <button class="btn btn-primary">
                        <spring:message code="update" />
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>