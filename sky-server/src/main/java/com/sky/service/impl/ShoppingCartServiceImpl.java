package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void save(ShoppingCartDTO shoppingCartDTO) {
        //购物车中是否存在该商品
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list= shoppingCartMapper.list(shoppingCart);
        //存在则数量+1
        if(list!=null && !list.isEmpty()){
            ShoppingCart cart= list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.update(cart);
        }else{
            //不存在则插入
            //先判断是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId!=null){
                Dish dish = dishMapper.getDishById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(null);
        return shoppingCarts;
    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list= shoppingCartMapper.list(shoppingCart);
        ShoppingCart cart = null;
        if(list!=null && !list.isEmpty()){
            cart = list.get(0);
        }
        //如果有多份就减少一份
        if(cart!=null && cart.getNumber()>1){
            cart.setNumber(cart.getNumber()-1);
            shoppingCartMapper.update(cart);
        }else{
            //如果只有一份就直接删除
            shoppingCartMapper.delete(shoppingCart);
        }
    }

    @Override
    public void clean() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        shoppingCartMapper.delete(shoppingCart);
    }
}
