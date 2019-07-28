package org.kx.util.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunkx on 2017/4/23.
 */
public class MailTemplateFactory {
    private static String templatePath ;

    // 模板文件内容编码
    private static final String ENCODING = "utf-8";

    // 模板生成配置
    private static Configuration conf = new Configuration();

    // 邮件模板缓存池
    private static Map<String, Template> tempMap = new HashMap<String, Template>();

    /**
     * 加载templatePath目录下的所有文件
     */
    static{
        conf.setClassForTemplateLoading(MailTemplateFactory.class, "/mailtemplates");
    }

    private static Template getTemplateByName(String name) throws IOException {
        if (tempMap.containsKey(name)) {
            // 缓存中有该模板直接返回
            return tempMap.get(name);
        }
        // 缓存中没有该模板时，生成新模板并放入缓存中
        Template temp = conf.getTemplate(name);
        tempMap.put(name, temp);
        return temp;
    }
    /**
     * 生成内容
     */
    public static String generateHtmlFromFtl(String name,
                                             Map<String, String> map) throws IOException, TemplateException {
        Writer out = new StringWriter(2048);
        Template temp = getTemplateByName(name);
        temp.setEncoding(ENCODING);
        temp.process(map, out);
        return out.toString();
    }

    public static String getTemplatePath() {
        return templatePath;
    }

    public static void setTemplatePath(String templatePath) {
        MailTemplateFactory.templatePath = templatePath;
    }
}
