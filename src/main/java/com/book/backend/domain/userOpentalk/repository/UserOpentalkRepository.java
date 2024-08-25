package com.book.backend.domain.userOpentalk.repository;

import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOpentalkRepository extends JpaRepository<UserOpentalk, Long> {
    List<UserOpentalk> findAllByUserId(User user);
    UserOpentalk findByUserAndOpentalk(User user, Opentalk opentalk);
}
