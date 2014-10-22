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

	@Load: (pageName) ->
		$.get "./view/"+pageName+".html", (data) ->
			$("#content").html data
