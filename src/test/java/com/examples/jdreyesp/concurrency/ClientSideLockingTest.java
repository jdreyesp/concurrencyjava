package com.examples.jdreyesp.concurrency;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * We assure in this test that the list is well managed by the client lock
 */
public class ClientSideLockingTest {

    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private static final int NUM_PRODUCERS = 100;
    private static final int NUM_CONSUMERS = 100;

    private CyclicBarrier barrier = new CyclicBarrier(NUM_CONSUMERS + NUM_PRODUCERS + 1);

    private ClientSideLocking sut = new ClientSideLocking();

    @Test
    public void shouldBehaveWellOnConcurrentProducersAndConsumers() throws BrokenBarrierException, InterruptedException {

        for(int i = 0; i< NUM_PRODUCERS; i++) {
            pool.execute(new Producer());
        }

        for(int i = 0; i< NUM_PRODUCERS; i++) {
            pool.execute(new Consumer());
        }

        System.out.println("Awaiting for producers and consumers to be ready");
        barrier.await(); //Wait for all threads to be ready
        System.out.println("Awaiting for producers and consumers to finish");
        barrier.await(); //Wait for all threads to finish
        System.out.println(String.format("Generated list: %s", Arrays.toString(sut.getElements().toArray())));
        assertEquals(100, sut.getElements().size());
    }

    private class Producer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                String element = Thread.currentThread().getName();

                System.out.println(String.format("Producer %s ready", element));
                barrier.await();
                sut.putOnList(element);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException | ClientSideLocking.AlreadyOnListException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class Consumer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                String element = Thread.currentThread().getName();
                System.out.println(String.format("Consumer %s ready", element));
                barrier.await();
                sut.getElements();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
