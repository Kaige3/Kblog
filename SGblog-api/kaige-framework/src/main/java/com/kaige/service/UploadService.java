package com.kaige.service;

import com.kaige.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
