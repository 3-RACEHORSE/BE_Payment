package com.skyhorsemanpower.payment.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class GenerateRandom {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    private static String string(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String auctionUuid() {
        return String.format("%s-%s",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")),
            string(10));
    }

    public static String paymentUuid() {
        String character = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder paymentUuid = new StringBuilder();
        Random random = new Random();
        paymentUuid.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        paymentUuid.append("-");
        for (int i = 0; i < 9; i++) {
            paymentUuid.append(character.charAt(random.nextInt(character.length())));
        }

        return paymentUuid.toString();
    }

    public static String memberUuid() {
        return UUID.randomUUID().toString();
    }

    public static String sellerUuid() {
        return UUID.randomUUID().toString();
    }

    public static String sellerHandle() {
        return String.format("@user-%s", string(9));
    }
}
