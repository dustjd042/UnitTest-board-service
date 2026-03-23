package com.yeonseong.board.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequestDto {

  private String name;

  private String password;

  private String passwordConfirm;

  private String email;

  private String intro;

}
