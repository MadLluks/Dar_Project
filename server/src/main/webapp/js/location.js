// Generated by CoffeeScript 1.4.0
var debug;

debug = true;

window.LocationManager = (function() {

  function LocationManager() {}

  LocationManager.theaterPosition = "";

  LocationManager.Init = function(args) {
    if (debug) {
      console.log(args);
    }
    LocationManager.theaterPosition = args;
    navigator.geolocation.getCurrentPosition(LocationManager.GetLocation, LocationManager.GetLocationError, {
      timeout: 6000
    });
  };

  LocationManager.GetLocation = function(position) {
    return $.ajax({
      type: "GET",
      url: "" + address + "/gmaps",
      data: {
        origin: "" + position.coords.latitude + "," + position.coords.longitude,
        destination: "" + LocationManager.theaterPosition.lat + ", " + LocationManager.theaterPosition.long
      },
      success: function(msg) {
        var bound1, bound2, bounds, map, mapCanvas, mapOptions, northeast, southwest, x, y;
        console.log(msg);
        northeast = msg.result.routes[0].bounds.northeast;
        southwest = msg.result.routes[0].bounds.southwest;
        bounds = new google.maps.LatLngBounds();
        bound1 = new google.maps.LatLng(northeast.lat, northeast.lng);
        bound2 = new google.maps.LatLng(southwest.lat, southwest.lng);
        x = (parseInt(position.coords.latitude) + parseInt(LocationManager.theaterPosition.lat)) / 2;
        y = (parseInt(position.coords.longitude) + parseInt(LocationManager.theaterPosition.long)) / 2;
        mapCanvas = document.getElementById('maps');
        mapOptions = {
          center: new google.maps.LatLng(x, y),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(mapCanvas, mapOptions);
        bounds.extend(bound1);
        bounds.extend(bound2);
        return map.fitBounds(bounds);
      },
      error: function(err) {
        return alert("location error");
      }
    });
  };

  LocationManager.GetLocationError = function(err) {
    if (err.code === 1) {
      return alert("Error: Access is denied!");
    } else if (err.code === 2) {
      return alert("Error: Position is unavailable!");
    } else {
      return console.log("error");
    }
  };

  LocationManager.GetocationFromPlace = function(coordinates) {
    return $.ajax({
      type: "GET",
      url: "https://maps.googleapis.com/maps/api/geocode/json",
      data: {
        address: coordinates
      },
      success: function(data) {
        if (debug) {
          console.log(data);
        }
        User.SetLocation(data.results[0].geometry.location.lat, data.results[0].geometry.location.lng);
        return $('#errorGeolocation').foundation('reveal', 'close');
      },
      error: function(err) {
        return console.log(err);
      }
    });
  };

  return LocationManager;

}).call(this);
