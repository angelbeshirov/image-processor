package com.fmi.rest;

import com.fmi.rest.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public boolean createUser(User user) {
        return false;
    }

    public User getUser() {
        return null;
    }


}
