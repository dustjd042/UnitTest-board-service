package com.yeonseong.board.service;

import com.yeonseong.board.config.JwtProvider;
import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.dto.member.MemberFindNameRequestDto;
import com.yeonseong.board.dto.member.MemberFindNameResponseDto;
import com.yeonseong.board.dto.member.MemberJoinRequestDto;
import com.yeonseong.board.dto.member.MemberLoginRequestDto;
import com.yeonseong.board.dto.member.MemberLoginResponseDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenRequestDto;
import com.yeonseong.board.dto.member.MemberRefreshTokenResponseDto;
import com.yeonseong.board.dto.member.MemberResetPasswordRequestDto;
import com.yeonseong.board.dto.member.MemberResetPasswordResponseDto;
import com.yeonseong.board.entity.MemberEntity;
import com.yeonseong.board.entity.VerifyEmailEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.MemberRepository;
import com.yeonseong.board.repository.VerifyEmailRepository;
import com.yeonseong.board.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final VerifyEmailRepository verifyEmailRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  public void join(MemberJoinRequestDto memberJoinRequestDto) {

    validateDuplicateMember(memberJoinRequestDto.getName(), memberJoinRequestDto.getEmail());

    MemberEntity memberEntity = new MemberEntity(passwordEncoder, memberJoinRequestDto);

    memberRepository.save(memberEntity);
  }

  public MemberFindNameResponseDto findName(MemberFindNameRequestDto memberFindNameRequestDto) {

    MemberEntity memberEntity = memberRepository.findByEmail(memberFindNameRequestDto.getEmail())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    return new MemberFindNameResponseDto(memberEntity.getName());
  }

  public MemberResetPasswordResponseDto resetPassword(MemberResetPasswordRequestDto memberResetPasswordRequestDto) {

    VerifyEmailEntity verifyEmailEntity = verifyEmailRepository.findByEmailAndVerifyCode(memberResetPasswordRequestDto.getEmail(), memberResetPasswordRequestDto.getAuthCode())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_VERIFY_EMAIL));

    verifyEmailEntity.verifyEmailCheck();

    MemberEntity memberEntity = memberRepository.findByEmail(memberResetPasswordRequestDto.getEmail())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    String newPassword = memberEntity.resetPassword(passwordEncoder);
    memberRepository.updatePasswordById(memberEntity);

    return new MemberResetPasswordResponseDto(newPassword);
  }

  public void deleteMember() {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    memberEntity.deleteMember();
    memberRepository.updateStatusById(memberEntity);
  }

  public MemberLoginResponseDto login(MemberLoginRequestDto memberLoginRequestDto) {

    MemberEntity memberEntity = memberRepository.findByName(memberLoginRequestDto.getName())
        .orElseThrow(()-> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    memberEntity.login(passwordEncoder, memberLoginRequestDto.getPassword(), jwtProvider);
    memberRepository.updateRefreshTokenAndLoginDtById(memberEntity);

    return new MemberLoginResponseDto(
        "Bearer"
        , memberEntity.getAccessToken()
        , jwtProvider.ACCESS_TOKEN_EXPIRE
        , memberEntity.getRefreshToken()
        , jwtProvider.REFRESH_TOKEN_EXPIRE
    );
  }

  public MemberRefreshTokenResponseDto refreshToken(MemberRefreshTokenRequestDto memberRefreshTokenRequestDto) {

    if (!jwtProvider.validateToken(memberRefreshTokenRequestDto.getRefreshToken())) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER);
    }

    MemberEntity memberEntity = memberRepository.findById(jwtProvider.getMemberId(memberRefreshTokenRequestDto.getRefreshToken()))
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    memberEntity.token(jwtProvider);
    memberRepository.updateRefreshTokenAndLoginDtById(memberEntity);

    return new MemberRefreshTokenResponseDto(
        "Bearer"
        , memberEntity.getAccessToken()
        , jwtProvider.ACCESS_TOKEN_EXPIRE
        , memberEntity.getRefreshToken()
        , jwtProvider.REFRESH_TOKEN_EXPIRE
    );
  }

  public void logout() {

    MemberEntity memberEntity = memberRepository.findById(SecurityUtils.getMemberId())
        .orElseThrow(() -> new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_MEMBER));

    memberEntity.logout();
    memberRepository.updateRefreshTokenAndLoginDtById(memberEntity);
  }

  private void validateDuplicateMember(String name, String email) {

    if (memberRepository.existsByName(name)) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.DUPLICATE_ACCOUNT);
    }

    if (memberRepository.existsByEmail(email)) {
      throw new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.DUPLICATE_EMAIL);
    }
  }
}