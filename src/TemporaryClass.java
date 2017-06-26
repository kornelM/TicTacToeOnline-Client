/**
 * Created by Kornel Mikolajski on 26.06.2017.
 */
public class TemporaryClass {
    public static void main(String[] args) {


        String tmpString = "1234567";
        int[] tmpTable = new int[tmpString.length()];


        for (int i = 0; i < tmpString.length(); i++) {
            tmpTable[i] = Integer.parseInt(String.valueOf(tmpString.charAt(i)));
        }

        for (int i = 0; i < tmpTable.length; i++) {
            System.out.println(tmpTable[i]);
        }

    }
}
