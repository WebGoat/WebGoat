<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : hints
    Created on : Aug 27, 2014, 3:41:46 PM
    Author     : rlawson
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<div class="panel-group" id="accordion">
    <c:forEach var="hint" items="${hints}" varStatus="status">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse_${hint.number}">
                        Hint-${hint.number}
                    </a>
                </h3>
            </div>
            <div id="collapse_${hint.number}" class="panel-collapse collapse">
                <div class="panel-body">
                    ${hint.hint}
                </div>
            </div>
        </div>
    </c:forEach>
</div>

