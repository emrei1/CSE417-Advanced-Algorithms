import java.util.*;

public class GraphColoring {
    public static void main(String[] args) {
        int n = 5000; // number of vertices in graph
        double p = 0.018; // probability of vertex v to have edge with vertex u
        RandomGraphGenerator randomGraphGenerator = new RandomGraphGenerator();

        int totalColorsStandard = 0;
        int totalColorsBetter = 0;
        int numberOfTests = 1;
        for (int i = 0; i < numberOfTests; i++) {
            HashMap<Integer, HashSet<Integer>> graph = randomGraphGenerator.generateRandomGraph(n, p);
            // HashMap<Integer, Integer> coloredGraph = colorGraph(graph);
            // System.out.println(graph.toString());
            int numberOfColorsStandard = colorGraph(graph);
            totalColorsStandard += numberOfColorsStandard;
            int numberOfColorsBetter = betterColorGraph(graph);
            totalColorsBetter += numberOfColorsBetter;
            // HashMap<Integer, Integer> coloredGraphBetter = betterColorGraph(graph);
            // System.out.println(coloredGraphBetter.toString());
        }
        int averageColorsStandard = totalColorsStandard / numberOfTests;
        int averageColorBetter = totalColorsBetter / numberOfTests;
        System.out.println("For standard, average is " + averageColorsStandard);
        System.out.println("For better, average is " + averageColorBetter);

        // HashMap<Integer, HashSet<Integer>> graph2 = randomGraphGenerator.generateRandomGraph(n, p);
        // int numberOfColorsStandard  = colorGraph(graph2);
        // int numberOfColorsInBetter = betterColorGraph(graph2);
        // System.out.println("graph to be colored:");
        // System.out.println(graph.toString());
        // System.out.println(coloredGraph.toString());
    }

    public static int betterColorGraph(HashMap<Integer, HashSet<Integer>> graph) {
        HashMap<Integer, Integer> coloredGraph = new HashMap<>();
        HashSet<Integer> uncoloredVertices = new HashSet<>();
        for (int vertex : graph.keySet()) { // initially label all vertices in graph as uncolored
            uncoloredVertices.add(vertex);
        }
        int maxNeighbors = 0;
        int maxColor = -1; // color to be added to vertices in every other iteration of the first for loop
        while (!uncoloredVertices.isEmpty()) {
            maxColor++;
            for (int vertex : graph.keySet()) {
                HashSet<Integer> observedColors = new HashSet<>(); // set of colors that were observed in neighbors (a color can only contribute +1 to the set size)
                int countNeighbors = 0;
                for (int neighbor : graph.get(vertex)) {
                    countNeighbors++;
                    if (coloredGraph.containsKey(neighbor)) {
                        if (!observedColors.contains(coloredGraph.get(neighbor))) {
                            observedColors.add(coloredGraph.get(neighbor));
                        }
                    }
                }
                if (maxNeighbors < countNeighbors) {
                    maxNeighbors = countNeighbors;
                }
                if (uncoloredVertices.contains(vertex)) {
                    if (observedColors.size() != maxColor) { // only color if the vertex can be colored the color is currently being iterated inside this while loop
                        coloredGraph.put(vertex, maxColor);
                        uncoloredVertices.remove(vertex);
                    }
                }
            }
        }
        System.out.println("max neighbors for a vertex is " + maxNeighbors);
        return maxColor;
        // return coloredGraph;
    }

    public static int colorGraph(HashMap<Integer, HashSet<Integer>> graph) {
        HashMap<Integer, Integer> coloredGraph = new HashMap<>();
        PriorityQueue<Integer> colors = new PriorityQueue<>(); // where integers represent colors
        int lowestColor = 0;
        colors.add(lowestColor);
        int maxColor = lowestColor;
        for (int vertex : graph.keySet()) {
            PriorityQueue<Integer> availableColors = colors;
            for (int neighbor : graph.get(vertex)) {
                if (coloredGraph.containsKey(neighbor)) {
                    colors.remove(coloredGraph.get(neighbor));
                }
            }

            if (availableColors.size() == 0) {
                maxColor += 1;
                colors.add(maxColor);
                coloredGraph.put(vertex, maxColor);
            } else {
                coloredGraph.put(vertex, availableColors.peek());
            }
        }
        // return coloredGraph;
        return maxColor;
    }
}
