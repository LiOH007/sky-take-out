package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import com.sky.service.ShoppingCartService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    void update(ShoppingCart cart);

    void insert(ShoppingCart shoppingCart);

    void delete(ShoppingCart shoppingCart);
}
