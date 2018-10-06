package io.geewit.business.service;

import io.geewit.business.location.dao.LocationDao;
import io.geewit.business.location.entity.*;
import io.geewit.business.location.enums.LocationType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author geewit
 */
@Service
public class LocationService {
    private final static Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private CountryService countryService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;

    private final LocationDao locationDao;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public Page<Location> findAll(Pageable pageable) {
        return locationDao.findAll(pageable);
    }

    public String findNameById(String id) {
        Optional<Location> optional = locationDao.findById(id);
        return optional.map(Location::getName).orElse(null);
    }

    public List<Location> findList(Specification<Location> specification) {
        return locationDao.findAll(specification);
    }

    public List<Location> findListByType(String type) {
        return locationDao.findByType(type);
    }

    public List<Location> findList() {
        return locationDao.findList();
    }

    public Map<LocationType, String> findCascadeNamesById(String id) {
        return locationDao.findCascadeNamesById(id);
    }

    public void init() {
        logger.info("InitService.init()");
        //batchUpdateIdPath();
        String txtPath = "/init/district.txt";
        logger.info("txtPath = " + txtPath);
        InputStream in = getClass().getResourceAsStream(txtPath);
        try {
            if(in == null) {
                logger.info(txtPath + " not found");
                return;
            }
            List<String> lines = IOUtils.readLines(in, StandardCharsets.UTF_8);
            for(String line : lines) {
                logger.debug("line: " + line);
                String[] splited = StringUtils.split(line, " ");
                if(splited.length != 3) {
                    logger.info("splited.length != 3, continue");
                    continue;
                }
                String districtName = StringUtils.trim(splited[0]);
                logger.debug("districtName = " + districtName);
                String cityName = StringUtils.trim(splited[1]);
                logger.debug("cityName = " + cityName);
                String provinceName = StringUtils.trim(splited[2]);
                logger.debug("provinceName = " + provinceName);
                Location city = locationDao.findByNameAndParentNameAndTypeAndIdPath(cityName, provinceName, LocationType.CITY, "L000010046");
                if(city == null) {
                    logger.info("cityName not found in database, continue");
                    continue;
                }
                if(!LocationType.CITY.equals(city.getType())) {
                    logger.info(cityName + " is not CITY, continue");
                    continue;
                }
                long count = districtService.countByParentIdAndName(city.getId(), districtName);
                if(count > 0) {
                    logger.info("countByParentIdAndName > 0, continue");
                    continue;
                }
                District district = new District();
                district.setName(districtName);
                district.setType(LocationType.DISTRICT);
                district.setParent(city);
                district.setParentId(city.getId());
                districtService.save(district);
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 批量全更新 Location.idPath
     */
    @Async
    public void batchUpdateIdPath() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Location> page;
        do {
            try {
                page = findAll(pageable);
                for (Location location : page) {
                    StringBuilder builder = null;
                    Location temp = location;
                    do {
                        if(builder != null) {
                            builder.insert(0, temp.getId() + "|");
                        } else {
                            builder = new StringBuilder(temp.getId());
                        }

                        temp = temp.getParent();
                    } while (temp != null);
                    String idPath = builder.toString();

                    location.setIdPath(idPath);
                    if (location instanceof Country) {
                        try {
                            countryService.save((Country) location);
                        } catch (Exception ignore) {
                        }

                    } else if (location instanceof Province) {
                        try {
                            provinceService.save((Province) location);
                        } catch (Exception ignore) {
                        }
                    } else if (location instanceof City) {
                        try {
                            cityService.save((City) location);
                        } catch (Exception ignore) {
                        }
                    }
                }
                pageable = page.nextPageable();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }


        } while (page.hasNext());
    }
}
