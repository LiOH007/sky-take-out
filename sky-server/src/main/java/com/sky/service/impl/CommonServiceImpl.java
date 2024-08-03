package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Override
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传{}", file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extensions = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String objectName = UUID.randomUUID().toString() + extensions;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
