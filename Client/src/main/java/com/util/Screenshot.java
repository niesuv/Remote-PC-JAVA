package com.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Screenshot {
    Robot robot;
    private static Screenshot instance = new Screenshot();
    private Rectangle rectangle;

    private Screenshot() {
        try {
            robot = new Robot();
            rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        } catch (AWTException e) {
            System.out.println("Cannot create robot");
        }
    }

    public static Screenshot getInstance() {
        return instance;
    }

    public String takeScreenShot() {
        try {
            String filename ="snapshot" + ZonedDateTime.now().format(DateTimeFormatter
                    .ofPattern("dd-MM-yyyy HH-mm")) +".png";
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            ImageIO.write(bufferedImage, "png", new File(filename));
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Screenshot.getInstance().takeScreenShot();
    }
}
