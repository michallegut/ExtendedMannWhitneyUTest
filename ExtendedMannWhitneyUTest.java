import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;

import java.util.Map;
import java.util.TreeMap;

class ExtendedMannWhitneyUTest {

    private NaturalRanking naturalRanking;

    ExtendedMannWhitneyUTest() {
        naturalRanking = new NaturalRanking(NaNStrategy.FIXED,
                TiesStrategy.AVERAGE);
    }

    private void ensureDataConformance(final double[] x, final double[] y)
            throws NullArgumentException, NoDataException {

        if (x == null ||
                y == null) {
            throw new NullArgumentException();
        }
        if (x.length == 0 ||
                y.length == 0) {
            throw new NoDataException();
        }
    }

    private double[] concatenateSamples(final double[] x, final double[] y) {
        final double[] z = new double[x.length + y.length];

        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, 0, z, x.length, y.length);

        return z;
    }

    private Map<String, Double> mannWhitneyStatistics(final double[] x, final double[] y) throws NullArgumentException, NoDataException {

        ensureDataConformance(x, y);

        final double[] z = concatenateSamples(x, y);
        final double[] ranks = naturalRanking.rank(z);

        double R1 = 0;

        for (int i = 0; i < x.length; ++i) {
            R1 += ranks[i];
        }

        double n1 = x.length;
        double n2 = y.length;

        final double U1 = R1 - n1 * (n1 + 1) / 2;

        final double U2 = n1 * n2 - U1;

        final double R2 = U2 + n2 * (n2 + 1) / 2;

        final double meanr1 = R1 / n1;
        final double meanr2 = R2 / n2;

        Map<String, Double> statistics = new TreeMap<>();
        statistics.put("n1", n1);
        statistics.put("n2", n2);
        statistics.put("U1", U1);
        statistics.put("U2", U2);
        statistics.put("R1", R1);
        statistics.put("R2", R2);
        statistics.put("meanr1", meanr1);
        statistics.put("meanr2", meanr2);

        return statistics;
    }

    private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2)
            throws ConvergenceException, MaxCountExceededException {

        final long n1n2prod = (long) n1 * n2;

        final double EU = n1n2prod / 2.0;
        final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;

        final double z = (Umin - EU) / FastMath.sqrt(VarU);

        final NormalDistribution standardNormal = new NormalDistribution(null, 0, 1);

        return 2 * standardNormal.cumulativeProbability(z);
    }

    Map<String, Double> mannWhitneyUTest(final double[] x, final double[] y)
            throws NullArgumentException, NoDataException,
            ConvergenceException, MaxCountExceededException {

        ensureDataConformance(x, y);

        Map<String, Double> statistics = mannWhitneyStatistics(x, y);

        final double Umin = FastMath.min(statistics.get("U1"), statistics.get("U2"));

        final double p = calculateAsymptoticPValue(Umin, x.length, y.length);

        statistics.put("Umin", Umin);
        statistics.put("p", p);

        return statistics;
    }
}