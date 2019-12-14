window.onload = function() {
  retrieveAndPopulate();
};

function myFunction(imgs) {
    //var expandImg = document.getElementById("expandedImg");
    //expandImg.src = imgs.src;
    //expandImg.parentElement.style.display = "block";
  }

function retrieveAndPopulate() {
    ajax("http://localhost:8081/images/getAllResults", {}, populate);
}

function clearTable() {
    //document.querySelector("#main-table tbody").innerHTML = "";
}

function populate(xhr) {
    if (xhr.status == 200 && xhr.response) {
        var body = document.getElementById("gradient");
        //console.log(body);
        //var tableBody = document.querySelector("#main-table tbody");
        //console.log(xhr.response);
        var files = JSON.parse(xhr.response);
        var row = document.createElement('div');
        row.classList.add("row");
        var cnt = 0;
        console.log(files.length);
        for (var i = 0; i < files.length; i++) {
          if(cnt >= 3) {
              body.appendChild(row);
              row = document.createElement('div');
              row.classList.add("row");
          }
          var file = files[i];
          addImage(row, file);
          cnt++;
        }

        body.appendChild(row);
        row = document.createElement('div');
    }
}

function addImage(row, file) {
  var tile = document.createElement('div');
  var img = document.createElement('img');
  tile.classList.add("column");
  img.src = "data:image/png;base64," + file;
  img.style = "width:100%";
  img.onclick = myFunction(img);
  tile.appendChild(img);
  row.appendChild(tile);
}