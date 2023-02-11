import java.util.*;

public class MatchingCouples {

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);
        System.out.print("enter value for n: ");
        int n = console.nextInt();

        Random rand = new Random();

        int[][] preferencesM = new int[n][n];
        int[][] preferencesW = new int[n][n];

        int[] identityArray = new int[n];

        for (int i = 0; i < n; i++) {
            identityArray[i] = i;
        }

        for (int i = 0; i < n; i++) {
            preferencesM[i] = Permutation(identityArray, n, rand);
            // System.out.println(preferencesM[i]);
        }

        for (int i = 0; i < n; i++) {
            preferencesW[i] = Permutation(identityArray, n, rand);
        }

        /*
        int[][] preferencesM = new int[3][3];
        int[][] preferencesW = new int[3][3];

        preferencesM[0] = new int[]{2,0,1};
        preferencesM[1] = new int[]{2,0,1};
        preferencesM[2] = new int[]{2,1,0};
        // preferencesM[3] = new int[]{2,0,3,1};

        preferencesW[0] = new int[]{2,0,1};
        preferencesW[1] = new int[]{0,2,1};
        preferencesW[2] = new int[]{2,0,1};
        // preferencesW[3] = new int[]{0,1,2,3};
         */

        HashMap<Integer, Integer> couples = findCouples(preferencesM, preferencesW);

        // calculate mrank and wrank
        int wRank = 0;
        for (int w = 0; w < n; w++) {
            int wMatch = couples.get(w);
            int rankOffset = -1;
            for (int i = 0; i < n; i++) {
                if (preferencesW[w][i] == wMatch) {
                    rankOffset = i;
                }
            }
            wRank += rankOffset;
        }
        System.out.println("Goodness of this algorithm is (W-Rank/n): "+ (wRank/n));
    }

    public static HashMap<Integer, Integer> findCouples(int[][] preferencesM, int[][] preferencesW) {
        HashMap<Integer, Integer> couples = new HashMap<>();

        for (int i = 0; i < preferencesW.length; i++) {
            couples.put(i, null);
        }

        Stack<Integer> bachelors = new Stack<>();
        // Set<Integer> bachelors = new HashSet<>();
        for (int i = 0; i < preferencesM.length; i++) {
            bachelors.push(i);
            // bachelors.add(i);
        }

        int bachelorCount = bachelors.size();

        while (bachelorCount > 0) {
            // int currentBachelor = bachelors.iterator().next();
            int currentBachelor = bachelors.pop();
            // // // System.out.println("currentbachelor is"+currentBachelor);

            for (int w = 0; w < preferencesW[currentBachelor].length; w++) {
                // System.out.println("right now "+currentBachelor+" is proposing to "+w);
                // if part correct
                if (couples.get(w) == null) {
                    couples.put(w, currentBachelor);
                    // bachelors.remove(currentBachelor);
                    // System.out.println("proposal accepted");
                    break;
                } else {
                    int oldBachelor = couples.get(w); //
                    if (wouldChangePartner(currentBachelor, oldBachelor, w, preferencesW)) {
                        couples.put(w, currentBachelor);
                        // bachelors.remove(currentBachelor);
                        bachelors.push(oldBachelor);
                        // System.out.println("proposal accepted");
                        break;
                    }
                    // bachelors.push(currentBachelor);
                    // System.out.println("proposal rejected");
                }
            }
            bachelorCount = bachelors.size();
        }
        return couples;
    }

    // entirely correct
    public static boolean wouldChangePartner(int currentBachelor, int oldBachelor, int currentWomen, int[][] women) {
        int prefForCurrent = -1;
        int prefForOld = -1;

        for (int i = 0; i < women[currentWomen].length; i++) {
            if (women[currentWomen][i] == currentBachelor) {
                prefForCurrent = i;
            }

            if (women[currentWomen][i] == oldBachelor) {
                prefForOld = i;
            }
        }

        if (prefForCurrent < prefForOld) {
            return true;
        }
        return false;
    }

    public static int[] Permutation(int[] arr, int n, Random rand) {
        for (int i = 1; i < n; i++) {
            int j = rand.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }
}
