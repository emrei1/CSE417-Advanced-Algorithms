import java.util.*;

// Homework 9 - Problem 2
public class TradersConvertRupees {

    public static void main(String[] args) {

        // Sample set 1 of parameters
        int[] traders = {0, 1, 2}; // i = trader index
        int[] currencies = {0, 1, 2}; // j = currency index
        int[] traderRupees = {2000, 1000, 1500}; // i = trader index
        //
        int[] trader0WillingToPayAmountForCurrencyJ = {1500, 750, 400}; // j = currency index
        int[] trader1WillingToPayAmountForCurrencyJ = {500, 600, 1000}; // j = currency index
        int[] trader2WillingToPayAmountForCurrencyJ = {750, 800, 1200}; // j = currency index
        //
        int[][] traderToWillingToPayAmountsForCurrencyJ = new int[traders.length][currencies.length];
        traderToWillingToPayAmountsForCurrencyJ[0] = trader0WillingToPayAmountForCurrencyJ;
        traderToWillingToPayAmountsForCurrencyJ[1] = trader1WillingToPayAmountForCurrencyJ;
        traderToWillingToPayAmountsForCurrencyJ[2] = trader2WillingToPayAmountForCurrencyJ;
        //
        int[] bankCurrencyToAmountAvailableInRupees = new int[currencies.length];
        bankCurrencyToAmountAvailableInRupees[0] = 2500;
        bankCurrencyToAmountAvailableInRupees[1] = 3500;
        bankCurrencyToAmountAvailableInRupees[2] = 3000;

        boolean areTradersRequestsSatisfied = satisfyTraderRequests(traders, currencies, traderRupees, traderToWillingToPayAmountsForCurrencyJ, bankCurrencyToAmountAvailableInRupees);

        if (areTradersRequestsSatisfied) {
            System.out.println("traders requests ARE satisfied with all traders having converted their rupees up to the amount of their desired currencies");
        } else {
            System.out.println("traders requests ARE NOT satisfied with all traders not being able to convert their rupees up to the amount of their desired currencies");
        }
    }

    private static boolean satisfyTraderRequests(int[] traders, int[] currencies, int[] traderRupees, int[][] traderToWillingToPayAmountsForCurrencyJ, int[] bankCurrencyToAmountAvailableInRupees) {

        int startingNodeSIndex = (int)Double.NEGATIVE_INFINITY;
        int endingNodeTIndex = (int)Double.POSITIVE_INFINITY;

        HashMap<Integer, HashSet<ResidualGraphEdge>> residualGraph = getResidualGraph(startingNodeSIndex, endingNodeTIndex, traders, currencies, traderRupees, traderToWillingToPayAmountsForCurrencyJ, bankCurrencyToAmountAvailableInRupees);

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

        System.out.println();
        for (int node : residualGraph.keySet()) {
            if (node >= traders.length && node < (int)Double.POSITIVE_INFINITY) {
                System.out.println("traders (index) who trade with currency (index) " + (node - traders.length) + ":   ");
                for (ResidualGraphEdge edge : residualGraph.get(node)) {
                    if (edge.isBackward) {
                        System.out.print(edge.end.index);
                        System.out.print(", with the amount of rupees traded = " + edge.rEdgeCapacity);
                        System.out.println();
                    }
                }
                System.out.println();
            }
        }
        System.out.println();
        int maxFlow = 0;
        // max flow is the sum of capacities of all back-edges to starting node s
        for (int node : residualGraph.keySet()) {
            for (ResidualGraphEdge edge : residualGraph.get(node)) { // all edges that start from this node
                if (edge.isBackward && edge.end.index == startingNodeSIndex) {
                    System.out.println("trader " + edge.start.index + " is trading " + edge.rEdgeCapacity + "(" + traderRupees[edge.start.index] + ") rupees");
                    maxFlow += edge.rEdgeCapacity;
                }
            }
        }
        System.out.println();
        int totalRupeesOfTraders = 0;
        // if max flow is equal to total rupees available to traders then it is true that all traders converted their rupees up to the amount of their desired currencies
        for (int i = 0; i < traders.length; i++) {
            totalRupeesOfTraders += traderRupees[i];
        }
        if (maxFlow == totalRupeesOfTraders) {
            return true;
        }
        return false;
    }

