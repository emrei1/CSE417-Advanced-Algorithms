import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class PQMinHeap {

    private ArrayList<PriorityNode> priorityList;
    private HashMap<Integer, Integer> priorityMap; // Key: Index, Value: PriorityIndex in priorityList of Node with this Value

    private int size = 0;

    public PQMinHeap() {
        priorityList = new ArrayList<>();
        priorityMap = new HashMap<>();
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private PriorityNode getParent(int index) {
        return priorityList.get(getParentIndex(index));
    }

    private void swap(int parentIndex, int childIndex, PriorityNode parent, PriorityNode child) {
        priorityList.set(parentIndex, child);
        priorityList.set(childIndex, parent);
        priorityMap.put(parent.value, childIndex);
        priorityMap.put(child.value, parentIndex);
    }

    public void add(PriorityNode newNode) {
        // System.out.println("added priority = " + newNode.priority);
        priorityList.add(newNode);
        priorityMap.put(newNode.value, size);
        size++;
        int index = size - 1;
        swim(index);
        // System.out.println("added node " + newNode.value + " with priority " + newNode.priority);
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public PriorityNode remove() {
        // System.out.println("size is "+ size);
        if (size == 0) {
            throw new NoSuchElementException();
        }
        priorityMap.remove(priorityList.get(0).value);
        PriorityNode topPriorityNode = priorityList.get(0);
        priorityList.set(0, priorityList.get(size - 1));
        priorityMap.put(priorityList.get(size - 1).value, 0);
        priorityList.remove(size - 1);
        size--;
        int index = 0;
        sink(index);
        return topPriorityNode;
    }

    public int getSize() {
        return size;
    }

    public boolean contains(int value) {
        if (priorityMap.keySet().contains(value)) {
            return true;
        }
        return false;
    }

    public float getPriority(int value) {
        return priorityList.get(priorityMap.get(value)).priority;
    }

    private void swim(int index) { // for add operation
        while (hasParent(index) && getParent(index).priority > priorityList.get(index).priority) {
            swap(getParentIndex(index), index, getParent(index), priorityList.get(index));
            index = getParentIndex(index);
        }
    }

    private void sink(int index) { // for remove operation
        while (hasLeftChild(index)) {
            int smallestChildIndex = getLeftChildIndex(index);

            if (hasRightChild(index) && priorityList.get(getRightChildIndex(index)).priority < priorityList.get(smallestChildIndex).priority) {
                smallestChildIndex = getRightChildIndex(index);
            }

            if (priorityList.get(index).priority < priorityList.get(smallestChildIndex).priority) {
                break;
            } else {
                swap(index, smallestChildIndex, priorityList.get(index), priorityList.get(smallestChildIndex));
            }
            index = smallestChildIndex;
        }
    }

    public void replacePriority(int value, float newPriority) {
        int replacedNodeIndex = priorityMap.get(value);
        PriorityNode replacedNode = priorityList.get(replacedNodeIndex);
        float oldPriority = replacedNode.priority;
        replacedNode.priority = newPriority;

        if (oldPriority < newPriority) {
            sink(replacedNodeIndex);
        } else if (oldPriority > newPriority) {
            swim(replacedNodeIndex);
        }
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
    }

    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    /*
    public static void main(String[] args) {
        PriorityNode root1 = new PriorityNode(1, 10);
        PriorityNode root2 = new PriorityNode(2, 5);
        PriorityNode root3 = new PriorityNode(3, 7);
        PriorityNode root4 = new PriorityNode(4, 20);
        PriorityNode root5 = new PriorityNode(5, 2);
        PriorityNode root6 = new PriorityNode(6, 3);
        PriorityNode root7 = new PriorityNode(7, 1);
        PriorityNode root8 = new PriorityNode(8, 9);

        add(root1);
        add(root2);
        add(root3);
        add(root4);
        add(root5);
        add(root6);
        add(root7);
        add(root8);

        remove();
        replacePriority(5, 4);
//        remove();
//        remove();
//        remove();

        int firstValueArrayIndex = priorityMap.get(priorityList.get(0).value);
        int secondValueArrayIndex = priorityMap.get(priorityList.get(1).value);
        int thirdValueArrayIndex = priorityMap.get(priorityList.get(2).value);

//        System.out.println("array index of top priority: " + firstValueArrayIndex);
//        System.out.println("array index of top.left priority: " + secondValueArrayIndex);
//        System.out.println("array index of top.right priority: " + thirdValueArrayIndex);

        System.out.println("root node is " + priorityList.get(0).value + " with .left = " + priorityList.get(1).value + " and .right = " + priorityList.get(2).value);
    }
     */
}
