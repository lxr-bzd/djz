<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
	String cpath = request.getContextPath();
	request.setAttribute("contextPath", cpath);
	%>
<c:set var="member" value="<%=request.getSession().getAttribute(com.util.DataUtil.USER)%>" scope="session"/>
