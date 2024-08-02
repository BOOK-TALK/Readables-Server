package com.book.backend.domain.genre.repository;

import com.book.backend.domain.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByParentGenreKdcNumAndKdcNum(String parentKdcNum, String kdcNum);
}