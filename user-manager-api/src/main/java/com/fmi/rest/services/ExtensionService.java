package com.fmi.rest.services;

import com.fmi.rest.model.Extension;
import com.fmi.rest.repositories.ExtensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author angel.beshirov
 */
@Service
public class ExtensionService {

    private ExtensionRepository extensionRepository;

    @Autowired
    public ExtensionService(final ExtensionRepository extensionRepository) {
        this.extensionRepository = extensionRepository;
    }

    public Extension findByValue(String value) {
        return extensionRepository.findByValue(value);
    }

    public Extension findById(Integer id) {
        return extensionRepository.findById(id).orElse(null);
    }
}
