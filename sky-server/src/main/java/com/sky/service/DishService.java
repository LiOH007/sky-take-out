package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);

    PageResult query(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    void status(Integer status, Long id);

    void update(DishDTO dishDTO);

    DishVO queryById(Long id);

    List<Dish> queryByCategoryId(Long categoryId);
}
