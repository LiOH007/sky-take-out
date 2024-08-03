package com.sky.controller.admin;

import com.sky.config.AliOssConfiguration;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@Slf4j
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private CommonService commonService;
    /**
     * 文件上传
     *
     * @param file file
     **/
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file) {
        Result<String> result = commonService.upload(file);
        return result;
    }
}