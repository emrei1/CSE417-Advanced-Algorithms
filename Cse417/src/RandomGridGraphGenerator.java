import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

// Assignment 5 - Problems 8, 9, 10
public class RandomGridGraphGenerator {

    private static final int DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS = 2;

    public static void main(String[] args) {
        /*
        Scanner console = new Scanner(System.in);
        System.out.print("enter value for n ");
        int n = console.nextInt();
         */
        int n = 500;

        /*
        System.out.println("Dijkstra's algorithm to compute the shortest distance from s to v");
        System.out.print("enter value i for s: ");
        int startingVertexI = console.nextInt();
        System.out.print("enter value j for s: ");
        int startingVertexJ = console.nextInt();
        System.out.print("enter value i for v: ");
        int endingVertexI = console.nextInt();
        System.out.print("enter value j for v: ");
        int endingVertexJ = console.nextInt();
        int startingVertex = getValue(startingVertexI, startingVertexJ, n);
        int endingVertex = getValue(endingVertexI, endingVertexJ, n);
         */

        int startingVertex = 0;
        int endingVertex = n*n-1;

        float iterations = 10;
        // float totalShortest = 0;
        float totalBotleNeck = 0;
        /*
        for (int i = 0; i < iterations; i++) {
            float[][][] randomGridGraph = getRandomGridGraph(n);
            System.out.println("finding shortest path from "+startingVertex+" to "+endingVertex+ " for random n = " + n +" grid graph.....");
            float shortestDistance = dijkstrasNonMemoryOptimized(randomGridGraph, n, startingVertex, endingVertex);
            System.out.println("Dijkstra's found the shortest distance to be " + shortestDistance);
            totalShortest += shortestDistance;
        }
        System.out.println("average shortest distance for "+(int)iterations+" iterations was "+(totalShortest/iterations));

         */

        for (int i = 0; i < iterations; i++) {
            float[][][] randomGridGraph = getRandomGridGraph(n);
            float bottleNeck = dijkstrasComputeBottleneck(randomGridGraph, n, startingVertex, endingVertex);
            System.out.println("running new iteration to find bottleneck distance from "+startingVertex+" to "+endingVertex+" for gird graph with n = "+ n);
            System.out.println("bottleneck distance was found to be :  " + bottleNeck);
            totalBotleNeck += bottleNeck;
        }
        float averageBottleNeck = ((totalBotleNeck)/(iterations));
        System.out.println();
        System.out.println("average bottleneck from "+iterations+" iterations was found to be:     "+averageBottleNeck);
    }

    private static float dijkstrasMemoryOptimized(float[][][] randomGridGraph, int n, int startingVertex, int endingVertex) {
        PQMinHeap pq = new PQMinHeap();

        int vertex = startingVertex;
        pq.add(new PriorityNode(vertex, 0));
        float lastDistance = 0;

        while (vertex != endingVertex) {
            PriorityNode fringeRemove = pq.remove();
            vertex = fringeRemove.value;
            float distance = fringeRemove.priority;
            lastDistance = distance;

            int vertexI = getI(vertex, n);
            int vertexJ = getJ(vertex, n);

            for (int i = 0; i < DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS; i++) {
                float neighbor = randomGridGraph[vertexI][vertexJ][i];
                float neighborCost = randomGridGraph[vertexI][vertexJ][i + DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS];

                if (neighbor != -1) {
                    float distanceToNeighbor = distance + neighborCost;
                    PriorityNode neighborNode = new PriorityNode((int)neighbor, distanceToNeighbor);
                    if (!pq.contains((int)neighbor)) {
                        pq.add(neighborNode);
                    } else if (pq.contains((int)neighbor) && distanceToNeighbor < pq.getPriority((int)neighbor)) {
                        pq.replacePriority((int)neighbor, distanceToNeighbor);
                    }
                }
            }
        }
        return lastDistance;
    }

