1)Created simple quickstart Maven Project
2)Domain objects contain SeatHold, Row and Seat
Assumptions:
------------
Each SeatHold has heldSeatCount for the hold, customer email and expiry(which is used to check is particular hold is valid or not) fields
Each Row will have many seats. Also it has reservedPerRow, which tells how many seats are already reserved for this row
Each seat has id and confirmation number

3) Created TicketServiceException as Runtime exception and is thrown
  a)When customerEmail doesn't match during reservation operation
  b)When numberOfSeats is more than totalAvailableSeats during findAndHold
  c)When numberOfSeats is less than 0 during findAndHold
  d)When seatHoldId or customer email is not passed during reservation operation

4) Used Streams, Optional, Instant and Duration features from Java8

5)Written test cases to cover the 3 methods written in TicketServiecImpl
   Example: reserving seats as holding multiple seats with different holds and able to
                  reserve only one hold seats and other hold is expired.

    Also covered with other few test scenarios







