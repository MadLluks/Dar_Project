debug = true

$(document).on "keypress", "#errorGeoaddress", (e) ->
	if e.keyCode == 13
		$("#errorGeoaddressSubmit").click()

class window.LocationManager
	@theaterPosition = ""

	@Init: (args) =>
		if debug
			console.log args

		@theaterPosition = args

		User.SaveMovie(args)

		@GetLocation(args)
		return

	@GetLocation: () =>
		$.ajax
			type: "GET"
			url: "#{address}/gmaps"
			data:
				origin: "#{User.latitude}, #{User.longitude}"
				destination: "#{@theaterPosition.lat}, #{@theaterPosition.long}"
			success: (msg) =>
				console.log msg

				northeast = msg.result.routes[0].bounds.northeast
				southwest = msg.result.routes[0].bounds.southwest
				
				bounds = new google.maps.LatLngBounds()
				bound1 = new google.maps.LatLng(northeast.lat, northeast.lng)
				bound2 = new google.maps.LatLng(southwest.lat, southwest.lng)

				

				x = (parseInt(User.latitude) + parseInt(@theaterPosition.lat)) /2
				y = (parseInt(User.longitude) + parseInt(@theaterPosition.long)) /2

				mapCanvas = document.getElementById('maps')
				mapOptions =
		          center: new google.maps.LatLng(x, y)
		          zoom: 8
		          mapTypeId: google.maps.MapTypeId.ROADMAP

		        map = new google.maps.Map(mapCanvas, mapOptions)


		        bounds.extend(bound1)
				bounds.extend(bound2)

				polyOptions = {
				    strokeColor: '#7F0000'
				    strokeOpacity: 1.0
				    strokeWeight: 3				 
				} 

				poly = new google.maps.Polyline(polyOptions)

				poly.setMap(map);

				path = poly.getPath()

				for step in msg.result.routes[0].legs[0].steps
					path.push(new google.maps.LatLng(step.start_location.lat, step.start_location.lng))
					path.push(new google.maps.LatLng(step.end_location.lat, step.end_location.lng))


				new google.maps.Marker(
					position: new google.maps.LatLng(msg.result.routes[0].legs[0].start_location.lat, msg.result.routes[0].legs[0].start_location.lng),
					map: map,
					title: 'Hello World!'
				)

				new google.maps.Marker(
					position: new google.maps.LatLng(msg.result.routes[0].legs[0].end_location.lat, msg.result.routes[0].legs[0].end_location.lng),
					map: map,
					title: 'Hello World!'
				)

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