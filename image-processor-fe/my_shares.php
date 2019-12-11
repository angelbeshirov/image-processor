<?php
	require "util/util.php";
	should_redirect_not_logged_in();
?>

<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>My Shares</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">

	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" media="screen" href="css/my_shares.css">
	<link rel="stylesheet" type="text/css" media="screen" href="css/navigation.css">

	<script type="text/javascript" src="js/navigation.js" defer></script>
	<script type="text/javascript" src="js/my_shares.js" defer></script>
	<script type="text/javascript" src="js/rest.js"></script>
</head>

<body id="gradient">
	<nav id="navigation-bar">
	</nav>
	<section>
		<div id="files-container">
				<div class="table-container">
					<table id="main-table">
						<thead>
							<tr class="table-head">
								<th class="column1">Файл</th>
								<th class="column2">Споделен с</th>
								<th class="column3">Размер</th>
								<th class="column4">Качен на</th>
								<th class="column5">Последна промяна на</th>
								<th class="column6">Вид</th>
								<th class="column7">Действие</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>
</body>
</html>