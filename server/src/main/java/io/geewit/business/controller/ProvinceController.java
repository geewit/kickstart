package io.geewit.business.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.business.location.entity.City;
import io.geewit.business.location.entity.Province;
import io.geewit.business.service.CityService;
import io.geewit.business.service.ProvinceService;
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
 省列表Controller
 @author luna
 @since  2015-5-18
 */
@RestController
@RequestMapping(value = {"/api/province"})
public class ProvinceController {
    private final static Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @ApiOperation("省列表分页查询")
    @RequestMapping(name = "省列表分页查询", value = {""}, method = RequestMethod.GET)
    public Page<Province> page(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Province> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "id", Sort.Direction.ASC);
        Page<Province> page = provinceService.findAll(specification, pageable);
        return page;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("省列表")
    @RequestMapping(name = "省列表", value = {"/list"}, method = RequestMethod.GET)
    public List<Province> list(WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Province> specification = DynamicSpecifications.bySearchFilter(filters);
        List<Province> list = provinceService.findAll(specification);
        return list;
    }

    @ApiOperation(value="省列表新增")
    @RequestMapping(name = "省列表新增", value = {""}, method = RequestMethod.POST)
    public Province add(@RequestBody Province province){
        return provinceService.save(province);
    }

    @ApiOperation(value="省列表修改")
    @RequestMapping(name = "省列表修改", value = {"/{id}"}, method = RequestMethod.PUT)
    public Province update(@PathVariable String id, @RequestBody Province province){
        province.setId(id);
        return provinceService.save(province);
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="省信息")
    @RequestMapping(name = "省信息", value = {"/{id}"}, method = RequestMethod.GET)
    public Province get(@PathVariable String id){
        return provinceService.findOne(id);
    }

    @ApiOperation(value="省列表删除")
    @RequestMapping(name = "省列表删除", value = {"/{id}"}, method = RequestMethod.DELETE)
    public String delete(@PathVariable String id){
        provinceService.delete(id);
        return "删除成功";
    }

    @ApiOperation(value="省修改状态")
    @RequestMapping(name = "省修改状态", value = {"/{id}/change_status"}, method = RequestMethod.PUT)
    public String change_status(@PathVariable String id){
        provinceService.changeStatus(id);
        return "修改成功";
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="城市列表")
    @RequestMapping(name = "城市列表", value = {"/{id}/cities"}, method = RequestMethod.GET)
    public List<City> cities(@PathVariable String id) {
        List<City> list = cityService.findByParentId(id);
        return list;
    }


    /**
     * 刷新缓存
     */
    @ApiOperation(value="刷新缓存")
    @RequestMapping(name = "刷新缓存", value = {"/reload"}, method = RequestMethod.GET)
    public String reload() {
        provinceService.reload();
        return "刷新成功";
    }
}
