import java.util.*;

// 417 Homework 9 - Problem 1
public class NetworkFlowVirusOutbreak {

    public static void main(String[] args) {
         /*
        // Sample Set 1 of Parameters
        // IMPORTANT: it is assumed that person indeces start from 0 to n - 1.
        int[] persons = {0, 1, 2, 3};
        // IMPORTANT: it is assumed that hospital indeces start from 0 to k -1.
        int[] hospitals = {0, 1};
        HashMap<Integer, ArrayList<Integer>> personsToCompatibleHospitals = new HashMap<>();
        // sample key/value pairs for compatible hospitals. For person --> list of hospitals that are 30 minute away from him/her
        personsToCompatibleHospitals.put(0, new ArrayList<>());
        personsToCompatibleHospitals.put(1, new ArrayList());
        personsToCompatibleHospitals.put(2, new ArrayList<>());
        personsToCompatibleHospitals.put(3, new ArrayList<>());
        //
        personsToCompatibleHospitals.get(0).add(0);
        personsToCompatibleHospitals.get(0).add(1);
        personsToCompatibleHospitals.get(1).add(1);
        personsToCompatibleHospitals.get(2).add(1);
        personsToCompatibleHospitals.get(3).add(1);
         */
        // /*
        // Sample Set 2 of Parameters
        // IMPORTANT: it is assumed that person indeces start from 0 to n - 1.
        int[] persons = {0, 1, 2, 3, 4, 5};
        // IMPORTANT: it is assumed that hospital indeces start from 0 to k -1.
        int[] hospitals = {0, 1, 2};
        HashMap<Integer, ArrayList<Integer>> personsToCompatibleHospitals = new HashMap<>();
        // sample key/value pairs for compatible hospitals. For person --> list of hospitals that are 30 minute away from him/her
        personsToCompatibleHospitals.put(0, new ArrayList<>());
        personsToCompatibleHospitals.put(1, new ArrayList());
        personsToCompatibleHospitals.put(2, new ArrayList<>());
        personsToCompatibleHospitals.put(3, new ArrayList<>());
        personsToCompatibleHospitals.put(4, new ArrayList<>());
        personsToCompatibleHospitals.put(5, new ArrayList<>());
        //
        personsToCompatibleHospitals.get(0).add(0);
        personsToCompatibleHospitals.get(0).add(2);
        personsToCompatibleHospitals.get(1).add(0);
        personsToCompatibleHospitals.get(1).add(1);
        personsToCompatibleHospitals.get(2).add(2);
        personsToCompatibleHospitals.get(3).add(1);
        personsToCompatibleHospitals.get(3).add(2);
        personsToCompatibleHospitals.get(4).add(2);
        personsToCompatibleHospitals.get(5).add(0);
        // */

        boolean isBalancedDistributionPossible = isBalancedDistributionToHospitalsPossible(persons, hospitals, personsToCompatibleHospitals);

        if (isBalancedDistributionPossible) {
            System.out.println("balanced distribution IS possible with a hospital assigned for each of the sick people");
        } else {
            System.out.println("balanced distribution IS NOT possible with a hospital assigned for each of the sick people");
        }
    }

