package org.example;

import java.util.ArrayList;
import java.util.List;

public class Task implements Runnable {
    private int[] histogram;
    private List<Integer> rows = new ArrayList<>();
    private int rowLength;
    private Obraz obraz;

    public Task(Obraz obraz, int m) {
        this.obraz = obraz;
        this.rowLength = m;
        this.histogram = new int[obraz.getLiczbaZnakow()];
    }

    public void addRow(int row) {
        rows.add(row);
    }

    @Override
    public void run() {
        char[][] tab = obraz.getTab();

        for (int row : rows) {
            for (int j = 0; j < rowLength; j++) {
                histogram[(int) tab[row][j] - 33]++;
            }
        }
    }

    public int[] getHistogram() {
        return histogram;
    }
}
