package io.geewit.business.location.dao;

import io.geewit.business.location.entity.District;
import io.geewit.data.jpa.essential.search.SearchFilter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author yao
 * @date 2017/8/14
 */
public interface DistrictDao extends PagingAndSortingRepository<District, String>, JpaSpecificationExecutor<District> {
    @Query("select d from District d where d.parentId = :parentId")
    List<District> findByParentId(@Param("parentId") String parentId);

    List<District> findCascadeByParentId(String parentId, Collection<SearchFilter> filters);

    long countByParentIdAndName(String parentId, String name);
}
