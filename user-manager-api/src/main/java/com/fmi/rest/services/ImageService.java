package com.fmi.rest.services;

import com.fmi.rest.model.Image;
import com.fmi.rest.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author angel.beshirov
 */
@Service
public class ImageService {

    private ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Optional<Image> findById(Integer id) {
        return imageRepository.findById(id);
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public Iterable<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public Image findUploadedBy(Integer uploadedBy) {
        return imageRepository.findByUploadedBy(uploadedBy);
    }

    public Iterable<Image> findAllUploadedBy(Integer uploadedBy) {
        return imageRepository.findAllByUploadedBy(uploadedBy);
    }

    public String findImageLocation(Integer id, String filename) {
        String result = "";
        Iterable<Image> imagesForUser = imageRepository.findAllByUploadedBy(id);
        for (Image image : imagesForUser) {
            if (Objects.equals(image.getName(), filename)) {
                result = image.getLocation();
                break;
            }
        }

        return result;
    }
}
