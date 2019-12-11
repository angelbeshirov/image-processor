package com.fmi.rest.util;

import com.fmi.rest.model.User;

/**
 * @author angel.beshirov
 */
public class Validator {

    public static boolean isValid(final User user) {
        return user != null
                && user.getEmail() != null
                && user.getPassword() != null
                && user.getUsername() != null;
    }
}
