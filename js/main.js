// Generated by CoffeeScript 1.4.0

window.ViewManager = (function() {

  function ViewManager() {}

  ViewManager.Init = function() {
    $.get("./view/header.html", function(data) {
      return $("#header").html(data);
    });
    $.get("./view/footer.html", function(data) {
      return $("#footer").html(data);
    });
    return $.get("./view/home.html", function(data) {
      return $("#content").html(data);
    });
  };

  ViewManager.Load = function(pageName) {
    return $.get("./view/" + pageName + ".html", function(data) {
      return $("#content").html(data);
    });
  };

  return ViewManager;

})();
