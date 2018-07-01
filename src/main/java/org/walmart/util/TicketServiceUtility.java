package org.walmart.util;

import java.util.Random;

public class TicketServiceUtility {

    private static final Random random = new Random();
    public static String getConfirmationNumber(){
        return new Integer(random.nextInt()+5).toString();
    }
}
