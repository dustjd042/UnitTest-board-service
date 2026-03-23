package com.yeonseong.board.repository;

import com.yeonseong.board.entity.MemberEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

  int save(MemberEntity memberEntity);

  int updateRefreshTokenAndLoginDtById(MemberEntity memberEntity);

  int updatePasswordById(MemberEntity memberEntity);

  int updateStatusById(MemberEntity memberEntity);

  boolean existsByName(String name);

  boolean existsByEmail(String email);

  Optional<MemberEntity> findById(Long id);

  Optional<MemberEntity> findByName(String name);

  Optional<MemberEntity> findByEmail(String email);
}