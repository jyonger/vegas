package org.yong.mall.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yong.mall.service.IFileService;
import org.yong.mall.util.FTPUtil;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Yong on 2017/6/13.
 */
@Service
public class FileServiceImpl implements IFileService {

    private final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String saveFile(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        // get extension
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + extension;

        LOG.info("开始上传文件，上传名称：{}，上传路径：{}，新文件名：{}", fileName, path, uploadFileName);

        // create file dir
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        // copy file
        File targetFile = new File(path, uploadFileName);
        try {

            file.transferTo(targetFile);
            // upload FTP server
            FTPUtil.saveFile(Lists.newArrayList(targetFile));
            // delete upload file
            targetFile.delete();
        } catch (IOException e) {
            LOG.error("上传文件异常", e);
            e.printStackTrace();
        }
        return targetFile.getName();
    }
}
