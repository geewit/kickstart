package io.geewit.business.service;

import io.geewit.business.location.dao.CountryDao;
import io.geewit.business.location.dao.LocationDao;
import io.geewit.business.location.entity.Country;
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

import java.util.List;
import java.util.Optional;

/**
 *
 * @author geewit
 * @date 2017/8/14
 */
@CacheConfig(cacheNames = "countries", keyGenerator = "wiselyKeyGenerator")
@Service
public class CountryService {
    private final static Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final LocationDao locationDao;


    private final CountryDao countryDao;

    public CountryService(LocationDao locationDao, CountryDao countryDao) {
        this.locationDao = locationDao;
        this.countryDao = countryDao;
    }


    /**
     * 查询单挑数据
     * @param id
     * @return
     */
    @Cacheable(unless = "#result == null")
    public Country findOne(String id) {
        Optional<Country> optional = countryDao.findById(id);
        return optional.orElse(null);
    }


    /**
     * 查询仓库列表
     * @return
     */
    public Page<Country> findAll(Specification<Country> specification, Pageable pageable){
        return countryDao.findAll(specification, pageable);
    }


    /**
     * 查询仓库列表
     * @return
     */
    public List<Country> findAll(Specification<Country> specification){
        return countryDao.findAll(specification);
    }

    /**
     * 新增仓库
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Country save(Country country) {
        boolean isNew = country.getId() == null;
        country = countryDao.save(country);
        if(isNew) {
            country.setIdPath(country.getId());
            country = countryDao.save(country);
        }
        return country;
    }

    /**
     * 删除仓库
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id){
        locationDao.deleteById(id);
    }

    /**
     * 状态修改
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void changeDelFlag(String id) {
        locationDao.changeDelFlag(id);
    }

    @CacheEvict(allEntries = true)
    public void reload() {
    }

    public Country findOneByCode(String countryCode) {
        return countryDao.findByCode(countryCode);
    }
}
