package io.geewit.business.controller;


import com.fasterxml.jackson.annotation.JsonView;
import io.geewit.business.location.entity.District;
import io.geewit.business.service.DistrictService;
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
 区县列表Controller
 @author luna
 @since  2015-5-18
 */
@RestController
@RequestMapping(value = {"/api/district"})
public class DistrictController {
    private final static Logger logger = LoggerFactory.getLogger(DistrictController.class);

    @Autowired
    private DistrictService districtService;


    @ApiOperation(value="区县列表分页查询")
    @RequestMapping(name = "区县列表分页查询", value = {""}, method = RequestMethod.GET)
    public Page<District> page(WebRequest request, Pageable pageable) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<District> specification = DynamicSpecifications.bySearchFilter(filters);
        pageable = PageableFactory.create(pageable, "id", Sort.Direction.ASC);
        Page<District> page = districtService.findAll(specification, pageable);
        return page;
    }

    @CrossOrigin(origins = "*")
    @JsonView(View.List.class)
    @ApiOperation("区县列表")
    @RequestMapping(name = "区县列表", value = {"/list"}, method = RequestMethod.GET)
    public List<District> list(WebRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Collection<SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<District> specification = DynamicSpecifications.bySearchFilter(filters);
        List<District> list = districtService.findAll(specification);
        return list;
    }

    @ApiOperation(value="区县列表新增")
    @RequestMapping(name = "区县列表新增", value = {""}, method = RequestMethod.POST)
    public District add(@RequestBody District district){
        return districtService.save(district);
    }

    @ApiOperation(value="区县列表修改")
    @RequestMapping(name = "区县列表修改", value = {"/{id}"}, method = RequestMethod.PUT)
    public District update(@PathVariable String id, @RequestBody District district) {
        district.setId(id);
        return districtService.save(district);
    }

    @ApiOperation(value="城市删除")
    @RequestMapping(name = "城市删除", value = {"/{id}"}, method = RequestMethod.DELETE)
    public String delete(@PathVariable String id) {
        districtService.delete(id);
        return "删除成功";
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value="区县信息")
    @RequestMapping(name = "区县信息", value = {"/{id}"}, method = RequestMethod.GET)
    public District get(@PathVariable String id){
        return districtService.findOne(id);
    }

    @ApiOperation(value="区县修改状态")
    @RequestMapping(name = "区县修改状态", value = {"/{id}/change_status"}, method = RequestMethod.PUT)
    public String change_status(@PathVariable String id){
        districtService.changeStatus(id);
        return "修改成功";
    }

    /**
     * 刷新缓存
     */
    @ApiOperation(value="刷新缓存")
    @RequestMapping(name = "刷新缓存", value = {"/reload"}, method = RequestMethod.GET)
    public String reload() {
        districtService.reload();
        return "刷新成功";
    }
}
