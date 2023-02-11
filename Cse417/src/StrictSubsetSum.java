import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Emre Inceoglu - 417 Problem 3 & 4 & 5
public class StrictSubsetSum {

    public static void main(String[] args) {
        int n = 19;
        int[] weights = {2, 6, 10, 5, 2, 5, 11, 2, 6, 1, 4, 5, 5, 1, 1, 4, 12, 3, 1};
        int w = 10;
        // int n = 6;
        // int[] weights = {3, 2, 1, 3, 3, 2};
        // int w = 3;
        // ArrayList<Integer> strictSum = (strictSubsetSum(n, w, (weights)));
        int subsetSumCount = subsetSumCounting3(n, w, weights);

        int totalWeight = 0;
        /*
        for (int i = 0; i < strictSum.size(); i++) {
            totalWeight += weights[strictSum.get(i) - 1];
        }


        if (totalWeight == w) {
            System.out.println("Strict sum problem was FOUND with final array: " + strictSum.toString() + " and total weight = "+totalWeight);
        } else {
            System.out.println("Strict sum was NOT FOUND with final array: " + strictSum.toString() + " and total weight = "+totalWeight);
        }
        System.out.println();
        System.out.println("subset sum count was found to be = " + subsetSumCount);
        System.out.println();
        */

        // Problem 5 - Electoral College Ties
        String states[] = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia","Florida",
                "Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
                "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
                "New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina",
                "South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
        long[] electoralVotes = {9,3,11,6,55,9,7,3,3,29,16,4,4,20,11,6,6,8,8,4,10,11,16,10,6,10,3,5,6,4,14,5,29,15,3,18,7,7,20,4,9,3,11,38,6,3,13,12,5,10,3};

        // 5a
        long numberOfWaysFor269Tie = subsetSumCounting2(states.length, 269, electoralVotes);
        System.out.println("there were found to be " + numberOfWaysFor269Tie + " ways the US presidential election could be summed up in a 269-269 tie");
        System.out.println();
        // 5b
        ArrayList<Long> longestSetFor269 = strictSubsetSum(electoralVotes.length, 269, electoralVotes);
        int electoralTotalWeight = 0;
        for (int i = 0; i < longestSetFor269.size(); i++) {
            electoralTotalWeight += electoralVotes[Math.toIntExact(longestSetFor269.get(i) - 1)];
        }
        System.out.println("total weight for the set: "+ electoralTotalWeight);
        System.out.println("longest set of states yielding count/weight 269: ");
        System.out.println(longestSetFor269.size());
        for (int i = 0; i < longestSetFor269.size(); i++) {
            System.out.println("" + states[Math.toIntExact(longestSetFor269.get(i) - 1)] + " (" + electoralVotes[Math.toIntExact(longestSetFor269.get(i) - 1)] + ")");
        }
    }

