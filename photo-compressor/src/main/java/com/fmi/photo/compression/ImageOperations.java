package com.fmi.photo.compression;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ImageOperations {
    public enum ExtractColor {
        RED, GREEN, BLUE, ALPHA
    }

    public int[][] getMatrix(BufferedImage image, ExtractColor extractColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int tempValue = image.getRGB(col, row);
                Color clr = new Color(tempValue);
                if (extractColor == ExtractColor.RED) {
                    result[row][col] = clr.getRed();
                } else if (extractColor == ExtractColor.GREEN) {
                    result[row][col] = clr.getGreen();
                } else if (extractColor == ExtractColor.BLUE) {
                    result[row][col] = clr.getBlue();
                } else if (extractColor == ExtractColor.ALPHA) {
                    result[row][col] = clr.getAlpha();
                }
            }
        }
        return result;
    }

    public int[][] getMatrixRGB(BufferedImage image) {
        int[][] imageMatrix = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int rgb = image.getRGB(i, j);
                imageMatrix[i][j] = rgb;
            }
        }

        return imageMatrix;
    }

    public static void writeSingularValues(double[][] singMatrix, String fileName) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write("a = {");
            for (int row = 0; row < singMatrix.length; row++) {

                printWriter.write(Double.valueOf(singMatrix[row][row]).toString());
                if (row != singMatrix.length - 1) {
                    printWriter.write(", ");
                }
            }
            printWriter.write("}");
            printWriter.flush();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!!!");
        }
    }

    public void writeToFile(int[][] matrix, String fileName) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {

            for (int row = 0; row < matrix.length; row++) {
                for (int column = 0; column < matrix[row].length; column++) {
                    printWriter.print(matrix[row][column] + " ");
                }
                printWriter.println();
            }
            printWriter.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("Problem with the file, it was not found or invalid");
        }
    }

    public void writeToFile(double[][] matrix, String fileName) {
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {

            for (int row = 0; row < matrix.length; row++) {
                for (int column = 0; column < matrix[row].length; column++) {
                    printWriter.print(matrix[row][column] + " ");
                }
                printWriter.println();
            }
            printWriter.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("Problem with the file, it was not found or invalid");
        }
    }

    public int[][] getRGBMatrix(int[][] matrixRed, int[][] matrixGreen, int[][] matrixBlue, int[][] matrixAlpha) {
        int rows = matrixRed.length;
        int columns = matrixRed[0].length;
        int[][] result = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = new Color(matrixRed[i][j], matrixGreen[i][j], matrixBlue[i][j], matrixAlpha[i][j]).getRGB();

            }
        }
        return result;
    }

    public BufferedImage generateImage(int[][] rgbMatrix) {
        BufferedImage img = new BufferedImage(rgbMatrix[0].length, rgbMatrix.length, BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < rgbMatrix.length; i++) {
            for (int j = 0; j < rgbMatrix[i].length; j++) {
                img.setRGB(j, i, rgbMatrix[i][j]);

            }
        }

        return img;
    }

    public void writeImage(BufferedImage bufferedImage, String filename, String extension) throws IOException {
        File output = new File(filename);
        ImageIO.write(bufferedImage, extension, output);
    }
}
