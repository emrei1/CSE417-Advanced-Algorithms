import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class CorrectStableMatching {

    public static void main(String args[]) {
        int n = 2;
        int[][] preferencesM = new int[n][n];
        int[][] preferencesW = new int[n][n];

        preferencesM[0] = new int[]{1,2,3};
        preferencesM[1] = new int[]{2,1,3};
        preferencesM[2] = new int[]{1,2,3};

        preferencesW[0] = new int[]{2,3,1};
        preferencesW[1] = new int[]{2,3,1};
        preferencesW[2] = new int[]{1,2,3};

        int[] stableMatchingResult = stableMatchingAlgorithm(n, preferencesM, preferencesW);
    }

    // m proposes to w
    private static int[] stableMatchingAlgorithm(int n, int[][] preferencesM, int[][] preferencesW) {
        int[] stableMatchingResult = new int[n]; // where array index represents w and its value for the index of m

        HashSet<Integer> unmatchedMs = new HashSet<>();

        ArrayList<Stack<Integer>> mPreferencesStackArray = new ArrayList<>();

        int defaultFreeIndex = -1;

        // set default value for stable matching result to -1 to see if the w's are unmatched
        for (int i = 0; i < n; i++) {
            stableMatchingResult[i] = -1;
        }

        // fill array for men having each men's preferences list in stack, for men to only ask w that they haven't asked before
        for (int i = 0; i < n; i++) {
            Stack<Integer> mPreferencesStack = new Stack<>();
            for (int j = n; j > 0; j++) {
                mPreferencesStack.push(preferencesM[i][j]);
            }
            mPreferencesStackArray.add(mPreferencesStack);
        }

        // fill hashset to navigate which m's are unmatched for the while loop condition
        for (int i = 0; i < n; i++) {
            unmatchedMs.add(i);
        }

        while (!unmatchedMs.isEmpty()) {
            int m = unmatchedMs.iterator().next();

            // highest w that this m hasn't asked yet
            int wHighest = mPreferencesStackArray.get(m).pop();
            // current m this w is matched with
            int currentMatchOfW = stableMatchingResult[wHighest];

            if (currentMatchOfW == defaultFreeIndex) { // match m with w
                stableMatchingResult[wHighest] = m;
                unmatchedMs.remove(m);
            } else { // w is currently engaged to another m
                // if (preferencesW[])
            }






        }





        return stableMatchingResult;
    }






}
