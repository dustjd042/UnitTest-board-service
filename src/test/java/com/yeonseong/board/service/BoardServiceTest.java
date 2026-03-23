package com.yeonseong.board.service;

import static com.yeonseong.board.helper.TestSecurityHelper.runWithAuthentication;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.yeonseong.board.construct.BoardStatusCode;
import com.yeonseong.board.dto.board.BoardCreateRequestDto;
import com.yeonseong.board.dto.board.BoardGetListRequestDto;
import com.yeonseong.board.dto.board.BoardGetListResponseDto;
import com.yeonseong.board.dto.board.BoardGetResponseDto;
import com.yeonseong.board.dto.board.BoardUpdateRequestDto;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.entity.BoardEntity;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.repository.fake.BoardRepositoryFake;
import com.yeonseong.board.repository.fake.MemberRepositoryFake;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BoardServiceTest {

  private MemberRepositoryFake memberRepositoryFake;
  private BoardRepositoryFake boardRepositoryFake;
  private BoardService boardService;

  private MemberEntity memberEntity;

  @BeforeEach
  public void setUp() {
    memberRepositoryFake = new MemberRepositoryFake();
    boardRepositoryFake = new BoardRepositoryFake();
    boardService = new BoardService(boardRepositoryFake, memberRepositoryFake);

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
  }

  @Test
  public void 게시글_작성() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given

      // when
      BoardCreateRequestDto boardCreateRequestDto =
          new BoardCreateRequestDto(
              "테스트 게시글입니다."
              , "안녕하세요. 테스트 게시글을 등록하고 있습니다."
          );
      boardService.create(boardCreateRequestDto);

      // then
      BoardEntity boardEntity = boardRepositoryFake.findByLast();
      assertThat(boardCreateRequestDto.getTitle()).isEqualTo(boardEntity.getTitle());
      assertThat(boardCreateRequestDto.getContent()).isEqualTo(boardEntity.getContent());
      assertThat(BoardStatusCode.CREATE).isEqualTo(boardEntity.getStatus());
      assertThat(memberEntity).isEqualTo(boardEntity.getMemberEntity());
    });
  }

  @Test
  public void 게시글_조회() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given
      BoardCreateRequestDto boardCreateRequestDto =
          new BoardCreateRequestDto(
              "테스트 게시글입니다."
              , "안녕하세요. 테스트 게시글을 등록하고 있습니다."
          );
      boardService.create(boardCreateRequestDto);
      BoardEntity boardEntity = boardRepositoryFake.findByLast();

      // when
      BoardGetResponseDto boardGetResponseDto = boardService.get(boardEntity.getId());

      // then
      assertThat(boardCreateRequestDto.getTitle()).isEqualTo(boardGetResponseDto.getBoardTitle());
      assertThat(boardCreateRequestDto.getContent()).isEqualTo(boardGetResponseDto.getBoardContent());
      assertThat(memberEntity.getId()).isEqualTo(boardGetResponseDto.getMemberId());
      assertThat(memberEntity.getName()).isEqualTo(boardGetResponseDto.getMemberName());
    });
  }

  @Test
  public void 게시글_수정() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given
      BoardCreateRequestDto boardCreateRequestDto =
          new BoardCreateRequestDto(
              "테스트 게시글입니다."
              , "안녕하세요. 테스트 게시글을 등록하고 있습니다."
          );
      boardService.create(boardCreateRequestDto);
      BoardEntity boardEntity = boardRepositoryFake.findByLast();

      // when
      BoardUpdateRequestDto boardUpdateRequestDto =
          new BoardUpdateRequestDto(
              boardCreateRequestDto.getTitle() + " 수정"
              , boardCreateRequestDto.getContent() + " 수정"
          );
      boardService.update(boardEntity.getId(), boardUpdateRequestDto);

      // then
      boardEntity = boardRepositoryFake.findByLast();
      assertThat(boardUpdateRequestDto.getTitle()).isEqualTo(boardEntity.getTitle());
      assertThat(boardUpdateRequestDto.getContent()).isEqualTo(boardEntity.getContent());
    });
  }

  @Test
  public void 게시글_삭제() {

    runWithAuthentication(memberEntity.getId(), () -> {

      // given
      BoardCreateRequestDto boardCreateRequestDto =
          new BoardCreateRequestDto(
              "테스트 게시글입니다."
              , "안녕하세요. 테스트 게시글을 등록하고 있습니다."
          );
      boardService.create(boardCreateRequestDto);
      BoardEntity boardEntity = boardRepositoryFake.findByLast();

      // when
      boardService.delete(boardEntity.getId());

      // then
      boardEntity = boardRepositoryFake.findByLast();
      assertThat(BoardStatusCode.DELETE).isEqualTo(boardEntity.getStatus());
    });
  }

  @Test
  public void 게시글_리스트_조회() {

    // given
    String title = "테스트 게시글입니다.";
    String content = "안녕하세요. 테스트 게시글을 등록하고 있습니다.";
    int totalSize = 10;
    IntStream.range(0, totalSize).forEach(i -> {
      BoardEntity boardEntity = new BoardEntity(
          new BoardCreateRequestDto(title + i, content + i)
          , memberEntity
      );
      boardRepositoryFake.save(boardEntity);
    });

    // when
    int size = 5;
    BoardGetListResponseDto boardGetListResponseDto = boardService.getList(
        new BoardGetListRequestDto(1, size, null, null)
    );

    // then
    assertThat(totalSize).isEqualTo(boardGetListResponseDto.getTotalSize());
    assertThat(size).isEqualTo(boardGetListResponseDto.getList().size());
  }

  @Test
  public void 게시글_리스트_조건_조회() {

    // given
    String title = "테스트 게시글입니다.";
    String content = "안녕하세요. 테스트 게시글을 등록하고 있습니다.";
    int totalSize = 20;
    IntStream.range(0, totalSize).forEach(i -> {
      BoardEntity boardEntity = new BoardEntity(
          new BoardCreateRequestDto(title + i, content + i)
          , memberEntity
      );
      boardRepositoryFake.save(boardEntity);
    });

    // when
    BoardGetListResponseDto boardGetListResponseDto = boardService.getList(
        new BoardGetListRequestDto(2, 10, "1", null)
    );

    // then
    assertThat(11).isEqualTo(boardGetListResponseDto.getTotalSize());
    assertThat(1).isEqualTo(boardGetListResponseDto.getList().size());
  }
}