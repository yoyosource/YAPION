// SPDX-License-Identifier: Apache-2.0
// YAPION
// Copyright (C) 2019,2020 yoyosource

package yapion.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ReferenceIDUtils {

    public static long calc(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(s.getBytes(StandardCharsets.UTF_8));
            // System.out.println("MD5: " + s);
            // System.out.println((long)bytes[0] << 56);
            // System.out.println((long)bytes[1] << 48);
            // System.out.println((long)bytes[2] << 40);
            // System.out.println((long)bytes[3] << 32);
            // System.out.println((long)bytes[4] << 24);
            // System.out.println((long)bytes[5] << 16);
            // System.out.println((long)bytes[6] << 8);
            // System.out.println((long)bytes[7] << 0);
            // System.out.println((long)bytes[0] << 56 | (long)bytes[1] << 48 | (long)bytes[2] << 40 | (long)bytes[3] << 32 | (long)bytes[4] << 24 | (long)bytes[5] << 16 | (long)bytes[6] << 8 | (long)bytes[7]);
            return (long)bytes[0] << 56 | (long)bytes[1] << 48 | (long)bytes[2] << 40 | (long)bytes[3] << 32 | (long)bytes[4] << 24 | (long)bytes[5] << 16 | (long)bytes[6] << 8 | (long)bytes[7];
        } catch (NoSuchAlgorithmException e) {
            return 0x0000000000000000L;
        }
    }

    public static String format(long l) {
        return String.format("%016X", l);
    }

    public static String formatLazy(long l) {
        return String.format("%01X", l);
    }

}