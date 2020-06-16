package org.kx.util;

import org.apache.commons.lang3.StringUtils;
import org.fla.nnd.s1.Cx;
import org.kx.util.image.Base64ImageUtil;

import javax.annotation.processing.FilerException;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunkx on 2017/5/17.
 */
public class FileUtil {
    public static void createDir(String dirPath) throws IOException {
        File daofile = new File(dirPath);
        if (!daofile.exists()) daofile.mkdirs();
    }

    public static File createFile(String filePath) throws IOException {
        filePath = filePath.replace("\\", "/");
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        createDir(dirPath);
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile(); //创建文件
        return file;
    }

    public static boolean createFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            createDir(file.getParentFile());
        }
        return file.createNewFile();
    }


    public static void createDir(File dir) {
        if (!dir.getParentFile().exists()) {
            createDir(dir.getParentFile());
        }
        dir.mkdir();
    }


    /**
     * 将文本文件中的内容读入到buffer中
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    /**
     * 读取文本文件内容
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtil.readToBuffer(sb, filePath);
        return sb.toString();
    }


    /**
     * 读取第一行
     */
    public static String readFirstLine(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        reader.close();
        is.close();
        return line;
    }


    /**
     * 获取目录下所有的文件
     *
     * @param filePath
     * @return
     */
    public static List<File> listFiles(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            return Arrays.asList(files);
        }
        return null;
    }

    /**
     * 字符串写入文件
     * 使用缓冲区
     */
    public static void writeStringToFile(String content, String filePath) throws IOException {
        File file = new File(filePath);
        //创建文件
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //FileWriter fwriter = new FileWriter(filePath);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
        BufferedWriter writer = new BufferedWriter(out);
        writer.write(content);
        writer.close();
    }

    /**
     * 将字符串追加到文件最后
     */
    public static void appedStringToFile(String content, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath, true);//true表示在文件末尾追加
        fos.write(content.getBytes());
        fos.close();//流要及时关闭
    }


    public static byte[] readFileInBytes(String filePath) {
        final int readArraySizePerRead = 4096;
        File file = new File(filePath);
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        try {
            if (file.exists()) {
                DataInputStream isr = new DataInputStream(new FileInputStream(
                        file));
                byte[] tempchars = new byte[readArraySizePerRead];
                int charsReadCount = 0;

                while ((charsReadCount = isr.read(tempchars)) != -1) {
                    for (int i = 0; i < charsReadCount; i++) {
                        bytes.add(tempchars[i]);
                    }
                }
                isr.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return toPrimitives(bytes.toArray(new Byte[0]));
    }


//5116328

    //获取最大的数字,行
    public static String getMaxNumLine(String filePath) throws Exception {

        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        Long max = 0L;
        Long laji = 0L;
        Long youyong = 294683693L;
        Long size = 0L;
        Long pages = 0L;
        String maxLineStr = null;
        StringBuilder ss = new StringBuilder();
        while (line != null) { // 如果 line 为空说明读完了
            String[] blocks = line.split(",");
            if (blocks.length == 2) {
                Long currentNum = Long.parseLong(blocks[1]);
                if (currentNum >= 1150L) {
                    laji = laji + currentNum;
                    size++;
                    if (size % 5000 == 0) {
                        writeStringToFile(ss.toString(), "/Users/xianguang/Downloads/20200203_" + pages + ".txt");
                        ss = new StringBuilder();
                        size = 0L;
                        pages++;
                    }
                    ss.append(blocks[0]).append(",");
                }
                if (currentNum >= max) {
                    max = currentNum;
                    maxLineStr = line;
                }
            }
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        System.out.println(size);
        System.out.println(laji);
        System.out.println(youyong);
        double f = 100 * laji / youyong;
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println(df.format(f) + "%");
        writeStringToFile(ss.toString(), "/Users/xianguang/Downloads/20200203_1.txt");
        return maxLineStr;
    }


    static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }


    /**
     * 返回当前目录所有文件(包含子目录)
     *
     * @param dir
     * @return list
     * @throws Exception
     */
    public static List<File> showListFile(File dir) throws Exception {
        List<File> list = new ArrayList<>();
        //查找参数文件是否存在,只检查第一个入参
        if (!dir.exists()) {
            throw new FilerException("找不到文件");
        }
        //如果是目录那么进行递归调用
        if (dir.isDirectory()) {
            //获取目录下的所有文件
            File[] f = dir.listFiles();
            //进行递归调用,最后总会返回一个list
            for (File file : f) {
                list.addAll(showListFile(file));
            }
        } else {//不是目录直接添加进去
            list.add(dir);
        }
        return list;
    }

    /**
     * 是否为文本
     *
     * @return
     */
    public static boolean isTxt(String fileSuffix) {

        List<String> strs = Arrays.asList("txt", "json", "md", "sql", "java", "xml", "properties");

        return strs.stream().filter(str -> str.equalsIgnoreCase(fileSuffix)).findAny().isPresent();
    }

    /**
     * 是否为文本
     *
     * @return
     */
    public static boolean isJava(String fileSuffix) {


        return fileSuffix.equals("java") ;
    }

    /**
     * 是否为图片
     *
     * @return
     */
    public static boolean isPic(String fileSuffix) {

        List<String> strs = Arrays.asList("jpg", "eddx", "gif", "jpeg", "pdf");

        return strs.stream().filter(str -> str.equalsIgnoreCase(fileSuffix)).findAny().isPresent();
    }


    /**
     * 是否为图片
     *
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


    public static void readAll(String filepath) throws Exception {
        String dirPath = filepath.substring(0, filepath.lastIndexOf(File.separator));
        String dirName = "PZ";
        String currentFileName = null;
        InputStream is = new FileInputStream(filepath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            if (StringUtils.isNotBlank(line)) {
                if (line.startsWith("PZ")) {
                    dirName = Cx.show(line.substring(2));
                    File dirPathNew = new File(dirPath + File.separator + dirName);
                    dirPath = dirPathNew.getAbsolutePath();
                    //创建目录
                    if (!dirPathNew.exists()) {
                        dirPathNew.getParentFile().mkdirs();

                    }
                } else if (line.startsWith("CZ")) {
                    String content = Cx.show(line.substring(2));
                    if (isTxt(getSuffix(currentFileName))) {
                        writeStringToFile(content, dirPath + File.separator + currentFileName);

                    } else if (isPic(getSuffix(currentFileName))) {

                        Base64ImageUtil.GenerateImage(content, dirPath + File.separator + currentFileName);
                    }
                } else if (line.startsWith("FZ")) {
                    currentFileName = Cx.show(line.substring(2));
                }
            }
        }
        reader.close();
        is.close();


    }

    public static void writeAll(String dirpath) throws Exception {
        File dir = new File(dirpath);
        String dirPath_ = dirpath.replace(File.separator, "_");
        String dirname = dir.getName();
        List<File> files = filterHidden(dirpath, showListFile(dir));
        String br = "\n\n";
        StringBuilder sbt = new StringBuilder("PZ" + Cx.encrypt(dirPath_)).append(br);
        for (File file : files) {
            String _fileName = "FZ" + Cx.encrypt(file.getName());
            //String _fileName = file.getAbsolutePath();
            if (isTxt(getSuffix(file.getName()))) {
                String content = "CZ" + Cx.encrypt(FileUtil.readFile(file.getAbsolutePath()));
                sbt.append(_fileName).append("\n").append(content).append(br);
            } else if (isPic(getSuffix(file.getName()))) {
                String content = "CZ" + Cx.encrypt(Base64ImageUtil.GetImageStr(file.getAbsolutePath()));
                sbt.append(_fileName).append("\n").append(content).append(br);
            }
        }
        writeStringToFile(sbt.toString(), "/Users/xianguang/IdeaProjects/OLL/" + DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmmss") + ".rm");
    }


    public static boolean filterHidden(String dirpath, File file) {
        String fileName = file.getAbsolutePath().substring(dirpath.length());
        return !fileName.startsWith(".");

    }

    public static List<File> filterHidden(String dirpath, List<File> files) {
        return files.stream().filter(e -> filterHidden(dirpath, e)).collect(Collectors.toList());
    }


    //判断编码格式方法
    public static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    public static void main(String[] args) throws Exception {
        //writeAll("/Users/xianguang/temp/绘图3.txt");
        String filePath = " ";

        System.out.println(getFilecharset(new File(filePath)));

    }

}


