package com.book.backend.domain.libcode.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "libcode")
@Getter
@Setter
public class Libcode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long libcodeId;
}
