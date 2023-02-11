import java.util.HashMap;
import java.util.HashSet;

// Assignment 4 problem 4. Test your program on random graphs using the generator from HW 3. Use values of n of 1000 (or
// larger). You should report results for values of p in the range 0.002 and 0.02.
public class ComputeGraphDegrees {

    public static void main(String[] args) {
        RandomGraphGenerator randomGraphGenerator = new RandomGraphGenerator();
        int n = 5000; // number of nodes graph will have
        double p = 0.016; // probability of node u having an edge with node v
        int degreeToCheck = 3;


        HashMap<Integer, HashSet<Integer>> graph = randomGraphGenerator.generateRandomGraph(n, p);
        HashMap<Integer, Integer> graphDegrees = computeGraphDegrees(graph);

        System.out.println("n = " + n);
        System.out.println("p = " + p);
        for (int i = 0; i < n; i++) {
            if (graphDegrees.get(i) != null) {
                System.out.println("number of nodes with degree " + i + ": " + graphDegrees.get(i));
            }
        }
    }

    private static HashMap<Integer, Integer> computeGraphDegrees(HashMap<Integer, HashSet<Integer>> graph) {
        HashMap<Integer, Integer> graphDegrees = new HashMap<>(); // key: degree, value: count of degree

        for (int vertex : graph.keySet()) {
            int thisVertexDegree = graph.get(vertex).size();
            if (!graphDegrees.containsKey(thisVertexDegree)) {
                graphDegrees.put(thisVertexDegree, 1);
            } else {
                graphDegrees.put(thisVertexDegree, graphDegrees.get(thisVertexDegree + 1));
            }
        }
        return graphDegrees;
    }
}
