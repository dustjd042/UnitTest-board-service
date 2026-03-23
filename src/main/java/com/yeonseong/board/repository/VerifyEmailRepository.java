package com.yeonseong.board.repository;

import com.yeonseong.board.entity.VerifyEmailEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyEmailRepository {

  int save(VerifyEmailEntity verifyEmailEntity);

  int updateStatusByEmailAndVerifyCode(VerifyEmailEntity verifyEmailEntity);

  Optional<VerifyEmailEntity> findByEmailAndVerifyCode(String email, String verifyCode);
}
