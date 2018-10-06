package io.geewit.business.location.dao;

import io.geewit.business.location.entity.Location;
import io.geewit.business.location.enums.LocationType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


/**
 *
 * @author geewit
 * @since 2018-08-03
 */
public interface LocationDao extends PagingAndSortingRepository<Location, String>, JpaSpecificationExecutor<Location> {
    List<Location> findList();

    List<Location> findByType(String type);

    List<Location> findByParentId(String parentId);

    String findIdPathById(String id);

    @Query("select l from Location l where l.name = :name and l.type = :type")
    List<Location> findNameAndTypes(@Param("name") String name, @Param("type") LocationType type);

    @Query(value = "select l.id from locations l where l.name = :name limit 1", nativeQuery = true)
    String findIdByName(@Param("name") String name);

    @Query("select l from Location l where l.name = :name")
    Location findByName(@Param("name") String name);

    Map<LocationType, String> findCascadeNamesById(String id);

    @Query("select l from Location l where l.name = :name and l.idPath like concat(:idPath, '%')")
    Location findByNameAndIdPath(@Param("name") String name, @Param("idPath") String idPath);

    @Query("select l from Location l where l.name = :name and l.type = :type and l.idPath like concat(:idPath, '%')")
    List<Location> findByNameAndTypeAndIdPath(@Param("name") String name, @Param("type") LocationType type, @Param("idPath") String idPath);

    @Query("select l from Location l where l.name = :name and l.type = :type and l.idPath like concat(:idPath, '%') and l.parent.name = :parentName")
    Location findByNameAndParentNameAndTypeAndIdPath(@Param("name") String name, @Param("parentName") String parentName, @Param("type") LocationType type, @Param("idPath") String idPath);

    void changeDelFlag(String id);
}
