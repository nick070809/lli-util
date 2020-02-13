package org.fla.nnd.s1;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.junit.Test;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;
import org.kx.util.mail.MailSendUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */

public class CommonTest {
    @Test
    public void soo() throws ParseException {

        Date signedDate = DateUtil.getDate("2019-11-21 23:59:59","yyyy-MM-dd HH:mm:ss") ;
        signedDate = DateUtil.getMonthLastDay(signedDate);
        int month  = Months.monthsBetween(new DateTime(signedDate), new DateTime(DateUtil.getDate("2019-10-26 23:59:59","yyyy-MM-dd HH:mm:ss"))).getMonths() ;

        System.out.println(month);

        BigDecimal ww = BigDecimal.ZERO ;
        BigDecimal wwww = new BigDecimal(123456.789066666666666661);
        Integer runMonth = month;
        if(runMonth >0){
            ww =  wwww.divide(BigDecimal.valueOf(runMonth),6,RoundingMode.HALF_UP) .multiply(BigDecimal.valueOf(12)) ; //gmv折算成年
        }
        System.out.println(ww.toString());

    }
    @Test
    public void soqo() throws ParseException, IOException {
        String  lll = FileUtil.readFile("/Users/xianguang/temp/32.txt");
        String[] lines  = lll.split("\n");
        int li = 0;
        for(String line :lines){
            try{
                li ++ ;
                String[] words  = line.split("\t");

                BigDecimal result = new BigDecimal(words[2]).add(new BigDecimal(words[4]));
                if(result.compareTo(BigDecimal.ZERO) != 0){
                    System.out.println((li +1)+" ___ "+line);
                }
            }catch (Exception ee){
                System.out.println(li +" ___ "+ee);
            }

        }
    }


    @Test
    public void soqos() throws ParseException, IOException {
        String  lll = FileUtil.readFile("/Users/xianguang/temp/32.txt");
        String[] lines  = lll.split("\n");
        int li = 0;
        BigDecimal sss =BigDecimal.ZERO;
        for(String line :lines){
            try{
                li ++ ;
                String[] words  = line.split("\t");
                sss = sss.add(new BigDecimal(words[3]));
            }catch (Exception ee){
                System.out.println(li +" ___ "+ee);
            }

        }
        System.out.println(sss.toString());
    }


    @Test
    public  void sendMail() throws Exception {

        String content = FileUtil.readFile("").trim();
        MailSendUtil.sendCommonMail("King",content);

    }


}
