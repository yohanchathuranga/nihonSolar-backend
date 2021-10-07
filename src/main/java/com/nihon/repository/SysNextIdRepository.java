package com.nihon.repository;

import com.nihon.entity.DOSysNextId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysNextIdRepository extends JpaRepository<DOSysNextId, String> {

    @Transactional
    @Modifying
    @Query(value = "update sys_next_id sn set sn.next_id = ?1 where sn.type = ?2", nativeQuery = true)
    int setNextId(int id, String type);

    @Transactional
    @Modifying
    @Query(value = "update sys_next_id sn set sn.status = ?1 where sn.id = ?2", nativeQuery = true)
    int setStatus(String status, String id);

    @Query(value = "select * from sys_next_id sn where sn.type = ?1", nativeQuery = true)
    DOSysNextId getNextId(String type);
    
    @Transactional
    @Modifying
    @Query(value = "update sys_next_id sn set sn.prefix = ?1 where sn.type = ?2", nativeQuery = true)
    int setPrefix(String prefix, String type);

}
