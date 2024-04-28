package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalkaExecutor {
    private final ExecutorService executorService;

    public CalkaExecutor(int threads) {
        this.executorService = Executors.newFixedThreadPool(threads);
    }

    public double execute(CalkaCallable[] tasks) {
        List<Future<Double>> futures = new ArrayList<>();
        for (CalkaCallable task : tasks) {
            Future<Double> result = executorService.submit(task);
            futures.add(result);
        }

        double result = 0;
        for (Future<Double> future : futures) {
            try {
                Double subResult = future.get();
                result += subResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        return result;
    }
}
