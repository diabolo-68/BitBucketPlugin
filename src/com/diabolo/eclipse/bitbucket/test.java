package com.diabolo.eclipse.bitbucket;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class test {

	public static void main(String[] args) {
		BufferedImage input_image = null; 
		 try {
			input_image = ImageIO.read(new File("C:/DEV/Eclipse/BitBucketPlugin/icons/comment.svg"));
			File outputfile = new File("C:\\Temp\\imageio_png_output.png"); //create new outputfile object
			ImageIO.write(input_image, "PNG", outputfile); //write PNG output to file 	}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
