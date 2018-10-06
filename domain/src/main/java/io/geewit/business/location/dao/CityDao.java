package io.geewit.business.location.dao;

import io.geewit.business.location.entity.City;
import io.geewit.data.jpa.essential.search.SearchFilter;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author geewit
 * @date 2017/8/14
 */
public interface CityDao extends PagingAndSortingRepository<City, String>, JpaSpecificationExecutor<City> {
    @Query("select c from City c where c.parentId = :parentId")
    List<City> findByParentId(@Param("parentId") String parentId);

    List<City> findCascadeByParentId(String parentId, Collection<SearchFilter> filters);
}
