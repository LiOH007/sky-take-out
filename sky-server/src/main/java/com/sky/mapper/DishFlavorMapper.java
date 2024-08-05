package com.sky.mapper;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> flavors);

    void delete(List<Long> dishIds);
}
