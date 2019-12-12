<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" type="text/css" media="screen" href="css/login.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">

	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/login.js" defer></script>
	
	<script type="text/javascript" src="js/rest.js"></script>
</head>
<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<section class="main-container">
		<form id="login-form">
			<h2 class="form-title">Вход</h2>
			<div class="input-container">
				<input class="field-input" id="email" name="email" type="email" placeholder="Имейл" required/>
			</div>
			<div class="input-container">
				<input class="field-input" id="password" name="password" type="password" placeholder="Парола" required/>
			</div>
			<div class="input-container">
				<div class="error"></div>
				<input id="login" class="btn" type="submit" value="Вход"/>
			</div>
		</form>
	</section>
</body>
</html>
