window.onload = function() {
    var url = new URL(document.location.href);
    var file = url.searchParams.get("file");
    var title = document.getElementById("title");
    if(title) {
        title.innerHTML += (file);
    }
    ajax("http://localhost:8081/images/getImage?file=" + file, {}, setUp);

    document.getElementById("send").addEventListener("click", function() {
        var jsonObject = {};
        jsonObject["action"] = parseInt(getAction());
        jsonObject["file"] = file;
        var settings = {};
        settings["method"] = "POST";
        settings["data"] = JSON.stringify(jsonObject);
        console.log(JSON.stringify(jsonObject));
        ajax("http://localhost:8081/tasks/perform",  settings, handleResponse);
    });
};

function handleResponse(xhr) {
    if(xhr.status != 200) {
        console.log("There was an error"); // not good error handling;
    }
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