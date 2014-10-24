adress = "http://test"

class window.ViewManager
	@Init: () =>

		if $("#header").children().length == 0
			$.get "./view/header.html", (data) ->
				$("#header").html data

		###$.get "./view/footer.html", (data) ->
			$("#footer").html data###

		$.get "./view/home.html", (data) ->
			$("#content").html data

		$("body").on "click", "a.viewmanager", () ->
			ViewManager.Load this.classList[1]
			return false

	@Load: (pageName, args) ->
		if pageName != "home"
			$("#header").children().remove()
		$.get "./view/"+pageName+".html", (data) ->
			$("#content").html data

		switch pageName
			when "movie" then MovieManager.Init(args)
			when "location" then LocationManager.Init(args)

