package org.example;

import java.util.Scanner;

class Histogram {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Set image size: n (#rows), m(#kolumns)");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int liczbaZnakow = 5;
        Obraz obraz = new Obraz(n, m, liczbaZnakow);

        System.out.println("\n--- Rozpoczynamy obliczenia sekwencyjne histogramu ---");
        long startTime = System.nanoTime();
        obraz.calculate_histogram();
        obraz.printHistogram();
        System.out.printf("--- Koniec obliczeń sekwencyjnych, czas=%d ns ---\n", (System.nanoTime() - startTime));


        System.out.println("\n--- Rozpoczynamy obliczenia histogramu blokowego ---");
        System.out.println("Set number of threads");
        int numThreads = scanner.nextInt();
        long startTimeBlock = System.nanoTime();
        double N = Math.ceil((double) liczbaZnakow / (double) numThreads);
        Thread[] threadsBlock = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int start = (int) (i * N);
            int end = (int) Math.min((i + 1) * N, liczbaZnakow);
            RownoleglyHistogramBlokowy rhb = new RownoleglyHistogramBlokowy(start, end, obraz);
            threadsBlock[i] = new Thread(rhb);
            threadsBlock[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threadsBlock[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTimeBlock = System.nanoTime();
        obraz.printHistogramParallelBlock();
        System.out.println("--- Koniec obliczeń histogramu blokowego, czas=" + (endTimeBlock - startTimeBlock) + " ns ---\n");

        System.out.println("--- Porownanie zgodności histogramów ---");
        System.out.println(obraz.compareHistogramsParallelBlock() ? "Histogramy są zgodne" : "Histogramy nie są zgodne");

        long startTimeParallel = System.nanoTime();
        System.out.println("\n--- Rozpoczynamy obliczenia rownolegle histogramu ---");
        System.out.printf("Liczba watkow to %d\n", liczbaZnakow);
        RownoleglyHistogram[] threads = new RownoleglyHistogram[liczbaZnakow];
        for (int i = 0; i < liczbaZnakow; i++) {
            threads[i] = new RownoleglyHistogram(i, obraz);
            threads[i].start();
        }

        for (int i = 0; i < liczbaZnakow; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        obraz.printHistogramParallel();
        System.out.printf("--- Koniec obliczeń równoległych, czas=%d ns ---\n\n", (endTime - startTimeParallel));

        System.out.println("--- Porownanie zgodności histogramów ---");
        System.out.println(obraz.compareHistogramsParallel() ? "Histogramy są zgodne" : "Histogramy nie są zgodne");

        // System.out.println("Set number of threads");
        // int num_threads = scanner.nextInt();

        // Watek[] NewThr = new Watek[num_threads];

        // for (int i = 0; i < num_threads; i++) {
        //     (NewThr[i] = new Watek(...,obraz)).start();
        // }

        // for (int i = 0; i < num_threads; i++) {
        //     try {
        // 	NewThr[i].join();
        //     } catch (InterruptedException e) {}
        // }

    }

}