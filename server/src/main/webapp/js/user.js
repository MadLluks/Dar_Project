// Generated by CoffeeScript 1.8.0
$(document).on("click", ".login-btn", function() {
  ViewManger.Load("login");
  return false;
});

$(document).on("click", "#valid-login-input", function() {
  User.GetConnection($("#login-input").val(), $("#password-input").val());
  return false;
});

$(document).on("click", "a.logout", function() {
  document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
  User.Logout();
  return false;
});

$(document).on("click", "a.register", function() {
  ViewnManager.Load("register");
  return false;
});

$(document).on("click", "#valid-register-input", function() {
  User.Register($("#login-input").val(), $("#password-input").val());
  return false;
});

$(document).on("click", "#bar-profil-btn #history", function() {
  return User.GetHistory();
});

window.User = (function() {
  function User() {}

  User.latitude;

  User.longitude;

  User.pseudo;

  User.Init = function() {
    if (navigator.geolocation) {
      return navigator.geolocation.getCurrentPosition(User.GeolocSuccess, User.GeolocError, {
        timeout: 1000
      });
    } else {
      return console.err("error");
    }
  };

  User.GeolocSuccess = function(position) {
    User.latitude = position.coords.latitude;
    return User.longitude = position.coords.longitude;
  };

  User.GeolocError = function() {
    var bloc;
    $('#errorGeolocation').children().remove();
    bloc = "<div> <h1>Une erreur est survenue</h1> <h2>Nous n'avons pas réussi à vous géolocaliser.</h2> <p>Pour pouvoir continuer à utiliser notre site merci de bien vouloir entrer le nom de votre ville ou son code postal. Pour plus de précision vous pouvez également entrer une addresse. </p> <input type=\"text\" id=\"errorGeoaddress\" placeHolder=\"nom de ville, code postal, addresse, ...\"/> <div id=\"errorGeoaddressSubmit\" >Valider</div> </div>";
    $('#errorGeolocation').append(bloc);
    return $('#errorGeolocation').foundation('reveal', 'open');
  };

  User.SetLocation = function(latitude, longitude) {
    User.latitude = latitude;
    return User.longitude = longitude;
  };

  User.GetConnection = function(login, password) {
    return $.ajax({
      type: "POST",
      url: "" + address + "/user",
      data: {
        action: "login",
        login: login,
        password: password
      },
      success: function(msg) {
        User.pseudo = login;
        return ViewManager.Load("home");
      },
      error: function(msg) {
        return alert("error");
      }
    });
  };

  User.Logout = function() {
    document.cookie = "user=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    User.pseudo = "";
    $("#bar-login-btn").removeClass("hide");
    $("#bar-register-btn").removeClass("hide");
    $("#bar-profil-btn").addClass("hide");
    return ViewManager.Load("home");
  };

  User.Register = function(login, password) {
    return $.ajax({
      type: "POST",
      url: "" + address + "/user",
      data: {
        action: "register",
        login: login,
        password: password
      },
      success: function(msg) {
        User.pseudo = login;
        return User.GetConnection(login, password);
      },
      error: function(msg) {
        return alert("error");
      }
    });
  };

  User.IsConnected = function() {
    if (Cookie.isCreated()) {
      return User.pseudo === Cookie.getUser();
    }
  };

  User.GetHistory = function() {
    return $.ajax({
      type: "GET",
      url: "" + address + "/cinema",
      data: {
        action: "user_list",
        type: "json"
      },
      success: function(msg) {
        return console.log(msg);
      },
      error: function(err) {
        return alert("error");
      }
    });
  };

  User.SaveMovie = function(args) {
    return $.ajax({
      type: "POST",
      url: "" + address + "/movie",
      data: {
        title: args.movietitle,
        movie_id: args.movieid,
        cine_lon: args.long,
        cine_lat: args.lat,
        cine_name: args.theater
      },
      success: function(msg) {
        return console.log(msg);
      },
      error: function(err) {
        return alert("error");
      }
    });
  };

  return User;

})();