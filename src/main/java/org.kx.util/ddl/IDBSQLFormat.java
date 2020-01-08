package org.kx.util.ddl;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ： Created by  xianguang.skx Since 2019/11/28
 */

public class IDBSQLFormat {
    static String EN_ = " "; //英文空格
    static String CN_ = " "; ///中文空格
    static String SPLIT_ = "'"; //分割符号

    public static void main(String[] args) {
        String ss = "select user_db  from sv_ss where id =  '6305721'  ;update user_db  set status = -1 ,gmt_modified = now() where id =  6305721  ;"
            + "update     fill_db    set status = -1 ,gmt_modified = now()   where id in "
            + "('   68910456,68409390, 67939131,67936760,67926705   ')   ;;; " ;

        System.out.println(dealStr(ss));
    }


    public static String dealStr(String sqls) {
        String[] items_ = sqls.split(";");
        StringBuilder sbt = new StringBuilder();
        for (String i : items_) {
            if (StringUtils.isBlank(i)) {
                continue;
            }
            sbt.append(dealStr_(i)).append(";\n");
        }
        return sbt.toString();
    }


    public static String dealStr_(String sql) {
        List<WordChar> list = getWordChars(sql);
        return parseWorde(list);
    }


    public static List<WordChar> getWordChars(String sql) {
        String[] items_ = sql.split(EN_);
        List<WordChar> lists = new ArrayList<>();
        for (String i : items_) {
            String k = i.trim();

            if (StringUtils.isBlank(k) || k.equals(CN_) || k.equals(EN_)) {
                continue;
            }
            WordChar wordChar = new WordChar(k);
            lists.add(wordChar);

        }
        return lists;
    }


    public static String parseWorde(List<WordChar> words) {
        StringBuilder sbt = new StringBuilder();
        int size = words.size();
        for (int index = 0; index < size; index++) {
            WordChar wordChar = words.get(index);
            if (wordChar.getAppendBankSpace() == null) {
                if (wordChar.getWord().endsWith(SPLIT_)) {
                    int nextIndex = index + 1;
                    if (nextIndex < size) {
                        words.get(nextIndex).setAppendBankSpace(false);
                    }
                }
                if (wordChar.getWord().startsWith(SPLIT_)) {
                    wordChar.setAppendBankSpace(false);
                } else {
                    wordChar.setAppendBankSpace(true);
                }
            }
        }
        for (WordChar wordChar : words) {
            if (sbt.length() == 0) {
                sbt.append(wordChar.getWord());
            } else {
                if (wordChar.getAppendBankSpace() != null && wordChar.getAppendBankSpace()) {
                    sbt.append(EN_).append(wordChar.getWord());
                } else {
                    sbt.append(wordChar.getWord());
                }

            }

        }

        return sbt.toString();
    }


}
