// Homework 7, Problem 2. uses dynamic programming technique from page 260 of textbook
public class TaskChoice {

    public static void main(String[] args) {
        int[] highPayoffs = {4, 9, 10};
        int[] lowPayoffs = {3, 4, 5};
        int maxPay = calculateMaxPay(highPayoffs, lowPayoffs);
        System.out.println("max pay was found to be " + maxPay);
    }

    public static int calculateMaxPay(int[] highPayoffs, int[] lowPayoffs) {

        int[] weightsOfIntervals = new int[highPayoffs.length + lowPayoffs.length + 1];
        int[] psOfIntervals = new int[highPayoffs.length + lowPayoffs.length + 1];
        int[] fillingArray = new int[highPayoffs.length + lowPayoffs.length + 1];
        weightsOfIntervals[0] = 0;
        psOfIntervals[0] = 0;
        fillingArray[0] = 0;
        fillingArray[1] = lowPayoffs[0];

        // weights of intervals in order of increasing time
        for (int i = 0; i < highPayoffs.length; i ++) {
            weightsOfIntervals[i * 2 + 1] = lowPayoffs[i];
            weightsOfIntervals[i * 2 + 2] = highPayoffs[i];
        }

        // reference indicating index (in weightsOfIntervals) of the latest nonoverlapping interval
        for (int i = 0; i < highPayoffs.length; i++) {
            if (i == 0) {
                psOfIntervals[1] = 0;
                psOfIntervals[2] = 0;
            } else {
                psOfIntervals[i * 2 + 1] = i * 2 - 1;
                psOfIntervals[i * 2 + 2] = i * 2 - 1;
            }
        }

        for (int i = 2; i < weightsOfIntervals.length; i++) {
            int nonOverlappingMax = fillingArray[psOfIntervals[i]]; // 0 at first
            int overlappingMax = fillingArray[i - 1];
            int newMaxInNonOverlapping = weightsOfIntervals[i] + nonOverlappingMax;
            if (overlappingMax > newMaxInNonOverlapping) {
                fillingArray[i] = overlappingMax;
            } else {
                fillingArray[i] = newMaxInNonOverlapping;
            }
        }
        return fillingArray[fillingArray.length - 1];
    }
}
