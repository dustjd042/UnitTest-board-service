package com.yeonseong.board.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardGetListResponseDto {

  private Integer totalSize;

  private List<BoardGetList> list;

  @Getter
  @AllArgsConstructor
  public static class BoardGetList {

    private Long boardId;

    private String boardTitle;

    private String memberName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime boardCreateDt;
  }
}
