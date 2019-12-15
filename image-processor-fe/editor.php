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
	<div id="menu">
		<div id="select-send-container">
			<label id="select_action_label" for="select_action">Избор на обработка</label>
			<select id="select_action">
				<option value="0">Компресиране</option>
				<option value="1">Премахване на шум</option>
				<option value="2">Отрази спрямо централен вертикал</option> <!-- ПО ДОБРО ИМЕ??? -->
				<option value="3">Преобразуване в сива снимка</option>
				<option value="4">Преобразуване в черно-бяла снимка</option>
				<option value="5">Извличане на контур</option>
			</select>
			<button id="send">Изпрати</button>
			<p id="response-message"></p>
		</div>
	</div>
	<div id="item_container">
		<img id="item_previewer" src="" />
	</div>
	
</body>
</html>