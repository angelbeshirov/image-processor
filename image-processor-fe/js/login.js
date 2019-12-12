document.getElementById("login-form").addEventListener("submit", function(event) {
    event.preventDefault();
    resetError();
    var fields = document.querySelectorAll(".field-input");
    var jsonObject = {};
    fields.forEach(function(element) {
        jsonObject[element.name] = element.value;
    });
    var settings = {};
    settings["method"] = "POST";
    settings["data"] = JSON.stringify(jsonObject);
    ajax("http://localhost:8081/users/login", settings, handleResponse);
});

function handleResponse(xhr) {
    console.log(xhr);
    if(xhr.status != 200 && xhr.response) {
        if (xhr.response) {
            var errorElement = document.querySelector(".error");
            errorElement.appendChild(document.createTextNode(xhr.response));
        }
    } else if (xhr.status == 200) {
        window.location.replace("index.php");
    }
}

function resetError() {
    document.querySelector(".error").innerHTML = "";
}