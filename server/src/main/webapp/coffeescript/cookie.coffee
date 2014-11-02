class window.Cookie
	@isCreated: () =>
		return document.cookie != ""

	@getUser: () =>
		c = "user="
		return document.cookie.substring(c.length, document.cookie.length)
