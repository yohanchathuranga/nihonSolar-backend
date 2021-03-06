package com.nihon.repository;

import com.nihon.entity.DOProjectElectricityBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.nihon.customrepositories.ProjectElectricityBoardRepositoryCustom;

@Repository
public interface ProjectElectricityBoardRepository extends JpaRepository<DOProjectElectricityBoard, String>,ProjectElectricityBoardRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update electricity_board_process peb set peb.deleted = ?1 where peb.id = ?2", nativeQuery = true)
    int delete(boolean deleted, String id);

    @Query(value = "select case when count(peb.id) > 0 then 'true' else 'false' end from electricity_board_process peb where peb.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select * from electricity_board_process where project_id = ?1 and deleted = false limit 1", nativeQuery = true)
    DOProjectElectricityBoard getItemsByProjectId(String projectId);

    @Transactional
    @Modifying
    @Query(value = "update electricity_board_process peb set peb.deleted = ?1 where peb.project_id = ?2", nativeQuery = true)
    int deleteByProjectId(boolean deleted, String projectId);
}