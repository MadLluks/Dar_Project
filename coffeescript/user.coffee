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
				<h2>Nous n'avons pas réussi à vous géolocalisez.</h2>
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

	@GetConnection: (login, password) =>
		LoginManager.GetConnection

	@GetPage: () ->
		ViewManager.Load("login")

	@GetConnection: (login, password) ->
		$.ajax
			type: "POST"
			url: "#{address}user"
			dataType: "json"
			data:
				login: $("#login-input").val()
				password: $("#password-input").val()
			success: (msg) ->
				console.log msg
				alert "success"
			error: (msg) ->
				alert "error"

