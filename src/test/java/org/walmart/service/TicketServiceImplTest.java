package org.walmart.service;

import org.junit.Assert;
import org.junit.Test;
import org.walmart.domain.SeatHold;
import org.walmart.exception.TicketServiceException;

import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.*;

public class TicketServiceImplTest {
    @Test
    public void numSeatsAvailable() {
        Instant t0 = Instant.EPOCH;
        String customer = "pradeep@email.com";
        int capacity = 20;
        Duration holdLength = Duration.ofDays(1);
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        Assert.assertEquals("Seats Available", 20, ticketService.numSeatsAvailable());
    }

    @Test
    public void findAndHoldSeats() {
        Instant t0 = Instant.EPOCH;
        String customerEmail = "pradeep@email.com";
        int capacity = 20;
        Duration holdLength = Duration.ofDays(1);
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        try {
            SeatHold seatHold = ticketService.findAndHoldSeats(20, customerEmail);
            Assert.assertTrue(seatHold != null);
            Assert.assertEquals("Check email", customerEmail, seatHold.getCustomerEmail());

        } catch (TicketServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkholdSeats() {
        Duration holdLength = Duration.ofDays(1);

        Instant expiredHold = Instant.EPOCH.minus(holdLength);

        String customerEmail = "pradeep@email.com";
        int capacity = 20;
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        try {
            SeatHold seatHold = ticketService.findAndHoldSeats(20, customerEmail);
            seatHold.isValidHold(expiredHold);
            Assert.assertEquals("Hold Expiration",capacity,ticketService.numSeatsAvailable());

        } catch (TicketServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reserveSeats() throws TicketServiceException{
        String customerEmail = "pradeep@email.com";
        int capacity = 20;
        Duration holdLength = Duration.ofDays(1);
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        SeatHold seatHold = ticketService.findAndHoldSeats(capacity,customerEmail);
        String confirmation = ticketService.reserveSeats(seatHold.getId(),customerEmail);
        Assert.assertNotNull(confirmation);
    }

    @Test
    public void multipleHoldAndReserveSeats() throws TicketServiceException{
        Instant first = Instant.now();

        String customerEmail = "pradeep@email.com";
        int capacity = 20;
        Duration holdLength = Duration.ofDays(1);
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        SeatHold seatHold1 = ticketService.findAndHoldSeats(2,customerEmail);
        seatHold1.setExpiry(Instant.EPOCH.minus(holdLength));
        SeatHold seatHold = ticketService.findAndHoldSeats(1,customerEmail);
        //Without Expired Hold
        seatHold.setExpiry(Instant.EPOCH.minus(holdLength));
        Assert.assertEquals(17,ticketService.numSeatsAvailable());
        ticketService.reserveSeats(seatHold1.getId(),customerEmail);
        Assert.assertEquals(17,ticketService.numSeatsAvailable());
        //With Expired Hold
        seatHold.setExpiry(Instant.now().plus(Duration.ofDays(2)));
        ticketService.reserveSeats(seatHold.getId(),customerEmail);
        Assert.assertEquals(18,ticketService.numSeatsAvailable());

    }

    @Test
    public void checkInvalidCustomerEmail() throws TicketServiceException{
        String customerEmail = "pradeep@email.com";
        int capacity = 20;
        Duration holdLength = Duration.ofDays(1);
        TicketServiceImpl ticketService = new TicketServiceImpl(4, 5, holdLength);
        SeatHold seatHold = ticketService.findAndHoldSeats(capacity,customerEmail);
        try{
            String confirmation = ticketService.reserveSeats(seatHold.getId(),"deep@email.com");
        }catch (TicketServiceException e){
            Assert.assertEquals("Please provide correct customerEmail",e.getMessage());
        }
    }
}