<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Register page</h1>
<form method="POST" action="register">
	<label for="login">Login :</label>
	<input name="login" type="text" width="150" />
	<label for="password">Password :</label>
	<input name="password" type="password" width="150" />
	<input type="submit" value="Register" />
	<input type="hidden" name="action" value="register" />
</form>
</body>
</html>