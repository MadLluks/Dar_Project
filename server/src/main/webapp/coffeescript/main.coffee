address = "https://rihaninsta.herokuapp.com/"

$('#loading-image').bind('ajaxStart', () ->
    $(this).show()
).bind('ajaxStop', () ->
    $(this).hide()
)

$(document).on "click", "#errorGeoaddressSubmit", () ->
	if $("#errorGeolocation input").val() != ""
		LocationManager.GetocationFromPlace $("#errorGeolocation input").val()

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
		if User.IsConnected()
			$("#bar-login-btn").addClass("hide")
			$("#bar-register-btn").addClass("hide")
			$("#bar-profil-btn").removeClass("hide")

		if pageName != "home"
			$("#header").children().remove()
		$.get "./view/"+pageName+".html", (data) ->
			$("#content").html data

		switch pageName
			when "movie" then MovieManager.Init(args)
			when "location" then LocationManager.Init(args)
