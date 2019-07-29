package org.kx.util.generate;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.FileUtil;
import org.kx.util.config.MyCnfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/28
 */

public class IdGenerate {
    static String file_path = MyCnfig.ID_FILE;

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static Long getId() throws IOException {
        try {
            String id_str = FileUtil.readFile(file_path);
            if(id_str.endsWith("\n")){
                id_str = id_str.substring(0,id_str.length()-1) ;
            }

            if (StringUtils.isBlank(id_str) || !isNumeric(id_str)) {
                id_str = "10001";
            }
            Long id_ = Long.parseLong(id_str) + 1;
            FileUtil.writeStringToFile(id_ + "", file_path);
            return id_;
        } catch (IOException e) {
            Long defaultValue = 10001L;
            if(e instanceof FileNotFoundException){
                FileUtil.writeStringToFile(defaultValue + "", file_path);
            }
            return 10001L;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getId());
    }
}
