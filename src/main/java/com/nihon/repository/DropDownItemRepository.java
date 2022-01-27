package com.nihon.repository;

import com.nihon.entity.DODropDownItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.nihon.customrepositories.DropDownItemRepositoryCustom;

@Repository
public interface DropDownItemRepository extends JpaRepository<DODropDownItem, String>,DropDownItemRepositoryCustom {

    @Transactional
    @Modifying
    @Query(value = "update drop_down_items dp set dp.deleted = ?1 where dp.id = ?2", nativeQuery = true)
    int setStatus(boolean deleted, String id);

    @Query(value = "select id,data from drop_down_items where type = ?1 and deleted = 0 order by data", nativeQuery = true)
    List<DODropDownItem.DODropDownListItem> getItemsByType(String type);

    @Query(value = "select case when count(dp.id) > 0 then 'true' else 'false' end from drop_down_items dp where dp.id = ?1 and deleted = false", nativeQuery = true)
    boolean isExistsById(String id);

    @Query(value = "select case when count(dp.id) > 0 then 'true' else 'false' end from drop_down_items dp where dp.data = ?1 and dp.type = ?2  and deleted = false", nativeQuery = true)
    boolean isExistsDataAndType(String data, String type);

    @Query(value = "select distinct type from drop_down_items where deleted = 0 order by type", nativeQuery = true)
    List<String> getTypeList();

}