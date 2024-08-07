package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    List<Long> getDishIdByIds(List<Long> ids);

    void insert(List<SetmealDish> setmealDishes);


    List<SetmealDish> getDishIdById(Long id);

    void delete(Long id);

    void deleteByIds(List<Long> ids);
}
