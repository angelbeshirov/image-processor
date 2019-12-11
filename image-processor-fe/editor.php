<?php
require "util/util.php";
require "database_manager.php";
require "config_manager.php";

should_redirect_not_logged_in();

if ($_SERVER["REQUEST_METHOD"] === "GET" && isset($_GET["file"])) {
	should_start_session();
	
    if(isset($_SESSION["loggedin"]) && isset($_SESSION["id"]) && $_SESSION["loggedin"]) {
		$database_manager = new database_manager();
		$file_to_open = $_GET["file"];
		$result = $database_manager->get_path($_SESSION["id"], $file_to_open);
		if($result) {
			$path = $result[0]["path"];
		} else if(isset($_GET["shared_by"])) {
			$user = $database_manager->get_user_by_username($_GET["shared_by"]);
			if($user) {
				$userID = $user[0]["id"];
				$share = $database_manager->get_share($userID, $_SESSION["id"], $file_to_open);
				if($share) {
					$path = $database_manager->get_path($userID, $file_to_open)[0]["path"];
				}
			}
		}
	}
}

if(!isset($path) || !file_exists($path)) {
	redirect("index.php");
}
?>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0>
	<meta charset='UTF-8'/>
	<title>Editor</title>
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
		<div id="users-wrapper">
			<label>Потребители онлайн:</label>
			<span id="users-online-value">1</span>
		</div>
		<div id=title-container>
			<h3 id="title">Файл: <?php echo $file_to_open; ?></h3>
		</div>
	</header>
	<textarea id="editor" name="content"><?php echo file_get_contents($path); ?></textarea>
</body>
</html>