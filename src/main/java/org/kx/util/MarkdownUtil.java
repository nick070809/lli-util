package org.kx.util;

import org.junit.Test;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/23
 */

public class MarkdownUtil {


    public  String read2Table(String originContent){
        String[] lines = originContent.split("\n");
        StringBuilder sbt = new StringBuilder("\n");
        boolean getTitle = false ;
        for(String line :lines){
            String[] words = line.split("\t");
            sbt.append("|");
            for(String word:words){
                sbt.append(word).append("|");
            }
            sbt.append("\n");
            if(!getTitle){
                sbt.append("|");
                for(String word:words){
                    sbt.append(" ----  ").append("|");
                }
                sbt.append("\n");
                getTitle = true ;
            }
        }

        return   sbt.toString();

    }


    @Test
    public  void testR(){
        System.out.println(read2Table("gmt_create\tdeal_state\tbiz_no\tgmt_modified\n" +
                "2020-08-19 17:24:38\tS\t2020081922001148431444077786sMBD717161442087\t2020-08-19 17:24:38\n" +
                "2020-08-19 17:24:39\tS\t2020081922001148431444077786sMBD716835233263\t2020-08-19 17:24:39\n" +
                "2020-08-19 17:24:40\tS\t2020081922001148431444077786sMBD716886947272\t2020-08-19 17:24:40\n" +
                "2020-08-19 17:26:38\tS\t2020081922001148431444077786sMBD717176770061\t2020-08-19 17:26:38\n" +
                "2020-08-19 17:26:39\tS\t2020081922001148431444077786sMBD716736995487\t2020-08-19 17:26:39\n" +
                "2020-08-19 17:26:40\tS\t2020081922001148431444077786sMBD716634688532\t2020-08-19 17:26:40\n" +
                "2020-09-23 11:22:02\tS\t2020081922001148431444077786sdz_b_2138628929460\t2020-09-23 11:22:03"));
    }
}
