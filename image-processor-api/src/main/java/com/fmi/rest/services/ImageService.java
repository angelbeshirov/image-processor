package com.fmi.rest.services;

import com.fmi.rest.model.Image;
import com.fmi.rest.repositories.ImageRepository;
import com.fmi.rest.util.Constants;
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
    public ImageService(final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void deleteById(final Integer id) {
        imageRepository.deleteById(id);
    }

    public Image saveImage(final Image image) {
        return imageRepository.save(image);
    }

    public Iterable<Image> findAllUploadedBy(final Integer uploadedBy) {
        return imageRepository.findAllByUploadedBy(uploadedBy);
    }

    public String getBasePath(final Integer uploadedBy) {
        String basePath = null;
        Iterable<Image> images = imageRepository.findAllByUploadedBy(uploadedBy);
        if (images != null && images.iterator().hasNext()) {
            basePath = images.iterator().next().getLocation();
            if (basePath != null) {
                basePath = basePath.substring(0, basePath.lastIndexOf(Constants.FILE_SEPARATOR));
            }
        }

        return basePath;
    }

    public String findImageLocation(final Integer id, final String filename) {
        String result = "";
        final Iterable<Image> imagesForUser = imageRepository.findAllByUploadedBy(id);
        for (final Image image : imagesForUser) {
            if (Objects.equals(image.getName(), filename)) {
                result = image.getLocation();
                break;
            }
        }

        return result;
    }

    public Integer findImageId(final Integer id, final String filename) {
        Integer result = null;
        final Iterable<Image> imagesForUser = imageRepository.findAllByUploadedBy(id);
        for (final Image image : imagesForUser) {
            if (Objects.equals(image.getName(), filename)) {
                result = image.getId();
                break;
            }
        }

        return result;
    }
}
