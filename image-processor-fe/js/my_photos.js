window.onload = function() {
    //retrieveAndPopulate();
};

window.onclick = function(event) {
    var dropdowns = document.querySelectorAll("#main-table tbody .file-menu")
    for (var i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i].querySelector(".dropdown-menu");
        if (!openDropdown.classList.contains('non-visible')) {
            openDropdown.classList.add('non-visible');
        }
    }
    if (event.target.matches('.column5') || event.target.matches('.file-menu')) {
        event.target.querySelector(".dropdown-menu").classList.remove("non-visible");
    }
}

function retrieveAndPopulate() {
    //ajax("docs_api.php/retrieve", {}, populate);
}

function clearTable() {
    document.querySelector("#main-table tbody").innerHTML = "";
}

function populate(response) {
    if (response) {
        var tableBody = document.querySelector("#main-table tbody");
        //for (var i = 0; i < response.files.length; i++) {
        for (var i = 0; i < 5; i++) {
            //var file = response.files[i];
            var tr = document.createElement("tr");
            // populateRow(tr, file);
            populateRow(tr, "sth");
            tableBody.appendChild(tr);
        }
    }
}

function populateRow(tr, file) {
    tr.appendChild(createTD("asdasd", ["column1"]));
    tr.appendChild(createTD("asdasd" + " B", ["column2"]));
    tr.appendChild(createTD("asdasd", ["column3"]));
    tr.appendChild(createTD("asdasd", ["column4"]));
    tr.appendChild(addActionsToFile());
}

function createTD(value, classes) {
    var col = document.createElement("td");
    col.innerHTML = value;
    for (var i = 0; i < classes.length; i++) {
        col.classList.add(classes[i]);
    }

    return col;
}

function addActionsToFile() {
    col5 = document.createElement("td");
    col5.classList.add("column5");
    divWrapper = document.createElement("div");
    divWrapper.classList.add("file-menu");
    divWrapper.classList.add("fas");
    divWrapper.classList.add("fa-ellipsis-h");

    divChild = document.createElement("div");
    divChild.classList.add("dropdown-menu");
    divChild.classList.add("non-visible");

    divChild.appendChild(createA("Изтрий", "#", function(event) {
        var fileName = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        deleteFile(fileName);
    }));
    divChild.appendChild(createA("Отвори", "#", function(event) {
        var fileName = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        window.location = "editor.php?file=" + fileName;
    }));
    divChild.appendChild(createA("Изтегли", "#", function(event) {
        var fileName = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        window.location = "docs_api.php/download?file=" + fileName;
    }));

    divWrapper.appendChild(divChild);

    col5.appendChild(divWrapper);

    return col5;
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

function deleteFile(fileName) {
    var settings = {};
    settings["method"] = "DELETE";
    //ajax("docs_api.php/delete_file/:" + encodeURIComponent(fileName), settings, handleResponseFromDelete);
}

function handleResponseFromDelete(response) {
    if (response.error_description) {
        alert(response.error_description);
    } else {
        clearTable();
        retrieveAndPopulate();
    }
}

function createA(value, href, callback) {
    el = document.createElement("a");
    el.href = href;
    el.addEventListener("click", callback);
    el.innerHTML = value;

    return el;
}

function handleResponseFromSharing(response) {
    if (response.error_description) {
        alert(response.error_description);
    } else {
        alert("Файлът беше успешно споделен с " + response.email);
        var modal = document.querySelector("#shareWith");
        modal.style.display = "none";
        modal.innerHTML = "";
    }
}

function upload_file(e) {
    var fileobj;
    e.preventDefault();
    fileobj = e.dataTransfer.files;
    file_upload(fileobj);
}

function file_explorer() {
    document.getElementById('selectfile').click();
    document.getElementById('selectfile').onchange = function() {
        files = document.getElementById('selectfile').files;
        file_upload(files);
    };
}

function file_upload(files) {
    console.log(files);
    populate(files);
    if (files != undefined) {
        var formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            let file = files[i]
            formData.append('filesToUpload[]', file)
        }
        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;
        xhr.open('POST', 'http://localhost:8081/images/upload', true);
        //xhr.open('POST', 'http://localhost:8081/users/upload', true);
        
        xhr.onload = function() {
            if (xhr.response) {
                var response = JSON.parse(xhr.response);
                if (response.error_description) {
                    alert(response.error_description);
                }
            } else {
                //clearTable();
                //retrieveAndPopulate();
            }
        };
        xhr.send(formData);
    }
}