    public static ArrayList<Long> strictSubsetSum(long n, long w, long[] weights) {
        long[][] M = new long[(int) (n + 1)][(int) (w + 1)];
        HashMap<Long, ArrayList<Long>> indexToSet = new HashMap<>();
        HashMap<Long, Long> totalWeightToIndex = new HashMap<>();

        for (long i = 0; i < ((n + 1) * (w + 1)); i++) {
            indexToSet.put(i, new ArrayList<>());
        }

        for (long i = 1; i <= n; i++) {
            for (long j = 0; j <= w; j++) {
                if (j < weights[(int) (i - 1)] || M[(int) (i - 1)][(int) j] > (weights[(int) (i - 1)] + M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])])) { //
                    M[(int) i][(int) j] = M[(int) (i - 1)][(int) j];
                    ArrayList<Long> copyingArray = indexToSet.get((i - 1) * (w + 1) + j);
                    for (long k = 0; k < copyingArray.size(); k++) {
                        indexToSet.get(i * (w + 1) + j).add(copyingArray.get((int) k));
                    }
                    totalWeightToIndex.put(M[(int) i][(int) j], i * (w + 1) + j);
                } else {
                    if (!totalWeightToIndex.containsKey(M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])] + weights[(int) (i - 1)])) { // would be weight
                        ArrayList<Long> copyingArray = indexToSet.get(((i - 1) * (w + 1) + j - weights[(int) (i - 1)]));
                        M[(int) i][(int) j] = M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])];
                        for (int k = 0; k < copyingArray.size(); k++) {
                            indexToSet.get(i * (w + 1) + j).add(copyingArray.get(k));
                        }
                        M[(int) i][(int) j] += weights[(int) (i - 1)];
                        indexToSet.get((i * (w + 1) + j)).add(i);
                        totalWeightToIndex.put(M[(int) i][(int) j], (i * (w + 1) + j));
                    } else { // go to that array get size, see if its bigger than your would be size
                        long tableIndex = totalWeightToIndex.get(M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])] + weights[(int) (i - 1)]);
                        long oldSize = indexToSet.get(tableIndex).size();
                        long newSize = indexToSet.get((i - 1) * (w + 1) + j - weights[(int) (i - 1)]).size() + 1;
                        if (newSize > oldSize) {
                            M[(int) i][(int) j] = M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])];
                            ArrayList<Long> copyingArray = indexToSet.get((i - 1) * (w + 1) + j - weights[(int) (i - 1)]);
                            for (long k = 0; k < copyingArray.size(); k++) {
                                indexToSet.get(i * (w + 1) + j).add(copyingArray.get((int) k));
                            }
                            M[(int) i][(int) j] += weights[(int) (i - 1)];
                            indexToSet.get((i * (w + 1) + j)).add(i);
                            totalWeightToIndex.put(M[(int) i][(int) j], (i * (w + 1) + j));
                        } else {
                            ArrayList<Long> copyingArray = indexToSet.get(tableIndex);
                            for (long k = 0; k < copyingArray.size(); k++) {
                                indexToSet.get(i * (w + 1) + j).add(copyingArray.get((int) k));
                            }
                            M[(int) i][(int) j] += (M[(int) (i - 1)][(int) (j - weights[(int) (i - 1)])] + weights[(int) (i - 1)]);
                            totalWeightToIndex.put(M[(int) i][(int) j], (i * (w + 1) + j));
                        }
                    }
                }
            }
        }
        return indexToSet.get((n + 1) * (w + 1) - 1); // return top right arraylist from table
    }

    // returns all sets that make up a weight
    public static int subsetSumCounting(int n, int w, long[] weights) { // weights was changed to
        int[][] M = new int[n + 1][w + 1];
        HashMap<Integer, ArrayList<HashSet<Integer>>> weightToSets = new HashMap<>(); // weight to ArrayList of different Sets (HashSets)

        for (int i = 0; i <= w; i++) {
            weightToSets.put(i, new ArrayList<>());
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= w; j++) {
                if (j < weights[i - 1] || M[i - 1][j] > (weights[i - 1] + M[i - 1][(int) ((long)j - weights[i - 1])])) {
                    M[i][j] = M[i - 1][j];
                } else {
                    M[i][j] = M[i - 1][(int) (j - weights[i - 1])];
                    M[i][j] += weights[i - 1];
                    boolean includesWeight = false;
                    for (int k = 0; k < weightToSets.get(M[i][j]).size(); k++) {
                        if (weightToSets.get(M[i][j]).get(k).contains(i)) {
                            includesWeight = true;
                        }
                    }
                    if (!includesWeight) {
                        if (M[i - 1][(int) (j - weights[i - 1])] == 0) {
                            HashSet<Integer> newSet = new HashSet<>();
                            newSet.add(i);
                            weightToSets.get(M[i][j]).add(newSet);
                        } else {
                            int numberOfSetsWithWeightWMinusCurrentWeight = weightToSets.get(M[i - 1][(int) (j - weights[i - 1])]).size();
                            for (int k = 0; k < numberOfSetsWithWeightWMinusCurrentWeight; k++) { // iterating through array list
                                HashSet<Integer> newSet = new HashSet<>();
                                HashSet<Integer> onAddedWeightSet = weightToSets.get(M[i - 1][(int) (j - weights[i - 1])]).get(k);
                                if (!onAddedWeightSet.contains(i)) {
                                    for (int value : onAddedWeightSet) { // iterating through hashset
                                        newSet.add(value);
                                    }
                                    newSet.add(i);
                                    weightToSets.get(M[i][j]).add(newSet);
                                }
                            }
                        }
                    }
                }
            }
        }
        ArrayList<HashSet<Integer>> topRightArrayList = weightToSets.get(M[n][w]);
        /*
        System.out.println("found following sets: ...");
        for (HashSet<Integer> set : topRightArrayList) {
            System.out.println("new set: " + set.toString());
            System.out.println();
        }
         */
        return topRightArrayList.size();
    }

    public static int subsetSumCounting3(int n, int w, int[] weights) { // weights was changed to long for question's sake
        int[][] M = new int[n + 1][w + 1];
        HashMap<Integer, Integer> weightToCount = new HashMap<>();

        for (int i = 0; i <= w; i++) {
            weightToCount.put(i, 0);
        }

        for (int i = 1; i <= n; i++) {
            HashSet<Integer> weightAdded = new HashSet<>();
            HashMap<Integer, Integer> tempMap = new HashMap<>();
            for (int j = 0; j <= w; j++) {
                if (j < weights[i - 1] || M[i - 1][j] > (weights[i - 1] + M[i - 1][(j - weights[i - 1])])) {
                    M[i][j] = M[i - 1][j];
                } else { //
                    M[i][j] = M[i - 1][(j - weights[i - 1])];
                    M[i][j] += weights[i - 1];
                    if (!weightAdded.contains(M[i][j])) {
                        if (M[i - 1][(j - weights[i - 1])] == 0) {
                            tempMap.put(M[i][j], 1);
                        } else {
                            int numberOfSetsWithWeightWMinusCurrentWeight = weightToCount.get(M[i - 1][(j - weights[i - 1])]);
                            tempMap.put(M[i][j], numberOfSetsWithWeightWMinusCurrentWeight);
                        }
                        weightAdded.add(M[i][j]);
                    }
                }
            }
            for (int key : tempMap.keySet()) { // key is weight
                weightToCount.put(key, weightToCount.get(key) + tempMap.get(key));
            }
        }
        return weightToCount.get(w); // final count of different sets that sum to w
    }



    public static long subsetSumCounting2(int n, int w, long[] weights) { // weights was changed to long for question's sake
        int[][] M = new int[n + 1][w + 1];
        HashMap<Integer, Long> weightToCount = new HashMap<>();

        for (int i = 0; i <= w; i++) {
            weightToCount.put(i, (long) 0);
        }

        for (int i = 1; i <= n; i++) {
            HashSet<Integer> weightAdded = new HashSet<>();
            HashMap<Integer, Long> tempMap = new HashMap<>();
            for (int j = 0; j <= w; j++) {
                if (j < weights[i - 1] || M[i - 1][j] > (weights[i - 1] + M[i - 1][(int) ((long)j - weights[i - 1])])) {
                    M[i][j] = M[i - 1][j];
                } else { //
                    M[i][j] = M[i - 1][(int) (j - weights[i - 1])];
                    M[i][j] += weights[i - 1];
                    if (!weightAdded.contains(M[i][j])) {
                        if (M[i - 1][(int) (j - weights[i - 1])] == 0) {
                            tempMap.put(M[i][j], (long) 1);
                        } else {
                            long numberOfSetsWithWeightWMinusCurrentWeight = weightToCount.get(M[i - 1][(int) (j - weights[i - 1])]);
                            tempMap.put(M[i][j], numberOfSetsWithWeightWMinusCurrentWeight);
                        }
                        weightAdded.add(M[i][j]);
                    }
                }
            }
            for (int key : tempMap.keySet()) { // key is weight
                weightToCount.put(key, weightToCount.get(key) + tempMap.get(key));
            }
        }
        return weightToCount.get(w); // final count of different sets that sum to w
    }
}
