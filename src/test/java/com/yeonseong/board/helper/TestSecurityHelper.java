package com.yeonseong.board.helper;

import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestSecurityHelper {

  public static void runWithAuthentication(Long memberId, Runnable action) {

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        memberId.toString(), null, Collections.emptyList());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    try {
      action.run();
    } finally {
      SecurityContextHolder.clearContext();
    }
  }
}
