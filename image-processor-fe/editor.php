<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0>
	<meta charset='UTF-8'/>
	<title>Image Editor</title>
	<link rel="stylesheet" type="text/css" media="screen" href="css/editor.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">

	<script type="text/javascript" src="js/editor.js" defer></script>
	<script type="text/javascript" src="js/websocket.js" defer></script>
	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/rest.js"></script>
</head>

<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<header>
		<div id=title-container>
			<h3 id="title">Снимка: </h3>
		</div>
	</header>
	<div id="menu"></div>
	<img id="item_previewer" src="" />
	<button id="send"> Изпрати </button>
</body>
</html>