(function displayLogin() {
    console.log(document.cookie);
    var cookie = getCookie("username");
    var loginInfo = document.getElementById("login-info");

    if (loginInfo.hasChildNodes()) {
        document.getElementById("login-info").innerHTML = "";
    }

    if (cookie) {
        console.log(cookie);
        var displayText = decodeURIComponent(cookie).replace("+", " ");
        loginInfo.appendChild(document.createTextNode("Здравейте, " + displayText));
    } else {
        //ajax("http://localhost:8081/users/logout", {}, handleResponse);
    }
})();

function handleResponse(response) {
    populateNavigation(response);
}