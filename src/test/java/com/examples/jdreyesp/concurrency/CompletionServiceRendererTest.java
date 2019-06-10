package com.examples.jdreyesp.concurrency;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static junit.framework.TestCase.fail;

public class CompletionServiceRendererTest {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    final CompletionServiceRenderer sut = new CompletionServiceRenderer(executorService);

    @Test
    public void shouldRenderAllMessages() {
        try {
            sut.renderMessages();
        } catch (ExecutionException e) {
            fail(e.getMessage());
        }
    }
}
