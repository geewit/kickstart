package io.geewit.business.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.business.location.entity.City;
import io.geewit.business.service.CityService;
import io.geewit.core.jackson.view.View;
import io.geewit.data.jpa.essential.domain.PageableFactory;
import io.geewit.data.jpa.essential.search.DynamicSpecifications;
import io.geewit.data.jpa.essential.search.SearchFilter;
import io.geewit.data.jpa.essential.web.servlet.Servlets;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 城市列表Controller
 @author luna
 @since  2015-5-18
 */
@RestController
@RequestMapping(value = {"/api/city"})
public class CityController {
    private final static Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityService cityService;


    @ApiOperation(value="城市列表分页查询")
    @RequestMapping(name = "城市列表分页查询", value = {""}, method = RequestMethod.GET)
    public Page<City> page(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<City> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "id", Sort.Direction.ASC);
        Page<City> page = cityService.findAll(specification, pageable);
        return page;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("城市列表")
    @RequestMapping(name = "城市列表", value = {"/list"}, method = RequestMethod.GET)
    public List<City> list(WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<City> specification = DynamicSpecifications.bySearchFilter(filters);
        List<City> list = cityService.findAll(specification);
        return list;
    }

    @ApiOperation(value="城市列表新增")
    @RequestMapping(name = "城市列表新增", value = {""}, method = RequestMethod.POST)
    public City add(@RequestBody City city){
        return cityService.save(city);
    }

    @ApiOperation(value="城市列表修改")
    @RequestMapping(name = "城市列表修改", value = {"/{id}"}, method = RequestMethod.PUT)
    public City update(@PathVariable String id, @RequestBody City city){
        city.setId(id);
        return cityService.save(city);
    }

    @ApiOperation(value="城市删除")
    @RequestMapping(name = "城市删除", value = {"/{id}"}, method = RequestMethod.DELETE)
    public String delete(@PathVariable String id){
        cityService.delete(id);
        return "删除成功";
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="城市信息")
    @RequestMapping(name = "城市信息", value = {"/{id}"}, method = RequestMethod.GET)
    public City get(@PathVariable String id){
        return cityService.findById(id);
    }

    @ApiOperation(value="城市修改状态")
    @RequestMapping(name = "城市修改状态", value = {"/{id}/change_status"}, method = RequestMethod.PUT)
    public String change_status(@PathVariable String id){
        cityService.changeStatus(id);
        return "修改成功";
    }

    /**
     * 刷新缓存
     */
    @ApiOperation(value="刷新缓存")
    @RequestMapping(name = "刷新缓存", value = {"/reload"}, method = RequestMethod.GET)
    public String reload() {
        cityService.reload();
        return "刷新成功";
    }
}
