<?php

function validate_registration($new_user) {
    $database_manager = new database_manager();
    $errors = [];
    $result = $database_manager->get_user_by_email($new_user["email"]);
    if($result) {
        $errors["email"] = "Вече съществува потребител с този имейл.";
    }

    $result = $database_manager->get_user_by_username($new_user["username"]);
    if($result) {
        $errors["username"] = "Вече съществува потребител с това потребителско име.";
    }

    return $errors;
}

?>