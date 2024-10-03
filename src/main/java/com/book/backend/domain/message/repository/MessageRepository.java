package com.book.backend.domain.message.repository;

import com.book.backend.domain.message.entity.Message;
import com.book.backend.domain.opentalk.entity.Opentalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop200ByOrderByCreatedAtDesc();
    List<Message> findTop50ByOrderByCreatedAtDesc();

    // 특정 opentalk Id 를 갖는 데이터 조회 (페이지네이션)
    Page<Message> findAllByOpentalk(Opentalk opentalk, Pageable pageable);
}