    private static boolean isBalancedDistributionToHospitalsPossible(int[] persons, int[] hospitals, HashMap<Integer, ArrayList<Integer>> personsToCompatibleHospitals) {
        int startingNodeSIndex = (int)Double.NEGATIVE_INFINITY;
        int endingNodeTIndex = (int)Double.POSITIVE_INFINITY;

        HashMap<Integer, HashSet<ResidualGraphEdge>> residualGraph = getResidualGraph(startingNodeSIndex, endingNodeTIndex, persons, hospitals, personsToCompatibleHospitals);

        HashMap<ResidualGraphEdge, ResidualGraphEdge> reverseResidualEdgeMapper = new HashMap<>();
        HashSet<ResidualGraphEdge> alreadyIteratedEdgesPreProcessing = new HashSet<>();

        // al edges in initial graph will be front edges therefore won't contain any backward edges
        for (int node : residualGraph.keySet()) {
            for (ResidualGraphEdge edge : residualGraph.get(node)) {
                if (!alreadyIteratedEdgesPreProcessing.contains(edge)) {
                    reverseResidualEdgeMapper.put(edge, null);
                }
            }
        }

        ArrayList<ResidualGraphEdge> path = bfsFromNode(startingNodeSIndex, endingNodeTIndex, residualGraph);
        while (path != null) {
            // compute bottleneck distance
            int bottleNeckCapacity = (int)Double.POSITIVE_INFINITY;
            for (ResidualGraphEdge edge : path) {
                if (edge.rEdgeCapacity < bottleNeckCapacity) {
                    bottleNeckCapacity = edge.rEdgeCapacity;
                }
            }
            for (ResidualGraphEdge edge : path) {
                if (edge.isBackward) { // edge is backward
                    ResidualGraphEdge reverseEdge = reverseResidualEdgeMapper.get(edge);
                    int backwardCapacity = edge.rEdgeCapacity;
                    int newBackwardCapacity = backwardCapacity - bottleNeckCapacity;
                    edge.rEdgeCapacity = newBackwardCapacity;
                    if (reverseEdge != null) { // has reverse front edge
                        int reverseCapacity = reverseEdge.rEdgeCapacity;
                        int newReverseCapacity = reverseCapacity + bottleNeckCapacity;
                        reverseEdge.rEdgeCapacity = newReverseCapacity;
                    } else { // has no reverse front edge
                        ResidualGraphEdge newReverseEdge = new ResidualGraphEdge(bottleNeckCapacity, edge.end, edge.start, false);
                        reverseResidualEdgeMapper.put(edge, newReverseEdge);
                        reverseResidualEdgeMapper.put(newReverseEdge, edge);
                        residualGraph.get(edge.end.index).add(newReverseEdge);
                    }
                    if (edge.rEdgeCapacity == 0) {
                        reverseEdge = reverseResidualEdgeMapper.get(edge);
                        reverseResidualEdgeMapper.remove(edge);
                        reverseResidualEdgeMapper.put(reverseEdge, null);
                        residualGraph.get(edge.start.index).remove(edge);
                    }
                } else { // edge is front
                    ResidualGraphEdge reverseEdge = reverseResidualEdgeMapper.get(edge);
                    int frontCapacity = edge.rEdgeCapacity;
                    int newFrontCapacity = frontCapacity - bottleNeckCapacity;
                    edge.rEdgeCapacity = newFrontCapacity;
                    if (reverseEdge != null) {
                        int reverseCapacity = reverseEdge.rEdgeCapacity;
                        int newReverseCapacity = reverseCapacity + bottleNeckCapacity;
                        reverseEdge.rEdgeCapacity = newReverseCapacity;
                    } else {
                        ResidualGraphEdge newReverseEdge = new ResidualGraphEdge(bottleNeckCapacity, edge.end, edge.start, true);
                        reverseResidualEdgeMapper.put(edge, newReverseEdge);
                        reverseResidualEdgeMapper.put(newReverseEdge, edge);
                        residualGraph.get(edge.end.index).add(newReverseEdge);
                    }
                    if (edge.rEdgeCapacity == 0) {
                        reverseEdge = reverseResidualEdgeMapper.get(edge);
                        reverseResidualEdgeMapper.remove(edge);
                        reverseResidualEdgeMapper.put(reverseEdge, null);
                        residualGraph.get(edge.start.index).remove(edge);
                    }
                }
            }
            //
            /*
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("updated residual graph after this while loop iteration: ");
            for (int node : residualGraph.keySet()) {
                for (ResidualGraphEdge edge : residualGraph.get(node)) {
                    if (edge.isBackward) {
                        System.out.println("backward edge:   " + edge.start.index + "-" + edge.end.index + " (" + edge.rEdgeCapacity + ")");
                    } else {
                        System.out.println("front edge:    " + edge.start.index + "-" + edge.end.index + " (" + edge.rEdgeCapacity + ")");
                    }
                }
                System.out.println();
            }
            */
            //
            // update new bottleneck to overflow - bottleneck and make changes
            path = bfsFromNode(startingNodeSIndex, endingNodeTIndex, residualGraph);
        }

        for (int node : residualGraph.keySet()) {
            if (node >= persons.length && node < (int)Double.POSITIVE_INFINITY) {
                System.out.println("persons who are assigned to hospital " + (node - persons.length));
                for (ResidualGraphEdge edge : residualGraph.get(node)) {
                    if (edge.isBackward) {
                        System.out.println(edge.end.index);
                    }
                }
                System.out.println();
            }
        }

        int maxFlow = 0;
        // max flow is the sum of capacities of all back-edges to starting node s
        for (int node : residualGraph.keySet()) {
            for (ResidualGraphEdge edge : residualGraph.get(node)) { // all edges that start from this node
                if (edge.isBackward && edge.end.index == startingNodeSIndex) {
                    maxFlow += edge.rEdgeCapacity;
                }
            }
        }
        if (maxFlow == persons.length) {
            return true;
        }
        return false;
    }

    public static ArrayList<ResidualGraphEdge> bfsFromNode(int startingNodeIndex, int endingNodeIndex, HashMap<Integer, HashSet<ResidualGraphEdge>> residualGraph) {
        ArrayDeque<Integer> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.push(startingNodeIndex);
        HashMap<Integer, Integer> aboveNodeToBelowNode = new HashMap<>();
        HashSet<Integer> alreadyVisitedNodes = new HashSet<>();
        alreadyVisitedNodes.add(startingNodeIndex);
        while (!nodesToVisit.isEmpty()) {
            int newNode = nodesToVisit.pop();
            if (newNode == endingNodeIndex) {
                break;
            }
            for (ResidualGraphEdge attachedEdge : residualGraph.get(newNode)) {
                int neighbor = attachedEdge.end.index;
                if (!alreadyVisitedNodes.contains(neighbor)) {
                    alreadyVisitedNodes.add(neighbor);
                    nodesToVisit.push(neighbor);
                    aboveNodeToBelowNode.put(neighbor, newNode);
                }
            }
        }
        ArrayList<ResidualGraphEdge> path = new ArrayList<>();
        if (aboveNodeToBelowNode.containsKey(endingNodeIndex)) {
            Stack<ResidualGraphEdge> storeEdges = new Stack<>();
            ArrayDeque<Integer> nodeToAddToPath = new ArrayDeque<>();
            nodeToAddToPath.add(endingNodeIndex);
            while (nodeToAddToPath.peek() != startingNodeIndex) {
                int aboveNode = nodeToAddToPath.pop();
                int belowNode = aboveNodeToBelowNode.get(aboveNode);
                for (ResidualGraphEdge edge : residualGraph.get(belowNode)) {
                    if (edge.start.index == belowNode && edge.end.index == aboveNode) {
                        storeEdges.push(edge);
                    }
                }
                nodeToAddToPath.add(belowNode);
            }
            int size = storeEdges.size();
            for (int i = 0; i < size; i++) {
                path.add(storeEdges.pop());
            }
            return path;
        }
        return null;
    }

