debug = true

$(document).on "click", ".hour-list a", () ->
	theaterInfos = $(this).parents(".theater").children("input")

	args =
		hour: $(@).html()
		theater: theaterInfos.val()
		long: theaterInfos.attr "attr-long"
		lat: theaterInfos.attr "attr-lat"

	if debug
		console.log args
	ViewManager.Load("location", args)
	return false;

class window.MovieManager
	@movienum = ""
	@movieInfos = ""

	@Init: (args) =>
		@movienum = args["movienum"]
		$.ajax
			type: "GET"
			url: "#{address}/cinema"
			data:
				action: "api_request"
				type: "movie"	
				code: @movienum
			success: (msg) =>
				@movieInfos = msg.result.movie
				@GetMovie()
			error: (err) ->
				alert "une erreur est survenue lors de la requête sur les informations du film"

	@GetMovie: () =>
		$.ajax
			type: "GET"
			url: "#{address}/cinema"
			data:
				action:"api_request"
				type: "showtimelist"
				movie: @movienum
				lat: User.latitude
				long: User.longitude
			success: (msg) =>
				if debug
					console.log msg.result.feed.theaterShowtimes
					console.log @movieInfos
				base = $("#theater-list")
				base.append "<div class=\"small-12\" id=\"movieInfos\"><input type=\"hidden\" value=\"#{@movieInfos.code}\"/><h1>#{@movieInfos.title}</h1></div>"
				if @movieInfos.originalTitle != undefined
					$("#movieInfos").append("<h2>#{@movieInfos.originalTitle}</h2>")
				if @movieInfos.productionYear != undefined
					$("#movieInfos>h1").append("<span> (#{@movieInfos.productionYear})</span>")
				###if @movieInfos.trailerEmbed != undefined
					$("#movieInfos").append(@movieInfos.trailerEmbed)###

				for theater in msg.result.feed.theaterShowtimes
					base.append "<div class=\"theater small-6 columns\"></div>"
					currentElement = $(".theater").last()
					
					place = theater.place.theater

					currentElement.append "<input type=\"hidden\" value=\"#{place.code}\" attr-lat=\"#{place.geoloc.lat}\" attr-long=\"#{place.geoloc.long}\"/>"
					currentElement.append "<p class=\"title\">#{place.name}</p>"
					currentElement.append "<div class=\"seance\">"

					for show in theater.movieShowtimes
						for day in show.scr
							showtime = "<p class=\"day\">#{day.d} (#{show.screenFormat.$}) : </p><div class=\"hour-list\">"
							for hour in day.t
								showtime += "<a href=\"#\">" + hour.$ + "</a> "
							showtime += "</div>"
							currentElement.append showtime

			error: (data, err) ->
				console.log data
				console.log Error