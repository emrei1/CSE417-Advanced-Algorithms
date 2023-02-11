import java.util.Random;
// Homework 8 - Problem 3
public class LongestCommonSubsequent {
    public static void main(String[] args) {
        Random rand = new Random();
        int n = 20000;
        int k = 16;
        int numberOfTrials = 1000;
        int lcsCount = 0;
        for (int i = 0; i < numberOfTrials; i++) {
            int[] stringA = new int[n];
            int[] stringB = new int[n];
            for (int j = 0; j < n; j++) {
                int randomCharacterA = rand.nextInt(k);
                int randomCharacterB = rand.nextInt(k);
                stringA[j] = randomCharacterA;
                stringB[j] = randomCharacterB;
            }
            int longestCommonSubsequentValue = getLCS(n, stringA, stringB);
            lcsCount += longestCommonSubsequentValue;
        }
        int expectedLCS = lcsCount / numberOfTrials;
        System.out.println("average lcs was found to be: " + expectedLCS);
        double lambdaK = (double)expectedLCS / (double)n;
        System.out.println("lambdaK was found to be: " + lambdaK);
    }

    public static int getLCS(int n, int[] stringA, int[] stringB) {
        int[][] table = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            table[i][0] = 0;
        }
        for (int i = 0; i <= n; i++) {
            table[0][i] = 0;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (stringA[i - 1] == stringB[j - 1]) {
                    table[i][j] = table[i - 1][j - 1] + 1;
                } else if (table[i - 1][j] >= table[i][j - 1]) {
                    table[i][j] = table[i - 1][j];
                } else {
                    table[i][j] = table[i][j - 1];
                }
            }
        }
        return table[n][n];
    }
}
