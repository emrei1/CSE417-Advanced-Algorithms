public class ReverseNumber {

    public static void main(String[] args) {
        int number = 12345;
        int returned = reverseNumber(number);
        System.out.println("reversed number = " + returned);

        int charseyi = 25;
        char character = (char)(charseyi + 65);

        System.out.println(character);
    }

    public static int reverseNumber(int number) {
        if (number != 0) {
            int numberOfDigits = 0;
            int checkedNumber = number;
            while (checkedNumber != 0) {
                checkedNumber /= 10;
                numberOfDigits++;
            }
            int[] reverseNumberArray = new int[numberOfDigits];
            for (int i = 0; i < numberOfDigits - 1; i++) {
                int remainder = number % 10;
                reverseNumberArray[i] = remainder;
                number /= 10;
            }
            reverseNumberArray[numberOfDigits - 1] = number;
            int returnedNumber = 0;
            for (int i = 0; i < numberOfDigits - 1; i ++) {
                int tenthCounter = 1;
                for (int j = 0; j < (numberOfDigits - 1) - i; j++) {
                    tenthCounter *= 10;
                }
                returnedNumber += (tenthCounter * reverseNumberArray[i]);
            }
            returnedNumber += reverseNumberArray[numberOfDigits - 1];
            return returnedNumber;
        } else {
            return 0;
        }
    }
}
