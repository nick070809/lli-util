package org.kx.util.biz;

import org.junit.Test;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/14
 */

public class XD {

    final static String kg =" ";
    final static String d =".";
    final static double sw = 12.5;
    final static  Integer sg = 94;
    final static  Integer hg = 85;
    final static  Integer leg = 1170 ;


    @Test
    public void  prid(){
       System.out.println(getL1s2());
        System.out.println(getL1s1(true,leg));
        //System.out.println(1060/12.5);
        System.out.println(getL1s1(true,25));

    }

    private  String  getL1s2(){
        //11.7*100
        StringBuilder sbt = new StringBuilder();
        sbt.append(getL1s1(true,25)).append(getL1b1( 340)).append(getL1b1( 390)).append(getL1b1( 340));
        return  sbt.toString();
    }
    private  String  getL1s1(boolean start ,Integer legth){
        //11.7*100
        StringBuilder sbt = start?new StringBuilder(d):new StringBuilder();
        int foot = 0;
        int bz = 0;

        while (true){

            if((bz+1) %2 == 0){
                sbt.append(d);
            }else {
                foot ++ ;
                sbt.append(kg);
            }

            if(foot* sw >= legth){
                sbt.append(d);
                break;
            }
            bz ++ ;
         }
        return  sbt.toString();
    }

    private  String  getL1b1(Integer legth){
        //11.7*100
        StringBuilder sbt =  new StringBuilder();
        int foot = 0;
        int bz = 0;

        while (true){

            if((bz+1) %2 == 0){
                sbt.append(kg);
            }else {
                foot ++ ;
                sbt.append(kg);
            }

            if(foot* sw >= legth){
                sbt.append(d);
                break;
            }
            bz ++ ;
        }
        return  sbt.toString();
    }


    private  String  getL1w(){
        //10.6*100





        return  null;
    }

}
