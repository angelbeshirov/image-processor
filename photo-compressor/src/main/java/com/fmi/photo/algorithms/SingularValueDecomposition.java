package com.fmi.photo.algorithms;

public class SingularValueDecomposition {

    private double[][] A;
    private double[][] U;
    private double[][] S;
    private double[][] V;

    private MatrixOperations matrixOperations = new MatrixOperations();

    private boolean isIrregular = false; // true if the columns are greater than the rows

    public SingularValueDecomposition(double[][] A) {
        this.A = A;

        double[][] result;
        int columns = A[0].length;
        int rows = A.length;
        if (columns > rows) {
            this.isIrregular = true;
            result = matrixOperations.multiply(A, matrixOperations.transpose(A));
        } else {
            result = matrixOperations.multiply(matrixOperations.transpose(A), A);
        }

        double[] eigenValues = matrixOperations.getEigenValues(result);
        double[][] eigenVectors = matrixOperations.getEigenVectors(result);
        double[][] Av = generateAvis(eigenVectors);

        double[] singularValues = matrixOperations.getSingularValues(eigenValues);

        U = generateUis(Av, singularValues);
//            U is by row so needs to be transposed to become the real U
        S = matrixOperations.generateDiagonalMatrix(singularValues);

        V = eigenVectors;
//          each eigenVector is a row in this matrix, so there is no point to transpose it here and then transpose it again
//          when multiplying at the end
    }

    private double[][] generateUis(double[][] Av, double[] singularValues) {
        double[][] Ui = new double[singularValues.length][Av[0].length];

        for (int i = 0; i < singularValues.length; i++) {
            Ui[i] = matrixOperations.multiplyWithScalar(Av[i], 1.0 / singularValues[i]);
        }

        return Ui;
    }

    private double[][] generateAvis(double[][] eigenVectors) {
        double Av[][];
        if (A[0].length == eigenVectors.length) {
            Av = new double[eigenVectors.length][A.length];
            for (int i = 0; i < eigenVectors.length; i++) {
                double[][] eigenVectorsAsColumns = new double[][]{eigenVectors[i]};
                Av[i] = matrixOperations.transpose(matrixOperations.multiply(A, matrixOperations.transpose(eigenVectorsAsColumns)))[0];
            }
        } else {
            Av = new double[eigenVectors.length][A[0].length];
            for (int i = 0; i < eigenVectors.length; ++i) {
                double[][] eigenVectorAsRow = new double[][]{eigenVectors[i]};
                Av[i] = matrixOperations.multiply(eigenVectorAsRow, A)[0];
            }
        }
        return Av;

    }

    public double[][] getResult() {

        double[][] finalResult;
        if(isIrregular) {
            finalResult = matrixOperations.multiply(matrixOperations.transpose(V), matrixOperations.multiply(S, U));
        } else {
            finalResult = matrixOperations.multiply(matrixOperations.transpose(U), matrixOperations.multiply(S, V));
//          not transposed V?
        }

        return finalResult;
    }

    public double[][] getA() {
        return this.A;
    }

    public double[][] getU() {
        return this.U;
    }

    public double[][] getS() {
        return this.S;
    }

    public double[][] getV() {
        return this.V;
    }

    /**
     * Removes the last N singular values from the Sigma matrix. It is assumed that the matrix is diagonal.
     * Compressing happens only on the diagonal.
     * @param n n values to be removed
     */
    public void applyCompression(int n) {
        for(int i = this.S.length - 1; i >= 0; i--) {
            if(n > 0) {
                this.S[i][i] = 0;
                n--;
            } else {
                break;
            }
        }
    }

    /**
     * Removes the values which are smaller than percent of the greatest value in the diagonal matrix.
     * It is assumed that the matrix is diagonal.
     * @param percentage percent based on which the threshold is calculated
     */
    public void applyPercentageCompression(int percentage) {
        double threshold = (percentage/100.0)*S[0][0];
        for(int i = this.S.length - 1; i >= 0; i--) {
            if(this.S[i][i] <= threshold) {
                this.S[i][i] = 0;
            } else {
                break;
            }
        }
    }

}
