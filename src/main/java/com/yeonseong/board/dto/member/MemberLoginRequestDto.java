package com.yeonseong.board.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@AllArgsConstructor
public class MemberLoginRequestDto {

  private String name;

  private String password;

}
