window.onload = function() {
    retrieveAndPopulate();
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
    ajax("http://localhost:8081/images/getAll", {}, populate);
}

function clearTable() {
    document.querySelector("#main-table tbody").innerHTML = "";
}

function populate(xhr) {
    if (xhr.status == 200 && xhr.response) {
        var tableBody = document.querySelector("#main-table tbody");
        console.log(xhr.response);
        var files = JSON.parse(xhr.response);
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var tr = document.createElement("tr");
            populateRow(tr, file);
            tableBody.appendChild(tr);
        }
    }
}

function populateRow(tr, file) {
    tr.appendChild(createTD(file.name, ["column1"]));
    tr.appendChild(createTD(file.size + " B", ["column2"]));
    tr.appendChild(createTD(file.uploadedOn, ["column3"]));
    tr.appendChild(createTD(file.extension, ["column4"]));
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
        var a = document.createElement("a");
        a.style.display = "none";
        
        a.href = "http://localhost:8081/images/download?file=" + fileName;
        a.setAttribute('target', '_parent');
        a.setAttribute("download", fileName);
        a.click();
        document.body.appendChild(a);
        window.URL.revokeObjectURL(a.href);
        document.body.removeChild(a);
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
    // will this work with cyrilic filename? encodeURI?
    ajax("http://localhost:8081/images/delete?file=" + fileName, settings, handleResponseFromDelete);
}

function handleResponseFromDelete(xhr) {
    if (xhr.status == 200) {
        clearTable();
        retrieveAndPopulate();
    } else {
        alert("There was an error while deleting this file!");
    }
}

function createA(value, href, callback) {
    el = document.createElement("a");
    el.href = href;
    el.addEventListener("click", callback);
    el.innerHTML = value;

    return el;
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
        
        xhr.onload = function() {
            if (xhr.status == 401) {
                alert("The session has expired!");
                ajax("http://localhost:8081/users/logout", {});
            } else if(xhr.status == 500) {
                alert("Internal server error. Something went astray :(");
            } else {
                clearTable();
                retrieveAndPopulate();
            }
        };
        xhr.send(formData);
    }
}