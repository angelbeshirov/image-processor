window.onload = function() {
  clearAndPopulate();

  document.getElementById("refresh-btn").addEventListener("click", function() {
    clearAndPopulate();
  });
};

function myFunction(imgs) {
    var expandImg = document.getElementById("expandedImg");
    expandImg.src = imgs.src;
    expandImg.parentElement.style.display = "block";
}

function clearAndPopulate() {
    ajax("http://localhost:8081/images/getAllResults", {}, populate);
}

function clearTable() {
    document.querySelectorAll('.row').forEach(e => e.remove());
}

function populate(xhr) {
    if (xhr.status == 200 && xhr.response) {
        clearTable();
        var body = document.getElementById("gradient");
        var files = JSON.parse(xhr.response);
        var row = document.createElement("div");
        row.classList.add("row");
        var cnt = 0;
        for (var i = 0; i < files.length; i++) {
          if(cnt > 3) {
              body.appendChild(row);
              row = document.createElement("div");
              row.classList.add("row");
              cnt = 0;
          }
          var file = files[i];
          addImage(row, file);
          cnt++;
        }

        body.appendChild(row);
        row = document.createElement("div");
    }
}

function addImage(row, file) {
  var tile = document.createElement("div");
  var img = document.createElement("img");
  tile.classList.add("column");
  img.src = "data:image/png;base64," + file;
  img.style = "width:100%";
  img.addEventListener("click", function() {
    myFunction(img);
  });
  tile.appendChild(img);
  row.appendChild(tile);
}