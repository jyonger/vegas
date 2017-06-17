package org.yong.mall.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Yong on 2017/6/12.
 */
public class PropertiesUtil {

    private static Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        properties = new Properties();
        String fileName = "ftp.properties";
        try {
            properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName)));
        } catch (IOException e) {
            LOG.error("加载配置文件出错", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key).trim();
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return value;
    }

    public static String getProperty(String key, String defVal) {
        String value = properties.getProperty(key).trim();
        if (StringUtils.isBlank(value)) {
            return defVal;
        }

        return value;
    }
}
