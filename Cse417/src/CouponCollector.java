// Emre Inceoglu
import java.util.HashSet;
import java.util.Random;

public class CouponCollector {
    /*
    Homework 2 - Problem 5
    average number of iterations for different values of n ((n = value) - average number of tries until the set is complete)
    (n = 100)-518    (n = 500)-3397    (n = 1500)-11835  (n = 2500)-21000   (n = 4000)-35535

    Compared to the problem 5 of homework 1 the results show similarity in that as the sample size n increases the average
    number of tries it takes for the algorithm to complete increases. In such case, the ratio (number of tries) / n,
    for sample sizes n = 100, 500, 1500, 2500, 4000 respectively increases by 5.18, 6.794, 7.89, 8.4, and 8.88. As the numberOfIterations
    (variable seen below) increases the number of times the algorithm is tested is adjusted and it is observed that on multiple executions of
    this algorithm with numberOfIterations = a very large number like 10000 both number of coupons C and C/2 output almost the same values as the ones
    tested on other executions (i.e. 1) C = 100000, C/2 = 500000, 2) C = 100000, C/2 = 500000).
     */
    public static void main(String[] args) {
        collectCoupons();
    }

    public static void collectCoupons() {
        int n = 4000;
        int numberOfIterations = 10000;
        collectCoupons(n, numberOfIterations);
    }

    private static void collectCoupons(int n, int numberOfIterations) {
        Random rand = new Random();
        int total = 0;
        for (int i = 0; i < numberOfIterations; i++) {
            HashSet<Integer> currentIntegers = new HashSet<>();
            int count = 0;
            while (currentIntegers.size() != n) {
                int thisRandomNumber = rand.nextInt(n) + 1;
                count++;
                if (!currentIntegers.contains(thisRandomNumber)) {
                    currentIntegers.add(thisRandomNumber);
                }
            }
            total += count;
        }
        int average = total / numberOfIterations;
        System.out.println("average is " + average);
    }
}
