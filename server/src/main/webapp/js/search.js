// Generated by CoffeeScript 1.8.0
$(document).on("click", "#search-button", function() {
  var tab;
  $("#header").children().remove();
  tab = {
    q: $("input#search-word").val(),
    filter: $("input[type=\"radio\"][checked=\"checked\"]").val()
  };
  if (tab["q"] !== "") {
    return Search.GetSearch(tab);
  }
});

$(document).on("keypress", "#search-word", function(e) {
  if (e.keyCode === 13) {
    return $("#search-button").click();
  }
});

$(document).on("click", ".pagination:not(.active)", function() {
  return Search.GetSearchPage($(this).attr("attr-id"));
});

$(document).on("click", ".showtime-btn", function() {
  var args;
  args = {
    movienum: $(this).attr("attr-code")
  };
  return ViewManager.Load("movie", args);
});

window.Search = (function() {
  function Search() {}

  Search.partner = "YW5kcm9pZC12M3M";

  Search.movielist = null;


  /*
  	URL: http://api.allocine.fr/xml/search
  	Parameters:
  		q: chaîne à chercher (chaîne de caractères)
  		partner: schéma XML à utiliser en fonction des partenaires (1, 2, 3, 4)
  		json (optionnel): renvoie le résultat au format JSON si json=1 (booléen)
  		count (optionnel): nombre de résultats à renvoyer (entier)
  		profile (optionnel): degré d'informations renvoyées (énumération : small, medium, large)
  		page (optionnel): numéro de la page de résultats à afficher (10 résultats par page par défaut)
  		declare (optionnel): aucune idée !?
   */

  Search.GetSearch = function(tab) {
    var count, filter, json, page, q;
    json = 1;
    filter = tab["filter"];
    count = 5;
    page = tab["page"] || 1;
    q = tab["q"];
    return $.ajax({
      type: "GET",
      url: "" + address + "/cinema",
      data: {
        action: "api_request",
        type: "search",
        q: q,
        filter: filter,
        page: page,
        count: count
      },
      success: function(msg) {
        var element, i, isActive, movie, nbPage, title, _i, _j, _len, _ref, _results;
        $("#results #movie-results").html("<h1>Résultats</h1>");
        $("#search-error").remove;
        if (msg.result.feed.movie === void 0) {
          return $("#results #movie-results").append("<div id=\"search-error\">Aucun film trouvé</div>");
        } else {
          _ref = msg.result.feed.movie;
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            movie = _ref[_i];
            element = "<div class=\"movie-result\">";
            title = movie.originalTitle;
            if (movie.title !== void 0) {
              title = movie.title;
            }
            if (movie.productionYear === void 0) {
              element += "<p class=\"title\">" + title + "</p>";
            } else {
              element += "<p class=\"title\">" + title + " (" + movie.productionYear + ")</p>";
            }
            if (movie.castingShort !== void 0 && movie.castingShort.actors !== void 0) {
              element += "<p class=\"actors-label\">Acteurs : </p><p class=\"actors-value\">" + movie.castingShort.actors + "</p><br/>";
            }
            if (movie.castingShort !== void 0 && movie.castingShort.directors !== void 0) {
              element += "<p class=\"productor-label\">Directeur : </p><p class=\"productor-value\">" + movie.castingShort.directors + "</p><br/>";
            }
            element += "<a href=\"#\" attr-code=\"" + movie.code + "\" class=\"showtime-btn\">Voir les séances</a>";
            element += "</div><hr/>";
            $("#results #movie-results").append(element);
          }
          nbPage = Math.ceil(msg.result.feed.totalResults / msg.result.feed.count);
          $("#results #pagination-zone").children().remove();
          _results = [];
          for (i = _j = 1; 1 <= nbPage ? _j < nbPage : _j > nbPage; i = 1 <= nbPage ? ++_j : --_j) {
            if (i === msg.result.feed.page) {
              isActive = "active";
            } else {
              isActive = "";
            }
            _results.push($("#results #pagination-zone").append("<a href=\"#\" class=\"pagination " + isActive + "\" attr-id=\"" + i + "\">" + i + "</a>"));
          }
          return _results;
        }
      },
      error: function(msg) {
        return alert("error");
      }
    });
  };

  Search.GetSearchPage = function(page) {
    var tab;
    tab = {
      q: $("input#search-word").val(),
      filter: $("input[type=\"radio\"][checked=\"checked\"]").val(),
      page: page
    };
    if (tab["q"] !== "") {
      return Search.GetSearch(tab);
    }
  };

  Search.GetMovieList = function() {
    return $.ajax({
      type: "GET",
      url: "" + address + "/cinema",
      data: {
        action: "api_request",
        type: "movielist",
        count: "25",
        filter: "nowshowing",
        page: "1",
        order: "datedesc"
      },
      success: function(msg) {
        var element, movie, _i, _len, _ref;
        if (msg.result.feed.movie !== void 0) {
          _ref = msg.result.feed.movie;
          for (_i = 0, _len = _ref.length; _i < _len; _i++) {
            movie = _ref[_i];
            element = "<div class=\"movie\">";
            element += "<p class=\"title\">" + movie.title + "</p>";
            element += "</div>";
            $(".weekly-movies").append(element);
          }
          return $(document).ready(function() {
            return $('.weekly-movies').slick({
              slidesToShow: 3
            }, {
              slidesToScroll: 3,
              autoplay: true,
              autoplaySpeed: 2000
            });
          });
        }
      },
      error: function(data, err) {
        var response;
        response = "Impossible d'établir la connexion avec le serveur.";
        return $(".weekly-movies").append("<p class=\"error\">" + response + "</p>");
      }
    });
  };

  return Search;

})();
