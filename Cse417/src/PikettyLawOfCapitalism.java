// May 20, 2020
public class PikettyLawOfCapitalism {
    public static void main(String[] args) {
        int s = 4; // savings rate
        int g = 2; // income (GDP) growth rate
        double startingIncome = 0.5;
        int numberOfYears = 300;
        calculateGrowth(s, g, startingIncome, numberOfYears);
    }
    public static void calculateGrowth(int s, int g, double startingIncome, int numberOfYears) {
        double[] incomeAtYearArray = new double[numberOfYears];
        double[] savingsAtYearArray = new double[numberOfYears];
        for (int i = 0; i < numberOfYears; i++) {
            if (i == 0) {
                incomeAtYearArray[0] = startingIncome;
                savingsAtYearArray[0] = s;
            } else {
                double incomeIncrease = ((incomeAtYearArray[i - 1]) * g) / 100;
                incomeAtYearArray[i] = incomeAtYearArray[i - 1] + incomeIncrease;
                double savingsIncrease = ((incomeAtYearArray[i] * s) / 100);
                savingsAtYearArray[i] = savingsAtYearArray[i - 1] + savingsIncrease;
                double ratio = savingsIncrease / incomeIncrease;
                System.out.println("income increase by "+incomeIncrease);
                System.out.println("savings increase by "+savingsIncrease);
                System.out.println("ratio: " + ratio);
            }
            System.out.println("income at year "+i+": "+incomeAtYearArray[i]);
            System.out.println("savings at year "+i+": "+savingsAtYearArray[i]);
        }
        /*
        for (int j = 0; j < numberOfYears; j++) {
            System.out.println("income at year " + j + ": " + incomeAtYearArray[j]);
            System.out.println("savings at year " + j + ": " + savingsAtYearArray[j]);
        }
         */
    }
}
