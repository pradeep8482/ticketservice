package org.walmart.domain;

import java.util.List;

public class Row implements Comparable{
    private int id;
    private List<Seat> seats;
    private int reservedPerRow;
    private int capacityPerRow;

    public int getReservedPerRow() {
        return reservedPerRow;
    }

    public void setReservedPerRow(int reservedPerRow) {
        this.reservedPerRow = reservedPerRow;
    }

    public int getCapacityPerRow() {
        return capacityPerRow;
    }

    public void setCapacityPerRow(int capacityPerRow) {
        this.capacityPerRow = capacityPerRow;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }


    public Row(int id,int capacityPerRow ,List<Seat> seats){
        this.id = id;
        this.seats = seats;
        this.capacityPerRow = capacityPerRow;
        for(int i=1;i<=capacityPerRow;i++){
            Seat seat = new Seat();
            seat.setSeatId(i);
            seats.add(seat);
        }
    }


    @Override
    public int compareTo(Object o) {
        return new Integer(((Row)o).getId()).compareTo(new Integer(this.getId()));
    }
}
