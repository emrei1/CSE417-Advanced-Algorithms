public class ReferenceSemanticsDeneme {

    public static void main(String[] args) {

        int[] array = {1, 2, 3, 4, 5, 6};
        int number = 10;

        method(array, number);

        int finalValue = array[3];

        System.out.println("the final value of array turned out to be:  " + finalValue);
        System.out.println("the final value of number turned out to be:  " + number);

    }

    public static void method(int[] array, int number) {
        array[3] = 10;
        number = 5;
    }
}
