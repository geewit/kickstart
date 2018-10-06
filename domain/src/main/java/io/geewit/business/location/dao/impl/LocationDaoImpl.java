package io.geewit.business.location.dao.impl;

import io.geewit.business.location.entity.Location;
import io.geewit.business.location.enums.LocationType;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;

/**
 * @author huangfang
 * @since  2017-11-06
 */
@SuppressWarnings({"unused"})
public class LocationDaoImpl {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LocationDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Location> findList() {
        String sql = "select l.id, l.name from locations l where l.del_flag = 0";
        List<Location> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Location location = new Location();
            location.setId(rs.getString("id"));
            location.setName(rs.getString("name"));
            return location;
        });
        return list;
    }


    public List<Location> findByType(String type) {
        String sql = "select l.id, l.name from locations l where l.type = :type and l.del_flag = 0";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("type", type);
        return findList(sql, parameters);
    }

    public List<Location> findByParentId(String parentId) {
        String sql = "select l.id, l.name from locations l where l.parent_id = :parentId and l.del_flag = 0";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("parentId", parentId);
        return findList(sql, parameters);
    }

    private List<Location> findList(String sql, SqlParameterSource parameters) {
        List<Location> list = jdbcTemplate.query(sql, parameters, (rs, rowNum) -> {
            Location location = new Location();
            location.setId(rs.getString("id"));
            location.setName(rs.getString("name"));
            return location;
        });
        return list;
    }

    public String findIdPathById(String id) {
        String sql = "select l.id_path from locations l where l.id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        String idPath = jdbcTemplate.queryForObject(sql, parameters, String.class);
        return idPath;
    }

    public Map<LocationType, String> findCascadeNamesById(String id) {
        String sql = "SELECT l.name, l.type from locations l where (SELECT ll.id_path FROM locations AS ll WHERE ll.id = :id) like CONCAT(l.id_path,'%')";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, parameters);
        Map<LocationType, String> result;
        if(CollectionUtils.isNotEmpty(list)) {
            result = Maps.newHashMap();
            for (Map<String, Object> item : list) {
                LocationType type = LocationType.valueOf((String)item.get("type"));
                String name = (String) item.get("name");
                result.put(type, name);
            }
        } else {
            result = null;
        }
        return result;
    }

    @SuppressWarnings({"unchecked"})
    public void changeDelFlag(String id) {
        String sql = "update locations set del_flag = !del_flag where id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        jdbcTemplate.update(sql, parameters);
    }

}
