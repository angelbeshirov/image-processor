import com.fmi.photo.algorithms.SingularValueDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionTest {

    private boolean compareMatrices(double[][] a, double[][] b) {
        if (a.length != b.length) return false;

        if (a[0].length != b[0].length) return false;

        boolean result = true;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] != b[i][j]) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    @Test
    public void testSquareMatrix() {
        double[][] a = {{1, 4, 6}, {8, 3, 2}, {16, 13, 11}};
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        double[][] actual = singularValueDecomposition.getResult();
        double[][] expected = a;

        Assert.assertTrue(compareMatrices(expected, actual));
    }

    @Test
    public void testIrregularMatrix() {
        double[][] a = {{1, 4, 3, 1, 4, 7}, {8, 3, 2, 2, 1, 6}, {16, 13, 11, 1, 2, 7}};
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        double[][] actual = singularValueDecomposition.getResult();
        double[][] expected = a;
        Assert.assertTrue(compareMatrices(expected, actual));
    }

    @Test
    public void testRegularMatrix() {
        double[][] a = {{1, 4}, {8, 3}, {16, 13}, {18, 23}, {33, 44}};
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        double[][] actual = singularValueDecomposition.getResult();
        double[][] expected = a;

        Assert.assertTrue(compareMatrices(expected, actual));
    }

    @Test
    public void testSAfterCompressingNValues() {
        double[][] a = {{1, 4, 3, 1}, {8, 3, 2, 1}, {16, 13, 4, 1}, {18, 23, 12, 3}, {33, 44, 1, 2}};
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        double[][] actual = singularValueDecomposition.getS();
        double[][] expected = new double[actual.length][actual.length];

        for(int i =0;i < actual.length; i++){
            for(int j=0; j < actual.length; j++) {
                expected[i][j] = actual[i][j];
            }
        }
        int lastIndex = expected.length-1;
        expected[lastIndex][lastIndex] = 0;
        expected[lastIndex-1][lastIndex-1] = 0;
        singularValueDecomposition.applyCompression(2);


        Assert.assertTrue(compareMatrices(expected, actual));
    }

    @Test
    public void testSAfterCompressingValuesSmallerThanThreshold() {
        double[][] a = {{1, 4, 3, 1}, {8, 3, 2, 1}, {16, 13, 4, 1}, {18, 23, 12, 3}, {33, 44, 1, 2}};
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(a);
        double[][] actual = singularValueDecomposition.getS();
        double[][] expected = new double[actual.length][actual.length];

        for(int i =0;i < actual.length; i++){
            for(int j=0; j < actual.length; j++) {
                expected[i][j] = actual[i][j];
            }
        }
        int lastIndex = expected.length-1;
        expected[lastIndex][lastIndex] = 0;
        expected[lastIndex-1][lastIndex-1] = 0;
        singularValueDecomposition.applyCompression(80);


        Assert.assertTrue(compareMatrices(expected, actual));
    }

    @Test
    public void testU() {
        //TODO
    }

    @Test
    public void testS() {
        //TODO
    }

    @Test
    public void testV() {
        //TODO
    }
}
