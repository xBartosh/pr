package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Histogram {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Set image size: n (#rows), m(#kolumns)");
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int liczbaZnakow = 10;
        Obraz obraz = new Obraz(n, m, liczbaZnakow);

        System.out.println("Set number of threads");
        int numThreads = scanner.nextInt();
        List<Thread> threads = new ArrayList<>(numThreads);
        List<Task> tasks = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            tasks.add(new Task(obraz, m));
            threads.add(new Thread(tasks.get(i)));
        }

        for (int i = 0; i < m; i++) {
            tasks.get(i % numThreads).addRow(i);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        int[] histogram = new int[liczbaZnakow];
        for (int i = 0; i < numThreads; i++) {
            try {
                threads.get(i).join();
                int[] subHistogram = tasks.get(i).getHistogram();
                for (int j = 0; j < subHistogram.length; j++) {
                    histogram[j] += subHistogram[j];
                }
            } catch (InterruptedException e) {
            }
        }

        obraz.calculate_histogram();
        obraz.print_histogram();

        System.out.println();
        Obraz.print_histogram(histogram, obraz.getTab_symb());



        // Watek[] NewThr = new Watek[num_threads];

        // for (int i = 0; i < num_threads; i++) {
        //     (NewThr[i] = new Watek(...,obraz_1)).start();
        // }

        // for (int i = 0; i < num_threads; i++) {
        //     try {
        // 	NewThr[i].join();
        //     } catch (InterruptedException e) {}
        // }

    }

}
