package com.book.backend.domain.message.repository;

import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.entity.Opentalk;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.userOpentalk.entity.UserOpentalk;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop200ByOrderByCreatedAtDesc();

    // 메세지 페이지네이션
//    Page<Message> findAll(Pageable pageable);
}
