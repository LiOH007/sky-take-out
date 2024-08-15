package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 购物车新增
     *
     * @param shoppingCartDTO shopping cart dto
     * @return {@link Result }
     **/
    @PostMapping("/add")
    @ApiOperation("购物车新增")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.save(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return {@link Result }<{@link List }<{@link ShoppingCart }>>
     **/
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> shoppingCarts = shoppingCartService.list();
        return Result.success(shoppingCarts);
    }

    /**
     * 删除购物车中的一个
     *
     * @param shoppingCartDTO shopping cart dto
     * @return {@link Result }
     **/
    @PostMapping("/sub")
    @ApiOperation("删除购物车中的一个")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shoppingCartService.clean();
        return Result.success();
    }
}
