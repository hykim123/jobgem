package com.sist.jobgem.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "re_idx", nullable = false)
    private Integer id;

    @Column(name = "jo_idx", nullable = false)
    private Integer joIdx;

    @Column(name = "co_idx", nullable = false)
    private Integer coIdx;

    @Column(name = "re_title", nullable = false, length = 20)
    private String reTitle;

    @Lob
    @Column(name = "re_content", nullable = false)
    private String reContent;

    @Column(name = "re_score", nullable = false)
    private Integer reScore;

    @Column(name = "re_write_date", nullable = false)
    private LocalDate reWriteDate;

    @Column(name = "re_state", nullable = false)
    private Integer reState;

}