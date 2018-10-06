package io.geewit.business.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.business.location.entity.City;
import io.geewit.business.location.entity.District;
import io.geewit.business.location.entity.Location;
import io.geewit.business.location.entity.Province;
import io.geewit.business.location.enums.LocationType;
import io.geewit.business.service.CityService;
import io.geewit.business.service.DistrictService;
import io.geewit.business.service.LocationService;
import io.geewit.business.service.ProvinceService;
import io.geewit.core.jackson.view.View;
import io.geewit.data.jpa.essential.search.DynamicSpecifications;
import io.geewit.data.jpa.essential.search.Operator;
import io.geewit.data.jpa.essential.search.SearchFilter;
import io.geewit.data.jpa.essential.web.servlet.Servlets;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 位置列表Controller
 @author huangfang
 @since  2017-11-06
 */
@RestController
@RequestMapping(value = {"/api/location"})
public class LocationController {
    private final static Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;

    @CrossOrigin(origins = "*")
    @ApiOperation("位置名称")
    @RequestMapping(name = "位置名称", value = {"/{id}/name"}, method = RequestMethod.GET)
    public String name(@PathVariable String id) {
        String name = locationService.findNameById(id);
        return name;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("所有位置列表")
    @RequestMapping(name = "所有位置列表", value = {"/list"}, method = RequestMethod.GET)
    public List<Location> all() {
        List<Location> locations = locationService.findList();
        return locations;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation(value="指定类型位置列表")
    @RequestMapping(name = "指定类型位置列表", value = {"/{locationType}/list"}, method = RequestMethod.GET)
    public List<Location> list(@PathVariable("locationType") LocationType locationType, WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.add(SearchFilter.build("type", Operator.EQ, locationType.name()));

        Specification<Location> specification = DynamicSpecifications.bySearchFilter(filters);
        List<Location> locations = locationService.findList(specification);
        return locations;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("省市列表")
    @RequestMapping(name = "省市列表", value = {"/{parentId}/provinces"}, method = RequestMethod.GET)
    public List<Province> provinces(@PathVariable String parentId) {
        List<Province> provinces = provinceService.findByParentId(parentId);
        return provinces;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("城市列表")
    @RequestMapping(name = "城市列表", value = {"/{parentId}/cities"}, method = RequestMethod.GET)
    public List<City> cities(@PathVariable String parentId, WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        List<City> cities = cityService.findCascadeByParentId(parentId, filters);
        return cities;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("区县列表")
    @RequestMapping(name = "仓库列表", value = {"/{parentId}/distrcts"}, method = RequestMethod.GET)
    public List<District> distrcts(@PathVariable String parentId, WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        List<District> districts = districtService.findCascadeByParentId(parentId, filters);
        return districts;
    }

    @CrossOrigin(origins = "*")
    @ApiOperation("批量全更新 Location.idPath")
    @RequestMapping(name = "批量全更新 Location.idPath", value = {"/batch_update_id_path"}, method = RequestMethod.GET)
    public String batch_update_id_path() {
        locationService.batchUpdateIdPath();
        return "批量更新完毕";
    }

    @CrossOrigin(origins = "*")
    @ApiOperation("获取层级名称")
    @RequestMapping(name = "获取层级名称", value = {"/{id}/cascade"}, method = RequestMethod.GET)
    public Map<LocationType, String> cascadeNameById(@PathVariable String id) {
        Map<LocationType, String> cascadeNames = locationService.findCascadeNamesById(id);
        return cascadeNames;
    }
}
