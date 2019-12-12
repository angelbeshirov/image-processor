const table = document.getElementById("table");

document.addEventListener("DOMContentLoaded", function() {
    loadImages();
});

function addTableRow(table, imageName, imageSize) {
    var currentIndex = table.rows[table.rows.length - 1].cells[0].innerHTML;
    var newTableRow = table.insertRow(-1);

    if(currentIndex == "#") {
        currentIndex = 0;
    }
    
    newTableRow.insertCell(-1).innerHTML = parseInt(currentIndex, 10) + 1;
    newTableRow.insertCell(-1).innerHTML = imageName;
    newTableRow.insertCell(-1).innerHTML = imageSize;
    var editCell = newTableRow.insertCell(-1);
    editCell.innerHTML = "Edit";
    editCell.className += "aligned-cell";
    editCell.className += " edit-cell";
    var deleteCell = newTableRow.insertCell(-1);
    deleteCell.innerHTML = "Delete";
    deleteCell.className += "aligned-cell";
    deleteCell.className += " delete-cell";

    updateTableClickListeners(table);
}

function removeTableRow(table, index) {
    const idColumn = 0;

    table.deleteRow(index);

    for(let i = 1; i < table.rows.length; i++) {
        table.rows[i].cells[idColumn].innerHTML = i;
    }

    updateTableClickListeners(table);
}

function updateTableClickListeners(table) {
    var nameColumn = 1;
    var editColumn = 3;
    var deleteColumn = 4;
    
    for(let i = 1; i < table.rows.length; i++) {
        recreateNode(table.rows[i], true);
        table.rows[i].cells[editColumn].addEventListener("click", function() {
            // TODO fix url
            const imageName = table.rows[i].cells[nameColumn].innerHTML;
            location.href = "http://localhost/image-processor/edit.php?image=" + imageName;
        });
        table.rows[i].cells[deleteColumn].addEventListener("click", function() {
            // TODO fix url
            const url = "http://localhost/image-processor/delete.php";
            const settings = {};
            settings["method"] = "POST";
            settings["data"] = JSON.stringify(table.rows[i].cells[nameColumn].innerHTML);
            ajax(url, settings, handleDelete);
        });
    }
}

function recreateNode(el, withChildren) {
    if (withChildren) {
      el.parentNode.replaceChild(el.cloneNode(true), el);
    }
    else {
      var newEl = el.cloneNode(false);
      while (el.hasChildNodes()) newEl.appendChild(el.firstChild);
      el.parentNode.replaceChild(newEl, el);
    }
}

function loadImages() {
    event.preventDefault();

    // TODO fix url
    const url = "http://localhost/image-processor/all.php";
    const settings = {};
    ajax(url, settings, handleResponse);
}

function ajax(url, settings, callback) {
    if(settings) {
        var xhr = new XMLHttpRequest();
        xhr.withCredentials = true;
        xhr.onload = function() {
            if(callback) {
                callback(xhr);
            }
        };
        xhr.open(settings.method || "GET", url, true);
        if(settings.method == "POST") {
            xhr.setRequestHeader("Content-Type", "application-json");
        }
        xhr.send(settings.data || null);
    }
}

function handleResponse(xhr) {
    if (xhr.status != 200 && xhr.response) {
        if (xhr.response) {
            console.log("error while loading table");
        }
    } else if (xhr.status == 200) {
        var data = JSON.parse(xhr.response);
        data.forEach(function (image) {
            addTableRow(table, image.name, image.size);
        });
    }
}

function handleDelete(xhr) {
    if (xhr.status != 200 && xhr.response) {
        if (xhr.response) {
            console.log("error while deleting image");
        }
    } else if (xhr.status == 200) {
        console.log("deleted: " + JSON.parse(xhr.response));
        updateTableClickListeners(table);
    }
}