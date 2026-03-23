package com.yeonseong.board.service;

import com.yeonseong.board.construct.BoardResponsePopupMessage;
import com.yeonseong.board.construct.BoardResponseResultType;
import com.yeonseong.board.construct.NoticeSendTypeCode;
import com.yeonseong.board.dto.notice.NoticeSendVerifyEmailRequestDto;
import com.yeonseong.board.dto.notice.NoticeVerifyEmailRequestDto;
import com.yeonseong.board.entity.VerifyEmailEntity;
import com.yeonseong.board.exception.BoardException;
import com.yeonseong.board.repository.VerifyEmailRepository;
import com.yeonseong.board.service.send.NoticeSendService;
import com.yeonseong.board.util.RandomUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

  private final VerifyEmailRepository verifyEmailRepository;
  private final Map<String, NoticeSendService> noticeSendServiceMap;

  public void sendVerifyEmail(NoticeSendVerifyEmailRequestDto noticeSendVerifyEmailRequestDto) {

    String verifyCode = RandomUtil.generateRandomString(8);

    noticeSendServiceMap.get(NoticeSendTypeCode.EMAIL.getBeanName())
        .send(noticeSendVerifyEmailRequestDto.getEmail(), "이메일 인증 번호", verifyCode);

    VerifyEmailEntity verifyEmailEntity = new VerifyEmailEntity(noticeSendVerifyEmailRequestDto, verifyCode);
    verifyEmailRepository.save(verifyEmailEntity);
  }

  public void verifyEmail(NoticeVerifyEmailRequestDto noticeVerifyEmailRequestDto) {

    VerifyEmailEntity verifyEmailEntity = verifyEmailRepository.findByEmailAndVerifyCode(noticeVerifyEmailRequestDto.getEmail(), noticeVerifyEmailRequestDto.getVerifyCode())
        .orElseThrow(()->new BoardException(BoardResponseResultType.FAIL, BoardResponsePopupMessage.NOT_FOUND_VERIFY_EMAIL));

    verifyEmailEntity.verifyEmail();
    verifyEmailRepository.updateStatusByEmailAndVerifyCode(verifyEmailEntity);
  }
}
