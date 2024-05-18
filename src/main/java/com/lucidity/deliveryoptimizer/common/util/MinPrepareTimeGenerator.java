package com.lucidity.deliveryoptimizer.common.util;

import java.util.Random;

public class MinPrepareTimeGenerator {
    public static long generateMinOrderPrepareTime() {
        Random random = new Random();

        // Generating a random integer between 0 and 30
        int randomNumber = random.nextInt(31); // Generates numbers from 0 to 30

        return (long) randomNumber;
    }
}
