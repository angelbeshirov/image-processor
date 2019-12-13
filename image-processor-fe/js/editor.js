window.onload = function() {
    var url = new URL(document.location.href);
    var file = url.searchParams.get("file");
    var title = document.getElementById("title");
    if(title) {
        title.innerHTML += (file);
    }
    ajax("http://localhost:8081/images/getImage?file=" + file, {}, setUp);
};

function setUp(xhr) {
    if(xhr.status == 200) {
        //console.log(xhr.response);
        document.getElementById("item_previewer").src = "data:image/png;base64," + xhr.response;
    }
    
};