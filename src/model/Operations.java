package model;

/**
 * Created by Kornel Mikolajski on 26.06.2017.
 */
public class Operations {

    public int getField(int[] tableOfInts, int numberOfField) {
        int field = 0;

        for (int i = 0; i < tableOfInts.length; i++) {
            if (field == numberOfField) {
                return tableOfInts[i];
            } else
                field++;
        }
        return 10;
    }

}
