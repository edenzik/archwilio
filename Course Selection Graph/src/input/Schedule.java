package input;

import java.util.BitSet;

/**
 * Use BitSet Array to represent schedule table
 * Created by zhan on 7/28/15.
 */
public class Schedule {
    private BitSet[] bitArr;

    public Schedule(int rows, int cols) {
        bitArr = new BitSet[rows];
        for (int i = 0; i < rows; i++)
            bitArr[i] = new BitSet(cols);
    }

    public boolean get(int r, int c) {
        return bitArr[r].get(c);
    }

    public void set(int r, int c) {
        bitArr[r].set(c);
    }

    public void display()
    {
        System.out.println("\nBit Matrix : ");
        for (BitSet bs : bitArr) {
            String line = "";
            for (int i = 0; i < 4; i++) {
                if (bs.get(i))
                    line += "1 ";
                else
                    line += "0 ";
            }
            System.out.println(line);
        }
        System.out.println();
    }
}
