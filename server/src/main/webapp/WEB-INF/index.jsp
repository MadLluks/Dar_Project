<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dar Project</title>

    <link href='stylesheets/fonts/Lobster-Regular.ttf' rel='stylesheets' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=PT+Serif:700|Lobster' rel='stylesheet' type='text/css'>

    <link rel="stylesheet" type="text/css" href="stylesheets/app.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/main.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/header.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/content.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/footer.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/search.css" />
    <link rel="stylesheet" type="text/css" href="stylesheets/login.css"/>
    <link rel="stylesheet" type="text/css" href="stylesheets/movie.css"/>
    <link rel="stylesheet" type="text/css" href="stylesheets/location.css"/>
    <link rel="stylesheet" type="text/css" href="stylesheets/slick.css"/>
    <link rel="stylesheet" type="text/css" href="stylesheets/errorGeolocation.css"/>

    <script type="text/javascript" src="scripts/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="bower_components/foundation/js/vendor/modernizr.js"></script>
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/connexion.js"></script>
    <script type="text/javascript" src="js/search.js"></script>
    <script type="text/javascript" src="js/movie.js"></script>
    <script type="text/javascript" src="js/location.js"></script>
    <script type="text/javascript" src="js/user.js"></script>

    <script src="https://maps.googleapis.com/maps/api/js"></script>
    <script>
      function initialize() {
        var mapCanvas = document.getElementById('maps');
        var mapOptions = {
          center: new google.maps.LatLng(44.5403, -78.5463),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        var map = new google.maps.Map(mapCanvas, mapOptions)
      }
    </script>
</head>
<body>
  <div class="sticky">
    <nav class="top-bar" data-topbar role="navigation" data-options="is_hover: false">
      <ul class="title-area">
        <li class="name"></li>
        <li class="name">
          <a href="#"><span>Menu</span></a>
        </li>
      </ul>
      <section class="top-bar-section">
        <!-- Right Nav Section -->
        <ul class="right">
          <li>
            <a href="#" class="viewmanager home">Accueil</a>
          </li>
          <li>
            <a href="#" class="viewmanager login">Connexion</a>
          </li>
          <li class="has-dropdown hide">
            <a href="#">Profil</a>
            <ul class="dropdown">
              <li><a href="#">Mon profil</a></li>
              <li><a href="#">Mes favoris</a></li>
              <li><a href="#">Mon historique</a></li>
            </ul>
          </li>
        </ul>

        <!-- Left Nav Section -->
        <ul class="left">

        </ul>
      </section>
    </nav>
  </div>

  <div id="main">
    <div class="row" id="main-content">
      <div id="content-block">
        <div id="header">
        </div>

        <div id="content" class="large-12 medium-12 small-12 columns">
        </div>
      </div>
    </div>
  </div>
  <div id="footer" class="large-12 medium-12 small-12 columns">

  </div>
  <!-- close the off-canvas menu -->
  <a class="exit-off-canvas"></a>

  <div id="errorGeolocation" class="reveal-modal" data-reveal data-options="close_on_background_click:false">

  </div>
</body>

<script type="text/javascript" src="bower_components/foundation/js/foundation/foundation.js"></script>
<script type="text/javascript" src="bower_components/foundation/js/foundation/foundation.offcanvas.js"></script>
<script type="text/javascript" src="bower_components/foundation/js/foundation/foundation.topbar.js"></script>
<script type="text/javascript" src="bower_components/foundation/js/foundation/foundation.reveal.js"></script>

<script type="text/javascript" src="js/slick.min.js"></script>

<script>
    ViewManager.Init("connexion")
    User.Init()
    $(document).foundation();
</script>
</html>
