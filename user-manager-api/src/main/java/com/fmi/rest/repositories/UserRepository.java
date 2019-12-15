package com.fmi.rest.repositories;

import com.fmi.rest.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author angel.beshirov
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);
}
