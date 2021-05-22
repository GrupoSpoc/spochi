package com.spochi.repository;

import com.spochi.entity.User;

import java.util.Optional;

public interface UserRepository extends EntityRepository<User> {
    Optional<User> findByUid(String uid);
    Optional<User> findByNickname(String nickname);
}
