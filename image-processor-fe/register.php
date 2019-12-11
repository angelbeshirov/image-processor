<!-- php
	require "util/util.php";
	should_redirect_logged_in();
 > -->

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Registration</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="screen" href="css/register.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">

	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/register.js" defer></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>

	<script type="text/javascript" src="js/rest.js"></script>
</head>
<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<section class="main-container">
		<form id="register-form">
			<h2 class="form-title">Регистрация</h2>
			<div class="input-container">
				<input class="field-input" id="email" name="email" type="email" placeholder="Имейл" required/>
				<div class="error"></div>
			</div>
			<div class="input-container">
				<input class="field-input" id="username" name="username" type="text" placeholder="Потребителско име" required/>
				<div class="error"></div>
			</div>
			<div class="input-container">
				<input class="field-input" id="password" name="password" type="password" placeholder="Парола" required/>
				<div class="error"></div>
			</div>
			<div class="input-container">
				<input class="field-input" id="password-repeat" name="password-repeat" type="password" placeholder="Повторете паролата" required/>
				<div class="error"></div>
			</div>
			<div class="input-container">
				<div class="message"></div>
				<input id="register" class="btn" type="submit" value="Регистрация"/>
			</div>
		</form>
	</section>
</body>
</html>
