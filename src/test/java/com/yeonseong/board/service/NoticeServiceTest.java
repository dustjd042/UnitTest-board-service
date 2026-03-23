package com.yeonseong.board.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.construct.VerifyEmailStatusCode;
import com.yeonseong.board.dto.notice.NoticeSendVerifyEmailRequestDto;
import com.yeonseong.board.dto.notice.NoticeVerifyEmailRequestDto;
import com.yeonseong.board.entity.VerifyEmailEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.fake.NoticeSendHistoryRepositoryFake;
import com.yeonseong.board.repository.fake.VerifyEmailRepositoryFake;
import com.yeonseong.board.service.send.EmailSendService;
import com.yeonseong.board.service.send.NoticeSendService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class NoticeServiceTest {

  private NoticeSendHistoryRepositoryFake noticeSendHistoryRepositoryFake;
  private JavaMailSender javaMailSenderMock;
  private EmailSendService emailSendService;
  private VerifyEmailRepositoryFake verifyEmailRepositoryFake;
  private NoticeService noticeService;

  @BeforeEach
  public void setUp() {
    noticeSendHistoryRepositoryFake = new NoticeSendHistoryRepositoryFake();
    javaMailSenderMock = mock(JavaMailSender.class);
    emailSendService = new EmailSendService(noticeSendHistoryRepositoryFake, javaMailSenderMock);
    verifyEmailRepositoryFake = new VerifyEmailRepositoryFake();

    Map<String, NoticeSendService> sendServiceMap = new HashMap<>();
    sendServiceMap.put(NoticeSendTypeCode.EMAIL.getBeanName(), emailSendService);
    noticeService = new NoticeService(verifyEmailRepositoryFake, sendServiceMap);
  }

  @Test
  public void 메일_인증번호_발송() {

    // given
    String email = "test@test.com";
    NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto = new NoticeSendVerifyEmailRequestDto(email);

    // when
    noticeService.sendVerifyEmail(noticeSendVerifyEmailRequestDto);

    // then
    VerifyEmailEntity verifyEmailEntity = verifyEmailRepositoryFake.findByLast();
    assertThat(email).isEqualTo(verifyEmailEntity.getEmail());
    assertThat(VerifyEmailStatusCode.SEND).isEqualTo(verifyEmailEntity.getStatus());
    BDDMockito.then(javaMailSenderMock)
        .should(only())
        .send(any(SimpleMailMessage.class));
  }

  @Test
  public void 메일_인증_성공() {

    // given
    String email = "test@test.com";
    NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto = new NoticeSendVerifyEmailRequestDto(email);
    noticeService.sendVerifyEmail(noticeSendVerifyEmailRequestDto);
    VerifyEmailEntity verifyEmailEntity = verifyEmailRepositoryFake.findByLast();
    verifyEmailRepositoryFake.updateStatusByEmailAndVerifyCode(verifyEmailEntity);

    // when
    noticeService.verifyEmail(new NoticeVerifyEmailRequestDto(email, verifyEmailEntity.getVerifyCode()));

    // then
    verifyEmailEntity = verifyEmailRepositoryFake.findByLast();
    assertThat(email).isEqualTo(verifyEmailEntity.getEmail());
    assertThat(VerifyEmailStatusCode.AUTHORIZE).isEqualTo(verifyEmailEntity.getStatus());
  }

  @Test
  public void 메일_인증_시간초과() {

    // given
    String email = "test@test.com";
    NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto = new NoticeSendVerifyEmailRequestDto(email);
    noticeService.sendVerifyEmail(noticeSendVerifyEmailRequestDto);
    VerifyEmailEntity verifyEmailEntity = verifyEmailRepositoryFake.findByLast();
    verifyEmailRepositoryFake.updateExpiredPast(verifyEmailEntity);

    // when
    // then
    assertThrows(BoardException.class
        , () -> noticeService.verifyEmail(new NoticeVerifyEmailRequestDto(email, verifyEmailEntity.getVerifyCode())));
  }
}
