package edu.drexel.cs680.proj3.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	
	private static final String FOLDER = "C:\\Documents and Settings\\Owner\\My Documents\\proj2\\GraphicIFEngine_CS680\\graphics";

	public static void main(String[] args) throws IOException, Exception {
		  File livingroomFile = new File(String.format("%s\\mansion\\livingroom.png", FOLDER));
		  File bedroomFile = new File(String.format("%s\\mansion\\livingroom.png", FOLDER));
		  
		  BufferedImage livingroom = ImageIO.read(livingroomFile);
		  BufferedImage bedroom = ImageIO.read(bedroomFile);

		  int widthLivingroom = livingroom.getWidth();
		  int heightLivingroom = livingroom.getHeight();

		  int widthBedroom = bedroom.getWidth();
		  int heightBedroom = bedroom.getHeight();

		  BufferedImage img = new BufferedImage (
				  widthLivingroom + widthBedroom,   // Final image will have width and height as
				  heightLivingroom + heightBedroom,  // addition of widths and heights of the images we already have
		    BufferedImage.TYPE_INT_RGB);

		  boolean image1Drawn = img.createGraphics().drawImage(livingroom, 0, 0, widthLivingroom/2, heightLivingroom/2, null); // 0, 0 are the x and y positions
		  
		  if(!image1Drawn) System.out.println("Problems drawing first image"); //where we are placing image1 in final image

		  boolean image2Drawn = img.createGraphics().drawImage(bedroom, widthLivingroom, 0, null); // here width is mentioned as width of
		  
		  if(!image2Drawn) System.out.println("Problems drawing second image"); // image1 so both images will come in same level
		  
//		  boolean image3Drawn = img.createGraphics().drawImage(img3, 0, heightImg1, null);
		  
//		  if(!image3Drawn) System.out.println("Problems drawing third image");
		  
//		  boolean image4Drawn = img.createGraphics().drawImage(img4, widthImg1, heightImg1, null);
		  
//		  if(!image4Drawn) System.out.println("Problems drawing fourth image");
		  
		  // horizontally
		  File final_image = new File(String.format("%s\\mansion\\output\\output.jpg", FOLDER));
		  
		  boolean final_Image_drawing = ImageIO.write(img, "jpeg", final_image);
		  
		  if(!final_Image_drawing) System.out.println("Problems drawing final image");
		  
		  System.out.println("Successfull");
		 }
}