    // min bottlelength out of all paths from startingVertex - endingVertex
    private static float dijkstrasComputeBottleneck(float[][][] randomGridGraph, int n, int startingVertex, int endingVertex) {
        PQMinHeap pq = new PQMinHeap();
        int vertex = startingVertex;

        for (int i = 0; i < n*n; i++) {
            if (i == vertex) {
                pq.add(new PriorityNode(vertex, 0));
            } else {
                pq.add(new PriorityNode(i, (float)Double.POSITIVE_INFINITY));
            }
        }

        float bottleNeck = -1;

        while (vertex != endingVertex) {

            PriorityNode fringeRemove = pq.remove();
            vertex = fringeRemove.value;
            float distance = fringeRemove.priority;

            int vertexI = getI(vertex, n);
            int vertexJ = getJ(vertex, n);

            bottleNeck = distance;

            for (int i = 0; i < DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS; i++) {
                float neighbor = randomGridGraph[vertexI][vertexJ][i];
                float costToNeighbor = randomGridGraph[vertexI][vertexJ][i + DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS];

                if (neighbor != -1) {
                    if (pq.contains((int)neighbor)) {
                        float bottleNeckDistanceToNeighbor = Math.min(pq.getPriority(((int)neighbor)), Math.max(distance, costToNeighbor));
                        pq.replacePriority((int)neighbor, bottleNeckDistanceToNeighbor);
                    }
                }
            }
        }
        return bottleNeck;
    }

    private static float dijkstrasNonMemoryOptimized(float[][][] randomGridGraph, int n, int startingVertex, int endingVertex) {
        PQMinHeap pq = new PQMinHeap();
        int vertex = startingVertex;
        float lastDistance = 0;

        for (int i = 0; i < n*n; i++) {
            if (i == vertex) {
                pq.add(new PriorityNode(vertex, 0));
            } else {
                pq.add(new PriorityNode(i, (float)Double.POSITIVE_INFINITY));
            }
        }

        while (vertex != endingVertex) {
            PriorityNode fringeRemove = pq.remove();
            vertex = fringeRemove.value;
            float distance = fringeRemove.priority;
            lastDistance = distance;

            int vertexI = getI(vertex, n);
            int vertexJ = getJ(vertex, n);

            // every node has 2 vertices in directed grid graph, used only for this implementation when checking neighbors
            for (int i = 0; i < DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS; i++) {
                float neighbor = randomGridGraph[vertexI][vertexJ][i];
                float neighborCost = randomGridGraph[vertexI][vertexJ][i + DIRECTED_GRIDGRAPH_NUMBEROF_NEIGHBORS];

                if (neighbor != -1) {
                    float distanceToNeighbor = distance + neighborCost;
                    if (pq.contains((int)neighbor) && distanceToNeighbor < pq.getPriority((int)neighbor)) {
                        pq.replacePriority((int)neighbor, distanceToNeighbor);
                    }
                }
            }
        }
        return lastDistance;
    }

    private static int getI(int vertex, int n) {
        return vertex % n;
    }

    private static int getJ(int vertex, int n) {
        return vertex / n;
    }

    private static int getValue(int i, int j, int n) {
        return (j * n) + i;
    }

    private static float[][][] getRandomGridGraph(int n) {
        Random rand = new Random();
        float[][][] grid = new float[n][n][4]; // i * j * 4. 4 is for 0: 1st neighbor, 1: 2nd neighbor, 2: cost of edge to 1st neighbor, 3: cost of edge to 2nd neighbor

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                float firstRandom = (float)rand.nextDouble();
                float secondRandom = (float)rand.nextDouble();
                if (i < n - 1) {
                    grid[i][j][0] = getValue(i, j, n) + 1;
                    grid[i][j][2] = firstRandom;
                } else {
                    grid[i][j][0] = -1;
                    grid[i][j][2] = -1;
                }
                if (j < n - 1) {
                    grid[i][j][1] = getValue(i, j, n) + n;
                    grid[i][j][3] = secondRandom;
                } else {
                    grid[i][j][1] = -1;
                    grid[i][j][3] = -1;
                }
            }
        }

        System.out.println("printing grid graph. A neighbor value of -1 (and cost) indicates the vertex doesn't have a neighbor for that dimention");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int nodeI = i;
                int nodeJ = j;
                float firstNeighbor = grid[i][j][0];
                float secondNeighbor = grid[i][j][1];
                float costFirstEdge = grid[i][j][2];
                float costSecondEdge = grid[i][j][3];
                System.out.println("Node "+getValue(nodeI, nodeJ, n)+" ("+nodeI+", "+nodeJ+") has neighbors "+firstNeighbor+"("+costFirstEdge+")"+" " + "and "+secondNeighbor+"("+costSecondEdge+")");
            }
        }

        return grid;
    }
}
