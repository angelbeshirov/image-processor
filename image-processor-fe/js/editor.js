window.onload = function() {
    var url = new URL(document.location.href);
    var file = url.searchParams.get("file");
    var title = document.getElementById("title");
    if(title) {
        title.innerHTML += (file);
    }
    ajax("http://localhost:8081/images/getImage?file=" + file, {}, setUp);

    var sendButton = document.getElementById("send");
    sendButton.addEventListener("click", function() {
        sendButton.disabled = true;
        var jsonObject = {};
        jsonObject["action"] = parseInt(getAction());
        jsonObject["file"] = file;
        var settings = {};
        settings["method"] = "POST";
        settings["data"] = JSON.stringify(jsonObject);
        console.log(JSON.stringify(jsonObject));
        ajax("http://localhost:8081/tasks/perform",  settings, handleResponse);
        sendButton.disabled = false;
    });
};

function handleResponse(xhr) {
    var responseText = document.getElementById("response-message");
    if(xhr.status != 200) {
        responseText.innerHTML = "Възникна грешка";
        responseText.className = "error-response-message";
    } else {
        responseText.innerHTML = "Снимката е изпратена успешно за обработка";
        responseText.className = "successful-response-message";
    }
    setTimeout(function() {
        responseText.innerHTML = "";
    }, 3500);
}

function setUp(xhr) {
    if(xhr.status == 200) {
        document.getElementById("item_previewer").src = "data:image/png;base64," + xhr.response;
    }
};

function getAction() {
    var e = document.getElementById("select_action");
    return e.options[e.selectedIndex].value;
}