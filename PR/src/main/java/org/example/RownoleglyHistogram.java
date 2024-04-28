package org.example;

public class RownoleglyHistogram extends Thread {
    private int pozycjaZnaku;
    private Obraz obraz;

    public RownoleglyHistogram(int pozycjaZnaku, Obraz obraz) {
        this.pozycjaZnaku = pozycjaZnaku;
        this.obraz = obraz;
    }

    @Override
    public void run() {
        obraz.calculateHistogramParallel(pozycjaZnaku);
        obraz.printHistogramForSymbol(pozycjaZnaku);
    }
}
