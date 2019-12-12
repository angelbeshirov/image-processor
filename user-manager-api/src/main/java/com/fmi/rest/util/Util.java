package com.fmi.rest.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author angel.beshirov
 */
public class Util {

    private Util() {

    }

    public static void addSecureCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void addInsecureCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours

        response.addCookie(cookie);
    }

    public static void deleteSecureHttpOnlyCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteInsecureCookie(String name, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
