package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * save
     *
     * @param setmealDTO setmeal dto
     **/
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        log.info("新增套餐...{}",setmealDTO);
        //新增套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //新增套餐菜品对应关系
        Long id = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes!=null && !setmealDishes.isEmpty())
        {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
            setmealDishMapper.insert(setmealDishes);
        }
    }

    /**
     * page query
     *
     * @param setmealPageQueryDTO setmeal page query dto
     * @return {@link PageResult }
     **/
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询...{}",setmealPageQueryDTO);
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * update
     *
     * @param setmealDTO setmeal dto
     **/
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        log.info("更新套餐...{}",setmealDTO);
        //修改套餐内容
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //修改包含的菜品信息
        Long id = setmealDTO.getId();
        setmealDishMapper.delete(id);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * get by id
     *
     * @param id id
     * @return {@link SetmealVO }
     **/
    @Override
    public SetmealVO getById(Long id) {
        //获取套餐信息
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO =new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        //查询套餐包含的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getDishIdById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 在售状态
     *
     * @param status status
     * @param id     id
     **/
    @Override
    public void status(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * delete
     *
     * @param ids ids
     **/
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //是否处于在售状态
        List<Integer> statuses = setmealMapper.getByIds(ids);
        statuses.forEach(status->{
            if(status== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        //删除套餐
        setmealMapper.delete(ids);
        //删除关联的菜品
        setmealDishMapper.deleteByIds(ids);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
