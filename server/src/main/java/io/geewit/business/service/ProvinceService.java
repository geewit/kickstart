package io.geewit.business.service;

import io.geewit.business.location.dao.LocationDao;
import io.geewit.business.location.dao.ProvinceDao;
import io.geewit.business.location.entity.Province;
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

import java.util.List;
import java.util.Optional;

/**
 *
 * @author geewit
 * @since  2017/8/14
 */
@CacheConfig(cacheNames = "provinces", keyGenerator = "wiselyKeyGenerator")
@Service
public class ProvinceService {

    private final static Logger logger = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceDao provinceDao;

    private final LocationDao locationDao;

    public ProvinceService(ProvinceDao provinceDao, LocationDao locationDao) {
        this.provinceDao = provinceDao;
        this.locationDao = locationDao;
    }

    /**
     * 查询单挑数据
     * @param id
     * @return
     */
    @Cacheable(unless = "#result == null")
    public Province findOne(String id) {
        Optional<Province> optional = provinceDao.findById(id);
        return optional.orElse(null);
    }

    /**
     * 查询仓库列表
     * @return
     */
    public Page<Province> findAll(Specification<Province> specification, Pageable pageable){
        return provinceDao.findAll(specification, pageable);
    }

    /**
     * 查询仓库列表
     * @return
     */
    public List<Province> findAll(Specification<Province> specification){
        return provinceDao.findAll(specification);
    }

    /**
     * 新增仓库
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Province save(Province province){
        boolean isNew = province.getId() == null;
        province = provinceDao.save(province);
        if(isNew) {
            String parentIdPath = null;
            if(StringUtils.isNotEmpty(province.getParentId())) {
                parentIdPath = locationDao.findIdPathById(province.getParentId());
            }
            if (StringUtils.isEmpty(parentIdPath)) {
                province.setIdPath(province.getId());
            } else {
                province.setIdPath(parentIdPath + "|" + province.getId());
            }
            province = provinceDao.save(province);
        }
        return province;
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
     * 根据国家查询省列表
     * @return
     */
    @Cacheable(unless = "#result.isEmpty()")
    public List<Province> findByParentId(String id){
        return provinceDao.findByParentId(id);
    }

    /**
     * 状态修改
     */
    @CacheEvict(allEntries = true)
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
}
