import java.util.ArrayList;
import java.util.HashMap;

public class SimplerWeightedIndependentSet {

    HashMap<Integer, ArrayList<Integer>> memoizationMap = new HashMap<>();

    // Homework 7, Question 1
    // Important note: the structure of the returned array list is of the form: {TotalWeightOfSet, set of vertices.....}
    public ArrayList<Integer> getIndependent(ArrayList<Vertex> path, int vertexIndex) {
        if (vertexIndex == -1 || vertexIndex == -2) {
            ArrayList<Integer> newList = new ArrayList<>();
            newList.add(0);
            return newList;
        } else {
            if (!memoizationMap.keySet().contains(vertexIndex)) {
                ArrayList<Integer> left = getIndependent(path, vertexIndex - 1);
                ArrayList<Integer> right = getIndependent(path, vertexIndex - 2);
                ArrayList<Integer> returnedList;
                if (left.get(0) > (right.get(0)+path.get(vertexIndex).weight)) {
                    returnedList = left;
                } else {
                    right.set(0, right.get(0) + path.get(vertexIndex).weight);
                    right.add(vertexIndex);
                    returnedList = right;
                }
                memoizationMap.put(vertexIndex, returnedList);
                return returnedList;
            } else {
                return memoizationMap.get(vertexIndex);
            }
        }
    }

    public static class Vertex {
        int index;
        int weight;
        public Vertex(int index, int weight) {
            this.index = index;
            this.weight = weight;
        }
    }
}
