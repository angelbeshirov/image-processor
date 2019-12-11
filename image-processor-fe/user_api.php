<?php

require "util/validator.php";
require "util/util.php";
require "handlers/user_handler.php";
require "database_manager.php";
require "config_manager.php";

if ($_SERVER["REQUEST_METHOD"] === "GET") {
    handle_get_request();
} else if($_SERVER["REQUEST_METHOD"] === "POST") {
    handle_post_request();
}

function handle_get_request() {
    $str = explode("/", $_SERVER["REQUEST_URI"]);
    if(sizeof($str) > 2) {
        if($str[sizeof($str) - 1] == "is_logged_in") {
            handle_is_logged_in();
        } else if($str[sizeof($str) - 1] == "logout") {
            handle_logout();
        } else if($str[sizeof($str) - 1] == "get_socket_address") {
            handle_get_socket_address();
        }
    }
}

function handle_post_request() {
    $str = explode("/", $_SERVER["REQUEST_URI"]);
    if(sizeof($str) > 2) {
        if($str[sizeof($str) - 1] == "register") {
            $json = file_get_contents("php://input");
            $user = json_decode($json, true);
            handle_registration($user);
        } else if($str[sizeof($str) - 1] == "login") {
            $json = file_get_contents("php://input");
            $user = json_decode($json, true);
            handle_login($user);
        }
    }
}

?>