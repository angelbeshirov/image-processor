package com.fmi.rest.repositories;

import com.fmi.rest.model.Image;
import org.springframework.data.repository.CrudRepository;

/**
 * @author angel.beshirov
 */
public interface ImageRepository extends CrudRepository<Image, Integer> {

    Image findByUploadedBy(Integer uploadedBy);

    Iterable<Image> findAllByUploadedBy(Integer uploadedBy);
}
