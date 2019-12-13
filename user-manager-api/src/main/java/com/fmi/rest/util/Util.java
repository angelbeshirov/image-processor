package com.fmi.rest.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author angel.beshirov
 */
public class Util {

    private Util() {

    }

    public static void addSecureCookie(final String name, final String value, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void addInsecureCookie(final String name, final String value, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours

        response.addCookie(cookie);
    }

    public static void deleteSecureHttpOnlyCookie(final String name, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void deleteInsecureCookie(final String name, final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0); // deleted
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static boolean transfer(MultipartFile source, File destination) {
        boolean result = true;
        try {
            source.transferTo(destination);
        } catch (IOException ex) {
            System.out.printf("Error while trying to transfer file %s to destinations %s", source.getOriginalFilename(), destination.getAbsolutePath());
            result = false;
        }

        return result;
    }
}
