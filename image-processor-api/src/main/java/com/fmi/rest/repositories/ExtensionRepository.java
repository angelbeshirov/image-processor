package com.fmi.rest.repositories;

import com.fmi.rest.model.Extension;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author angel.beshirov
 */
public interface ExtensionRepository extends CrudRepository<Extension, Integer> {

    Extension findByValue(String value);

    Optional<Extension> findById(Integer id);
}
