package com.yeonseong.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import com.yeonseong.board.construct.NoticeSendHistoryStatus;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.construct.SlackChannel;
import com.yeonseong.board.entity.NoticeSendHistoryEntity;
import com.yeonseong.board.repository.fake.NoticeSendHistoryRepositoryFake;
import com.yeonseong.board.service.send.EmailSendService;
import com.yeonseong.board.service.send.SlackSendService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class NoticeSendServiceTest {

  private NoticeSendHistoryRepositoryFake noticeSendHistoryRepositoryFake;
  private Slack slackMock;
  private SlackSendService slackSendService;
  private JavaMailSender javaMailSenderMock;
  private EmailSendService emailSendService;

  @BeforeEach
  public void setUp() {
    noticeSendHistoryRepositoryFake = new NoticeSendHistoryRepositoryFake();
    slackMock = mock(Slack.class);
    slackSendService = new SlackSendService(noticeSendHistoryRepositoryFake, slackMock, "");
    javaMailSenderMock = mock(JavaMailSender.class);
    emailSendService = new EmailSendService(noticeSendHistoryRepositoryFake, javaMailSenderMock);
  }

  @Test
  public void 슬랙_발송_성공() throws IOException {

    // given
    int returnCode = 200;
    WebhookResponse webhookResponseMock = mock(WebhookResponse.class);
    given(webhookResponseMock.getCode()).willReturn(returnCode);
    given(slackMock.send(anyString(), any(Payload.class)))
        .willReturn(webhookResponseMock);

    // when
    String to = SlackChannel.SYSTEM_ALERT.getChannel();
    String title = "테스트 발송";
    String content = "테스트 입니다.";
    slackSendService.send(to, title, content);

    // then
    NoticeSendHistoryEntity noticeSendHistoryEntity = noticeSendHistoryRepositoryFake.findByLast();
    assertThat(NoticeSendTypeCode.SLACK).isEqualTo(noticeSendHistoryEntity.getSendType());
    assertThat(to).isEqualTo(noticeSendHistoryEntity.getTo());
    assertThat(title).isEqualTo(noticeSendHistoryEntity.getTitle());
    assertThat(content).isEqualTo(noticeSendHistoryEntity.getContent());
    assertThat(NoticeSendHistoryStatus.SUCCESS).isEqualTo(noticeSendHistoryEntity.getStatus());
  }

  @Test
  public void 슬랙_발송_실패() throws IOException {

    // given
    int returnCode = 500;
    WebhookResponse webhookResponseMock = mock(WebhookResponse.class);
    given(webhookResponseMock.getCode()).willReturn(returnCode);
    given(slackMock.send(anyString(), any(Payload.class)))
        .willReturn(webhookResponseMock);

    // when
    String to = SlackChannel.SYSTEM_ALERT.getChannel();
    String title = "테스트 발송";
    String content = "테스트 입니다.";
    slackSendService.send(to, title, content);

    // then
    NoticeSendHistoryEntity noticeSendHistoryEntity = noticeSendHistoryRepositoryFake.findByLast();
    assertThat(NoticeSendTypeCode.SLACK).isEqualTo(noticeSendHistoryEntity.getSendType());
    assertThat(to).isEqualTo(noticeSendHistoryEntity.getTo());
    assertThat(title).isEqualTo(noticeSendHistoryEntity.getTitle());
    assertThat(content).isEqualTo(noticeSendHistoryEntity.getContent());
    assertThat(NoticeSendHistoryStatus.FAIL).isEqualTo(noticeSendHistoryEntity.getStatus());
    assertThat(String.valueOf(returnCode)).isEqualTo(noticeSendHistoryEntity.getSendResultDetail());
  }

  @Test
  public void 슬랙_발송_에러() throws IOException {

    // given
    given(slackMock.send(anyString(), any(Payload.class)))
        .willThrow(new IOException("슬랙 발송 에러 발생"));

    // when
    String to = SlackChannel.SYSTEM_ALERT.getChannel();
    String title = "테스트 발송";
    String content = "테스트 입니다.";
    slackSendService.send(to, title, content);

    // then
    NoticeSendHistoryEntity noticeSendHistoryEntity = noticeSendHistoryRepositoryFake.findByLast();
    assertThat(NoticeSendTypeCode.SLACK).isEqualTo(noticeSendHistoryEntity.getSendType());
    assertThat(to).isEqualTo(noticeSendHistoryEntity.getTo());
    assertThat(title).isEqualTo(noticeSendHistoryEntity.getTitle());
    assertThat(content).isEqualTo(noticeSendHistoryEntity.getContent());
    assertThat(NoticeSendHistoryStatus.FAIL).isEqualTo(noticeSendHistoryEntity.getStatus());
  }

  @Test
  public void 이메일_발송_성공() {

    // given

    // when
    String to = "test@test.com";
    String title = "테스트 발송";
    String content = "테스트 입니다.";
    emailSendService.send(to, title, content);

    // then
    NoticeSendHistoryEntity noticeSendHistoryEntity = noticeSendHistoryRepositoryFake.findByLast();
    assertThat(NoticeSendTypeCode.EMAIL).isEqualTo(noticeSendHistoryEntity.getSendType());
    assertThat(to).isEqualTo(noticeSendHistoryEntity.getTo());
    assertThat(title).isEqualTo(noticeSendHistoryEntity.getTitle());
    assertThat(content).isEqualTo(noticeSendHistoryEntity.getContent());
    assertThat(NoticeSendHistoryStatus.SUCCESS).isEqualTo(noticeSendHistoryEntity.getStatus());
  }

  @Test
  public void 이메일_발송_에러() {

    // given
    willThrow(new RuntimeException("메일 발송 에러 발생"))
        .given(javaMailSenderMock).send(any(SimpleMailMessage.class));

    // when
    String to = "test@test.com";
    String title = "테스트 발송";
    String content = "테스트 입니다.";
    emailSendService.send(to, title, content);

    // then
    NoticeSendHistoryEntity noticeSendHistoryEntity = noticeSendHistoryRepositoryFake.findByLast();
    assertThat(NoticeSendTypeCode.EMAIL).isEqualTo(noticeSendHistoryEntity.getSendType());
    assertThat(to).isEqualTo(noticeSendHistoryEntity.getTo());
    assertThat(title).isEqualTo(noticeSendHistoryEntity.getTitle());
    assertThat(content).isEqualTo(noticeSendHistoryEntity.getContent());
    assertThat(NoticeSendHistoryStatus.FAIL).isEqualTo(noticeSendHistoryEntity.getStatus());
  }
}