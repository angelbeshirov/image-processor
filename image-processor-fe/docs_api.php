<?php

require "util/util.php";
require "handlers/docs_handler.php";
require "database_manager.php";
require "config_manager.php";

should_redirect_not_logged_in();

if ($_SERVER["REQUEST_METHOD"] === "GET") {
    handle_get_request();
} else if($_SERVER["REQUEST_METHOD"] === "DELETE") {
    handle_delete_request();
} else if($_SERVER["REQUEST_METHOD"] === "POST") {
    handle_post_request();
}

function handle_get_request() {
    $str = explode("/", $_SERVER["REQUEST_URI"]);
    if(sizeof($str) > 2) {
        should_start_session();
        if(isset($_SESSION["loggedin"]) && isset($_SESSION["id"]) && $_SESSION["loggedin"]) {
            if($str[sizeof($str) - 1] == "retrieve") {
                get_all_files();
            } else if ($str[sizeof($str) - 1] == "retrieve_shares") {
                get_all_shares();
            } else if($str[sizeof($str) - 1] == "retrieve_my_shares") {
                get_my_shares();
            } else if(strpos($str[sizeof($str) - 1], "retrieve_file_id") === 0 && isset($_GET["filename"])) {
                get_file_id($_GET["filename"]);
            } else if(strpos($str[sizeof($str) - 1], "download") === 0 && isset($_GET["file"])) {
                download_file($_GET["file"]);
            } else if(strpos($str[sizeof($str) - 1], "get_share_id") === 0 && isset($_GET["filename"])) {
                $file_name = $_GET["filename"];
                $database_manager = new database_manager();
                if(isset($_GET["shared_by"])) {
                    $shared_by = $_GET["shared_by"];
                    $userID = $database_manager->get_user_by_username($shared_by);
                    get_share_id($file_name, $userID[0]["id"], $_SESSION["id"]);
                } else if(isset($_GET["shared_to"])) {
                    $shared_to = $_GET["shared_to"];
                    $userID = $database_manager->get_user_by_username($shared_to);
                    get_share_id($file_name, $_SESSION["id"], $userID[0]["id"]);
                }
            }
        } else {
            echo json_encode(["error_description" => "Изтекла сесия."], JSON_UNESCAPED_UNICODE);
        }
    } else {
        echo json_encode(["error_description" => "Невалиден адрес."]);
    }
}

function handle_delete_request() {
    should_start_session();
    if(isset($_SESSION["loggedin"]) && isset($_SESSION["id"]) && $_SESSION["loggedin"]) {
        $str = explode("/", $_SERVER["REQUEST_URI"]);
        if(sizeof($str) > 3 && $str[sizeof($str) - 2] == "delete_file") {
            $file_to_delete = ltrim($str[sizeof($str) - 1], ":");
            delete_file(urldecode($file_to_delete));
        } else if(sizeof($str) > 3 && $str[sizeof($str) - 2] == "delete_share") {
            $share_to_delete = ltrim($str[sizeof($str) - 1], ":");
            delete_share($share_to_delete);
        }
    } else {
        echo json_encode(["error_description" => "Изтекла сесия."], JSON_UNESCAPED_UNICODE);
    }
}

function handle_post_request() {
    should_start_session();
    if(isset($_SESSION["loggedin"]) && isset($_SESSION["id"]) && $_SESSION["loggedin"]) {
        $str = explode("/", $_SERVER["REQUEST_URI"]);
        if(sizeof($str) > 2 && $str[sizeof($str) - 1] == "share") {
            $data = file_get_contents("php://input");
            $share = json_decode($data, true);
            share_file($share);
        }
    } else {
        echo json_encode(["error_description" => "Изтекла сесия."], JSON_UNESCAPED_UNICODE);
    }
}

?>