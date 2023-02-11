import java.util.ArrayList;

public class MainSimplerWeightedIndependentSet {

    public static void main(String[] args) {
        SimplerWeightedIndependentSet.Vertex vertexC = new SimplerWeightedIndependentSet.Vertex(4, 5);
        SimplerWeightedIndependentSet.Vertex vertexE = new SimplerWeightedIndependentSet.Vertex(3, 5);
        SimplerWeightedIndependentSet.Vertex vertexD = new SimplerWeightedIndependentSet.Vertex(2, 7);
        SimplerWeightedIndependentSet.Vertex vertexJ = new SimplerWeightedIndependentSet.Vertex(1, 8);
        SimplerWeightedIndependentSet.Vertex vertexG = new SimplerWeightedIndependentSet.Vertex(0, 4);
        ArrayList<SimplerWeightedIndependentSet.Vertex> path = new ArrayList();
        path.add(vertexG);
        path.add(vertexJ);
        path.add(vertexD);
        path.add(vertexE);
        path.add(vertexC);
        SimplerWeightedIndependentSet simplerWeightedIndependentSet= new SimplerWeightedIndependentSet();
        // magic # 4 is the highest numbered index in path
        System.out.println("Max Independent Set: "+simplerWeightedIndependentSet.getIndependent(path, 4).toString());
    }
}
