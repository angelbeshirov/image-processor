function ajax(url, settings, callback) {
    if (settings) {
        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;
        xhr.onload = function() {
            if (callback) {
                callback(xhr);
            }
        };
        xhr.open(settings.method || "GET", url, true);
        if (settings.method == "POST") {
            xhr.setRequestHeader("Content-Type", "application/json");
        }
        xhr.send(settings.data || null);
    }
}



function resetInput() {
    var errors = document.getElementsByClassName("field-input");
    Array.prototype.forEach.call(errors, function(el) {
        el.value = '';
    });
}

function updateLogin() {
    var cookie = getCookie("username");
    if (cookie) {
        var displayText = decodeURIComponent(cookie);
        loginInfo.appendChild(document.createTextNode("Здравейте, " + displayText));
    }
}