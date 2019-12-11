package com.fmi.rest;

import com.fmi.rest.model.User;
import org.springframework.stereotype.Service;

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
