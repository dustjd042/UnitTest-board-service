package com.yeonseong.board.entity;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonEntity {

  private Long id;

  private Long createId;

  private LocalDateTime createDt;

  private Long modifyId;

  private LocalDateTime modifyDt;

  protected void setCreate(Long createId) {
    this.createId = createId;
    this.createDt = LocalDateTime.now();
  }

  protected void setModify(Long modifyId) {
    this.modifyId = modifyId;
    this.modifyDt = LocalDateTime.now();
  }
}
