import java.util.*;

// Homework 8 - Problem 4
public class MaximumFlowNodeCapacities {

    public static void main(String[] args) {
        HashMap<Character, HashSet<ResidualGraphEdge>> graph = getFixedResidualGraph2();
        // method works with both graph input and residual graph input
        int maximumFlow = getMaximumFlowNodeCapacitied(graph);
        System.out.println("maximum flow was found to be " + maximumFlow);
    }

    // startingNode and endingNode parameters need to be changed to character type
    public static int getMaximumFlowNodeCapacitied (HashMap<Character, HashSet<ResidualGraphEdge>> residualGraph) {
        HashMap<ResidualGraphEdge, ResidualGraphEdge> reverseResidualEdgeMapper = new HashMap<>();
        HashSet<ResidualGraphEdge> alreadyIteratedEdgesPreProcessing = new HashSet<>();
        for (Character node : residualGraph.keySet()) {
            for (ResidualGraphEdge edge : residualGraph.get(node)) { // start is node (Character)
                if (!alreadyIteratedEdgesPreProcessing.contains(edge)) {
                    char neighbor = edge.end.name; // node neighbor letter
                    HashSet<ResidualGraphEdge> neighborEdges = residualGraph.get(neighbor);
                    boolean foundReverseEdge = false;
                    for (ResidualGraphEdge possibleReverseEdge : neighborEdges) {
                        if (possibleReverseEdge.end.name == node) { // found reverse edge - possibleReverseEdge
                            reverseResidualEdgeMapper.put(edge, possibleReverseEdge);
                            reverseResidualEdgeMapper.put(possibleReverseEdge, edge);
                            alreadyIteratedEdgesPreProcessing.add(possibleReverseEdge);
                            foundReverseEdge = true;
                            break;
                        }
                    }
                    if (!foundReverseEdge) {
                        reverseResidualEdgeMapper.put(edge, null);
                    }
                    alreadyIteratedEdgesPreProcessing.add(edge);
                }
            }
        }
        ArrayList<ResidualGraphEdge> path = bfsFromNode('s', 't', residualGraph);
        // no nodes should be overflowing before algorithm --> this is done in the step making the first residual graph
        HashMap<Character, Integer> nodeCurrentCapacity = new HashMap<>(); // node --> current capacity
        // initial filled capacities of nodes are sum of their backward edge capacities
        for (char node : residualGraph.keySet()) {
            int nodeFilledCapacity = 0;
            for (ResidualGraphEdge edge : residualGraph.get(node)) {
                if (edge.isBackward) {
                    nodeFilledCapacity += edge.rEdgeCapacity;
                }
            }
            nodeCurrentCapacity.put(node, nodeFilledCapacity);
        }
        while (path != null) {
            // compute bottleneck distance
            int bottleNeckCapacity = (int)Double.POSITIVE_INFINITY;
            for (ResidualGraphEdge edge : path) {
                if (edge.rEdgeCapacity < bottleNeckCapacity) {
                    bottleNeckCapacity = edge.rEdgeCapacity;
                }
            }
            // first decide how much the bottleneck should be to make sure there are no node overflows
            HashMap<ResidualGraphNode, Integer> nodeToAddedCapacity = new HashMap<>();
            for (ResidualGraphEdge edge : path) {
                if (edge.isBackward) {
                    if (nodeToAddedCapacity.containsKey(edge.start)) {
                        nodeToAddedCapacity.put(edge.start, nodeToAddedCapacity.get(edge.start) - bottleNeckCapacity);
                    } else {
                        nodeToAddedCapacity.put(edge.start, - bottleNeckCapacity);
                    }
                } else { // front edge
                    if (nodeToAddedCapacity.containsKey(edge.end.name)) {
                        nodeToAddedCapacity.put(edge.end, nodeToAddedCapacity.get(edge.end) + bottleNeckCapacity);
                    } else {
                        nodeToAddedCapacity.put(edge.end, bottleNeckCapacity);
                    }
                }
            }
            // mapped per node how much will be added
            int maximumOverFlow = 0;
            HashMap<Integer, HashSet<ResidualGraphNode>> overflowToNode = new HashMap<>();
            for (ResidualGraphNode node : nodeToAddedCapacity.keySet()) {
                int actualNodeCapacity = node.rNodeCapacity;
                int currentCapacity = nodeCurrentCapacity.get(node.name);
                int addedCapacity = nodeToAddedCapacity.get(node);
                int newCapacity = currentCapacity + addedCapacity;
                if (newCapacity > actualNodeCapacity) { // there is overflow
                    int overflow = newCapacity - actualNodeCapacity;
                    if (!overflowToNode.containsKey(overflow)) {
                        overflowToNode.put(overflow, new HashSet<>());
                        overflowToNode.get(overflow).add(node);
                    } else {
                        overflowToNode.get(overflow).add(node);
                    }
                    if (maximumOverFlow < overflow) {
                        maximumOverFlow = overflow;
                    }
                }
            }
            HashSet<ResidualGraphNode> capacityFilledNodes = new HashSet<>();
            if (!overflowToNode.isEmpty()) {
                capacityFilledNodes = overflowToNode.get(maximumOverFlow);
                bottleNeckCapacity = bottleNeckCapacity - maximumOverFlow;
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
                        residualGraph.get(edge.end.name).add(newReverseEdge);
                    }
                    if (edge.rEdgeCapacity == 0) {
                        reverseEdge = reverseResidualEdgeMapper.get(edge);
                        reverseResidualEdgeMapper.remove(edge);
                        reverseResidualEdgeMapper.put(reverseEdge, null);
                        residualGraph.get(edge.start.name).remove(edge);
                    }
                    nodeCurrentCapacity.put(edge.start.name, nodeCurrentCapacity.get(edge.start.name) - bottleNeckCapacity);
                } else { // edge is front
                    if (!capacityFilledNodes.contains(edge.end)) {
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
                            residualGraph.get(edge.end.name).add(newReverseEdge);
                        }
                        if (edge.rEdgeCapacity == 0) {
                            reverseEdge = reverseResidualEdgeMapper.get(edge);
                            reverseResidualEdgeMapper.remove(edge);
                            reverseResidualEdgeMapper.put(reverseEdge, null);
                            residualGraph.get(edge.start.name).remove(edge);
                        }
                    } else {
                        ResidualGraphEdge reverseEdge = reverseResidualEdgeMapper.get(edge);
                        if (reverseEdge != null) {
                            int reverseCapacity = reverseEdge.rEdgeCapacity;
                            int newReverseCapacity = reverseCapacity + bottleNeckCapacity;
                            reverseEdge.rEdgeCapacity = newReverseCapacity;
                            reverseResidualEdgeMapper.remove(edge);
                            reverseResidualEdgeMapper.put(reverseEdge, null);
                            residualGraph.get(edge.start.name).remove(edge);
                        } else {
                            ResidualGraphEdge newReverseEdge = new ResidualGraphEdge(bottleNeckCapacity, edge.end, edge.start, true);
                            reverseResidualEdgeMapper.put(newReverseEdge, null);
                            residualGraph.get(edge.end.name).add(newReverseEdge);
                            residualGraph.get(edge.start.name).remove(edge);
                        }
                    }
                    nodeCurrentCapacity.put(edge.end.name, nodeCurrentCapacity.get(edge.end.name) + bottleNeckCapacity);
                }
            }
            /*
            System.out.println("updated residual graph after this while loop iteration: ");
            for (char node : residualGraph.keySet()) {
                for (ResidualGraphEdge edge : residualGraph.get(node)) {
                    if (edge.isBackward) {
                        System.out.println("backward edge:   " + edge.start.name + "-" + edge.end.name + " (" + edge.rEdgeCapacity + ")");
                    } else {
                        System.out.println("front edge:    " + edge.start.name + "-" + edge.end.name + " (" + edge.rEdgeCapacity + ")");
                    }
                }
                System.out.println();
            }
            */
            // update new bottleneck to overflow - bottleneck and make changes
            path = bfsFromNode('s', 't', residualGraph);
        }

        int maxCapacity = 0;
        // max flow is the sum of capacities of all back-edges to starting node s
        for (char node : residualGraph.keySet()) {
            for (ResidualGraphEdge edge : residualGraph.get(node)) { // all edges that start from this node
                if (edge.isBackward && edge.end.name == 's') {
                    maxCapacity += edge.rEdgeCapacity;
                }
            }
        }
        return maxCapacity;
    }

    public static ArrayList<ResidualGraphEdge> bfsFromNode(char startingNode, char endingNode, HashMap<Character, HashSet<ResidualGraphEdge>> residualGraph) {
        ArrayDeque<Character> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.push(startingNode);
        HashMap<Character, Character> aboveNodeToBelowNode = new HashMap<>();
        HashSet<Character> alreadyVisitedNodes = new HashSet<>();
        alreadyVisitedNodes.add(startingNode);
        while (!nodesToVisit.isEmpty()) {
            char newNode = nodesToVisit.pop();
            if (newNode == endingNode) {
                break;
            }
            for (ResidualGraphEdge attachedEdge : residualGraph.get(newNode)) {
                char neighbor = attachedEdge.end.name;
                if (!alreadyVisitedNodes.contains(neighbor)) {
                    alreadyVisitedNodes.add(neighbor);
                    nodesToVisit.push(neighbor);
                    aboveNodeToBelowNode.put(neighbor, newNode);
                }
            }
        }
        ArrayList<ResidualGraphEdge> path = new ArrayList<>();
        if (aboveNodeToBelowNode.containsKey('t')) {
            Stack<ResidualGraphEdge> storeEdges = new Stack<>();
            ArrayDeque<Character> nodeToAddToPath = new ArrayDeque<>();
            nodeToAddToPath.add('t');
            while (nodeToAddToPath.peek() != 's') {
                char aboveNode = nodeToAddToPath.pop();
                char belowNode = aboveNodeToBelowNode.get(aboveNode);
                for (ResidualGraphEdge edge : residualGraph.get(belowNode)) {
                    if (edge.start.name == belowNode && edge.end.name == aboveNode) {
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

    // atomic residual graph starting instance for the graph that was built above in main
    public static HashMap<Character, HashSet<ResidualGraphEdge>> getFixedResidualGraph() {
        HashMap<Character, HashSet<ResidualGraphEdge>> residualGraph = new HashMap<>();
        ResidualGraphNode rNodeS = new ResidualGraphNode('s', (int)Double.NEGATIVE_INFINITY);
        ResidualGraphNode rNodeU = new ResidualGraphNode('u', 20);
        ResidualGraphNode rNodeV = new ResidualGraphNode('v', 22);
        ResidualGraphNode rNodeT = new ResidualGraphNode('t', 25);
        ResidualGraphEdge forwardStoV = new ResidualGraphEdge(10, rNodeS, rNodeV, false);
        ResidualGraphEdge backwardUtoS = new ResidualGraphEdge(20, rNodeU, rNodeS, true);
        ResidualGraphEdge backwardVtoU = new ResidualGraphEdge(20, rNodeV, rNodeU, true);
        ResidualGraphEdge forwardUtoV = new ResidualGraphEdge(10, rNodeU, rNodeV, false);
        ResidualGraphEdge forwardUtoT = new ResidualGraphEdge(10, rNodeU, rNodeT, false);
        ResidualGraphEdge backwardTtoV = new ResidualGraphEdge(20, rNodeT, rNodeV, true);
        residualGraph.put('s', new HashSet<>());
        residualGraph.put('u', new HashSet<>());
        residualGraph.put('v', new HashSet<>());
        residualGraph.put('t', new HashSet<>());
        residualGraph.get('s').add(forwardStoV);
        residualGraph.get('u').add(backwardUtoS);
        residualGraph.get('u').add(forwardUtoV);
        residualGraph.get('u').add(forwardUtoT);
        residualGraph.get('v').add(backwardVtoU);
        residualGraph.get('t').add(backwardTtoV);
        return residualGraph;
    }
    public static HashMap<Character, HashSet<ResidualGraphEdge>> getFixedResidualGraph2() {
        HashMap<Character, HashSet<ResidualGraphEdge>> residualGraph = new HashMap<>();
        ResidualGraphNode rNodeS = new ResidualGraphNode('s', (int)Double.NEGATIVE_INFINITY);
        ResidualGraphNode rNodeU = new ResidualGraphNode('u', 20);
        ResidualGraphNode rNodeV = new ResidualGraphNode('v', 22);
        ResidualGraphNode rNodeT = new ResidualGraphNode('t', 55);
        ResidualGraphNode rNodeE = new ResidualGraphNode('e', 30);
        ResidualGraphNode rNodeZ = new ResidualGraphNode('z', 20);
        ResidualGraphNode rNodeR = new ResidualGraphNode('r', 30);
        ResidualGraphEdge forwardStoV = new ResidualGraphEdge(10, rNodeS, rNodeV, false);
        ResidualGraphEdge forwardStoU = new ResidualGraphEdge(20, rNodeS, rNodeU, false);
        ResidualGraphEdge forwardUtoV = new ResidualGraphEdge(30, rNodeU, rNodeV, false);
        ResidualGraphEdge forwardUtoT = new ResidualGraphEdge(10, rNodeU, rNodeT, false);
        ResidualGraphEdge forwardVtoT = new ResidualGraphEdge(20, rNodeV, rNodeT, false);
        ResidualGraphEdge forwardStoE = new ResidualGraphEdge(10, rNodeS, rNodeE, false);
        ResidualGraphEdge forwardStoZ = new ResidualGraphEdge(20, rNodeS, rNodeZ, false);
        ResidualGraphEdge forwardZtoE = new ResidualGraphEdge(20, rNodeZ, rNodeE, false);
        ResidualGraphEdge forwardZtoR = new ResidualGraphEdge(10, rNodeZ, rNodeR, false);
        ResidualGraphEdge forwardEtoR = new ResidualGraphEdge(20, rNodeE, rNodeR, false);
        ResidualGraphEdge forwardRtoT = new ResidualGraphEdge(30, rNodeR, rNodeT, false);
        residualGraph.put('s', new HashSet<>());
        residualGraph.put('u', new HashSet<>());
        residualGraph.put('v', new HashSet<>());
        residualGraph.put('t', new HashSet<>());
        residualGraph.put('e', new HashSet<>());
        residualGraph.put('z', new HashSet<>());
        residualGraph.put('r', new HashSet<>());
        residualGraph.get('s').add(forwardStoV);
        residualGraph.get('s').add(forwardStoU);
        residualGraph.get('u').add(forwardUtoV);
        residualGraph.get('u').add(forwardUtoT);
        residualGraph.get('v').add(forwardVtoT);
        residualGraph.get('s').add(forwardStoE);
        residualGraph.get('s').add(forwardStoZ);
        residualGraph.get('z').add(forwardZtoE);
        residualGraph.get('z').add(forwardZtoR);
        residualGraph.get('e').add(forwardEtoR);
        residualGraph.get('r').add(forwardRtoT);
        return residualGraph;
    }

    private static class ResidualGraphNode {
        private char name;
        private int rNodeCapacity;
        public ResidualGraphNode(char name, int rNodeCapacity) {
            this.name = name;
            this.rNodeCapacity = rNodeCapacity;
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
