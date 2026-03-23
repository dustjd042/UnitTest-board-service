package com.yeonseong.board.service;

import static com.yeonseong.board.helper.TestSecurityHelper.runWithAuthentication;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.yeonseong.board.construct.CommentStatusCode;
import com.yeonseong.board.dto.board.BoardCreateRequestDto;
import com.yeonseong.board.dto.comment.CommentCreateRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListRequestDto;
import com.yeonseong.board.dto.comment.CommentGetListResponseDto;
import com.yeonseong.board.dto.comment.CommentUpdateRequestDto;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.entity.BoardEntity;
import com.yeonseong.board.entity.CommentEntity;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.repository.fake.BoardRepositoryFake;
import com.yeonseong.board.repository.fake.CommentRepositoryFake;
import com.yeonseong.board.repository.fake.MemberRepositoryFake;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommentServiceTest {

  private MemberRepositoryFake memberRepositoryFake;
  private BoardRepositoryFake boardRepositoryFake;
  private CommentRepositoryFake commentRepositoryFake;
  private CommentService commentService;

  private MemberEntity memberEntity;
  private BoardEntity boardEntity;

  @BeforeEach
  public void setUp() {
    memberRepositoryFake = new MemberRepositoryFake();
    boardRepositoryFake = new BoardRepositoryFake();
    commentRepositoryFake = new CommentRepositoryFake();
    commentService = new CommentService(commentRepositoryFake, boardRepositoryFake, memberRepositoryFake);

    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberEntity = new MemberEntity(new BCryptPasswordEncoder(), memberJoinRequestDto);
    memberRepositoryFake.save(memberEntity);

    BoardCreateRequestDto boardCreateRequestDto =
        new BoardCreateRequestDto(
            "테스트 게시글입니다."
            , "안녕하세요. 테스트 게시글을 등록하고 있습니다."
        );
    boardEntity = new BoardEntity(boardCreateRequestDto, memberEntity);
    boardRepositoryFake.save(boardEntity);
  }

  @Test
  public void 댓글_작성() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given

      // when
      CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("테스트 댓글 입니다.", boardEntity.getId());
      commentService.create(commentCreateRequestDto);

      // then
      CommentEntity commentEntity = commentRepositoryFake.findByLast();
      assertThat(commentCreateRequestDto.getContent()).isEqualTo(commentEntity.getContent());
      assertThat(CommentStatusCode.CREATE).isEqualTo(commentEntity.getStatus());
      assertThat(boardEntity).isEqualTo(commentEntity.getBoardEntity());
      assertThat(memberEntity).isEqualTo(boardEntity.getMemberEntity());
    });
  }

  @Test
  public void 댓글_수정() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given
      CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("테스트 댓글 입니다.", boardEntity.getId());
      commentService.create(commentCreateRequestDto);
      CommentEntity commentEntity = commentRepositoryFake.findByLast();

      // when
      String update = " (수정)";
      commentService.update(commentEntity.getId(), new CommentUpdateRequestDto(commentCreateRequestDto.getContent() + update));

      // then
      commentEntity = commentRepositoryFake.findByLast();
      assertThat(commentCreateRequestDto.getContent() + update).isEqualTo(commentEntity.getContent());
    });
  }

  @Test
  public void 댓글_삭제() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given
      CommentCreateRequestDto commentCreateRequestDto = new CommentCreateRequestDto("테스트 댓글 입니다.", boardEntity.getId());
      commentService.create(commentCreateRequestDto);
      CommentEntity commentEntity = commentRepositoryFake.findByLast();

      // when
      commentService.delete(commentEntity.getId());

      // then
      commentEntity = commentRepositoryFake.findByLast();
      assertThat(CommentStatusCode.DELETE).isEqualTo(commentEntity.getStatus());
    });
  }

  @Test
  public void 댓글_리스트_조회() {

    String content = "테스트 댓글 입니다.";
    int totalSize = 10;
    IntStream.range(0, totalSize).forEach(i -> {
      CommentEntity commentEntity = new CommentEntity(
          new CommentCreateRequestDto(content + i, boardEntity.getId())
          , memberEntity, boardEntity
      );
      commentRepositoryFake.save(commentEntity);
    });

    int size = 5;
    CommentGetListResponseDto commentGetListResponseDto = commentService.getList(
        new CommentGetListRequestDto(1, size, boardEntity.getId(), null)
    );

    assertThat(totalSize).isEqualTo(commentGetListResponseDto.getTotalSize());
    assertThat(size).isEqualTo(commentGetListResponseDto.getList().size());
  }
}