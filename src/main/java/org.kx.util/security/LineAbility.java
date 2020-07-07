package org.kx.util.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.Validator;

import java.io.IOException;
import java.util.*;

/**
 * Description ： 行能力
 * Created by  xianguang.skx
 * Since 2020/6/16
 */

public class LineAbility {

    public  static String KONGGE = " ";
    public  static String NEWLINE = "NL";
    private HashMap<String, Integer> counts = new HashMap<>();

    //public  static final String NEWLINE ="NEWLINE";

    @Test
    public void testgetWords3() throws IOException {
        // String content = FileUtil.readFile(" ");

        String content = "/**";

        String[] lines =  content.split(LineAbility.NEWLINE);
        for(String line_ :lines){
            if(StringUtils.isBlank(line_)){
                continue;
            }
            LineInfo lineInfo = new LineInfo();
            lineInfo.setDictedLine(line_);
            reDictLine(lineInfo);
            System.out.println(lineInfo.getOriginLine());
        }


      /*  LineInfo lineInfo = new LineInfo();
        lineInfo.setDictedLine(content);
        reDictLine(lineInfo);
        System.out.println(lineInfo.getOriginLine());*/
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
     * 原始行标准化
     */
    public void standOriginLine(LineInfo lineInfo) {
        String parsedLine = lineInfo.getPreDictLine();
        if (StringUtils.isBlank(parsedLine)) {
            return;
        }
        try {
            String[] words = parsedLine.split(KONGGE);
            StringBuilder sbt = new StringBuilder();
            Symbol lastSymbol = null;
            for (String word : words) {
                if (StringUtils.isNotBlank(word)) {
                    String word_ = word.trim();
                    Symbol symbol = Symbol.nameOf(word_);
                    if(symbol != null){
                        word_ = symbol.getSymbol();
                    }
                    if (sbt.length() == 0) {
                        sbt.append(word_);
                    } else {

                        if (symbol != null || lastSymbol != null) {
                            sbt.append(word_);
                        } else {
                            sbt.append(KONGGE).append(word_);
                        }
                    }

                    lastSymbol = symbol;

                }
            }
            lineInfo.setStandOriLine(sbt.toString());

        } catch (Exception ex) {
            System.out.println("standOriginLine " + parsedLine + " ox error :" + ex.getMessage());
        }

    }


    /**
     * 原始行标准化2
     */
    public void standOriginLine2(LineInfo lineInfo) {
        String parsedLine = lineInfo.getPreDictLine();
        if (StringUtils.isBlank(parsedLine)) {
            return;
        }
        try {
            String[] words = parsedLine.split(KONGGE);
            List<String> lines = new ArrayList<>(words.length);
            lineInfo.setStandOriLines(lines);

            StringBuilder sbt = new StringBuilder();
            Symbol lastSymbol = null;
            for (String word : words) {
                if (StringUtils.isNotBlank(word)) {
                    String word_ = word.trim();
                    Symbol symbol = Symbol.nameOf(word_);
                    if(symbol != null){
                        word_ = symbol.getSymbol();
                    }
                    if (sbt.length() == 0) {
                        sbt.append(word_);
                    } else {

                        if (symbol != null || lastSymbol != null) {
                            sbt.append(word_);
                        } else {
                            sbt.append(KONGGE).append(word_);
                        }
                    }

                    lastSymbol = symbol;
                    lines.add(sbt.toString());

                }
            }
            lineInfo.setStandOriLine(sbt.toString());

        } catch (Exception ex) {
            System.out.println("standOriginLine " + parsedLine + " ox error :" + ex.getMessage());
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
            if (word.startsWith(Symbol.SZ_.name())) {
                word = word.substring(Symbol.SZ_.name().length());
            } else if (word.startsWith(Symbol.ZW_.name())) {
                word = word.substring(Symbol.ZW_.name().length());
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
                str = Symbol.SZ_.name() + s;
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
                str = Symbol.ZW_.name() + s;
            }
            if (Validator.containsFullAngle(firstChar)) {
                str = Symbol.ZW_.name() + s;
            }
            if (sbt.length() == 0) {
                sbt.append(str);
            } else {
                sbt.append(KONGGE).append(str).append(KONGGE);
            }

        }
        return sbt.toString();
    }


    public void preSaveLineCount(List<String> standLines) {
        for (String standLine : standLines) {
            if(StringUtils.isBlank(standLine)){
                return;
            }
            try {
                Character character = standLine.charAt(0);
                if(!Validator.isChars(character+"")){
                    continue;
                }

                Integer count = counts.get(standLine);
                if (count == null) {
                    counts.put(standLine, 1);
                    continue;
                }
                counts.put(standLine, 1 + count);
            }catch (Exception x){
                System.out.println("standLine ex : "+standLine);
                x.printStackTrace();
                throw  x ;

            }

        }
    }

    public static void main(String[] args) {
        System.out.println(Validator.isChars("\"\tp"));
    }

    public void dicLine(LineInfo lineInfo) throws IOException {
        List<String> standLines = lineInfo.getStandOriLines();
        if (standLines == null || standLines.isEmpty()) {
            return;
        }
        int size = standLines.size();
        for (int i = size - 1; i >= 0; i--) {
            String line = standLines.get(i);
            String index = MyDict.getIndex(line);
            if (index != null) {
                String remainStr = standLines.get(size - 1).substring(line.length());
                if (StringUtils.isBlank(remainStr)) {
                    lineInfo.setDictedLine(index);
                    return;
                }
                LineInfo remainLineInfo = LineInfo.of(remainStr);
                parsingOriginLine(remainLineInfo);
                dictOriginLine(remainLineInfo);

                String xf = index + remainLineInfo.getDictedLine();
                lineInfo.setDictedLine(xf);
                return;
            }
        }
        dictOriginLine(lineInfo);
    }


    public void saveLineCount() throws IOException {

        Iterator<Map.Entry<String, Integer>> entries = counts.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (entry.getValue() != null && entry.getValue() > 2) {
                MyDict.putIndexNotPersistence(entry.getKey());
            }
        }
    }

}
