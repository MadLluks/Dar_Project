class window.LoginManager 
	@GetPage: () ->
		ViewManager.Load("login")

	@GetConnection: (login, password) ->
		$.ajax
			type: "POST"
			url: "#{address}/login"
			data:
				login: $("#login-input").val()
				password: $("#password-input").val()
			success: (msg) ->
				console.log msg
				alert "success"
			error: (msg) ->
				alert "error"

	return false