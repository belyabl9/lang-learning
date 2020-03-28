<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="modal fade" id="importCategoryFormContainer" tabindex="-1" role="dialog">
    <form id="importCategoryForm">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header text-center">
                    <h4 class="modal-title w-100 font-weight-bold">
                        <spring:message code="category.import" />
                    </h4>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body mx-3">
                    <div class="md-form mb-4">
                        <input type="text" id="categoryNameToImport" name="categoryName" class="form-control" required>
                        <label id="categoryNameToImportLbl" data-error="wrong" data-success="right" for="categoryNameToImport">
                            <spring:message code="category.name" />
                        </label>
                    </div>
                    <label id="categoryWordsFileLbl" data-error="wrong" data-success="right" for="categoryNameToImport">
                        <spring:message code="category.words.file" />:
                    </label>
                    <div class="md-form mb-4">
                        <input type="file" id="categoryWordsFile" name="categoryWordsFile" class="form-control" required>
                    </div>
                </div>
                <input type="hidden" id="newCategoryUserId" name="userId" />
                <div class="modal-footer d-flex justify-content-center">
                    <button id="addCategoryForm_addBtn" class="btn btn-primary">
                        <spring:message code="import" />
                    </button>
                </div>
            </div>
        </div>
    </form>
</div>