package com.fmi.rest.services;

import com.fmi.rest.repositories.UserRepository;
import com.fmi.rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author angel.beshirov
 */
@Service
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }
}
