package com.yeonseong.board.service;

import static com.yeonseong.board.helper.TestSecurityHelper.runWithAuthentication;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.yeonseong.board.config.JwtProvider;
import com.yeonseong.board.construct.MemberStatusCode;
import com.yeonseong.board.dto.member.MemberFindNameRequestDto;
import com.yeonseong.board.dto.member.MemberFindNameResponseDto;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.dto.member.MemberLoginRequestDto;
import com.yeonseong.board.dto.member.MemberLoginResponseDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenRequestDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenResponseDto;
import com.yeonseong.board.dto.member.MemberResetPasswordRequestDto;
import com.yeonseong.board.dto.member.MemberResetPasswordResponseDto;
import com.yeonseong.board.dto.notice.NoticeSendVerifyEmailRequestDto;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.entity.VerifyEmailEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.fake.MemberRepositoryFake;
import com.yeonseong.board.repository.fake.VerifyEmailRepositoryFake;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberServiceTest {

  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;
  private VerifyEmailRepositoryFake verifyEmailRepositoryFake;
  private MemberRepositoryFake memberRepositoryFake;
  private MemberService memberService;

  @BeforeEach
  public void setUp() {

    passwordEncoder = new BCryptPasswordEncoder();
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String randomSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
    long accessExpire = 1800000;
    long refreshExpire = 1209600000;
    jwtProvider = new JwtProvider(randomSecretKey, accessExpire, refreshExpire);

    verifyEmailRepositoryFake = new VerifyEmailRepositoryFake();
    memberRepositoryFake = new MemberRepositoryFake();
    memberService = new MemberService(memberRepositoryFake, verifyEmailRepositoryFake, jwtProvider, passwordEncoder);
  }

  @Test
  public void 회원가입() {

    // given

    // when
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);

    // then
    MemberEntity memberEntity = memberRepositoryFake.findByLast();
    assertThat(memberJoinRequestDto.getName()).isEqualTo(memberEntity.getName());
    assertThat(passwordEncoder.matches(memberJoinRequestDto.getPassword(), memberEntity.getPassword())).isTrue();
    assertThat(memberJoinRequestDto.getEmail()).isEqualTo(memberEntity.getEmail());
    assertThat(memberJoinRequestDto.getIntro()).isEqualTo(memberEntity.getIntro());
  }

  @Test
  public void 중복_회원가입() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);

    // when
    // then
    assertThrows(BoardException.class, () -> memberService.join(memberJoinRequestDto));
  }

  @Test
  public void 이름_찾기() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);

    // when
    MemberFindNameResponseDto memberFindNameResponseDto = memberService.findName(new MemberFindNameRequestDto(memberJoinRequestDto.getEmail()));

    // then
    assertThat(memberJoinRequestDto.getName()).isEqualTo(memberFindNameResponseDto.getName());
  }

  @Test
  public void 비밀번호_초기화() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);
    String verifyCode = "12345";
    VerifyEmailEntity verifyEmailEntity = new VerifyEmailEntity(new NoticeSendVerifyEmailRequestDto(memberJoinRequestDto.getEmail()), verifyCode);
    verifyEmailEntity.verifyEmail();
    verifyEmailRepositoryFake.save(verifyEmailEntity);

    // when
    MemberResetPasswordResponseDto memberResetPasswordResponseDto =
        memberService.resetPassword(new MemberResetPasswordRequestDto(memberJoinRequestDto.getEmail(), verifyCode));

    // then
    MemberEntity memberEntity = memberRepositoryFake.findByLast();
    assertThat(passwordEncoder.matches(memberJoinRequestDto.getPassword(), memberEntity.getPassword())).isFalse();
    assertThat(passwordEncoder.matches(memberResetPasswordResponseDto.getPassword(), memberEntity.getPassword())).isTrue();
  }

  @Test
  public void 탈퇴() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);

    runWithAuthentication(memberRepositoryFake.findByLast().getId(), () -> {

      // when
      memberService.deleteMember();

      // then
      MemberEntity memberEntity = memberRepositoryFake.findByLast();
      assertThat(MemberStatusCode.LEAVE).isEqualTo(memberEntity.getStatus());
    });
  }

  @Test
  public void 로그인() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);

    // when
    MemberLoginResponseDto memberLoginResponseDto =
        memberService.login(new MemberLoginRequestDto(memberJoinRequestDto.getName(), memberJoinRequestDto.getPassword()));

    // then
    MemberEntity memberEntity = memberRepositoryFake.findByLast();
    assertThat(memberEntity.getId()).isEqualTo(jwtProvider.getMemberId(memberLoginResponseDto.getAccessToken()));
    assertThat(memberEntity.getId()).isEqualTo(jwtProvider.getMemberId(memberLoginResponseDto.getRefreshToken()));
  }

  @Test
  public void 토큰갱신() throws InterruptedException {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);
    MemberLoginResponseDto memberLoginResponseDto =
        memberService.login(new MemberLoginRequestDto(memberJoinRequestDto.getName(), memberJoinRequestDto.getPassword()));
    Thread.sleep(2000);

    // when
    MemberRefreshTokenResponseDto memberRefreshTokenResponseDto =
        memberService.refreshToken(new MemberRefreshTokenRequestDto(memberLoginResponseDto.getRefreshToken()));

    // then
    MemberEntity memberEntity = memberRepositoryFake.findByLast();
    assertThat(memberEntity.getId()).isEqualTo(jwtProvider.getMemberId(memberRefreshTokenResponseDto.getAccessToken()));
    assertThat(memberEntity.getId()).isEqualTo(jwtProvider.getMemberId(memberRefreshTokenResponseDto.getRefreshToken()));
    assertThat(memberLoginResponseDto.getAccessToken()).isNotEqualTo(memberRefreshTokenResponseDto.getAccessToken());
    assertThat(memberLoginResponseDto.getRefreshToken()).isNotEqualTo(memberRefreshTokenResponseDto.getRefreshToken());
  }

  @Test
  public void 로그아웃() {

    // given
    MemberJoinRequestDto memberJoinRequestDto =
        new MemberJoinRequestDto(
            "test"
            , "qwe123!@#"
            , "qwe123!@#"
            , "test@test.com"
            , "안녕하세요. 테스트 가입자입니다."
        );
    memberService.join(memberJoinRequestDto);
    MemberLoginResponseDto memberLoginResponseDto =
        memberService.login(new MemberLoginRequestDto(memberJoinRequestDto.getName(), memberJoinRequestDto.getPassword()));

    runWithAuthentication(memberRepositoryFake.findByLast().getId(), () -> {

      // when
      memberService.logout();

      // then
      MemberEntity memberEntity = memberRepositoryFake.findByLast();
      assertThat(memberEntity.getAccessToken()).isNull();
      assertThat(memberEntity.getRefreshToken()).isNull();
    });
  }
}