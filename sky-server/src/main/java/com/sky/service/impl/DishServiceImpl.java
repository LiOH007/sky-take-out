package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO dish dto
     **/
    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        //插入菜品信息
        dishMapper.insert(dish);
        Long id = dish.getId();
        //插入口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null &&flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO dish page query dto
     * @return {@link PageResult }
     **/
    @Override
    public PageResult query(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品...{}",dishPageQueryDTO);
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> list = page.getResult();
        return new PageResult(total,list);
    }

    /**
     * delete
     *
     * @param ids ids
     **/
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        log.info("批量删除菜品{}",ids);
        //起售状态的不能删除
        List<Integer> idlist =  dishMapper.getByIds(ids);
        for(Integer id:idlist){
            if(id== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //与套餐表有关联的不能删除
        List<Long> setmealIds = setmealDishMapper.getDishIdByIds(ids);
        if(setmealIds!=null && setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品信息
        dishMapper.delete(ids);
        //删除口味信息
        dishFlavorMapper.delete(ids);
    }

    /**
     * 起售，停售
     *
     * @param status status
     * @param id     id
     **/
    @Override
    public void status(Integer status, Long id) {
        log.info("修改在售状态{},{}",status,id);
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO dish dto
     **/
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        log.info("更新菜品{}", dishDTO);
        //更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        //更新口味信息，覆盖修改
        List<Long> ids = new ArrayList<>();
        ids.add(dishDTO.getId());
        dishFlavorMapper.delete(ids);
        
        Long id = dishDTO.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
        dishFlavorMapper.insertBatch(flavors);
    }
}
    /**
     * 根据id查询菜品信息
     *
     * @return {@link DishVO }
     **/
    @Override
    public DishVO queryById(Long id) {
        log.info("查询菜品信息{}",id);
        DishVO dishVO = dishMapper.getById(id);
        List<DishFlavor> dishFlavors =  dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId category id
     * @return {@link Dish }
     **/
    @Override
    public List<Dish> queryByCategoryId(Long categoryId) {
        log.info("查找的分类{}",categoryId);
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .build();
        List<Dish> dishes = dishMapper.getByCategoryId(dish);
        return dishes;
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.getByCategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
