package com.example.repository;

import com.example.entity.DOProjectElectricityBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.customrepositories.ProjectElectricityBoardRepositoryCustom;

@Repository
public interface ProjectElectricityBoardRepository extends JpaRepository<DOProjectElectricityBoard, String>,ProjectElectricityBoardRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update electricity_board_process peb set peb.deleted = ?1 where peb.id = ?2", nativeQuery = true)
    int setStatus(boolean deleted, String id);

    @Query(value = "select case when count(peb.id) > 0 then 'true' else 'false' end from electricity_board_process peb where peb.id = ?1", nativeQuery = true)
    boolean isExistsById(String id);


}