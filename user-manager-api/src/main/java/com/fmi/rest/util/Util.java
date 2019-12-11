package com.fmi.rest.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author angel.beshirov
 */
public class Util {

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    private Util() {

    }

    public static void addSecureHttpOnlyCookie(HttpServletResponse response, String email) {
        Cookie cookie = new Cookie(EMAIL, email);
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void addCookieForUsername(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(USERNAME, username);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours

        response.addCookie(cookie);
    }

    public static void deleteSecureHttpOnlyCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(EMAIL, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteCookieForUsername(HttpServletResponse response) {
        Cookie cookie = new Cookie(USERNAME, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
