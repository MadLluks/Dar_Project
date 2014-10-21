$(document).on "click", ".login-btn", () ->
	#LoginManager.GetPage()
	tab = 
		"q":"turtle"
	Search.GetAll(tab)
	return false

success = (data, param) ->
	alert "ok"

$(document).on "click", "#valid-login-input", () ->
	$.ajax(
		type: "POST"
		url: "http://localhost:8080/server/login"
		data: {
			login: $("#login-input").val()
			password: $("#password-input").val()
		}
		success: (msg) ->
			alert "success"
		error: (msg) ->
			alert "error"
	)

	return false


class window.LoginManager 
	@GetPage: () ->
		ViewManager.Load("login")