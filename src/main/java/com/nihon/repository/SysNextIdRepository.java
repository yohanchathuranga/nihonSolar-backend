package com.nihon.repository;

import com.nihon.entity.DOSysNextId;
import java.util.UUID;
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

    @Query(value = "select * from sys_next_id sn where sn.type = ?1 and sn.prefix = ?2", nativeQuery = true)
    DOSysNextId getNextIdByPrefix(String type, String prefix);

    @Transactional
    @Modifying
    @Query(value = "insert into sys_next_id (id,next_id,prefix,status,type) values(?,?,?,?,?)", nativeQuery = true)
    int insert(String id, int nextId, String prefix, String status, String type);
    
    @Transactional
    @Modifying
    @Query(value = "update sys_next_id sn set sn.next_id = ?1 where sn.type = ?2 and sn.prefix = ?3", nativeQuery = true)
    int setNextIdByPrefix(int id, String type,String prefix);
    
    @Query(value = "select case when count(id) > 0 then 'true' else 'false' end from sys_next_id where prefix = ?1  and type = 'project'", nativeQuery = true)
    boolean isExistsByPrefix(String prefix);
}
