package org.walmart.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceIdGenerator {
    private AtomicInteger id = new AtomicInteger();

    public int getId(){
        return id.incrementAndGet();
    }
}
