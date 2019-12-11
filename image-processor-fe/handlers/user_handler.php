<?php

function handle_registration($user) {
    $errors = validate_registration($user);
    if (empty($errors)) {
        $hash = password_hash($user["password"], PASSWORD_DEFAULT);
        $database_manager = new database_manager();
        if($database_manager->add_user($user["email"], $user["username"], $hash)) {
            echo json_encode(["register" => $user], JSON_UNESCAPED_UNICODE);
        } else {
            echo json_encode(["error_description" => "Грешка с базата данни."], JSON_UNESCAPED_UNICODE);
        }
    } else {
        echo json_encode(["error_description" => $errors], JSON_UNESCAPED_UNICODE);
    }
}

function handle_login($user) {
    if(!isset($_SESSION)) {
        should_start_session();
        $database_manager = new database_manager();
        $result = $database_manager->get_user_by_email($user["email"]);
        if($result && password_verify($user["password"], $result[0]["password"])) {
            $_SESSION["id"] = $result[0]["id"];
            $_SESSION['loggedin'] = true;
            setcookie("username", $result[0]["username"], time() + 10800, "/", NULL, NULL, FALSE);
            echo json_encode(["login" => $result[0]], JSON_UNESCAPED_UNICODE);
        } else {
            echo json_encode(["error_description" => "Невалиден имейл или парола."], JSON_UNESCAPED_UNICODE);
        }
    } else {
        echo json_encode(["loggedin" => true], JSON_UNESCAPED_UNICODE);
    }
}

function handle_is_logged_in() {
    should_start_session();
    if(isset($_SESSION["id"])) {
        echo json_encode(["loggedin" => true], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["loggedin" => false], JSON_UNESCAPED_UNICODE);
    }
}

function handle_get_socket_address() {
    should_start_session();
    if(isset($_SESSION["loggedin"]) && isset($_SESSION["id"]) && $_SESSION["loggedin"]) {
        $configs = new config_manager();
        echo json_encode(["socket_address" => $configs->get_key("server_public_ip") . ":" . $configs->get_key("server_port")], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["error_description" => "Изтекла сесия."], JSON_UNESCAPED_UNICODE);
    }
}

function handle_logout() {
    should_start_session();
    if(isset($_SESSION["id"])) {
        if(isset($_COOKIE["username"])){
            setcookie("username", "", time() - 10800, "/", NULL, NULL, FALSE);
        }

        $params = session_get_cookie_params();
        setcookie(session_name(), "", 0, $params["path"], $params["domain"], $params["secure"], isset($params["httponly"]));
        session_destroy();
        echo json_encode(["loggedin" => false], JSON_UNESCAPED_UNICODE);
    }
}
?>