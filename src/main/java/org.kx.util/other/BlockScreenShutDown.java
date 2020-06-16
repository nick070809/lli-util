package org.kx.util.other;

import com.alibaba.fastjson.JSONObject;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;
import org.kx.util.ChineseToSpell;
import org.kx.util.FileUtil;
import org.kx.util.base.HttpClient;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/4
 */

public class BlockScreenShutDown {


    @Test
    public void mooveMouse() throws AWTException {
        Robot robot = new Robot();
        Random random = new Random();
        int a = 3500; //足够的时间去关闭程序
        robot.delay(3000);

        int times = 0;
        while (times < 2) {
            times++;
            robot.mouseMove(100, 100);
            robot.delay(a);
            robot.mouseMove(1000, 400);
            robot.delay(a);
            //鼠标无法移动
        }
        //robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Test
    public  void  pressKey() throws AWTException {
        Robot robot = new Robot();
        Random random = new Random();
        robot.delay(5000);
        int a = 0;
        while(true) {

            robot.keyPress(KeyEvent.VK_Y);
            robot.keyRelease(KeyEvent.VK_Y);
            a = Math.abs(random.nextInt()) % 100 + 50;
            robot.delay(a);

            robot.keyPress(KeyEvent.VK_B);
            robot.keyRelease(KeyEvent.VK_B);
            a = Math.abs(random.nextInt()) % 100 + 50;
            robot.delay(a);

            robot.keyPress(KeyEvent.VK_Q);
            robot.keyRelease(KeyEvent.VK_Q);
            a = Math.abs(random.nextInt()) % 100 + 50;
            robot.delay(a);

            robot.keyPress(KeyEvent.VK_U);
            robot.keyRelease(KeyEvent.VK_U);

            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

            a = Math.abs(random.nextInt()) % 2000 + 1000;
            System.out.println(a);
            robot.delay(a);
        }
    }


    @Test
    public  void  pressOneKey() throws AWTException {
        Robot robot = new Robot();
        robot.delay(5000);
        robot.keyPress(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_F);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }



    @Test
    public  void  dd() throws AWTException, UnsupportedEncodingException {
        String question = "你一点都不智能";
        System.out.println(question);
        String INFO = URLEncoder.encode(question, "utf-8");
        String APIKEY = "7d14fa4bc295404a9fced576c37453e5"; //图灵机器人的apikey
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;

        String result =HttpClient.sendModel(getURL,"",null) ;
        JSONObject  jsonContent = JSONObject.parseObject(result);
        //System.out.println(jsonContent.get("text"));


        String pin = ChineseToSpell.getFullSpell((String) jsonContent.get("text")) ;

        String[] pings = pin.split(" ");


        //KeyEvent keyEvent = KeyEvent.

                //new KeyEvent();

    }



    @Test
    public void getCharInt(){
        System.out.println((int)'D');
        System.out.println((int)'d');
        System.out.println(KeyEvent.VK_D);
        System.out.println(KeyEvent.getKeyText(68));
        System.out.println(KeyEvent.getKeyText(100));

    }



    @Test
    public void getCharInt3() throws IOException, AWTException {
        String getURL = "http://www.baidu.com";
        String result =HttpClient.sendModel(getURL,"",null) ;
        System.out.println(result);
        writeBy(result);

    }

    public void writeBy(String result) throws AWTException, IOException {

        Robot robot = new Robot();
        robot.delay(5000);

        String[] pings = result.split(",");

        for(String ps :pings){
            String pin = ChineseToSpell.getFullSpell(ps) ;
            char[] pps = pin.toUpperCase().toCharArray();
            for(char p:pps ){
                int keycode =(int)p;
                robot.keyPress(keycode);
                robot.keyRelease(keycode);
                robot.delay(280);
            }
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
        }
    }


    //不能写中文 ， 拼音无法选择
    @Test
    public void getCharInt2() throws AWTException, IOException {
        String z = FileUtil.readFile("/Users/xianguang/IdeaProjects/0910/huijin/settle-trademonitor/settle-trademonitor-service/src/main/java/com/alibaba/trademonitor/biz/dap/processor/AbstractMonitorPlanProcessor.java");
        writeBy(z);
    }


    public static String getFullSpell(String chinese) {
        // 用StringBuffer（字符串缓冲）来接收处理的数据
        StringBuffer sb = new StringBuffer();
        //字符串转换字节数组
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //转换类型（大写or小写）
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //定义中文声调的输出格式
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //定义字符的输出格式
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        for (int i = 0; i < arr.length; i++) {
            //判断是否是汉子字符
            if (arr[i] > 128) {
                try {
                    sb.append(ChineseToSpell.capitalize(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                // 如果不是汉字字符，直接拼接
                sb.append(arr[i]);
            }
        }
        return sb.toString();
    }


}
