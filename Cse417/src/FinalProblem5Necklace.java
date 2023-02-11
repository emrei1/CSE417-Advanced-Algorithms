import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FinalProblem5Necklace {

    public static void main(String[] args) {

        ArrayList<NecklaceNode> necklace = new ArrayList<>();

        NecklaceNode necklace0 = new NecklaceNode(1, 0);
        necklace.add(necklace0);
        NecklaceNode necklace1 = new NecklaceNode(2, 1);
        necklace.add(necklace1);
        NecklaceNode necklace2 = new NecklaceNode(3, 2);
        necklace.add(necklace2);
        NecklaceNode necklace3 = new NecklaceNode(1, 3);
        necklace.add(necklace3);
        NecklaceNode necklace4 = new NecklaceNode(1, 4);
        necklace.add(necklace4);
        NecklaceNode necklace5 = new NecklaceNode(1, 5);
        necklace.add(necklace5);
        NecklaceNode necklace6 = new NecklaceNode(3, 6);
        necklace.add(necklace6);

        ArrayList<Integer> c1Rule1 = new ArrayList<>();
        c1Rule1.add(1);
        c1Rule1.add(1);
        c1Rule1.add(3);

        ArrayList<Integer> c1Rule2 = new ArrayList<>();
        c1Rule2.add(1);
        c1Rule2.add(2);

        ArrayList<Integer> c2Rule1 = new ArrayList<>();
        c2Rule1.add(2);
        c2Rule1.add(3);
        c2Rule1.add(1);
        c2Rule1.add(1);

        //
        ArrayList<Integer> c2Rule2 = new ArrayList<>();
        c2Rule2.add(2);
        c2Rule2.add(3);
        //

        ArrayList<Integer> c3Rule1 = new ArrayList<>();
        c3Rule1.add(3);
        c3Rule1.add(3);
        c3Rule1.add(3);

        //
        ArrayList<Integer> c3Rule2 = new ArrayList<>();
        c3Rule2.add(3);
        c3Rule2.add(1);
        c3Rule2.add(1);
        //

        HashMap<Integer, HashSet<ArrayList<Integer>>> cToRules = new HashMap<>();

        cToRules.put(1, new HashSet<>());
        cToRules.put(2, new HashSet<>());
        cToRules.put(3, new HashSet<>());

        cToRules.get(1).add(c1Rule1);
        cToRules.get(1).add(c1Rule2);
        cToRules.get(2).add(c2Rule1);
        cToRules.get(3).add(c3Rule1);
        cToRules.get(2).add(c2Rule2);
        cToRules.get(3).add(c3Rule2);

        ArrayDeque<NecklaceNode> q = new ArrayDeque<>();
        q.add(necklace.get(0));

        //
        // run either a) or b) at once
        //

        /*
        // a)
        while (!q.isEmpty()) {
            NecklaceNode nodeToCheck = q.pop();
            HashSet<ArrayList<Integer>> listOfRules = cToRules.get(nodeToCheck.type);
            for (ArrayList<Integer> list : listOfRules) {
                boolean passed = true;
                int willBeSize = nodeToCheck.necklaceIndex + list.size() - 1;
                if (willBeSize <= necklace.size() - 1) {
                    for (int i = 0; i < list.size(); i++) {
                        if (necklace.get(i + nodeToCheck.necklaceIndex).type != list.get(i)) {
                            passed = false;
                        }
                    }
                    if (passed == true) {
                        NecklaceNode nodeToBeAdded = necklace.get(nodeToCheck.necklaceIndex + list.size() - 1);
                        if (nodeToBeAdded == necklace.get(necklace.size() - 1)) {
                            // returns true
                            System.out.println("all nodes are legit in this necklace !!!");
                        } else {
                            q.add(nodeToBeAdded);
                        }
                    }
                }
                System.out.println();
            }
        }

         */

        // /*
        // b --> same algorithm above with slight difference
        HashMap<NecklaceNode, HashSet<ArrayList<Integer>>> nodeToSetOfRules = new HashMap<>();
        HashSet<ArrayList<Integer>> firstSet = new HashSet<>();
        ArrayList<Integer> firstRules = new ArrayList<>();
        firstSet.add(firstRules);
        nodeToSetOfRules.put(necklace.get(0), firstSet);
        while (!q.isEmpty()) {
            NecklaceNode nodeToCheck = q.pop();
            HashSet<ArrayList<Integer>> listOfRules = cToRules.get(nodeToCheck.type);
            HashSet<ArrayList<Integer>> rulesUntilThisNode = nodeToSetOfRules.get(nodeToCheck);
            for (ArrayList<Integer> list : listOfRules) {
                boolean passed = true;
                int willBeSize = nodeToCheck.necklaceIndex + list.size() - 1;
                if (willBeSize <= necklace.size() - 1) {
                    for (int i = 0; i < list.size(); i++) {
                        if (necklace.get(i + nodeToCheck.necklaceIndex).type != list.get(i)) {
                            passed = false;
                        }
                    }
                    if (passed == true) {
                        NecklaceNode nodeToBeAdded = necklace.get(nodeToCheck.necklaceIndex + list.size() - 1);
                        HashSet<ArrayList<Integer>> replicaSet = new HashSet<>();
                        for (ArrayList<Integer> rules : rulesUntilThisNode) {
                            replicaSet.add(rules);
                        }
                        replicaSet.add(list);
                        nodeToSetOfRules.put(nodeToBeAdded, replicaSet);
                        if (nodeToBeAdded == necklace.get(necklace.size() - 1)) {
                            // returns set of rules
                            System.out.println("all nodes are legit in this necklace !!!");
                            System.out.println("set of rules contained in the necklace are as follows: ");
                            for (ArrayList<Integer> rule : nodeToSetOfRules.get(necklace6)) {
                                System.out.println("rule: " + rule.toString());
                            }
                        } else {
                            q.add(nodeToBeAdded);
                        }
                    }
                }
            }
        }

       //  */
    }

    private static class NecklaceNode {
        int type;
        int necklaceIndex;

        public NecklaceNode(int type, int necklaceIndex) {
            this.type = type;
            this.necklaceIndex = necklaceIndex;
        }
    }
}
