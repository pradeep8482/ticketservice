package org.walmart.service;

import org.walmart.util.TicketServiceUtility;
import org.walmart.domain.Row;
import org.walmart.domain.Seat;
import org.walmart.domain.SeatHold;
import org.walmart.exception.TicketServiceException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TicketServiceImpl implements TicketService {

    private final Duration holdTime;
    private final int capacity;
    private int reservedCount;
    private Map<Integer,SeatHold> seatHoldMap = new HashMap<>();
    private Set<Row> rows = new TreeSet<>();
    public TicketServiceImpl(int noOfRows, int seatsPerRow,Duration holdTime){
        this.holdTime = holdTime;
        this.capacity = noOfRows*seatsPerRow;

        //build Rows and Seats
        for(int i=1;i<=noOfRows;i++){
            rows.add(new Row(i,seatsPerRow,new ArrayList<>(seatsPerRow)));
        }
    }

    public int numSeatsAvailable(){
        Stream<Map.Entry<Integer,SeatHold>> seatHoldStream = seatHoldMap.entrySet().stream().filter(eachHold -> eachHold.getValue().isValidHold(Instant.now()));
        int totalHoldSeats = seatHoldStream.mapToInt(eachSeatHold -> eachSeatHold.getValue().getHeldSeatCount()).sum();
        return capacity - totalHoldSeats - reservedCount;
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if(numSeats < 0){
           throw new TicketServiceException("Number of Seats should be greater than 0");
        }else if(numSeats > numSeatsAvailable()){
            throw new TicketServiceException(numSeats+ "Number of Seats are not available");
        }

        SeatHold seatHold = new SeatHold(numSeats,Instant.now().plus(holdTime),customerEmail);
        seatHoldMap.put(seatHold.getId(),seatHold);
        return seatHold;
    }

    public String reserveSeats(int seatHoldId,String customerEmail) {
        SeatHold seatHold = seatHoldMap.remove(seatHoldId);
        Optional.ofNullable(seatHold).orElseThrow(() -> new TicketServiceException("Couldn't find Seat Hold for given seat hold Id"));
        Optional.ofNullable(customerEmail).orElseThrow(() -> new TicketServiceException("Please provide customerEmail"));

        if(!seatHold.getCustomerEmail().equalsIgnoreCase(customerEmail)){
           throw new TicketServiceException("Please provide correct customerEmail");
        }

        String confirmationNumber = TicketServiceUtility.getConfirmationNumber();
        if(seatHold.isValidHold(Instant.now())){
            int holdSeats = seatHold.getHeldSeatCount();
            reservedCount +=holdSeats;
            Set<Row> streamRows = rows.stream().filter(row -> (row.getCapacityPerRow() - row.getReservedPerRow()) < seatHold.getHeldSeatCount()).collect(Collectors.toSet());

            for(Row rowForReservation: streamRows){
                int currentReservedSeatsForRow = rowForReservation.getReservedPerRow();
                List<Seat> seatsList = rowForReservation.getSeats();
                if(holdSeats!=0){
                    for(Seat seat:seatsList){
                        seat.setConfirmation(confirmationNumber);
                        currentReservedSeatsForRow++;
                        holdSeats--;
                    }
                }
                rowForReservation.setReservedPerRow(currentReservedSeatsForRow);
            }
        }


        return confirmationNumber;


    }
}
