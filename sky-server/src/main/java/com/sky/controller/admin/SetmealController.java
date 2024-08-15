package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api("套餐管理接口")
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;


    /**
     * 新增套餐
     *
     * @return {@link Result }
     **/
    @PostMapping()
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "SetmealCache",key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO setmeal page query dto
     * @return {@link Result }<{@link PageResult }>
     **/
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改在售状态
     *
     * @param status status
     * @param id     id
     * @return {@link Result }
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("修改在售状态")
    @Caching(evict={
            @CacheEvict(cacheNames = "SetmealCache",allEntries = true),
            @CacheEvict(cacheNames = "SetmealDishCache",key = "#id")
    })
    public Result status(@PathVariable Integer status,Long id){
        setmealService.status(status,id);
        return Result.success();
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO setmeal dto
     * @return {@link Result }
     **/
    @PutMapping
    @ApiOperation("修改套餐")
    @Caching(evict={
            @CacheEvict(cacheNames = "SetmealCache",allEntries = true),
            @CacheEvict(cacheNames = "SetmealDishCache",key = "#setmealDTO.id")
    })
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 根据id查找套餐信息
     *
     * @param id id
     * @return {@link Result }<{@link SetmealVO }>
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查找套餐信息")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 批量删除
     *
     * @return {@link Result }
     **/
    @DeleteMapping
    @ApiOperation("批量删除")
    @Caching(evict={
            @CacheEvict(cacheNames = "SetmealCache",allEntries = true),
            @CacheEvict(cacheNames = "SetmealDishCache",allEntries = true)
    })
    public Result delete(@RequestParam List<Long> ids){
        setmealService.delete(ids);
        return Result.success();
    }

}
