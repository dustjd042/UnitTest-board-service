package com.yeonseong.board.util;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.exception.BoardException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  private SecurityUtils() { throw new IllegalStateException("Utility class"); }

  public static final Long SYSTEM_MEMBER_ID = 0L;

  public static Long getMemberId() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null || "anonymousUser".equals(authentication.getName())) {
      throw new BoardException(BoardResponseResultType.UNAUTHORIZED, BoardResponsePopupMessage.UNAUTHORIZED);
    }

    try {
      return Long.parseLong(authentication.getName());
    } catch(Exception e) {
      throw new BoardException(BoardResponseResultType.UNAUTHORIZED, BoardResponsePopupMessage.UNAUTHORIZED);
    }
  }
}
