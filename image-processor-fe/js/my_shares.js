window.onload = function() {
    retrieveSharesAndPopulate();
};

window.onclick = function(event) {
    var dropdowns = document.querySelectorAll("#main-table tbody .file-menu")
    for (var i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i].querySelector(".dropdown-menu");
        if (!openDropdown.classList.contains('non-visible')) {
            openDropdown.classList.add('non-visible');
        }
    }
    if (event.target.matches('.column7') || event.target.matches('.file-menu')) {
        event.target.querySelector(".dropdown-menu").classList.remove("non-visible");
    }
}

function retrieveSharesAndPopulate() {
    ajax("docs_api.php/retrieve_my_shares", {}, populate);
}

function clearTable() {
    document.querySelector("#main-table tbody").innerHTML = "";
}

function populate(response) {
    if (response) {
        var tableBody = document.querySelector("#main-table tbody");
        for (var i = 0; i < response.shares.length; i++) {
            var share = response.shares[i];
            var tr = document.createElement("tr");
            populateRow(tr, share);
            tableBody.appendChild(tr);
        }
    }
}

function populateRow(tr, share) {
    tr.appendChild(createTD(share["file_name"], ["column1"]));
    tr.appendChild(createTD(share["username"], ["column2"]));
    tr.appendChild(createTD(share["size"] + " B", ["column3"]));
    tr.appendChild(createTD(share["uploaded_on"], ["column4"]));
    tr.appendChild(createTD(share["last_changed"], ["column5"]));
    tr.appendChild(createTD(share["type_name"], ["column6"]));
    tr.appendChild(addActionsToShare());
}

function createTD(value, classes) {
    var col = document.createElement("td");
    col.innerHTML = value;
    for (var i = 0; i < classes.length; i++) {
        col.classList.add(classes[i]);
    }

    return col;
}

function addActionsToShare() {
    col7 = document.createElement("td");
    col7.classList.add("column7");
    divWrapper = document.createElement("div");
    divWrapper.classList.add("file-menu");
    divWrapper.classList.add("fas");
    divWrapper.classList.add("fa-ellipsis-h");

    divChild = document.createElement("div");
    divChild.classList.add("dropdown-menu");
    divChild.classList.add("non-visible");

    divChild.appendChild(createA("Премахни", "#", function(event) {
        var filename = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        var username = getParentN(event.target, 4).querySelector(".column2").innerHTML;
        getShareIDAndRemove(filename, username);
    }));
    divChild.appendChild(createA("Отвори", "#", function(event) {
        var filename = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        window.location = "editor.php?file=" + filename;
    }));
    divChild.appendChild(createA("Изтегли", "#", function(event) {
        var filename = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        window.location = "docs_api.php/download?file=" + filename;
    }));

    divWrapper.appendChild(divChild);

    col7.appendChild(divWrapper);

    return col7;
}

function getParentN(element, num) {
    var parent = element;
    for (var i = 0; i < num; i++) {
        if (parent.parentNode) {
            parent = parent.parentNode;
        }
    }
    return parent;
}

function getShareIDAndRemove(filename, username) {
    var settings = {};
    settings["method"] = "GET";
    ajax("docs_api.php/get_share_id?filename=" + filename + "&shared_to=" + username, {}, removeShare);
}

function removeShare(response) {
    if (response.id) {
        var settings = {};
        settings["method"] = "DELETE";
        ajax("docs_api.php/delete_share/:" + response.id, settings, handleResponseFromDelete);
    }
}

function handleResponseFromDelete(response) {
    if (response.error_description) {
        alert(response.error_description);
    } else {
        clearTable();
        retrieveSharesAndPopulate();
    }
}

function createA(value, href, callback) {
    el = document.createElement("a");
    el.href = href;
    el.addEventListener("click", callback);
    el.innerHTML = value;

    return el;
}