debug = true

class window.LocationManager
	@theaterPosition = ""

	@Init: (args) =>
		if debug
			console.log args

		@theaterPosition = args
		navigator.geolocation.getCurrentPosition(@.GetLocation, @.GetLocationError, {timeout:6000})
		return

	@GetLocation: (position) =>
		$.ajax
			type: "GET"
			url: "#{address}/gmaps"
			data:
				origin: "#{position.coords.latitude},#{position.coords.longitude}"
				destination: "#{@theaterPosition.lat}, #{@theaterPosition.long}"
			success: (msg) =>
				console.log msg

				northeast = msg.result.routes[0].bounds.northeast
				southwest = msg.result.routes[0].bounds.southwest
				
				bounds = new google.maps.LatLngBounds()
				bound1 = new google.maps.LatLng(northeast.lat, northeast.lng)
				bound2 = new google.maps.LatLng(southwest.lat, southwest.lng)

				

				x = (parseInt(position.coords.latitude) + parseInt(@theaterPosition.lat)) /2
				y = (parseInt(position.coords.longitude) + parseInt(@theaterPosition.long)) /2

				mapCanvas = document.getElementById('maps')
				mapOptions =
		          center: new google.maps.LatLng(x, y)
		          zoom: 8
		          mapTypeId: google.maps.MapTypeId.ROADMAP

		        map = new google.maps.Map(mapCanvas, mapOptions)

		        bounds.extend(bound1)
				bounds.extend(bound2)
				map.fitBounds(bounds)

			error: (err) ->
				alert "location error"

	@GetLocationError: (err) =>
		if(err.code == 1) 
    		alert("Error: Access is denied!");
  		else if( err.code == 2)
    		alert("Error: Position is unavailable!");
		else
			console.log "error"

	@GetocationFromPlace: (coordinates) =>
		$.ajax
			type: "GET"
			url: "https://maps.googleapis.com/maps/api/geocode/json"
			data:
				address: coordinates
			success: (data) ->
				if debug
					console.log data
				User.SetLocation data.results[0].geometry.location.lat, data.results[0].geometry.location.lng
				$('#errorGeolocation').foundation('reveal', 'close');
			error: (err) ->
				console.log err