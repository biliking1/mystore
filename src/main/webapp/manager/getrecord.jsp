<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>访问记录</title>
  </head>
  
  <body style="text-align:center;">
  	<h2>记录信息</h2>
  	<table border="1" width="50%" align="center">
  		<tr style="text-align:center">
  			<td>ip地址</td>
  			<td>用户id</td>
  			<td>登录时间</td>
  		</tr>
	    <c:forEach var="c" items="${recos }">
	    	<tr style="text-align:center">
	  			<td>${c.ip }</td>
	  			<td>${c.name }</td>
	  			<td>${c.time }</td>
	  		
  			</tr>
	    </c:forEach>
  	</table>
  </body>
</html>
