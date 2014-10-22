class window.Search
	this.partner = "YW5kcm9pZC12M3M"
	this.movielist = null

	# API search part
	###
	URL: http://api.allocine.fr/xml/search
	Parameters:
		q: chaîne à chercher (chaîne de caractères)
		partner: schéma XML à utiliser en fonction des partenaires (1, 2, 3, 4)
		json (optionnel): renvoie le résultat au format JSON si json=1 (booléen)
		count (optionnel): nombre de résultats à renvoyer (entier)
		profile (optionnel): degré d'informations renvoyées (énumération : small, medium, large)
		page (optionnel): numéro de la page de résultats à afficher (10 résultats par page par défaut)
		declare (optionnel): aucune idée !?
	###
	@GetAll: (tab) ->
		json = tab["json"] || 1
		filter = "movie,theater,person,news,tvseries"
		count = tab["count"] || 5	
		page = tab["page"] || 5
		q = tab["q"]
		format = "json"

		$.ajax
			type: "GET"
			url: "http://api.allocine.fr/rest/v3/search?partner=" + this.partner
			data:
				filter: filter
				count: count
				page: page
				q: q
				format: format
			success: (msg) ->
				console.log msg
			error: (msg) ->
				alert "error"
	# API infomovie part


	# API showtimelist part

	# API movielist part 
	@GetMovieList: () ->
		if this.movielist == null
			$.ajax
				type: "GET"
				url: "http://localhost:8080/server/cinema"
				data:
					action: "api_request"
					type: "movielist"
					count: "25"
					filter: "nowshowing"
					page: "1"
					order: "datedesc"
					format: "json"
				success: (msg) ->
					
					for movie in msg.feed.movie
						element = "<div class=\"movie\">"
						element += "<p class=\"title\">#{movie.title}</p>"
						element += "<img src=\"#{movie.poster.href}\"/>"
						element += "</div>"
						$(".weekly-movies").append(element)

					$(document).ready( () ->
						$('.weekly-movies').slick(
						 	slidesToShow: 3
							slidesToScroll: 3
							autoplay: true
							autoplaySpeed: 2000
						)
					)

				error: (data, err) ->
					if data.statusText.search(/NetworkError/) >= 0
						response = "Impossible d'établir la connexion avec le serveur."
						$(".weekly-movies").append "<p class=\"error\">#{response}</p>"