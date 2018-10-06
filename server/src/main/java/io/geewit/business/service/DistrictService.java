package io.geewit.business.service;

import io.geewit.business.location.dao.DistrictDao;
import io.geewit.business.location.dao.LocationDao;
import io.geewit.business.location.entity.District;
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
@CacheConfig(cacheNames = "districts", keyGenerator = "wiselyKeyGenerator")
@Service
public class DistrictService {
    private final static Logger logger = LoggerFactory.getLogger(DistrictService.class);

    private final DistrictDao districtDao;

    private final LocationDao locationDao;

    public DistrictService(DistrictDao districtDao, LocationDao locationDao) {
        this.districtDao = districtDao;
        this.locationDao = locationDao;
    }


    /**
     * 查询单条城市数据
     *
     * @param id
     * @return
     */
    @Cacheable(unless = "#result == null")
    public District findOne(String id) {
        Optional<District> districtOptional = districtDao.findById(id);
        return districtOptional.orElse(null);
    }

    /**
     * 分页查询城市列表
     *
     * @return
     */
    public Page<District> findAll(Specification<District> specification, Pageable pageable) {
        return districtDao.findAll(specification, pageable);
    }

    /**
     * 列表查询城市列表
     *
     * @return
     */
    public List<District> findAll(Specification<District> specification) {
        return districtDao.findAll(specification);
    }

    /**
     * 新增城市
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public District save(District district) {
        boolean isNew = district.getId() == null;
        district = districtDao.save(district);
        if(isNew) {
            String parentIdPath = null;
            if(StringUtils.isNotEmpty(district.getParentId())) {
                parentIdPath = locationDao.findIdPathById(district.getParentId());
            }
            if (StringUtils.isEmpty(parentIdPath)) {
                district.setIdPath(district.getId());
            } else {
                district.setIdPath(parentIdPath + "|" + district.getId());
            }
            district = districtDao.save(district);
        }
        return district;
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
    public List<District> findByParentId(String id) {
        return districtDao.findByParentId(id);
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
    public List<District> findCascadeByParentId(String parentId, Collection<SearchFilter> filters) {
        return districtDao.findCascadeByParentId(parentId, filters);
    }

    public long countByParentIdAndName(String parentId, String districtName) {
        return districtDao.countByParentIdAndName(parentId, districtName);
    }
}
