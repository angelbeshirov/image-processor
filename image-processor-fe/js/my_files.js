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
    if (event.target.matches('.column6') || event.target.matches('.file-menu')) {
        event.target.querySelector(".dropdown-menu").classList.remove("non-visible");
    }
}

function retrieveAndPopulate() {
    ajax("docs_api.php/retrieve", {}, populate);
}

function clearTable() {
    document.querySelector("#main-table tbody").innerHTML = "";
}

function populate(response) {
    if (response) {
        var tableBody = document.querySelector("#main-table tbody");
        for (var i = 0; i < response.files.length; i++) {
            var file = response.files[i];
            var tr = document.createElement("tr");
            populateRow(tr, file);
            tableBody.appendChild(tr);
        }
    }
}

function populateRow(tr, file) {
    tr.appendChild(createTD(file["name"], ["column1"]));
    tr.appendChild(createTD(file["size"] + " B", ["column2"]));
    tr.appendChild(createTD(file["uploaded_on"], ["column3"]));
    tr.appendChild(createTD(file["last_changed"], ["column4"]));
    tr.appendChild(createTD(file["type_name"], ["column5"]));
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
    col6 = document.createElement("td");
    col6.classList.add("column6");
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
    divChild.appendChild(createA("Сподели с", "#", function(event) {
        var fileName = getParentN(event.target, 4).querySelector(".column1").innerHTML;
        buildModal(fileName);
        var modal = document.querySelector("#shareWith");
        modal.style.display = "block";
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

    col6.appendChild(divWrapper);

    return col6;
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
    ajax("docs_api.php/delete_file/:" + encodeURIComponent(fileName), settings, handleResponseFromDelete);
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

function buildModal(fileName) {
    var modal = document.querySelector("#shareWith");
    var modalContent = document.createElement("div");
    var modalHeader = document.createElement("div");
    var modalBody = document.createElement("div");
    var close = document.createElement("span");
    var header = document.createElement("h2");
    modalContent.classList.add("modal-content");
    modalHeader.classList.add("modal-header");
    modalBody.classList.add("modal-body");
    close.classList.add("close");
    close.innerHTML = "&times;";
    header.innerHTML = "Сподели " + fileName;

    var form = document.createElement("form");
    var rowData = document.createElement("div");
    rowData.classList.add("row");
    var labelContainer = document.createElement("div");
    labelContainer.classList.add("col-25");
    var inputContainer = document.createElement("div");
    inputContainer.classList.add("col-75");
    var label = document.createElement("label");
    label.for = "email";
    label.innerHTML = "Имейл:"
    var inputEmail = document.createElement("input");
    inputEmail.type = "email";
    inputEmail.id = "input-email";
    inputEmail.name = "email"
    inputEmail.placeholder = "Въведете имейл...";
    inputEmail.required = true;
    var rowButton = document.createElement("div");
    rowButton.classList.add("row");
    var inputButton = document.createElement("input");
    inputButton.value = "Сподели";
    inputButton.type = "submit";
    inputButton.classList.add("shareWithBtn");

    close.onclick = function() {
        modal.style.display = "none";
        modal.innerHTML = "";
    }

    form.onsubmit = function(event) {
        event.preventDefault();
        var settings = {};
        var data = {};
        data["email"] = document.querySelector("#input-email").value;
        data["filename"] = fileName;
        settings["data"] = JSON.stringify(data);
        settings.method = "POST";
        ajax("docs_api.php/share", settings, handleResponseFromSharing);
    }

    modalHeader.appendChild(close);
    modalHeader.appendChild(header);

    labelContainer.appendChild(label);
    inputContainer.appendChild(inputEmail);
    rowData.appendChild(labelContainer);
    rowData.appendChild(inputContainer);
    rowButton.appendChild(inputButton);

    form.appendChild(rowData);
    form.appendChild(rowButton);

    modalBody.appendChild(form);

    modalContent.appendChild(modalHeader);
    modalContent.appendChild(modalBody);

    modal.appendChild(modalContent);
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
    if (files != undefined) {
        var formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            let file = files[i]
            formData.append('filesToUpload[]', file)
        }
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'upload.php', true);
        xhr.onload = function() {
            if (xhr.response) {
                var response = JSON.parse(xhr.response);
                if (response.error_description) {
                    alert(response.error_description);
                }
            } else {
                clearTable();
                retrieveAndPopulate();
            }
        };
        xhr.send(formData);
    }
}