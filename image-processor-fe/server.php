<?php

set_time_limit(0);

require "websocket/PHPWebSocket.php";
require "websocket/action.php";
require "database_manager.php";
require "config_manager.php";

// when a client sends data to the server
function wsOnMessage($clientID, $message, $messageLength, $binary) {
	date_default_timezone_set("Europe/Sofia");
	clearstatcache();
	$database_manager = new database_manager();

	global $Server;
	$ip = long2ip($Server->wsClients[$clientID][6]);
	$messageObj = json_decode($message, true);

	switch ($messageObj["type"]) {
		case Type::INITIALIZE_TYPE:
			$fileID = $messageObj["fileID"];
			
			$Server->log("Received initialize message for fileID " . $fileID);
			$Server->wsClients[$clientID][12] = $fileID;
			$file_path = $database_manager->get_path_by_id($fileID)[0]["path"];
			$Server->log("File path which was retrieved is " . realpath($file_path));
			$Server->wsClients[$clientID][13] = realpath($file_path);

			$contents = file_get_contents(realpath($file_path));
			$new_content = str_replace("\r\n", "\n", $contents);
			file_put_contents(realpath($file_path), $new_content);


			if (isset($Server->wsClientFileCount[$fileID]) && $Server->wsClientFileCount[$fileID] >= 1) {
				$Server->wsClientFileCount[$fileID]++;
			}
			else {
				$Server->wsClientFileCount[$fileID] = 1;
			}

			$Server->log("Set client count for " . $fileID . " to " . $Server->wsClientFileCount[$fileID]);

			foreach($Server->wsClients as $id => $client) {
				if($client[12] != 0 && $client[12] === $fileID) {
					$Server->wsSend($id, json_encode(new UpdateUsers($Server->wsClientFileCount[$fileID])));
				}
			}
			break;
		case Type::INSERT_TYPE:
			$fileID = $messageObj["fileID"];
			$position = $messageObj["position"];
			$data = $messageObj["data"];

			$path = $Server->wsClients[$clientID][13];

			$contents = file_get_contents($path);

			$new_content = mb_substr($contents, 0, $position) . $data . mb_substr($contents, $position, mb_strlen($contents));

			file_put_contents($path, $new_content);
			$database_manager->update_file_size_and_time(filesize($path), date("Y-m-d H:i:s", time()), $fileID);

			foreach($Server->wsClients as $id => $client) {
				if(isset($client[12]) && $client[12] === $fileID && $id != $clientID) {
					$Server->wsSend($id, json_encode(new Insert($position, $data)));
				}
			}
			break;
		case Type::DELETE_TYPE:
			$fileID = $messageObj["fileID"];
			$from = $messageObj["from"];
			$to = $messageObj["to"];

			$path = $Server->wsClients[$clientID][13];

			$contents = file_get_contents($path);

			$new_content = mb_substr($contents, 0, $from) . mb_substr($contents, $to, mb_strlen($contents));

			file_put_contents($path, $new_content);
			$database_manager->update_file_size_and_time(filesize($path), date("Y-m-d H:i:s", time()), $fileID);

			foreach($Server->wsClients as $id => $client) {
				if(isset($client[12]) && $client[12] === $fileID && $id != $clientID) {
					$Server->wsSend($id, json_encode(new Delete($from, $to)));
				}
			}
			break;
	}
}

// when a client connects
function wsOnOpen($client_id)
{
	global $Server;
	$ip = long2ip($Server->wsClients[$client_id][6]);

	$Server->log("$ip ($client_id) has connected.");
	$Server->log("Current number of connected users is:" . $Server->wsClientCount);
	$Server->log("Sending init request");

	$Server->wsSend($client_id, json_encode(new Init()));
}

// when a client closes or lost connection
function wsOnClose($clientID, $status) {
	global $Server;
	$ip = long2ip($Server->wsClients[$clientID][6]);

	$Server->log("$ip ($clientID) has disconnected.");
	$Server->log("Current number of connected users is:" . $Server->wsClientCount);

	$fileID = $Server->wsClients[$clientID][12];

	$Server->log("User for file :" . $fileID . " has disconnected");

	if ($Server->wsClientFileCount[$fileID] > 1) {
		$Server->wsClientFileCount[$fileID]--;
	}
	else {
		unset($Server->wsClientFileCount[$fileID]);
	}

	if(isset($Server->wsClientFileCount[$fileID])) {
		$Server->log("Current number of connected users for file:" . $fileID . " is " . $Server->wsClientFileCount[$fileID]);
	} else {
		$Server->log("All clients for file:" . $fileID . " disconnected");
	}
	

	// Send to everyone the new count of users for this file
	foreach($Server->wsClients as $id => $client) {
		if(isset($client[12]) && $client[12] === $fileID && $id != $clientID) {
			$Server->wsSend($id, json_encode(new UpdateUsers($Server->wsClientFileCount[$fileID])));
		}
	}
}

// start the server
$configs = new config_manager();
$Server = new PHPWebSocket();
$Server->bind("message", "wsOnMessage");
$Server->bind("open", "wsOnOpen");
$Server->bind("close", "wsOnClose");

$Server->wsStartServer($configs->get_key("server_private_ip"), $configs->get_key("server_port"));

?>