package org.example;

public class RownoleglyHistogramBlokowy implements Runnable {
    private int start;
    private int end;
    private Obraz obraz;

    public RownoleglyHistogramBlokowy(int start, int end, Obraz obraz) {
        this.start = start;
        this.end = end;
        this.obraz = obraz;
    }

    @Override
    public void run() {
        obraz.calculateHistogramParallelBlock(start, end);
        obraz.printHistogramForBlock(start, end);
    }
}
