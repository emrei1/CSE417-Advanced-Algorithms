import java.util.ArrayList;
import java.util.HashSet;

public class Matching {

    public static void main (String[] args) {
        printSample();
        int[][] preferencesM = new int[4][4];
        int[][] preferencesW = new int[4][4];

        preferencesM[0] = new int[]{2,1,3,0};
        preferencesM[1] = new int[]{0,1,3,2};
        preferencesM[2] = new int[]{0,1,3,3};
        preferencesM[3] = new int[]{0,1,2,3};

        preferencesW[0] = new int[]{0,2,1,3};
        preferencesW[1] = new int[]{2,0,3,1};
        preferencesW[2] = new int[]{3,2,1,0};
        preferencesW[3] = new int[]{2,3,1,0};

        executeMatching(preferencesM, preferencesW);
    }

    public static void printSample() {
        System.out.println("yrakhhassan");
    }

    public static void executeMatching(int[][] preferencesM, int[][] preferencesW) {
        ArrayList<WNode> matchedPairList = new ArrayList<>();

        WNode[] tempW = new WNode[preferencesW.length];
        MNode[] tempM = new MNode[preferencesM.length];

        int unassignedSize = preferencesW.length;

        WNode firstWNode = new WNode(0, null, null, null);
        WNode wPointer = firstWNode;
        tempW[0] = firstWNode;
        for (int i = 1; i < preferencesW.length; i++) {
            WNode newW = new WNode(i, tempW[i--], null, null);
            tempW[i] = newW;
            tempW[i--].next = newW;
        }

        MNode firstMNode = new MNode(0, null, null, null);
        MNode mPointer = firstMNode;
        tempM[0] = firstMNode;
        for (int i = 1; i < preferencesM.length; i++) {
            MNode newM = new MNode(i, tempM[i--], null, null);
            tempM[i] = newM;
            tempM[i--].next = newM;
        }

        while (wPointer != null) {
            WNode unassignedW = wPointer;

            for (int i = 0; i < unassignedSize; i++) {

            }


            /*
            for (int i = 0; i < remainingW.size(); i++) {

            }

             */
        }
    }

    private static class WNode {
        public int index;
        public WNode prev;
        public WNode next;
        public MNode pair;
        public WNode(int index, WNode prev, WNode next, MNode pair) {
            this.index = index;
            this.prev = prev;
            this.next = next;
            this.pair = pair;
        }
    }

    private static class MNode {
        public int index;
        public MNode prev;
        public MNode next;
        public WNode pair;
        public MNode (int index, MNode prev, MNode next, WNode pair) {
            this.index = index;
            this.prev = prev;
            this.next = next;
            this.pair = pair;
        }
    }

    private static class MatchingNode {
        int index;
        MatchingNode next;

        public MatchingNode(int index, MatchingNode pair) {
            this.index = index;
            this.next = next;
        }
    }
}
