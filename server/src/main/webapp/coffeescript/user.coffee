$(document).on "click", ".login-btn", () ->
	ViewManger.Load "login"
	return false

$(document).on "click", "#valid-login-input", () ->
	User.GetConnection $("#login-input").val(), $("#password-input").val()
	return false

$(document).on "click", "a.logout", () ->
	document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	User.Logout()
	return false

$(document).on "click", "a.register", () ->
	ViewnManager.Load "register"
	return false

$(document).on "click", "#valid-register-input", () ->
	User.Register $("#login-input").val(), $("#password-input").val()
	return false

$(document).on "click", "#bar-profil-btn #history", () ->
	User.GetHistory()

class window.User
	#//172.16.12.162/public
	@latitude
	@longitude
	@pseudo

	@Init: () =>
		if(navigator.geolocation)
			navigator.geolocation.getCurrentPosition @GeolocSuccess, @GeolocError, {timeout: 1000}
		else
			console.err "error"

	@GeolocSuccess: (position) =>
		@latitude = position.coords.latitude
		@longitude = position.coords.longitude

	@GeolocError: () =>
		$('#errorGeolocation').children().remove()

		bloc = 
			"<div>
				<h1>Une erreur est survenue</h1>
				<h2>Nous n'avons pas réussi à vous géolocaliser.</h2>
				<p>Pour pouvoir continuer à utiliser notre site merci de bien vouloir
				entrer le nom de votre ville ou son code postal.
				Pour plus de précision vous pouvez également entrer une addresse.
				</p>
				<input type=\"text\" id=\"errorGeoaddress\" placeHolder=\"nom de ville, code postal, addresse, ...\"/>
				<div id=\"errorGeoaddressSubmit\" >Valider</div>
			</div>"

		$('#errorGeolocation').append bloc
		$('#errorGeolocation').foundation('reveal', 'open');


	@SetLocation: (latitude, longitude) =>
		@latitude = latitude
		@longitude = longitude

	@GetConnection: (login, password) ->
		$.ajax
			type: "POST"
			url: "#{address}/user"
			data:
				action: "login"
				login: login
				password: password
			success: (msg) ->
				User.pseudo = login
				ViewManager.Load("home")
			error: (msg) ->
				alert "error"

	@Logout: () =>
		document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
		User.pseudo = ""
		$("#bar-login-btn").removeClass("hide")
		$("#bar-register-btn").removeClass("hide")
		$("#bar-profil-btn").addClass("hide")
		ViewManager.Load("home")

	@Register: (login, password) =>
		$.ajax
			type: "POST"
			url: "#{address}/user"
			data:
				action: "register"
				login: login
				password: password
			success: (msg) =>
				@pseudo = login
				@GetConnection login, password
			error: (msg) ->
				alert "error"


	@IsConnected: () =>
		if Cookie.isCreated()
			return User.pseudo == Cookie.getUser()

	@GetHistory: () =>
		$.ajax
			type: "GET"
			url: "#{address}/cinema"
			data:
				action: "user_list"
				type: "json"
			success: (msg) =>
				console.log msg

			error: (err) =>
				alert("error")

	@SaveMovie: (args) =>
		$.ajax
			type: "POST"
			url: "#{address}/movie"
			data:
				title: args.movietitle
				movie_id: args.movieid
				cine_lon: args.long
				cine_lat: args.lat
				cine_name: args.theater
			success: (msg) =>
				console.log msg
			error: (err) =>
				alert("error")

