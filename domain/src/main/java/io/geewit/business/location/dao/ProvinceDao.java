package io.geewit.business.location.dao;

import io.geewit.business.location.entity.Province;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by user on 2017/8/14.
 */
public interface ProvinceDao extends PagingAndSortingRepository<Province, String>, JpaSpecificationExecutor<Province> {
    @Query("select p from Province p where p.parentId = :parentId")
    List<Province> findByParentId(@Param("parentId") String parentId);
}
