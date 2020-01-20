package org.kx.util.ddl;

import com.github.houbb.word.checker.core.impl.EnWordChecker;
import org.apache.commons.lang3.StringUtils;
import org.kx.util.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ： Created by  xianguang.skx Since 2019/11/28
 */

public class IDBSQLFormat {
    static String EN_ = " "; //英文空格
    static String CN_ = " "; ///中文空格
    static String SPLIT_ = "'"; //分割符号

    static String RIGHTFX = "hj,ts,tc,gmt"; //特定字符过滤

    public static void main(String[] args) {
        String ss = "select user_db  from sv_ss where id =  '6305721'  ;update user_db  set status = -1 ,gmt_modified = now() where id =  6305721  ;"
            + "update     fill_db    set status = -1 ,gmt_modified = now()   where id in "
            + "('   68910456,68409390, 67939131,67936760,67926705   ')   ;;; " ;

        System.out.println(dealStr(ss));
    }


    public static String dealStr(String sqls) {
        String[] items_ = sqls.split(";");
        StringBuilder sbt = new StringBuilder();
        StringBuilder warnInfo = new StringBuilder(); //注意
        for (String i : items_) {
            if (StringUtils.isBlank(i)) {
                continue;
            }
            List<WordChar> list = getWordChars(i);
            sbt.append(dealStr_(list)).append(";\n");
            warnInfo.append(warnStr_(list));
        }
        sbt.append(warnInfo);
        return sbt.toString();
    }


    public static String dealStr_(List<WordChar> list) {
         return parseWorde(list);
    }

    public static String warnStr_(List<WordChar> list) {
        StringBuilder sbt = new StringBuilder();
        for (WordChar wordChar : list) {
            String word =wordChar.getWord() ;
            if(Validator.isChars(word)) {
                boolean result = EnWordChecker.getInstance().isCorrect(word);
                if (!result) {
                    String bestWord = EnWordChecker.getInstance().correct(word);
                    sbt.append("注意: "+wordChar.getWord()+"可能存在拼写错误，是否想要 : " + bestWord +"\n");
                }
            }else  if(word.contains("_")){
                String[] words = word.split("_");
                for(String w :words){
                    if(RIGHTFX.contains(w)){
                        continue;
                    }
                    if(Validator.isChars(w)) {
                        boolean result = EnWordChecker.getInstance().isCorrect(w);
                        if (!result) {
                            String bestWord = EnWordChecker.getInstance().correct(w);
                            sbt.append("注意: " + wordChar.getWord() + "可能存在拼写错误，是否想要 : " + bestWord + "\n");
                        }
                    }
                }
            }

        }
        return sbt.toString();
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
