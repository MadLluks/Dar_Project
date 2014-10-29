$(document).on "click", "#search-button", () ->
	$("#header").children().remove()
	tab =
		q: $("input#search-word").val()
		filter: $("input[type=\"radio\"][checked=\"checked\"]").val()
	if tab["q"] != ""
		Search.GetSearch(tab)


$(document).on "keypress", "#search-word", (e) ->
	if e.keyCode == 13
		$("#search-button").click()

$(document).on "click", ".pagination:not(.active)", () ->
	Search.GetSearchPage($(this).attr("attr-id"))

$(document).on "click", ".showtime-btn", () ->
	args = 
		movienum: $(@).attr("attr-code")
	ViewManager.Load("movie", args)

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
	@GetSearch: (tab) ->
		json = 1
		filter = tab["filter"]
		count = 5	
		page = tab["page"] || 1
		q = tab["q"]

		$.ajax
			type: "GET"
			url: "#{address}/cinema"
			data:
				action:"api_request"
				type: "search"
				q: q
				filter: filter
				page: page
				count: count
			success: (msg) ->
				$("#results #movie-results").html("<h1>Résultats</h1>")
				$("#search-error").remove
				if(msg.result.feed.movie == undefined)
					$("#results #movie-results").append "<div id=\"search-error\">Aucun film trouvé</div>"
				else
					for movie in msg.result.feed.movie
						element = "<div class=\"movie-result\">"

						title = movie.originalTitle
						if movie.title != undefined
							title = movie.title
						if movie.productionYear == undefined
							element += "<p class=\"title\">#{title}</p>"
						else
							element += "<p class=\"title\">#{title} (#{movie.productionYear})</p>"
						if movie.castingShort != undefined && movie.castingShort.actors != undefined
							element += "<p class=\"actors-label\">Acteurs : </p><p class=\"actors-value\">#{movie.castingShort.actors}</p><br/>"
						if movie.castingShort != undefined && movie.castingShort.directors != undefined
							element += "<p class=\"productor-label\">Directeur : </p><p class=\"productor-value\">#{movie.castingShort.directors}</p><br/>"
						element += "<a href=\"#\" attr-code=\"#{movie.code}\" class=\"showtime-btn\">Voir les séances</a>"
						element += "</div><hr/>"
						$("#results #movie-results").append element
					nbPage = Math.ceil( msg.result.feed.totalResults / msg.result.feed.count)
					$("#results #pagination-zone").children().remove()
					for i in [1...nbPage]
						if i == msg.result.feed.page
							isActive = "active"
						else
							isActive = ""
						$("#results #pagination-zone").append "<a href=\"#\" class=\"pagination #{isActive}\" attr-id=\"#{i}\">#{i}</a>"
			error: (msg) ->
				alert "error"

	@GetSearchPage: (page) ->
		tab =
			q: $("input#search-word").val()
			filter: $("input[type=\"radio\"][checked=\"checked\"]").val()
			page: page
		if tab["q"] != ""
			Search.GetSearch(tab)

	# API infomovie part

	# API showtimelist part

	# API movielist part 
	@GetMovieList: () ->

		$.ajax
			type: "GET"
			url: "#{address}/cinema"
			data:
				action: "api_request"
				type: "movielist"
				count: "25"
				filter: "nowshowing"
				page: "1"
				order: "datedesc"
			success: (msg) ->
				if msg.result.feed.movie != undefined
					for movie in msg.result.feed.movie
						element = "<div class=\"movie\">"
						element += "<p class=\"title\">#{movie.title}</p>"
						#element += "<img src=\"#{movie.poster.href}\"/>"
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
				response = "Impossible d'établir la connexion avec le serveur."
				$(".weekly-movies").append "<p class=\"error\">#{response}</p>"