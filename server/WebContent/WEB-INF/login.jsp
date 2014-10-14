<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
<h1>Login page</h1>
<form method="POST" action="login">
	<label for="login">Login :</label>
	<input name="login" type="text" width="150" />
	<label for="password">Password :</label>
	<input name="password" type="password" width="150" />
	<input type="submit" value="Login" />
</form>
</body>
</html>