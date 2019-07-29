package org.kx.util.base;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.FileUtil;
import org.kx.util.config.MyCnfig;
import org.kx.util.generate.IdGenerate;

import java.util.Map;

/**
 * Description ： Created by  xianguang.skx Since 2019/5/31
 */

public class WriteJarVersion {



    public  static void  parse(String info ,Map<String,String > maps) throws Exception {

        if(StringUtils.isBlank(info)){
            return;
        }
        try{
            //bugfix 特殊化处理
            if(info.contains("-bugfix-")){
                String[] sss1 = info.split("-bugfix-");
                String[] sss = sss1[0].split("-");

                if(sss.length >2){
                    StringBuilder sbt = new StringBuilder() ;
                    for(int index =0 ;index <sss.length-1 ;index++ ){
                        if(sbt.length() > 0){
                            sbt.append("-") ;
                            sbt.append(sss[index]) ;
                        }else {
                            sbt.append(sss[index]) ;
                        }
                    }
                    maps .put(sbt.toString(),sss[sss.length-1] +"-bugfix-"+ sss1[1]);
                }else {
                    maps .put(sss[0],sss[1]+"-bugfix-"+ sss1[1]);
                }
            } else

                //CN 特殊化处理
                if(info.contains("-CN-")){
                    String[] sss1 = info.split("-CN-");
                    String[] sss = sss1[0].split("-");

                    if(sss.length >2){
                        StringBuilder sbt = new StringBuilder() ;
                        for(int index =0 ;index <sss.length-1 ;index++ ){
                            if(sbt.length() > 0){
                                sbt.append("-") ;
                                sbt.append(sss[index]) ;
                            }else {
                                sbt.append(sss[index]) ;
                            }
                        }
                        maps.put(sbt.toString(),sss[sss.length-1] +"-CN-"+ sss1[1]);
                    }else {
                        maps.put(sss[0],sss[1]+"-CN-"+ sss1[1]);
                    }
                }else {

                    //SNAPSHOT 特殊化处理
                    if (info.endsWith("-SNAPSHOT")) {
                        String[] sss = info.split("-");
                        if (sss.length > 2) {
                            StringBuilder sbt = new StringBuilder();
                            for (int index = 0; index < sss.length - 2; index++) {
                                if (sbt.length() > 0) {
                                    sbt.append("-");
                                    sbt.append(sss[index]);
                                } else {
                                    sbt.append(sss[index]);
                                }
                            }
                            maps.put(sbt.toString(), sss[sss.length - 2] + "-SNAPSHOT");
                        } else {
                            maps.put(sss[0], sss[1] + "-SNAPSHOT");
                        }

                    } else {
                        String[] sss = info.split("-");
                        if (sss.length > 2) {
                            StringBuilder sbt = new StringBuilder();
                            String version = sss[sss.length - 1];
                            boolean isNumeric = false;
                            for (int index = 0; index < sss.length - 1; index++) {
                                if (IdGenerate.isNumeric(sss[index].replace(".", ""))) {
                                    StringBuilder sbt0 = new StringBuilder();
                                    StringBuilder sbt1 = new StringBuilder();
                                    isNumeric = true;
                                    for (int index0 = 0; index0 < index; index0++) {
                                        if (sbt0.length() > 0) {
                                            sbt0.append("-");
                                            sbt0.append(sss[index0]);
                                        } else {
                                            sbt0.append(sss[index0]);
                                        }
                                    }
                                    for (int index1 = index; index1 < sss.length - 1; index1++) {
                                        if (sbt1.length() > 0) {
                                            sbt1.append("-");
                                            sbt1.append(sss[index1]);
                                        } else {
                                            sbt1.append(sss[index1]);
                                        }
                                    }
                                    maps.put(sbt0.toString(), sbt1.toString() + "-" + version);
                                    break;
                                }

                                if (sbt.length() > 0) {
                                    sbt.append("-");
                                    sbt.append(sss[index]);
                                } else {
                                    sbt.append(sss[index]);
                                }
                            }
                            if (!isNumeric && sbt.length() > 0) {
                                maps.put(sbt.toString(), sss[sss.length - 1]);
                            }
                        } else if(sss.length == 2){
                            maps.put(sss[0], sss[1]);
                        }else if(info.endsWith("jar")){
                            maps.put(info, "0");
                        }
                    }

                }
        }catch (Exception e){
            System.out.println(info);
            throw e ;
        }
    }



    public static String  writeDiff(Map<String,String > map1 , Map<String,String > map2 ) throws Exception {



        StringBuilder b = new StringBuilder() ;
        StringBuilder a = new StringBuilder() ;
        StringBuilder c = new StringBuilder() ;

        for (Map.Entry<String,String> entry : map1.entrySet()) {

            String jarName = entry.getKey() ;
            String jarVersion = entry.getValue() ;
            String jarOldVersion = map2.get(jarName);
            if(jarOldVersion == null){
                b.append("新增 jar   " +jarName +".jar: " + jarVersion).append("\n");
            }else if(jarVersion.equals(jarOldVersion)){
            }else{
                a.append("版本变化   "+jarName +".jar : " +jarOldVersion +" -> " + jarVersion).append("\n");
            }
        }
        for (Map.Entry<String,String> entry : map2.entrySet()) {
            String jarName = entry.getKey() ;
            String jarVersion = entry.getValue() ;
            String jarNewVersion = map1.get(jarName);
            if(jarNewVersion == null){
                c.append("减少 jar   " +jarName+".jar: " + jarVersion).append("\n");
            }
        }


        a.append(b).append(c);
        String filePath = MyCnfig.JAR_COMPARE_FILE;
        FileUtil.writeStringToFile(a.toString(),filePath);

        return filePath;

    }





}
