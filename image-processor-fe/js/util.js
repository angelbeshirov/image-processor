function getCookie(nameOfCookie) {
    var value = "; " + document.cookie;
    var parts = value.split("; " + nameOfCookie + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();

    return false;
}