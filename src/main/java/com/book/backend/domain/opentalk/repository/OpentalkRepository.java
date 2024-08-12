package com.book.backend.domain.opentalk.repository;

import com.book.backend.domain.opentalk.entity.Opentalk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpentalkRepository extends JpaRepository<Opentalk, Long> {
    Optional<Opentalk> findByOpentalkId(long opentalkId);
}
