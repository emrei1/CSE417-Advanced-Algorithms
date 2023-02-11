import java.util.*;

public class RandomGraphGenerator {

    public static void main(String[] args) {
        int n = 3; // number of nodes
        double p = 0.3; // probability of a node u to have edge with node v

        // System.out.println(vertexPairs.toString());

        int noIterations = 1;

        for (int i = 0; i < noIterations; i++) {
            HashMap<Integer, HashSet<Integer>> newGraph = generateRandomGraph(n, p);
            int maxDistance = getDiameter(newGraph);
            System.out.println("returned graph for this iteration: " + newGraph.toString());
        }
        // System.out.println("finite (max) diameter is " + maxDistance);
    }

    public static HashMap<Integer, HashSet<Integer>> generateRandomGraph(int n, double p) {
        HashMap<Integer, HashSet<Integer>> returnedGraph = new HashMap<>();
        HashSet<Integer> alreadyIterated = new HashSet<>();
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            returnedGraph.put(i, new HashSet<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // j should start at i + 1. Otherwise every node (u, v) will have 2 chances to exist
                if (i != j) {
                    if (!alreadyIterated.contains(j)) {
                        double randDouble = rand.nextDouble();
                        if (randDouble <= p) {
                            returnedGraph.get(i).add(j);
                            returnedGraph.get(j).add(i);
                        }
                    }
                }
            }
            alreadyIterated.add(0);
        }
        return returnedGraph;
    }

    private static int getDiameter(HashMap<Integer, HashSet<Integer>> graph) {
        HashSet<Integer> allNodes = new HashSet<>();
        for (int node : graph.keySet()) {
            allNodes.add(node);
        }
        boolean isConnected = true;
        int maxDistance = 0;
        for (int i = 0; i < graph.keySet().size(); i++) {
            ArrayDeque visitNodes = new ArrayDeque();
            HashSet<Integer> alreadyVisitedNodes = new HashSet<>();
            int thisMax = 0;
            int[] vertexLayers = new int[graph.keySet().size()];
            vertexLayers[i] = 0;
            visitNodes.add(i);
            while (!visitNodes.isEmpty()) {
                int newVertex = (int)visitNodes.remove();
                // System.out.println("new vertex "+newVertex);
                alreadyVisitedNodes.add(i);
                for (int neighbor : graph.get(newVertex)) {
                    if (!alreadyVisitedNodes.contains(neighbor)) {
                        visitNodes.add(neighbor);
                        alreadyVisitedNodes.add(neighbor);
                        vertexLayers[neighbor] = vertexLayers[newVertex] + 1;
                        if (thisMax < vertexLayers[newVertex] + 1) {
                            thisMax = vertexLayers[newVertex] + 1;
                        }
                    }
                }
            }
            if (alreadyVisitedNodes.size() != allNodes.size()) {
                isConnected = false;

            }
            if (maxDistance < thisMax) {
                maxDistance = thisMax;
            }
        }
        if (isConnected) {
            System.out.println("graph is connected, diameter is " + maxDistance);
        } else {
            System.out.println("graph is disconnected, diameter is infinite");
        }
        System.out.println("finite diameter is " + maxDistance);
        return maxDistance;
    }
}
