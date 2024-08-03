package com.sky.service;

import com.sky.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    public Result<String> upload(MultipartFile file);

}
