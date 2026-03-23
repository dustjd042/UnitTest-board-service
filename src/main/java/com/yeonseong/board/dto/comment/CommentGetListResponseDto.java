package com.yeonseong.board.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentGetListResponseDto {

  private Integer totalSize;

  private List<CommentGetListResponseDto.CommentGetList> list;

  @Getter
  @AllArgsConstructor
  public static class CommentGetList {

    private Long commentId;

    private String commentContent;

    private String memberName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentCreateDt;
  }
}
