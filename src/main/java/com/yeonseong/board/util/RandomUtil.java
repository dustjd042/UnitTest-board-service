package com.yeonseong.board.util;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomUtil {

  private RandomUtil() { throw new IllegalStateException("Utility class"); }

  private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL = "!@#$%^&*()-_=+[]{}<>?~";

  private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIAL;
  private static final SecureRandom random = new SecureRandom();

  public static String generateRandomString(int length) {

    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(ALL_CHARS.length());
      sb.append(ALL_CHARS.charAt(randomIndex));
    }

    return shuffleString(sb.toString());
  }

  private static String shuffleString(String input) {
    List<Character> characters = input.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.toList());
    Collections.shuffle(characters);
    return characters.stream()
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        .toString();
  }
}
