(function displayLogin() {
    var cookie = getCookie("username");
    var loginInfo = document.getElementById("login-info");

    if (loginInfo.hasChildNodes()) {
        document.getElementById("login-info").innerHTML = "";
    }

    if (cookie) {
        var displayText = decodeURIComponent(cookie).replace("+", " ");
        loginInfo.appendChild(document.createTextNode("Здравейте, " + displayText + "!"));
    } else {
        ajax("http://localhost:8081/users/is-logged-in", {}, populateNavigation);
    }

     var slideIndex = 1;
    showSlides(slideIndex);

    var prev = document.getElementsByClassName("prev")[0];
    prev.addEventListener("click", function() {
        slideIndex -= 1;
        showSlides(slideIndex);
    });

    var next = document.getElementsByClassName("next")[0];
        next.addEventListener("click", function() {
        slideIndex += 1;
        showSlides(slideIndex);
    });

    document.getElementById("dot1").addEventListener("click", function() {
        slideIndex = 1;
        showSlides(slideIndex);
    });

    document.getElementById("dot2").addEventListener("click", function() {
            slideIndex = 2;
            showSlides(slideIndex);
    });

    document.getElementById("dot3").addEventListener("click", function() {
        slideIndex = 3;
        showSlides(slideIndex);
    });

    function showSlides(n) {
        var slides = document.getElementsByClassName("mySlides");
        var dots = document.getElementsByClassName("dot");

        if (n > slides.length) {
            slideIndex = 1;
        }

        if (n < 1) {
            slideIndex = slides.length;
        }

        for (var i = 0; i < slides.length; i++) {
            slides[i].style.display = "none";
        }

        for (var i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" active", "");
        }

        slides[slideIndex-1].style.display = "block";
        dots[slideIndex-1].className += " active";
    }
})();