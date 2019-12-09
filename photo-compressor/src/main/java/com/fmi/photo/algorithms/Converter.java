package com.fmi.photo.algorithms;

public class Converter {

    public static double[][] convertToDouble(int[][] a) {
        double[][] result = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            result[i] = new double[a[i].length];
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = a[i][j];
            }
        }
        return result;
    }

    public static int[][] convertToInt(double[][] a) {
        int[][] result = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = (int) Math.round(a[i][j]);
            }
        }
        return result;
    }

//    public static double[][] roundToInt(double[][] a) {
//        double eps = 0.0001;
//        double[][] result = new double[a.length][a[0].length];
//        for (int i = 0; i < a.length; i++) {
//            for (int j = 0; j < a[i].length; j++) {
//                int casted = (int) a[i][j];
//                if (Math.abs(a[i][j] - casted) < eps) {
//                    if (a[i][j] < 0) {
//                        result[i][j] = Math.ceil(a[i][j]);
//                    } else {
//                        result[i][j] = Math.floor(a[i][j]);
//                    }
//                } else {
//                    if (a[i][j] > 0) {
//                        result[i][j] = Math.ceil(a[i][j]);
//                    } else {
//                        result[i][j] = Math.floor(a[i][j]);
//                    }
//                }
//            }
//        }
//
//        return result;
//    }

}
