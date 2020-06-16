package org.kx.util.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description ： 行能力
 * Created by  xianguang.skx
 * Since 2020/6/16
 */

public class LineAbility {

    static String KONGGE = " ";
    //public  static final String NEWLINE ="NEWLINE";

    @Test
    public void testgetWords3() throws IOException {
        // String content = FileUtil.readFile(" ");

        String content = "/**";


        LineInfo lineInfo = new LineInfo();
        lineInfo.setDictedLine(content);
        reDictLine(lineInfo);
        //System.out.println("parsingOriginLine:\n "+lineInfo.getPreDictLine());
        //dictOriginLine(lineInfo) ;
        // System.out.println("dictOriginLine:\n "+lineInfo.getDictedLine());
    }


    /**
     * 字典解析
     */
    public void dictOriginLine(LineInfo lineInfo) {
        String parsedLine = lineInfo.getPreDictLine();
        if (StringUtils.isBlank(parsedLine)) {
            return;
        }
        try {
            String[] words = parsedLine.split(KONGGE);
            StringBuilder sbt = new StringBuilder();
            for (String word : words) {
                if (StringUtils.isNotBlank(word)) {
                    String word_ = word.trim();
                    Symbol symbol = Symbol.nameOf(word_);
                    if (symbol != null) {
                        sbt.append(symbol.getSymbol());
                    } else {
                        String index = MyDict.getIndex(word_);
                        if (index == null) {
                            index = MyDict.putIndexNotPersistence(word_);
                            lineInfo.setHasNewWord(true);
                        }
                        sbt.append(index);
                    }
                }

            }
            lineInfo.setDictedLine(sbt.toString());
        } catch (Exception ex) {
            System.out.println("parsedLine " + parsedLine + " ox error :" + ex.getMessage());
        }

    }


    /**
     * 字典逆向解析
     */
    public void reDictLine(LineInfo lineInfo) {
        String dictedLine = lineInfo.getDictedLine();
        if (StringUtils.isBlank(dictedLine)) {
            return;
        }

        Symbol[] values = Symbol.values();
        String parsedLine = new String(dictedLine);
        for (Symbol symbol : values) {
            parsedLine = parseOrigiLine(parsedLine, symbol);
        }
        parsedLine = reTransStr(parsedLine);
        lineInfo.setOriginLine(parsedLine);
    }



    /**
     * 拆词
     *
     * @return
     */
    public String reTransStr(String parsedLine) {
        String[] words = parsedLine.split(KONGGE);
        List<String> indexList = new ArrayList<>();
        for (String word : words) {
            if (Validator.hasDigit(word)) {
                indexList.addAll(splitStr(word));
            } else {
                indexList.add(word);
            }
        }
        StringBuilder sbt = new StringBuilder();
        Symbol lastSymbol = null;
        for (String index : indexList) {

            Symbol symbol = Symbol.nameOf(index);
            String word = index;
            if (symbol != null) {
                word = symbol.getSymbol();
            } else {
                word = MyDict.getWordByIndex(index);
            }
            if (StringUtils.isBlank(word)) {
                continue;
            }
            //数字,中文特殊处理
            if (word.startsWith(Symbol.SHUZI_.name())) {
                word = word.substring(Symbol.SHUZI_.name().length());
            } else if (word.startsWith(Symbol.ZHONGWEN_.name())) {
                word = word.substring(Symbol.ZHONGWEN_.name().length());
            }


            if (sbt.length() == 0) {
                sbt.append(word);
            } else {
                try {
                    if (symbol != null || lastSymbol != null) {
                        sbt.append(word);
                    } else {
                        sbt.append(KONGGE).append(word);
                    }
                } catch (Exception ex) {
                    System.out.println(word);
                    ex.printStackTrace();
                    throw ex;
                }

            }
            lastSymbol = symbol;
        }
        return sbt.toString();
    }


    /**
     * 拆词
     *
     * @return
     */
    public List<String> splitStr(String str) {
        String[] chars = str.split("");
        List<String> wordList = new ArrayList<>();
        StringBuilder word = null;
        for (String char_ : chars) {
            if (Validator.isChars(char_)) {
                if (word != null && word.length() > 0) {
                    wordList.add(word.toString());
                }
                word = new StringBuilder(char_);
            } else if (Validator.isNum(char_)) {
                word.append(char_);
            } else {
                if (word != null && word.length() > 0) {
                    wordList.add(word.toString());
                }
                word = new StringBuilder(char_);
            }
        }
        wordList.add(word.toString());
        return wordList;
    }


    /**
     * 原始行解析
     *
     * @return
     */
    public void parsingOriginLine(LineInfo lineInfo) {
        String oriLine = lineInfo.getOriginLine();
        if (StringUtils.isBlank(oriLine)) {
            return;
        }

        Symbol[] values = Symbol.values();
        String parsedLine = new String(lineInfo.getOriginLine());
        for (Symbol symbol : values) {
            //System.out.println(symbol.name());
            parsedLine = parseOrigiLine(parsedLine, symbol);
        }
        parsedLine = dealNum(parsedLine);
        parsedLine = dealChinese(parsedLine);
        lineInfo.setPreDictLine(parsedLine);
    }


    /**
     * 拆解原始行
     */
    private String parseOrigiLine(String line, Symbol symbol) {

        if (line.contains(symbol.getSymbol())) {
            return line.replace(symbol.getSymbol(), KONGGE + symbol.name() + KONGGE);
        }
        return line;
    }

    /**
     * 数字处理
     */
    private String dealNum(String line) {

        String[] strings = line.split(KONGGE);
        StringBuilder sbt = new StringBuilder();
        for (String s : strings) {
            if (s.length() < 1) {
                continue;
            }
            String firstChar = s.substring(0, 1);
            String str = s;
            if (Validator.isNum(firstChar)) {
                str = Symbol.SHUZI_.name() + s;
            }
            if (sbt.length() == 0) {
                sbt.append(str);
            } else {
                sbt.append(KONGGE).append(str).append(KONGGE);
            }

        }
        return sbt.toString();
    }

    /**
     * 中文处理
     */
    private String dealChinese(String line) {

        String[] strings = line.split(KONGGE);
        StringBuilder sbt = new StringBuilder();
        for (String s : strings) {
            if (s.length() < 1) {
                continue;
            }
            String firstChar = s.substring(0, 1);
            String str = s;
            if (Validator.containsChinese(firstChar)) {
                str = Symbol.ZHONGWEN_.name() + s;
            }
            if (Validator.containsFullAngle(firstChar)) {
                str = Symbol.ZHONGWEN_.name() + s;
            }
            if (sbt.length() == 0) {
                sbt.append(str);
            } else {
                sbt.append(KONGGE).append(str).append(KONGGE);
            }

        }
        return sbt.toString();
    }

}
