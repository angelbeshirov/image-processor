<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Image processor</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" 
	integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="screen" href="css/index.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">

	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/index.js" defer></script>
	<script type="text/javascript" src="js/util.js"></script>
	<script type="text/javascript" src="js/rest.js"></script>

</head>
<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<div id="login-info"></div>
	<section>
		<div class="slideshow-container">
        <div class="mySlides fade">
        <div class="numbertext">1 / 3</div>
        <img src="img/black_and_white.jpg" class="slideshow-image">
        <div class="image-caption">Преобразуване в черно-бяла снимка</div>
        </div>

        <div class="mySlides fade">
        <div class="numbertext">2 / 3</div>
        <img src="img/noise.jpg" class="slideshow-image">
        <div class="image-caption">Премахване на шум</div>
        </div>

        <div class="mySlides fade">
        <div class="numbertext">3 / 3</div>
        <img src="img/gray.jpg" class="slideshow-image">
        <div class="image-caption">Преобразуване в сива снимка</div>
        </div>

        <a class="prev">&#10094;</a>
        <a class="next">&#10095;</a>
        </div>
        <br>

        <div style="text-align:center">
        <span class="dot" id="dot1"></span>
        <span class="dot" id="dot2"></span>
        <span class="dot" id="dot3"></span>
        </div>
	</section>
</body>
</html>
