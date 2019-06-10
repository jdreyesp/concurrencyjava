package com.examples.jdreyesp.concurrency;

import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * This examples shows a completion service serving @see java.util.concurrent.FutureTask when done.
 */
public class CompletionServiceRenderer {


    private static final int NUM_MESSAGES = 10;

    private final ExecutorService executorService;

    CompletionServiceRenderer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    void renderMessages() throws ExecutionException {
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        for(int i=0; i<NUM_MESSAGES; i++) {
            completionService.submit(() -> {
                Random random = new Random();
                Thread.sleep(random.nextInt(10000));
                return Thread.currentThread().getName();
            });
        }

        try {
            for(int i=0; i<NUM_MESSAGES; i++) {
                Future<String> f = completionService.take();
                String message = String.format("Message received. Thread %s has completed!", f.get());
                System.out.println(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }
}
