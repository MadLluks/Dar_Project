class window.ViewManager
	@Init: () ->
		$.get "./view/header.html", (data) ->
			$("#header").html data

		$.get "./view/footer.html", (data) ->
			$("#footer").html data

		$.get "./view/home.html", (data) ->
			$("#content").html data

	@Load: (pageName) ->
		$.get "./view/"+pageName+".html", (data) ->
			$("#content").html data