package com.fmi.rest.repositories;

import com.fmi.rest.model.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
/**
 * @author angel.beshirov
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);
}
