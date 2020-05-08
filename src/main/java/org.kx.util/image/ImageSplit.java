package org.kx.util.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/8
 */

public class ImageSplit {



    /**
     * 切割图片
     *
     * @throws Exception
     */
    private static void splitImage(String imagePth) throws Exception {
        File file = new File(imagePth);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis);

        int rows = 2;
        int cols = 2;
        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;

        int count = 0;
        BufferedImage[] imgs = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }

        String pth = imagePth.substring(0, imagePth.indexOf("."));
        for (int i = 0; i < imgs.length; i++) {
            String partImages = pth+ "-"+i+".jpg";

            ImageIO.write(imgs[i], "jpg", new File(partImages));
        }
    }

    /**
     * 合并图片
     *
     * @throws Exception
     */
    private static void mergeImage(String imagePth) throws Exception {
        int rows = 2;
        int cols = 2;
        int chunks = rows * cols;

        int chunkWidth, chunkHeight;
        int type;

        File[] imgFiles = new File[chunks];
        String pth = imagePth.substring(0, imagePth.indexOf("."));
        for (int i = 0; i < chunks; i++) {
            String partImages = pth+ "-"+i+".jpg";
            imgFiles[i] = new File(partImages);
        }

        BufferedImage[] buffImages = new BufferedImage[chunks];
        for (int i = 0; i < chunks; i++) {
            buffImages[i] = ImageIO.read(imgFiles[i]);
        }
        type = buffImages[0].getType();
        chunkWidth = buffImages[0].getWidth();
        chunkHeight = buffImages[0].getHeight();

        BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
                num++;
            }
        }

        ImageIO.write(finalImg, "jpeg", new File(imagePth));
    }

    public static void main(String[] args) {
        try {
            String path ="/Users/xianguang/Desktop/rBEBp1uPSjGAcorHAAHSy0lm8bQ216.jpg";
            splitImage(path);
            mergeImage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
