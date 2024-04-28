package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj liczbe wątków:");
        int threads = scanner.nextInt();

        CalkaExecutor calkaExecutor = new CalkaExecutor(threads);

        double xp = 0;
        double xk = Math.PI;
        double dx = 0.000001;

//        CalkaCallable calkaCallable = new CalkaCallable(xp, xk, dx);
//        double result = calkaCallable.compute_integral();
//        System.out.println("Wynik calkowania: " + result);


        CalkaCallable[] tasks = new CalkaCallable[threads];

        double lengthPerThread = (xk - xp) / threads;

        for (int i = 0; i < threads; i++) {
            double a = xp + i * lengthPerThread;
            double b = (i + 1 == threads) ? xk : a + lengthPerThread;
            tasks[i] = new CalkaCallable(a, b, dx);
        }

        double result2 = calkaExecutor.execute(tasks);
        System.out.println("Wynik calkowania: " + result2);
    }
}