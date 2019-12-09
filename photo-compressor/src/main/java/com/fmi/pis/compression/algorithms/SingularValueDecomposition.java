package com.fmi.pis.compression.algorithms;

import com.fmi.pis.compression.util.Util;
import org.ojalgo.matrix.decomposition.SingularValue;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Compresses images with loss of data based on the singular value decomposition.
 */
public class SingularValueDecomposition {
    private final BufferedImage source;
    private BufferedImage output;

    private final SingularValue<Double> svdRed;
    private final SingularValue<Double> svdGreen;
    private final SingularValue<Double> svdBlue;

    private final int height;
    private final int width;

    /**
     * SVD setup. Splits the image into red, greed and red matrices and
     * performs the singular value decomposition.
     *
     * @param source the source image
     */
    public SingularValueDecomposition(final BufferedImage source) {
        this.source = source;
        output = source;

        height = source.getHeight();
        width = source.getWidth();

        final PrimitiveDenseStore red = PrimitiveDenseStore.FACTORY.makeEye(height, width);
        final PrimitiveDenseStore green = PrimitiveDenseStore.FACTORY.makeEye(height, width);
        final PrimitiveDenseStore blue = PrimitiveDenseStore.FACTORY.makeEye(height, width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final Color myColor = new Color(source.getRGB(j, i));

                red.set(i, j, myColor.getRed());
                green.set(i, j, myColor.getGreen());
                blue.set(i, j, myColor.getBlue());
            }
        }

        svdRed = SingularValue.make(red);
        svdRed.compute(red);

        svdGreen = SingularValue.make(green);
        svdGreen.compute(green);

        svdBlue = SingularValue.make(blue);
        svdBlue.compute(blue);
    }

    public BufferedImage getOutput() {
        return output;
    }

    /**
     * The percentage parameter should be in the interval [0, 1].
     * 0 = 0%
     * 0.5 = 50%
     * 1 = 100%
     * Higher percentage results in more data loss i.e. higher compression. This is a compression
     * with loss of data, so the higher the percentage the quality of the image gets
     * worse.
     *
     * @param percentage the percentage for compression in the interval [0,1].
     */
    public void compress(final double percentage) {
        if (percentage > 1.0 || percentage < 0) {
            throw new IllegalArgumentException("The percentage must be in the interval [0, 1]");
        }
        int minScale = Math.min(width, height);
        int factor = (int) (minScale - percentage * minScale);
        if (factor <= 0) {
            factor = 1;
        } else if (factor > minScale) {
            factor = minScale;
        }
        final double[][] red = getTruncatedSVD(svdRed, factor);
        final double[][] green = getTruncatedSVD(svdGreen, factor);
        final double[][] blue = getTruncatedSVD(svdBlue, factor);


        output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                red[i][j] = red[i][j] < 0 ? 0 : Math.min(red[i][j], 255);
                green[i][j] = green[i][j] < 0 ? 0 : Math.min(green[i][j], 255);
                blue[i][j] = blue[i][j] < 0 ? 0 : Math.min(blue[i][j], 255);

                final Color myColor = new Color((int) red[i][j], (int) green[i][j], (int) blue[i][j]);
                output.setRGB(j, i, myColor.getRGB());
            }
        }
    }

    /**
     * Truncates the matrices from the singular value decomposition.
     *
     * @param svd    the matrix to be truncated
     * @param factor how many values to keep
     * @return the truncated matrix
     */
    private double[][] getTruncatedSVD(final SingularValue<Double> svd, final int factor) {
        final MatrixStore<Double> U = svd.getQ1();
        final PrimitiveDenseStore truncatedU = PrimitiveDenseStore.FACTORY.makeEye(height, factor);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < factor; j++) {
                truncatedU.set(i, j, U.doubleValue(i, j));
            }
        }

        final MatrixStore<Double> S = svd.getD();
        final PrimitiveDenseStore truncatedS = PrimitiveDenseStore.FACTORY.makeZero(factor, factor);
        for (int i = 0; i < factor; i++) {
            truncatedS.set(i, i, S.doubleValue(i, i));
        }

        final MatrixStore<Double> VT = svd.getQ2().transpose();
        final PrimitiveDenseStore truncatedVT = PrimitiveDenseStore.FACTORY.makeEye(factor, width);
        for (int i = 0; i < factor; i++) {
            for (int j = 0; j < width; j++) {
                truncatedVT.set(i, j, VT.doubleValue(i, j));
            }
        }

        return (truncatedU.multiply(truncatedS)).multiply(truncatedVT).toRawCopy2D();
    }
}