    public static HashMap<Integer, HashSet<ResidualGraphEdge>> getResidualGraph(int startingNodeSIndex, int endingNodeTIndex, int[] traders, int[] currencies, int[] traderRupees, int[][] traderToWillingToPayAmountsForCurrencyJ, int[] bankCurrencyToAmountAvailableInRupees) {

        int numberOfTraders = traders.length;
        int numberOfCurrencies = currencies.length;
        int currenciesStartingIndex = numberOfTraders;

        ResidualGraphNode startingNodeS = new ResidualGraphNode(startingNodeSIndex);
        ResidualGraphNode endingNodeT = new ResidualGraphNode(endingNodeTIndex);

        HashMap<Integer, HashSet<ResidualGraphEdge>> returnedResidualGraph = new HashMap<>();

        HashMap<Integer, ResidualGraphNode> traderOrCurrencyToItsNode = new HashMap<>();

        // put s and t into graph
        returnedResidualGraph.put(startingNodeSIndex, new HashSet<>());
        returnedResidualGraph.put(endingNodeTIndex, new HashSet<>());

        // Map S to Traders with edges having T(i) capacities
        for (int i = 0; i < numberOfTraders; i++) {
            ResidualGraphNode thisTrader = new ResidualGraphNode(i);
            traderOrCurrencyToItsNode.put(i, thisTrader); // essentially putting traders[i] or i here will indicate the same reference since for convenience all nodes are indexed
            ResidualGraphEdge startToTraderEdge = new ResidualGraphEdge(traderRupees[i], startingNodeS, thisTrader, false);
            returnedResidualGraph.get(startingNodeSIndex).add(startToTraderEdge);
            // make new domain of available currencies for this person to trade
            returnedResidualGraph.put(i, new HashSet<>());
        }

        // currency --> t. All currencies are mapped to t with edge capacities of however many rupees worth of currency i the bank has.
        for (int i = 0; i < numberOfCurrencies; i++) {
            int currencyIndex = currenciesStartingIndex + i;
            ResidualGraphNode thisCurrencyNode = new ResidualGraphNode(currencyIndex);
            traderOrCurrencyToItsNode.put(currencyIndex, thisCurrencyNode);
            returnedResidualGraph.put(currencyIndex, new HashSet<>());
            ResidualGraphEdge thisCurrencyToT = new ResidualGraphEdge((bankCurrencyToAmountAvailableInRupees[i]), thisCurrencyNode, endingNodeT, false);
            returnedResidualGraph.get(currencyIndex).add(thisCurrencyToT);
        }

        // map traders to currencies with capacities of how much this trader is willing to trade of this currency
        for (int i = 0; i < numberOfTraders; i++) {
            ResidualGraphNode thisTraderNode = traderOrCurrencyToItsNode.get(i);
            for (int j = 0; j < numberOfCurrencies; j++) {
                int currencyIndex = currenciesStartingIndex + j;
                int cIJCapacity = traderToWillingToPayAmountsForCurrencyJ[i][j];
                ResidualGraphNode thisCurrencyNode = traderOrCurrencyToItsNode.get(currencyIndex);
                ResidualGraphEdge traderToCurrencyEdge = new ResidualGraphEdge(cIJCapacity, thisTraderNode, thisCurrencyNode, false);
                returnedResidualGraph.get(i).add(traderToCurrencyEdge);
            }
        }
        /*
        for (int nodeIndex : returnedResidualGraph.keySet()) {
            System.out.println("printing edges in domain of node index " + nodeIndex);
            for (ResidualGraphEdge edge : returnedResidualGraph.get(nodeIndex)) {
                System.out.println("edge " + edge.start.index + "-" + edge.end.index + "(" + edge.rEdgeCapacity + ")");
            }
        }
         */
        return returnedResidualGraph;
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
