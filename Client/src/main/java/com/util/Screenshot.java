package com.util;

import java.awt.Robot;
import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

public class Screenshot {
    Robot robot;
    Rectangle rectangle;
    String filename;
    public Screenshot(String fileName){//fileName/filePath: if u insert a path, it's will save as that.
        try{
            robot = new Robot();
            rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            filename = fileName;
        }catch (Exception e){ System.out.println("Can't take a screenshot due to error: "+e.toString());}
    }

    public void Capture(){
        try{
            BufferedImage bufferedImage = this.robot.createScreenCapture(rectangle);
            ImageIO.write(bufferedImage,"png",new File(this.filename));
            System.out.println("Succesfully, save as " + this.filename);
        }catch (Exception e){ System.out.println("Can't take a screenshot due to error: "+e.toString());}
    }

    // public static void main(String[] arg){
    //     Screenshot screenshot = new Screenshot("img3.png");
    //     screenshot.Capture();
    // }
}
