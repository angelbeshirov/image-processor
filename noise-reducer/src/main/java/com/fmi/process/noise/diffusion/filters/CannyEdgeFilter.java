package com.fmi.process.noise.diffusion.filters;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.Arrays;

/**
 * Filters the image to canny edges.
 */
public class CannyEdgeFilter implements Filter {
    private final static float GAUSSIAN_CUT_OFF = 0.005f;
    private final static float MAGNITUDE_SCALE = 100f;
    private final static float MAGNITUDE_LIMIT = 1000f;
    private final static int MAGNITUDE_MAX = (int) (MAGNITUDE_SCALE * MAGNITUDE_LIMIT);


    private int height;
    private int width;
    private int size;
    private int[] data;
    private int[] magnitude;
    private BufferedImage edgesImage;

    private float gaussianKernelRadius;
    private float lowThreshold;
    private float highThreshold;
    private int gaussianKernelWidth;
    private boolean contrastNormalized;

    private float[] xCoordinates;
    private float[] yCoordinates;
    private float[] xGradient;
    private float[] yGradient;

    public CannyEdgeFilter() {
        lowThreshold = 0.5f;
        highThreshold = 1f;
        gaussianKernelRadius = 2f;
        gaussianKernelWidth = 16;
        contrastNormalized = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage filter(final BufferedImage img) {
        process(img);
        final BufferedImage rgbImage = new BufferedImage(edgesImage.getWidth(), edgesImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        final ColorConvertOp op = new ColorConvertOp(null);
        op.filter(edgesImage, rgbImage);
        data = null;
        edgesImage = null;
        return rgbImage;
    }

    private void process(final BufferedImage bufferedImage) {
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        size = width * height;
        initArrays();
        readLuminance(bufferedImage);
        if (contrastNormalized) {
            normalizeContrast();
        }
        computeGradients(gaussianKernelRadius, gaussianKernelWidth);
        final int low = Math.round(lowThreshold * MAGNITUDE_SCALE);
        final int high = Math.round(highThreshold * MAGNITUDE_SCALE);
        performHysteresis(low, high);
        thresholdEdges();
        writeEdges(data);

    }

    private void initArrays() {
        if (data == null || size != data.length) {
            data = new int[size];
            magnitude = new int[size];

            xCoordinates = new float[size];
            yCoordinates = new float[size];
            xGradient = new float[size];
            yGradient = new float[size];
        }
    }

    private void computeGradients(final float kernelRadius, final int kernelWidth) {
        final float[] kernel = new float[kernelWidth];
        final float[] diffKernel = new float[kernelWidth];
        int kwidth;
        for (kwidth = 0; kwidth < kernelWidth; kwidth++) {
            final float g1 = gaussian(kwidth, kernelRadius);
            if (g1 <= GAUSSIAN_CUT_OFF && kwidth >= 2) break;
            final float g2 = gaussian(kwidth - 0.5f, kernelRadius);
            final float g3 = gaussian(kwidth + 0.5f, kernelRadius);
            kernel[kwidth] = (g1 + g2 + g3) / 3f / (2f * (float) Math.PI * kernelRadius * kernelRadius);
            diffKernel[kwidth] = g3 - g2;
        }

        int initX = kwidth - 1;
        int maxX = width - (kwidth - 1);
        int initY = width * (kwidth - 1);
        int maxY = width * (height - (kwidth - 1));

        //perform convolution in x and y directions
        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                final int index = x + y;
                float sumX = data[index] * kernel[0];
                float sumY = sumX;
                int xOffset = 1;
                int yOffset = width;
                for (; xOffset < kwidth; ) {
                    sumY += kernel[xOffset] * (data[index - yOffset] + data[index + yOffset]);
                    sumX += kernel[xOffset] * (data[index - xOffset] + data[index + xOffset]);
                    yOffset += width;
                    xOffset++;
                }

                yCoordinates[index] = sumY;
                xCoordinates[index] = sumX;
            }
        }

        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                float sum = 0f;
                final int index = x + y;
                for (int i = 1; i < kwidth; i++)
                    sum += diffKernel[i] * (yCoordinates[index - i] - yCoordinates[index + i]);

                xGradient[index] = sum;
            }
        }

        for (int x = kwidth; x < width - kwidth; x++) {
            for (int y = initY; y < maxY; y += width) {
                float sum = 0.0f;
                final int index = x + y;
                int yOffset = width;
                for (int i = 1; i < kwidth; i++) {
                    sum += diffKernel[i] * (xCoordinates[index - yOffset] - xCoordinates[index + yOffset]);
                    yOffset += width;
                }

                yGradient[index] = sum;
            }

        }

        initX = kwidth;
        maxX = width - kwidth;
        initY = width * kwidth;
        maxY = width * (height - kwidth);
        for (int x = initX; x < maxX; x++) {
            for (int y = initY; y < maxY; y += width) {
                final int index = x + y;
                final int indexN = index - width;
                final int indexS = index + width;
                final int indexW = index - 1;
                final int indexE = index + 1;
                final int indexNW = indexN - 1;
                final int indexNE = indexN + 1;
                final int indexSW = indexS - 1;
                final int indexSE = indexS + 1;

                final float xGrad = xGradient[index];
                final float yGrad = yGradient[index];
                final float gradMag = hypot(xGrad, yGrad);

                //perform non-maximal suppression
                final float nMag = hypot(xGradient[indexN], yGradient[indexN]);
                final float sMag = hypot(xGradient[indexS], yGradient[indexS]);
                final float wMag = hypot(xGradient[indexW], yGradient[indexW]);
                final float eMag = hypot(xGradient[indexE], yGradient[indexE]);
                final float neMag = hypot(xGradient[indexNE], yGradient[indexNE]);
                final float seMag = hypot(xGradient[indexSE], yGradient[indexSE]);
                final float swMag = hypot(xGradient[indexSW], yGradient[indexSW]);
                final float nwMag = hypot(xGradient[indexNW], yGradient[indexNW]);
                float tmp;

                if (xGrad * yGrad <= (float) 0
                        ? Math.abs(xGrad) >= Math.abs(yGrad)
                        ? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * neMag - (xGrad + yGrad) * eMag)
                        && tmp > Math.abs(yGrad * swMag - (xGrad + yGrad) * wMag)
                        : (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * neMag - (yGrad + xGrad) * nMag)
                        && tmp > Math.abs(xGrad * swMag - (yGrad + xGrad) * sMag)
                        : Math.abs(xGrad) >= Math.abs(yGrad)
                        ? (tmp = Math.abs(xGrad * gradMag)) >= Math.abs(yGrad * seMag + (xGrad - yGrad) * eMag)
                        && tmp > Math.abs(yGrad * nwMag + (xGrad - yGrad) * wMag)
                        : (tmp = Math.abs(yGrad * gradMag)) >= Math.abs(xGrad * seMag + (yGrad - xGrad) * sMag)
                        && tmp > Math.abs(xGrad * nwMag + (yGrad - xGrad) * nMag)
                ) {
                    magnitude[index] = gradMag >= MAGNITUDE_LIMIT ? MAGNITUDE_MAX : (int) (MAGNITUDE_SCALE * gradMag);
                } else {
                    magnitude[index] = 0;
                }
            }
        }
    }

    private float hypot(final float x, final float y) {
        return (float) Math.hypot(x, y);
    }

    private float gaussian(final float x, final float sigma) {
        return (float) Math.exp(-(x * x) / (2f * sigma * sigma));
    }

    private void performHysteresis(final int low, final int high) {
        Arrays.fill(data, 0);

        int offset = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[offset] == 0 && magnitude[offset] >= high) {
                    follow(x, y, offset, low);
                }
                offset++;
            }
        }
    }

    private void follow(final int x1, final int y1, final int i1, final int threshold) {
        final int x0 = x1 == 0 ? x1 : x1 - 1;
        final int x2 = x1 == width - 1 ? x1 : x1 + 1;
        final int y0 = y1 == 0 ? y1 : y1 - 1;
        final int y2 = y1 == height - 1 ? y1 : y1 + 1;

        data[i1] = magnitude[i1];
        for (int x = x0; x <= x2; x++) {
            for (int y = y0; y <= y2; y++) {
                final int i2 = x + y * width;
                if ((y != y1 || x != x1)
                        && data[i2] == 0
                        && magnitude[i2] >= threshold) {
                    follow(x, y, i2, threshold);
                    return;
                }
            }
        }
    }

    private void thresholdEdges() {
        for (int i = 0; i < size; i++) {
            data[i] = data[i] > 0 ? -1 : 0xff000000;
        }
    }

    private int luminance(final float r, final float g, final float b) {
        return Math.round(0.299f * r + 0.587f * g + 0.114f * b);
    }

    private void readLuminance(final BufferedImage bufferedImage) {
        final int type = bufferedImage.getType();
        if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB) {
            final int[] pixels = (int[]) bufferedImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < size; i++) {
                final int p = pixels[i];
                final int r = (p & 0xff0000) >> 16;
                final int g = (p & 0xff00) >> 8;
                final int b = p & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else if (type == BufferedImage.TYPE_BYTE_GRAY) {
            final byte[] pixels = (byte[]) bufferedImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < size; i++) {
                data[i] = (pixels[i] & 0xff);
            }
        } else if (type == BufferedImage.TYPE_USHORT_GRAY) {
            final short[] pixels = (short[]) bufferedImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < size; i++) {
                data[i] = (pixels[i] & 0xffff) / 256;
            }
        } else if (type == BufferedImage.TYPE_3BYTE_BGR) {
            final byte[] pixels = (byte[]) bufferedImage.getData().getDataElements(0, 0, width, height, null);
            int offset = 0;
            for (int i = 0; i < size; i++) {
                final int b = pixels[offset++] & 0xff;
                final int g = pixels[offset++] & 0xff;
                final int r = pixels[offset++] & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else {
            throw new IllegalArgumentException("Unsupported image type: " + type);
        }
    }

    private void normalizeContrast() {
        final int[] histogram = new int[256];
        for (final int datum : data) {
            histogram[datum]++;
        }
        final int[] remap = new int[256];
        int sum = 0;
        int j = 0;
        for (int i = 0; i < histogram.length; i++) {
            sum += histogram[i];
            final int target = sum * 255 / size;
            for (int k = j + 1; k <= target; k++) {
                remap[k] = i;
            }
            j = target;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = remap[data[i]];
        }
    }

    private void writeEdges(final int[] pixels) {
        if (edgesImage == null) {
            edgesImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        edgesImage.getWritableTile(0, 0).setDataElements(0, 0, width, height, pixels);
    }
}
