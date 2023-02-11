import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ShortestCycle {

    public static void main(String[] args) {

        int n = 10000;
        double p = 0.005;
        HashMap<Integer, HashSet<Integer>> randomGraph = RandomGraphGenerator.generateRandomGraph(n, p);

        // System.out.println("graph: " + randomGraph.toString());

        // pick an edge from generated graph that exists
        boolean noEdge = true;
        int edgeStart = -1;
        int edgeEnd = -1;
        for (int i = 0; i < n; i++) {
            if (!randomGraph.get(i).isEmpty()) {
                edgeStart = i;
                edgeEnd = randomGraph.get(i).iterator().next();
                noEdge = false;
                break;
            }
        }

        System.out.println("starting vertex: "+edgeStart);
        System.out.println("ending vertex: "+edgeEnd);

        ArrayList<Integer> shortestCycle;
        if (!noEdge) {
            System.out.println("finding shortest cycle.....");
            shortestCycle = findShortest(randomGraph, edgeStart, edgeEnd);
            System.out.println();
            if (shortestCycle == null) {
                System.out.println("no cycle was found");
            } else {
                System.out.print("was found to be: " + shortestCycle.toString());
            }
        } else {
            System.out.println("the graph has no edges");
        }
    }

    public static ArrayList<Integer> findShortest(HashMap<Integer, HashSet<Integer>> graph, int edgeStart, int edgeEnd) {
        HashMap<Integer, ArrayList<Integer>> shortestPathFromStart = new HashMap<>();

        for (int i = 0; i < graph.keySet().size(); i++) {
            shortestPathFromStart.put(i, new ArrayList<>());
        }

        HashSet<Integer> alreadyVisited = new HashSet<>();
        ArrayDeque<Integer> newNodesToVisit = new ArrayDeque<>();
        newNodesToVisit.add(edgeStart);
        boolean firstIteration = true;

        while (!newNodesToVisit.isEmpty()) {
            int newNode = newNodesToVisit.remove();
            alreadyVisited.add(newNode);
            ArrayList<Integer> thisNodePath = shortestPathFromStart.get(newNode);
            for (int neighbor : graph.get(newNode)) {
                    if (firstIteration && neighbor == edgeEnd) {
                        ArrayList<Integer> edgeEndPath = new ArrayList<>();
                        edgeEndPath.add(edgeStart);
                        edgeEndPath.add(edgeEnd);
                        shortestPathFromStart.put(neighbor, edgeEndPath);
                        newNodesToVisit.add(neighbor);
                    } else if (!firstIteration && newNode != edgeEnd && neighbor == edgeStart) {
                        thisNodePath.add(neighbor);
                        return thisNodePath;
                    } else {
                        if (!firstIteration && !alreadyVisited.contains(neighbor)) {
                            ArrayList<Integer> neighborPath = new ArrayList<>();
                            for (int i = 0; i < thisNodePath.size(); i++) {
                                neighborPath.add(thisNodePath.get(i));
                            }
                            neighborPath.add(neighbor);
                            shortestPathFromStart.put(neighbor, neighborPath);
                            newNodesToVisit.add(neighbor);
                        }
                    }
            }
            if (firstIteration) {
                firstIteration = false;
            }
        }
        return null;
    }
}
