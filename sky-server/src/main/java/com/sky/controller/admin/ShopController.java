package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String KEY="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 设置营业状态
     *
     * @param status status
     * @return {@link Result }
     **/
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置的营业状态为：{}",status==1? "营业":"打烊");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    /**
     * 查询营业状态
     *
     * @return {@link Result }<{@link Integer }>
     **/
    @GetMapping("/status")
    @ApiOperation("查询营业状态")
    public Result<Integer> getShopStatus(){
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("营业状态为 {}",shopStatus==1? "营业":"打烊");
        return Result.success(shopStatus);
    }
}
