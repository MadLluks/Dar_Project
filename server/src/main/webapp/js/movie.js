// Generated by CoffeeScript 1.4.0
var debug;

debug = true;

$(document).on("click", ".hour-list a", function() {
  var args, theaterInfos;
  theaterInfos = $(this).parents(".theater").children("input");
  args = {
    hour: $(this).html(),
    theater: theaterInfos.val(),
    long: theaterInfos.attr("attr-long"),
    lat: theaterInfos.attr("attr-lat")
  };
  if (debug) {
    console.log(args);
  }
  ViewManager.Load("location", args);
  return false;
});

window.MovieManager = (function() {

  function MovieManager() {}

  MovieManager.movienum = "";

  MovieManager.movieInfos = "";

  MovieManager.Init = function(args) {
    MovieManager.movienum = args["movienum"];
    return $.ajax({
      type: "GET",
      url: "" + address + "/cinema",
      data: {
        action: "api_request",
        type: "movie",
        code: MovieManager.movienum
      },
      success: function(msg) {
        MovieManager.movieInfos = msg.result.movie;
        return MovieManager.GetMovie();
      },
      error: function(err) {
        return alert("une erreur est survenue lors de la requête sur les informations du film");
      }
    });
  };

  MovieManager.GetMovie = function() {
    return $.ajax({
      type: "GET",
      url: "" + address + "/cinema",
      data: {
        action: "api_request",
        type: "showtimelist",
        movie: MovieManager.movienum,
        lat: User.latitude,
        long: User.longitude
      },
      success: function(msg) {
        var base, currentElement, day, hour, place, show, showtime, theater, _i, _len, _ref, _results;
        if (debug) {
          console.log(msg.result.feed.theaterShowtimes);
          console.log(MovieManager.movieInfos);
        }
        base = $("#theater-list");
        base.append("<div class=\"small-12\" id=\"movieInfos\"><input type=\"hidden\" value=\"" + MovieManager.movieInfos.code + "\"/><h1>" + MovieManager.movieInfos.title + "</h1></div>");
        if (MovieManager.movieInfos.originalTitle !== void 0) {
          $("#movieInfos").append("<h2>" + MovieManager.movieInfos.originalTitle + "</h2>");
        }
        if (MovieManager.movieInfos.productionYear !== void 0) {
          $("#movieInfos>h1").append("<span> (" + MovieManager.movieInfos.productionYear + ")</span>");
        }
        /*if @movieInfos.trailerEmbed != undefined
        					$("#movieInfos").append(@movieInfos.trailerEmbed)
        */

        _ref = msg.result.feed.theaterShowtimes;
        _results = [];
        for (_i = 0, _len = _ref.length; _i < _len; _i++) {
          theater = _ref[_i];
          base.append("<div class=\"theater small-6 columns\"></div>");
          currentElement = $(".theater").last();
          place = theater.place.theater;
          currentElement.append("<input type=\"hidden\" value=\"" + place.code + "\" attr-lat=\"" + place.geoloc.lat + "\" attr-long=\"" + place.geoloc.long + "\"/>");
          currentElement.append("<p class=\"title\">" + place.name + "</p>");
          currentElement.append("<div class=\"seance\">");
          _results.push((function() {
            var _j, _len1, _ref1, _results1;
            _ref1 = theater.movieShowtimes;
            _results1 = [];
            for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
              show = _ref1[_j];
              _results1.push((function() {
                var _k, _l, _len2, _len3, _ref2, _ref3, _results2;
                _ref2 = show.scr;
                _results2 = [];
                for (_k = 0, _len2 = _ref2.length; _k < _len2; _k++) {
                  day = _ref2[_k];
                  showtime = "<p class=\"day\">" + day.d + " (" + show.screenFormat.$ + ") : </p><div class=\"hour-list\">";
                  _ref3 = day.t;
                  for (_l = 0, _len3 = _ref3.length; _l < _len3; _l++) {
                    hour = _ref3[_l];
                    showtime += "<a href=\"#\">" + hour.$ + "</a> ";
                  }
                  showtime += "</div>";
                  _results2.push(currentElement.append(showtime));
                }
                return _results2;
              })());
            }
            return _results1;
          })());
        }
        return _results;
      },
      error: function(data, err) {
        console.log(data);
        return console.log(Error);
      }
    });
  };

  return MovieManager;

}).call(this);
