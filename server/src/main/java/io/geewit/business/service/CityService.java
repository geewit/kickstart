package io.geewit.business.service;

import io.geewit.business.location.dao.CityDao;
import io.geewit.business.location.dao.LocationDao;
import io.geewit.business.location.entity.City;
import io.geewit.data.jpa.essential.search.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author geewit
 * @date 2017/8/14
 */
@CacheConfig(cacheNames = "cities", keyGenerator = "wiselyKeyGenerator")
@Service
public class CityService {
    private final static Logger logger = LoggerFactory.getLogger(CityService.class);

    private final CityDao cityDao;

    private final LocationDao locationDao;

    public CityService(CityDao cityDao, LocationDao locationDao) {
        this.cityDao = cityDao;
        this.locationDao = locationDao;
    }

    /**
     * 查询单条城市数据
     *
     * @param id
     * @return
     */
    @Cacheable(unless = "#result == null")
    public City findById(String id) {
        Optional<City> cityOptional = cityDao.findById(id);
        return cityOptional.orElse(null);
    }

    /**
     * 分页查询城市列表
     *
     * @return
     */
    public Page<City> findAll(Specification<City> specification, Pageable pageable) {
        return cityDao.findAll(specification, pageable);
    }

    /**
     * 列表查询城市列表
     *
     * @return
     */
    public List<City> findAll(Specification<City> specification) {
        return cityDao.findAll(specification);
    }

    /**
     * 新增城市
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public City save(City city) {
        boolean isNew = city.getId() == null;
        city = cityDao.save(city);
        if(isNew) {
            String parentIdPath = null;
            if(StringUtils.isNotEmpty(city.getParentId())) {
                parentIdPath = locationDao.findIdPathById(city.getParentId());
            }
            if (StringUtils.isEmpty(parentIdPath)) {
                city.setIdPath(city.getId());
            } else {
                city.setIdPath(parentIdPath + "|" + city.getId());
            }
            city = cityDao.save(city);
        }
        return city;
    }

    /**
     * 删除城市
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        locationDao.deleteById(id);
    }

    /**
     * 列表查询城市列表
     *
     * @return
     */
    @Cacheable(unless = "#result.isEmpty()")
    public List<City> findByParentId(String id) {
        return cityDao.findByParentId(id);
    }

    /**
     * 状态修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(String id) {
        locationDao.changeDelFlag(id);
    }

    /**
     * 刷新缓存
     */
    @CacheEvict(allEntries = true)
    public void reload() {
    }

    @Cacheable(unless = "#result.isEmpty()")
    public List<City> findCascadeByParentId(String parentId, Collection<SearchFilter> filters) {
        return cityDao.findCascadeByParentId(parentId, filters);
    }
}
