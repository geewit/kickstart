package io.geewit.business.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.business.location.entity.Country;
import io.geewit.business.location.entity.Province;
import io.geewit.business.service.CountryService;
import io.geewit.business.service.ProvinceService;
import io.geewit.core.exception.CustomizedException;
import io.geewit.core.jackson.view.View;
import io.geewit.core.utils.BeanUtils;
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
 国家列表Controller
 @author luna
 @since  2015-5-18
 */
@RestController
@RequestMapping(value = {"/api/country"})
public class CountryController {
    private final static Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;


    @Autowired
    private ProvinceService provinceService;

    @ApiOperation(value="国家列表分页查询")
    @RequestMapping(name = "国家列表分页查询", value = {""}, method = RequestMethod.GET)
    public Page<Country> page(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Country> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "id", Sort.Direction.ASC);
        Page<Country> page = countryService.findAll(specification, pageable);
        return page;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("国家列表")
    @RequestMapping(name = "国家列表", value = {"/list"}, method = RequestMethod.GET)
    public List<Country> list(WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Country> specification = DynamicSpecifications.bySearchFilter(filters);
        List<Country> list = countryService.findAll(specification);
        return list;
    }

    @ApiOperation(value="国家列表新增")
    @RequestMapping(name = "国家列表新增", value = {""}, method = RequestMethod.POST)
    public Country add(@RequestBody Country country){
        return countryService.save(country);
    }

    @ApiOperation(value="国家列表修改")
    @RequestMapping(name = "国家列表修改", value = {"/{id}"}, method = RequestMethod.PUT)
    public Country update(@PathVariable String id, @RequestBody Country country) {
        Country existCountry = countryService.findOne(id);
        if(existCountry == null) {
            throw new CustomizedException("无法找到该国家");
        }
        BeanUtils.copyProperties(country, existCountry);
        return countryService.save(existCountry);
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="国家信息")
    @RequestMapping(name = "国家信息", value = {"/{id}"}, method = RequestMethod.GET)
    public Country get(@PathVariable String id) {
        Country country = countryService.findOne(id);
        if(country == null) {
            throw new CustomizedException("无法找到该国家");
        }
        return country;
    }

    @ApiOperation(value="国家列表删除")
    @RequestMapping(name = "国家列表删除", value = {"/{id}"}, method = RequestMethod.DELETE)
        public String delete(@PathVariable String id){
            countryService.delete(id);
            return "删除成功";
    }

    @ApiOperation(value="国家修改状态")
    @RequestMapping(name = "国家修改状态", value = {"/{id}/change_status"}, method = RequestMethod.PUT)
    public String change_status(@PathVariable String id) {
        countryService.changeDelFlag(id);
        return "修改成功";
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="根据国家查询省列表")
    @RequestMapping(name = "根据国家查询省列表", value = {"/{countryId}/provinces"}, method = RequestMethod.GET)
    public List<Province> listByCountry(@PathVariable String countryId) {
        List<Province> list = provinceService.findByParentId(countryId);
        return list;
    }


    @CrossOrigin(origins = "*")
    @ApiOperation(value="国家信息")
    @RequestMapping(name = "国家信息", value = {"/code/{code}"}, method = RequestMethod.GET)
    public Country findByCode(@PathVariable String code) {
        Country country = countryService.findOneByCode(code);
        if(country == null) {
            throw new CustomizedException("无法找到该国家");
        }
        return country;
    }

    /**
     * 刷新缓存
     */
    @ApiOperation(value="刷新缓存")
    @RequestMapping(name = "刷新缓存", value = {"/reload"}, method = RequestMethod.GET)
    public String reload() {
        countryService.reload();
        return "刷新成功";
    }
}
