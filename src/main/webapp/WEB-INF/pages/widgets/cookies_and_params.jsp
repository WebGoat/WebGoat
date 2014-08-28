<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : hints
    Created on : Aug 27, 2014, 3:41:46 PM
    Author     : rlawson
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<div class="col-md-6">
    <table class="table table-condensed table-striped">
        <caption><span class="label label-default">Parameters</span></caption>
        <thead>
            <tr><th>Name</th><th>Value</th></tr>
        </thead>
        <tbody>
            <c:forEach var="wgparam" items="${wgparams}" varStatus="status">
                <tr><td><span class="label label-info">${wgparam.name}</span></td><td>${wgparam.value}</td></tr>
            </c:forEach>  
        </tbody>
    </table>
</div>
<div class="col-md-6">
    <table class="table table-condensed  table-striped">
        <caption><span class="label label-default">Cookies</span></caption>
        <thead>
            <tr><th>Name</th><th>Value</th></tr>
        </thead>
        <tbody>
            <c:forEach var="wgcookie" items="${wgcookies}" varStatus="status">
                <tr><td><span class="label label-info">${wgcookie.name}</span></td><td>${wgcookie.value}</td></tr>
            </c:forEach>  
        </tbody>
    </table>
</div>





