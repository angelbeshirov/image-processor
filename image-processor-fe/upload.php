<?php

require "util/util.php";
require "database_manager.php";
require "config_manager.php";

should_redirect_not_logged_in();

if ($_SERVER["REQUEST_METHOD"] === "POST") {
	if(isset($_FILES["filesToUpload"])) {
		should_start_session();
		upload_file();
	}
}

function upload_file() {
	$configs = new config_manager();
	$errors = [];
	$path = $configs->get_key("uploads_dir") . $_SESSION["id"] . "/";
	$extensions = ["txt"];

	if (!is_dir($path)) {
		mkdir($path);
	}

	$all_files = count($_FILES["filesToUpload"]["tmp_name"]);
	for ($i = 0; $i < $all_files; $i++) {
		$localErrors = [];
		$file_name = $_FILES["filesToUpload"]["name"][$i];
		$file_tmp = $_FILES["filesToUpload"]["tmp_name"][$i];
		$file_size = $_FILES["filesToUpload"]["size"][$i];

		$tmp = explode(".", $_FILES["filesToUpload"]["name"][$i]);
		$file_extension = strtolower(end($tmp));

		$file = $path . $file_name;

		if (!in_array($file_extension, $extensions)) {
			$localErrors[] = "Extension not allowed: " . $file_name;
			$errors[] = "Extension not allowed: " . $file_name;
		}

		if ($file_size > 2097152) {
			$localErrors[] = "File size exceeds limit: " . $file_name;
			$errors[] = "File size exceeds limit: " . $file_name;
		}

		if (file_exists($file)) {
			$localErrors[] = "File " . $file_name . " already exists.";
			$errors[] = "File " . $file_name . " already exists.";
		}

		if (empty($localErrors)) {
			save_to_database($file, $file_name, $file_size, $file_extension);
			move_uploaded_file($file_tmp, $file);
		}
	}

	if ($errors) {
		echo json_encode(["error_description" => $errors], JSON_UNESCAPED_UNICODE);
	}
}

function get_file_extension_ID($file_extension) {
	$database_manager = new database_manager();
	return $database_manager->get_type_id($file_extension)[0]["id"];
}

function save_to_database($file, $file_name, $file_size, $file_extension) {
	date_default_timezone_set("Europe/Sofia");
	if(isset($_SESSION["id"])) {
		$database_manager = new database_manager();
		$user_id = $_SESSION["id"];
		$file_extension_id = get_file_extension_ID($file_extension);
		$date = date("Y-m-d H:i:s", time());
		if(!($database_manager->add_file($user_id, $file, $file_name, $file_size, $date, $date, $file_extension_id))) {
			echo json_encode(["error_description" => "Грешка с базата данни."], JSON_UNESCAPED_UNICODE);
		}
	}
}
?>