<?php

function get_all_files() {
    $database_manager = new database_manager();
    $result = $database_manager->get_files_for_user($_SESSION["id"]);
    echo json_encode(["files" => $result], JSON_UNESCAPED_UNICODE);
}

function get_all_shares() {
    $database_manager = new database_manager();
    $shares = $database_manager->get_shares_with_user($_SESSION["id"]);
    echo json_encode(["shares" => $shares], JSON_UNESCAPED_UNICODE);
}

function get_my_shares() {
    $database_manager = new database_manager();
    $shares = $database_manager->get_shares_by_user($_SESSION["id"]);
    echo json_encode(["shares" => $shares], JSON_UNESCAPED_UNICODE);
}

function get_file_id($filename) {
    $database_manager = new database_manager();
    if(isset($_GET["shared_by"])) {
        $user = $database_manager->get_user_by_username($_GET["shared_by"]);
        
        if($user) {
            $file = $database_manager->get_file($user[0]["id"], $filename);
            echo json_encode(["file_id" => $file[0]["id"]], JSON_UNESCAPED_UNICODE);
        }
    } else {
        $file = $database_manager->get_file($_SESSION["id"], $filename);
        echo json_encode(["file_id" => $file[0]["id"]], JSON_UNESCAPED_UNICODE);
    }
}

function download_file($file_name) {
    $database_manager = new database_manager();
    $result = $database_manager->get_path($_SESSION["id"], $file_name);
    if($result) {
        get_file($result[0]["path"]);
    } else if(isset($_GET["shared_by"])) {
        $user = $database_manager->get_user_by_username($_GET["shared_by"]);
        if($user) {
            $user_id = $user[0]["id"];
            $share = $database_manager->get_share($user_id, $_SESSION["id"], $file_name);
            if($share) {
                get_file($database_manager->get_path($user_id, $file_name)[0]["path"]);
            }
        }
    }
}

function get_file($path) {
    if(file_exists($path)) {
        header("Content-Description: File Transfer");
        header("Content-Type: application/octet-stream");
        header("Content-Disposition: attachment; filename=\"" . basename($path) . "\"");
        header("Expires: 0");
        header("Cache-Control: must-revalidate");
        header("Pragma: public");
        header("Content-Length: " . filesize($path));
        flush();
        readfile($path);
        exit;
    }
}

function delete_file($file_to_delete) {
    $database_manager = new database_manager();
    $result = $database_manager->get_path($_SESSION["id"], $file_to_delete);
    if($result) {
        $path = $result[0]["path"];
        $real_path = realpath($path);
        if($real_path && is_writable($real_path) && unlink($real_path)) {
            if($database_manager->delete_file_for_user($_SESSION["id"], $file_to_delete) && $database_manager->delete_share_by_filename_and_id($_SESSION["id"], $file_to_delete)) {
                echo json_encode(["response" => $file_to_delete . " was deleted successfully"], JSON_UNESCAPED_UNICODE);
            } else {
                echo json_encode(["error_description" => "Грешка с базата данни!"], JSON_UNESCAPED_UNICODE);
            }
        } else {
            echo json_encode(["error_description" => "Error while deleting file " + $file_to_delete], JSON_UNESCAPED_UNICODE);
        }
    }
}

function delete_share($share_id) {
    $database_manager = new database_manager();
    if(!$database_manager->delete_share($share_id)) {
        echo json_encode(["error_description" => "Грешка с базата данни!"], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["response" => "Share with id " . $share_id . " was deleted successfully"], JSON_UNESCAPED_UNICODE);
    }
}

function get_share_id($filename, $shared_by, $shared_to) {
    $database_manager = new database_manager();
    $share = $database_manager->get_share($shared_by, $shared_to, $filename);
    if($share) {
        echo json_encode(["id" => $share[0]["id"]], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(["error_description" => "Грешка с базата данни!"], JSON_UNESCAPED_UNICODE);
    }
}

function share_file($share) {
    $database_manager = new database_manager();
    $result = $database_manager->get_user_by_email($share["email"]);
    if(!$result) {
        echo json_encode(["error_description" => "Няма потребител с този имейл."], JSON_UNESCAPED_UNICODE);
        return;
    }

    if($result[0]["id"] == $_SESSION["id"]) {
        echo json_encode(["error_description" => "Не може да споделяте файл със себе си."], JSON_UNESCAPED_UNICODE);
        return;
    }

    $is_there_share_already = $database_manager->get_share($_SESSION["id"], $result[0]["id"], $share["filename"]);
    if ($is_there_share_already) {
        echo json_encode(["error_description" => "Файлът вече е споделен с този имейл."], JSON_UNESCAPED_UNICODE);
    } else {
        $database_manager->add_share($_SESSION["id"], $result[0]["id"], $share["filename"]);
        echo json_encode(["email" => $share["email"]], JSON_UNESCAPED_UNICODE);
    }
}
?>