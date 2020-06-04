package org.kx.util.other;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.db.FileDb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/1
 */

public class RandomSeat {
    String  userName0= "巨大";
    String  userName1 = "铆钉";
    String  userName2= "待发货";
    String  userName3 = "代发的";
    String  userName4 = "3热敷";

    String  userName ="东方大厦59";

    private  static  String filePath  =  System.getProperty("user.home") + File.separator + "filedb" + File.separator + "seat.txt";


    @Test
    public void  batchChoice() throws IOException {
        for(Integer integer = 3 ; integer<70;integer ++){
            userName = "东方大厦"+integer ;
            preChoice();
        }
        System.out.println(FileUtil.readFile(filePath));
    }

    //@Test
    public void preStart() throws IOException {
        Integer size = 70;
        List<Integer> allIndexs = new ArrayList<>(size);
        List<Seat> allSeats = new ArrayList<>(size);
        int index_ = 0;
        while (index_ < size) {
            Integer index = new Random().nextInt(size);
            if (!allIndexs.contains(index)) {
                allIndexs.add(index);
                Seat seat = new Seat();

                int LastNumber = index % 10 + 1;
                int LastSecondNumber = index / 10 + 1;
                String position = LastSecondNumber + "-" + LastNumber;

                seat.setIndex(index_);
                seat.setPosition(position);
                allSeats.add(seat);
                index_++;
            }
        }
        FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);
    }

    @Test
    public void preChoice() throws IOException {
         String  position = FileDb.getInstance().read("seat",userName);
        if(position != null){
            System.out.println(position);
            return;
        }

        String content = FileUtil.readFile(filePath);

        List<Seat> allSeats = JSON.parseArray(content,Seat.class);
        for(Seat seat:allSeats){
            if(seat.getConfirm() == null){
                seat.setUserName(userName);
                seat.setConfirm(2); //待确认
                FileDb.getInstance().insert("seat",userName,seat.position);
                break;
            }
        }
        FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);
       // System.out.println(FileUtil.readFile("/Users/xianguang/filedb/seat.txt"));
    }

    @Test
    public void confirm() throws IOException {
         String  position = FileDb.getInstance().read("seat",userName);
        if(position == null){
            System.out.println("not choice");
            return;
        }

        String content = FileUtil.readFile(filePath);
        System.out.println(content);

        List<Seat> allSeats = JSON.parseArray(content,Seat.class);


        for(Seat seat:allSeats){
            if(StringUtils.equals(seat.getUserName(),userName)) {

                seat.setConfirm(1); //确认
                break;
            }
        }

        FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);

    }


    @Test
    public void repeatChoice() throws IOException {
         String  position = FileDb.getInstance().read("seat",userName);
        if(position == null){
            System.out.println("not choice");
            return;
        }

        String content = FileUtil.readFile(filePath);
        System.out.println(content);

        List<Seat> allSeats = JSON.parseArray(content,Seat.class);

        Seat currentSeat = null;
        for(Seat seat:allSeats){
            if(StringUtils.equals(seat.getUserName(),userName)) {
                currentSeat = seat ;
            }
        }

       if(currentSeat.getConfirm() == 1){
           System.out.println("您已经确认座位: " + currentSeat.getPosition());
           return;
        }
        Integer randomFactor = new Random().nextInt(10);
        Integer targetSeat =  currentSeat.getIndex()+ randomFactor > 69 ? 0: currentSeat.getIndex()+ randomFactor ;

        Seat preSeat = allSeats.get(targetSeat);
        if(preSeat.getUserName() == null ){
            currentSeat.setUserName(null);
            currentSeat.setConfirm(null);
            preSeat.setUserName(userName);
            preSeat.setConfirm(2);
            FileDb.getInstance().insert("seat",userName,preSeat.position);
            FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);
            return;
        }

        for(int index = targetSeat; index<= 69 ;index ++){

            Seat preSeat0 = allSeats.get(targetSeat);
            if(preSeat0.getUserName() == null ){
                currentSeat.setUserName(null);
                currentSeat.setConfirm(null);
                preSeat.setUserName(userName);
                preSeat.setConfirm(2);
                FileDb.getInstance().insert("seat",userName,preSeat0.position);
                FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);
                return;
            }
        }


        for(int index = 0; index< targetSeat ;index ++){

            Seat preSeat0 = allSeats.get(targetSeat);
            if(preSeat0.getUserName() == null ){
                currentSeat.setUserName(null);
                currentSeat.setConfirm(null);
                preSeat.setUserName(userName);
                preSeat.setConfirm(2);
                FileDb.getInstance().insert("seat",userName,preSeat0.position);
                FileUtil.writeStringToFile(JSON.toJSONString(allSeats), filePath);
                return;
            }
        }
    }

}
