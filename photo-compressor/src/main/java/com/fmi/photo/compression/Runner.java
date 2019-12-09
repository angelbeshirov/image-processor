package com.fmi.photo.compression;

import java.io.IOException;

public class Runner {

    public static void main(String[] args) throws IOException {
        PhotoCompressor photoCompressor = new PhotoCompressor("src\\main\\resources\\sparrow.jpg");
        //photoCompressor.compress(0);
        photoCompressor.saveCompressedImage("src\\main\\resources\\newSparrow.jpg", "jpg");
        //photoCompressor.generateDifference("src\\main\\resources\\errors\\diffPlant.jpg", "jpg");
    }
}
