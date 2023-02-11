// Emre Inceoglu
import java.util.*;

public class CouponCollectorMinimum {
    /*
    Homework 2 - Problem 6
    average coupon value for different values of n ((n = value) - average of coupon value)
    (n = 100)-50    (n = 500)-250    (n = 1500)-750  (n = 2500)-1250   (n = 4000)-2000
    Compared to the problem 5 of homework these results are very similar in that, where for the matching algorithm the goodness of
    the algorithm for n matrices of permutations was n / 2 -1, in this algorithm it can be seen from the above observations that
    the average coupon value, no matter what the value of n is, is n / 2. Thus, the expected value of the average coupon value after the
    set of coupons is complete, is n / 2.
     */
    public static void main(String[] args) {
        collectCouponsMinimum();
    }
    public static void collectCouponsMinimum() {
        int n = 4000;
        int numberOfIterations = 20;
        collectCouponsMinimum(n, numberOfIterations);
    }
    private static void collectCouponsMinimum(int n, int numberOfIterations) {
        Random rand = new Random();
        int totalIterations = 0;
        int totalCouponValue = 0;
        for (int i = 0; i < numberOfIterations; i++) {
            int[] couponValues = new int[n];
            // determine couponValues from coupon index 0 to n
            for (int j = 0; j < n; j++) {
                int thisRandNo = rand.nextInt(n) + 1;
                couponValues[j] = thisRandNo;
            }
            HashSet<Integer> currentIntegers = new HashSet<>();
            int countIteration = 0;
            int countCouponValue = 0;
            int min = n + 1;
            while (currentIntegers.size() != n) {
                int randNo = rand.nextInt(n) + 1;
                countIteration++;
                countCouponValue += couponValues[randNo - 1];
                if (!currentIntegers.contains(randNo)) {
                    currentIntegers.add(randNo);
                    if (couponValues[randNo - 1] < min) {
                        min = couponValues[randNo - 1];
                    }
                }
            }
            // System.out.println("min val for this set is " + min + "!!);
            totalIterations += countIteration;
            totalCouponValue += countCouponValue;
        }
        int averageIterations = totalIterations / numberOfIterations;
        int averageCouponValue = totalCouponValue / totalIterations;
        System.out.println("average iteration per loop is " + averageIterations);
        System.out.println("average coupon value per loop is " + averageCouponValue);
    }
}
