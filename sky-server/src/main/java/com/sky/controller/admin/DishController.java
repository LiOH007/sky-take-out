package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Results;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api("菜品管理接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
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
        cleanCache("dish_"+dishDTO.getCategoryId());
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
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 修改在售状态
     *
     * @param status status
     * @param id     id
     * @return {@link Result }
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("启用停用菜品")
    public Result statuss(@PathVariable Integer status,Long id){
        dishService.status(status,id);
        cleanCache("dish_*");
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     *根据id查找菜品信息
     *
     * @param id id
     * @return {@link Result }<{@link DishVO }>
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查找菜品信息")
    public Result<DishVO> queryById(@PathVariable Long id){
        DishVO dishVO =  dishService.queryById(id);
        return Result.success(dishVO);
    }


    @GetMapping("list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> queryByCategoryId(Long categoryId){
        List<Dish> dishes = dishService.queryByCategoryId(categoryId);
        return Result.success(dishes);
    }

    /**
     * 清除缓存
     *
     * @param pattern pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
