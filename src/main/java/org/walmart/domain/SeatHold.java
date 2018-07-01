package org.walmart.domain;

import org.walmart.util.SequenceIdGenerator;

import java.time.Instant;

public class SeatHold {
    private int id;
    private int heldSeatCount;
    private Instant expiry;
    private String customerEmail;
    private static SequenceIdGenerator sequenceIdGenerator = new SequenceIdGenerator();

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getId() {
        return id;
    }

    public int getHeldSeatCount() {
        return heldSeatCount;
    }

    public static SequenceIdGenerator getSequenceIdGenerator() {
        return sequenceIdGenerator;
    }

    public SeatHold(int heldSeatCount, Instant expiry, String customerEmail) {
        this.id = sequenceIdGenerator.getId();
        this.heldSeatCount = heldSeatCount;
        this.expiry = expiry;
        this.customerEmail = customerEmail;
    }

    public boolean isValidHold(Instant now) {
        return expiry.isBefore(now);
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public Instant getExpiry() {
        return expiry;
    }

}
