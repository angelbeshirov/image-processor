<?php
	require "util/util.php";
	should_redirect_not_logged_in();
?>

<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>My Files</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="screen" href="css/my_files.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">

	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/my_files.js" defer></script>
	<script type="text/javascript" src="js/rest.js"></script>
</head>

<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<div class="header">
		<div id="drop_file_zone" ondrop="upload_file(event)" ondragover="return false">
			<div id="drag_upload_file">
				<p>Дръпни и пусни файл</p>
				<p>или</p>
				<p><input type="button" value="Избери файл" onclick="file_explorer();"></p>
				<input type="file" name="filesToUpload[]" id="selectfile" multiple />
			</div>
		</div>
	</div>
	<section>
		<div id="files-container">
				<div class="table-container">
					<table id="main-table">
						<thead>
							<tr class="table-head">
								<th class="column1">Файл</th>
								<th class="column2">Размер</th>
								<th class="column3">Качен на</th>
								<th class="column4">Последна промяна на</th>
								<th class="column5">Вид</th>
								<th class="column6">Действие</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div id="shareWith" class="modal">
				</div>
			</div>
		</div>
	</section>
</body>
</html>