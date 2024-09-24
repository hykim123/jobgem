package com.sist.jobgem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sist.jobgem.dto.PostCountApplyDto;
import com.sist.jobgem.dto.PostDto;
import com.sist.jobgem.entity.Post;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
        @Query("SELECT new com.sist.jobgem.dto.PostDto(t) FROM Post t WHERE t.poState = 1 AND t.poDeadline > CURRENT_DATE")
        Slice<PostDto> getPostListSlice(Pageable pageable);

        int countByCoIdxAndPoState(int coIdx, int poState);

        @Query("SELECT new com.sist.jobgem.dto.PostCountApplyDto(p.id, p.coIdx, p.poTitle, p.poContent, p.poDate, p.poDeadline, p.poImgurl, p.poSal, p.poSubType, p.poAddr, p.poEmail, p.poFax, p.poState, CAST(COUNT(a) AS INTEGER) as applyCount) "
                        +
                        "FROM Post p LEFT JOIN Applyment a ON p.id = a.poIdx " +
                        "WHERE p.coIdx = :coIdx " +
                        "GROUP BY p.id, p.coIdx, p.poTitle, p.poContent, p.poDate, p.poDeadline, p.poImgurl, p.poSal, p.poSubType, p.poAddr, p.poEmail, p.poFax, p.poState")
        List<PostCountApplyDto> findAllWithApplyCount(@Param("coIdx") int coIdx);

        int countByPoStateAndCoIdx(int poState, int coIdx);

        @Query("SELECT COUNT(*) FROM Post p WHERE p.coIdx = :coIdx AND p.poState != 0")
        int countByCoIdx(@Param("coIdx") int coIdx);

        @Query("SELECT COUNT(*) FROM Post p WHERE p.coIdx = :coIdx AND p.poDeadline = CURRENT_DATE AND p.poState != 0")
        int countByCoIdxAndPoDeadline(@Param("coIdx") int coIdx);

        @Modifying
        @Transactional
        @Query("UPDATE Post p SET p.poState = 2 WHERE p.poDeadline <= CURRENT_DATE")
        void updateStateByDeadline();

        @Query("SELECT p FROM Post p WHERE p.id = :id AND p.coIdx != 0")
        Post findById(@Param("id") int id);

        @Query("SELECT p FROM Post p LEFT JOIN FETCH p.company WHERE p.id = :id")
        PostDto findByIdWithCompany(@Param("id") int id);

        @Query("SELECT p.poTitle FROM Post p WHERE p.id = :id")
        String findTitleById(@Param("id") int id);

        @Modifying
        @Transactional
        @Query("UPDATE Post p SET p.poState = 0 WHERE p.id = :id")
        int updateStateById(@Param("id") int id);

        Page<Post> findByPoTitleContainsAndPoState(String keyword, int poState, Pageable pageable);

        @Query("SELECT p FROM Post p JOIN Company c ON p.coIdx = c.id " +
                        "WHERE p.poState = 1 AND " +
                        "(:type IS NULL OR :value IS NULL OR " +
                        "(:type = 'title' AND p.poTitle LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'company' AND c.coName LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'content' AND p.poContent LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'date' AND CAST(p.poDate AS string) LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'deadline' AND CAST(p.poDeadline AS string) LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'sal' AND p.poSal LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'startTime' AND p.wsStartTime LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'endTime' AND p.wsEndTime LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'type' AND p.poSubType LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'addr' AND p.poAddr LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'email' AND p.poEmail LIKE CONCAT('%', :value, '%')) OR " +
                        "(:type = 'fax' AND p.poFax LIKE CONCAT('%', :value, '%'))" +
                        ")")
        Page<Post> findByTitleOrContent(@Param("type") String type, @Param("value") String value, Pageable pageable);
}
