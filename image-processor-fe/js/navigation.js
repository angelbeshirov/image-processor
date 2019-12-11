(function buildNavigation() {
    console.log("BUILDING NAVIGATION");
    ajax("http://localhost:8081/users/is-logged-in", {}, populateNavigation);
}());

function populateNavigation(xhr) {
    var navigation = document.querySelector("#navigation-bar");
    navigation.innerHTML = "";
    var ul = document.createElement("ul");
    if (xhr.status == 200) {
        addNavigationElementsForLoggedUser(ul);
    } else {
        addNavigationElementsForNotLoggedUser(ul);
    }

    navigation.appendChild(ul);
}

function addNavigationElementsForNotLoggedUser(ul) {
    var liHome = document.createElement("li");
    var aHome = document.createElement("a");
    aHome.href = "index.php";
    aHome.classList.add("navigation");
    aHome.classList.add("fas");
    aHome.classList.add("fa-home");

    var liLogin = document.createElement("li");
    var aLogin = document.createElement("a");
    aLogin.innerHTML = "Вход";
    aLogin.href = "login.php";
    aLogin.classList.add("navigation");

    var liRegister = document.createElement("li");
    var aRegister = document.createElement("a");
    aRegister.innerHTML = "Регистрация";
    aRegister.href = "register.php";
    aRegister.classList.add("navigation");

    liRegister.appendChild(aRegister);
    liLogin.appendChild(aLogin);
    liHome.appendChild(aHome);

    ul.appendChild(liRegister);
    ul.appendChild(liLogin);
    ul.appendChild(liHome);
}

function addNavigationElementsForLoggedUser(ul) {
    var liHome = document.createElement("li");
    var aHome = document.createElement("a");
    aHome.href = "index.php";
    aHome.classList.add("navigation");
    aHome.classList.add("fas");
    aHome.classList.add("fa-home");

    var liSharedWithMe = document.createElement("li");
    var aSharedWithMe = document.createElement("a");
    aSharedWithMe.innerHTML = "Споделени с мен";
    aSharedWithMe.href = "shared_with_me.php";
    aSharedWithMe.classList.add("navigation");

    var liSharedWithOthers = document.createElement("li");
    var aSharedWithOthers = document.createElement("a");
    aSharedWithOthers.innerHTML = "Моите споделяния";
    aSharedWithOthers.href = "my_shares.php";
    aSharedWithOthers.classList.add("navigation");

    var liMyFiles = document.createElement("li");
    var aMyFiles = document.createElement("a");
    aMyFiles.innerHTML = "Моите файлове";
    aMyFiles.href = "my_files.php";
    aMyFiles.classList.add("navigation");

    var liLogout = document.createElement("li");
    var aLogout = document.createElement("a");
    aLogout.innerHTML = "Изход";
    aLogout.href = "#";
    aLogout.classList.add("navigation");
    aLogout.addEventListener("click", function(event) { 
        ajax("http://localhost:8081/users/logout", {}, handleResponseFromLogout);
    });

    liLogout.appendChild(aLogout);
    liMyFiles.appendChild(aMyFiles);
    liSharedWithOthers.appendChild(aSharedWithOthers);
    liSharedWithMe.appendChild(aSharedWithMe);
    liHome.appendChild(aHome);

    ul.appendChild(liLogout);
    ul.appendChild(liSharedWithOthers);
    ul.appendChild(liSharedWithMe);
    ul.appendChild(liMyFiles);
    ul.appendChild(liHome);
}

function handleResponseFromLogout(xhr) {
    if (xhr.status == 200) {
        window.location = "index.php";
    }
}