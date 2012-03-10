package edu.drexel.cs680.proj3.modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.drexel.cs680.proj3.gen.RoomUtils;
import edu.drexel.cs680.proj3.util.ImageUtil;

public class Mansion {

	public static final int MAX_ROOMS = 10;
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	
	public List<Room> rooms;
	public int width = WIDTH, height=HEIGHT;
	
	public Mansion() {
		rooms = RoomUtils.getRandomRooms(MAX_ROOMS, WIDTH, HEIGHT);
		rooms = RoomUtils.moveRoomsApart(rooms);
//		rooms = RoomUtils.filterCollidingRooms(rooms);
//		RoomUtils.moveRoomsClose(rooms);
		RoomUtils.setNeighbors(rooms);
		RoomUtils.removeUngroupedRooms(rooms);
//		RoomUtils.setDoors(rooms);
		RoomUtils.setPoints(rooms);
		
		
//		setMansionWidthHeight();
	}
	
	public String toXML() {
		StringBuilder str = new StringBuilder();
		
		for (Room room : rooms) {
			str.append(room.toXML());
		}
		
		return str.toString();
	}
	
	public void toFile() {
		String XML = toXML();
		String FOLDER = new File(".").getAbsolutePath();
		try{
			// Create file 
			FileWriter fstream = new FileWriter(String.format("%s\\graphics\\mansion\\out.xml", FOLDER));
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(XML);
			out.close();
			}
		catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());
		}
	  }
	
//	private void setMansionWidthHeight() {
//		int[] minMaxXY = RoomUtils.getMinMaxXY(rooms);
//		int minx = minMaxXY[0];
//		int miny = minMaxXY[1];
//		int maxx = minMaxXY[2];
//		int maxy = minMaxXY[3];
//		
//		width = maxx - minx;
//		height = maxy - miny;
//	}
	
	
	public static void main(String[]args) throws IOException{
		Mansion mansion = new Mansion();
		ImageUtil.drawMansion(mansion);
//		System.out.println(mansion.toXML());
		mansion.toFile();
	}

}
