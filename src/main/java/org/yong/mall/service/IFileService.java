package org.yong.mall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Yong on 2017/6/13.
 */
public interface IFileService {

    String saveFile(MultipartFile file,String path);

}