    private static HashMap<Integer, HashSet<ResidualGraphEdge>> getResidualGraph(int startingNodeSIndex, int endingNodeTIndex, int[] persons, int[] hospitals, HashMap<Integer, ArrayList<Integer>> personsToCompatibleHospitals) {

        int n = persons.length;
        int k = hospitals.length;
        int hospitalsStartingIndex = n; // since there are n - 1 persons.
        int perHospitalLimit = n / k;

        HashMap<Integer, HashSet<ResidualGraphEdge>> returnedResidualGraph = new HashMap<>();

        ResidualGraphNode startingNodeS = new ResidualGraphNode(startingNodeSIndex);
        ResidualGraphNode endingNodeT = new ResidualGraphNode(endingNodeTIndex);

        HashMap<Integer, ResidualGraphNode> personOrHospitalToItsNode = new HashMap<>();

        // put s and t into graph
        returnedResidualGraph.put(startingNodeSIndex, new HashSet<>());
        returnedResidualGraph.put(endingNodeTIndex, new HashSet<>());

        // Map S to Persons. s-p(i) capacities are 1 since one person can be assigned to only one hospital
        for (int i = 0; i < n; i++) {
            ResidualGraphNode thisPerson = new ResidualGraphNode(i); // essentially putting persons[i] or i here will indicate the same reference since for convenience all nodes are indexed
            personOrHospitalToItsNode.put(i, thisPerson);
            ResidualGraphEdge startToPersonEdge = new ResidualGraphEdge(1, startingNodeS, thisPerson, false);
            returnedResidualGraph.get(startingNodeSIndex).add(startToPersonEdge);
            // make new domain of available hospitals for this person
            returnedResidualGraph.put(i, new HashSet<>());
        }

        // hospital --> t. All hospitals are mapped to t with edge capacities of n / k.
        for (int i = 0; i < k; i++) {
            int hospitalIndex = hospitalsStartingIndex + i;
            ResidualGraphNode thisHospitalNode = new ResidualGraphNode(hospitalIndex);
            personOrHospitalToItsNode.put(hospitalIndex, thisHospitalNode);
            returnedResidualGraph.put(hospitalIndex, new HashSet<>());
            ResidualGraphEdge thisHospitalToT = new ResidualGraphEdge((perHospitalLimit), thisHospitalNode, endingNodeT, false);
            returnedResidualGraph.get(hospitalIndex).add(thisHospitalToT);
        }

        // map persons to hospitals that are within a half-hour driving time from the person
        for (int i = 0; i < n; i++) {
            ResidualGraphNode thisPersonNode = personOrHospitalToItsNode.get(i);
            for (int j = 0; j < personsToCompatibleHospitals.get(i).size(); j++) {
                int hospitalIndex = personsToCompatibleHospitals.get(i).get(j);
                int hospitalPlusStartingIndex = hospitalIndex + hospitalsStartingIndex;
                ResidualGraphNode thisHospitalNode = personOrHospitalToItsNode.get(hospitalPlusStartingIndex);
                ResidualGraphEdge personToHospitalEdge = new ResidualGraphEdge(1, thisPersonNode, thisHospitalNode, false);
                returnedResidualGraph.get(i).add(personToHospitalEdge);
            }
        }

        /*
        for (int nodeIndex : returnedResidualGraph.keySet()) {
            System.out.println("printing edges in domain of node index " + nodeIndex);
            for (ResidualGraphEdge edge : returnedResidualGraph.get(nodeIndex)) {
                System.out.println("edge " + edge.start.index + "-" + edge.end.index);
            }
        }
         */
        return returnedResidualGraph;
    }

    private static class ResidualGraphNode {
        private int index;
        public ResidualGraphNode(int personIndex) {
            this.index = personIndex;
        }
    }
    private static class ResidualGraphEdge {
        private int rEdgeCapacity;
        private ResidualGraphNode start;
        private ResidualGraphNode end;
        private boolean isBackward;
        public ResidualGraphEdge(int rEdgeCapacity, ResidualGraphNode start, ResidualGraphNode end, boolean isBackward) {
            this.rEdgeCapacity = rEdgeCapacity;
            this.start = start;
            this.end = end;
            this.isBackward = isBackward;
        }
    }
}
