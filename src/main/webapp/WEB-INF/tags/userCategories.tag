<%@ tag description="User categories" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ attribute name="categories" required="true" type="java.util.List" %>

<%-- User categories table --%>
<table id="categoriesTable" class="cell-border hover w-100">
    <thead class="text-white" style="background-color: #4285f4;">
        <th><spring:message code="name" /></th>
        <th style="width: 30px;"><spring:message code="actions" /></th>
    </thead>
    <tbody>
    <c:forEach items="${categories}" var="category">
        <tr>
            <td class="categoryCell">
                <a href="/categories/${category.id}">${category.name}</a>
            </td>
            <td>
                <div class="dropdown">
                    <button class="btn dropdown-toggle" style="background-color: #6675df; padding: 5px 30px;" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></button>
                    <div class="dropdown-menu" style="text-transform: none;" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item" style="height: 30px; margin-right: 10px;  cursor: pointer;" data-toggle="modal" data-target="#cloneCategoryFormContainer"
                           onclick="Categories.prepareCloningForm('${category.name}', ${category.id});">
                            <img data-toggle="modal" data-target="#cloneCategoryFormContainer" style="width: 16px; height: 16px;" src="/images/clone.png"
                                 onclick="Categories.prepareCloningForm('${category.name}', ${category.id});"> <spring:message code="clone" />
                        </a>
                        <a class="dropdown-item" style="height: 30px; margin-right: 10px;  cursor: pointer;" onclick="Categories.showUpdateForm(${category.id})">
                            <img style="width: 16px; height: 16px;" src="/images/edit.png" /> <spring:message code="edit" /><br><br>
                        </a>
                        <a class="dropdown-item" style="height: 30px; margin-right: 10px;  cursor: pointer;" onclick="Categories.removeCategory(${category.id});">
                            <img style="width: 16px; height: 16px;" src="/images/delete.png" /> <spring:message code="delete" /><br><br>
                        </a>
                        <hr />
                        <div class="d-flex" style="justify-content: center;">
                            <div>
                                <label class="switch" style="margin-bottom: 0px;">
                                    <input type="checkbox" onchange="Categories.shareCategory(this, ${category.id});" />
                                    <span class="slider round"></span>
                                </label>
                                <div><spring:message code="share" /></div>
                            </div>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%-- *** --%>