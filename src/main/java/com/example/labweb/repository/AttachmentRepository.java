package com.example.labweb.repository;

import com.example.labweb.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query(value = "select * from attachment where board_id = :id", nativeQuery = true)
    public List<Attachment> findByBoardId(@Param(value = "id") Long id);

    @Query(value = "select * from attachment where origin_path = :path", nativeQuery = true)
    public List<Attachment> findByOriginPath(@Param(value = "path") String path);
}
