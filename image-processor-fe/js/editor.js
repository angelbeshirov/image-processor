var Server;
var fileID;

function append(text) {
    log = document.getElementById("editor");
    log.value = log.value + text;
}

function send(text) {
    Server.send("message", text);
}

window.onload = function() {
    ajax("user_api.php/get_socket_address", {}, setUp);
};

function setUp(response) {
    if (response.socket_address) {
        Server = new CustomWebSocket("ws://" + response.socket_address + "/");

        Server.bind("close", function(data) {
            alert("Can't connect to the server.");
        });

        Server.bind("message", function(payload) {
            handleMessage(payload);
        });

        Server.connect();

        var el = document.getElementById("editor");
        el.addEventListener("keypress", function(event) {
            handleSelectedArea(el);
            var x = event.charCode || event.keyCode;
            if (x == 13)
                x = 10;
            action = {};
            action["type"] = "INSERT";
            action["position"] = event.target.selectionStart;
            action["fileID"] = fileID;
            action["data"] = String.fromCharCode(x);

            send(JSON.stringify(action));
        });

        el.addEventListener("paste", function(event) {
            var clipboardData, pastedData;
            handleSelectedArea(el);
            clipboardData = event.clipboardData || window.clipboardData;
            pastedData = clipboardData.getData('Text');

            action = {};
            action["type"] = "INSERT";
            action["position"] = event.target.selectionStart;
            action["fileID"] = fileID;
            action["data"] = pastedData;

            send(JSON.stringify(action));
        });

        el.addEventListener("keydown", function(event) {
            if (event.keyCode === 8) {
                var cursorPosition = event.target.selectionStart;
                if (!handleSelectedArea(el) && cursorPosition != 0) {
                    action = {};
                    action["type"] = "DELETE";
                    action["from"] = cursorPosition - 1;
                    action["to"] = cursorPosition;
                    action["fileID"] = fileID;

                    send(JSON.stringify(action));
                }
            }
        });
    } else {
        alert("Can't connect to server, problem with the database");
    }

};

function handleSelectedArea(textarea) {
    var start = textarea.selectionStart;
    var finish = textarea.selectionEnd;

    if (start != finish) {
        action = {};
        action["type"] = "DELETE";
        action["from"] = start;
        action["to"] = finish;
        action["fileID"] = fileID;

        send(JSON.stringify(action));
        return true;
    }

    return false;
}

function handleMessage(payload) {
    var message = JSON.parse(payload);
    switch (message.type) {
        case "INITIALIZE":
            var filename = (new URL(window.location.href)).searchParams.get("file");
            var url = "docs_api.php/retrieve_file_id?filename=" + filename;
            var sharedBy = (new URL(window.location.href)).searchParams.get("shared_by");

            if (sharedBy) {
                url += "&shared_by=" + sharedBy;
            }
            var settings = {};
            settings["method"] = "GET";
            ajax(url, settings, handleInitializeResponse);
        case "UPDATE USERS":
            var titleValue = document.querySelector("#users-online-value");
            titleValue.innerHTML = message.value;
            break;
        case "INSERT":
            var editor = document.querySelector("#editor");
            editor.value = editor.value.splice(message.position, 0, message.data);
            break;
        case "DELETE":
            var editor = document.querySelector("#editor");
            editor.value = editor.value.splice(message.from, message.to - message.from, "");
            break;
        default:
            console.log("Unknown type " + message.type);
    };
}

function handleInitializeResponse(response) {
    if (response.file_id) {
        fileID = response.file_id;
        action = {};
        action["type"] = "INITIALIZE";
        action["fileID"] = fileID;
        send(JSON.stringify(action));
    }
}

String.prototype.splice = function(idx, rem, str) {
    return this.slice(0, idx) + str + this.slice(idx + Math.abs(rem));
};