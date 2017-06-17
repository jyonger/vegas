package org.yong.mall.controller.common;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.service.IFileService;
import org.yong.mall.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Yong on 2017/6/13.
 */
@Controller
public class FileController {

    @Autowired
    IFileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadFile(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String uploadFileName = fileService.saveFile(file, path);
        if (StringUtils.isBlank(uploadFileName)) {
            return BaseResponse.getErrorMessage("上传文件失败");
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + uploadFileName;
        Map map = Maps.newHashMap();
        map.put("uri", uploadFileName);
        map.put("url", url);
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), map);
    }

    @RequestMapping(value = "/rich/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map richUploadFile(@RequestParam(value = "upload_file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map result = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String uploadFileName = fileService.saveFile(file, path);
        if (StringUtils.isBlank(uploadFileName)) {
            result.put("success", false);
            result.put("msg", "上传文件失败");
            return result;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + uploadFileName;
        result.put("success", true);
        result.put("msg", "上传文件成功");
        result.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return result;
    }

}
