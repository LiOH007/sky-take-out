package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Results;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api("菜品管理接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO dish dto
     * @return {@link Result }
     **/
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO dish page query dto
     * @return {@link Result }<{@link PageResult }>
     **/
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> query(DishPageQueryDTO dishPageQueryDTO){
        PageResult query = dishService.query(dishPageQueryDTO);
        return Result.success(query);
    }


    /**
     * 批量删除
     *
     * @param ids ids
     * @return {@link Result }
     **/
    @DeleteMapping
    @ApiOperation("批量删除")
    public Result delete(@RequestParam List<Long> ids){
        dishService.delete(ids);
        return Result.success();
    }
}
