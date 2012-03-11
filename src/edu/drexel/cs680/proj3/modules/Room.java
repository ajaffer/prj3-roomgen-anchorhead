package edu.drexel.cs680.proj3.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.drexel.cs680.proj3.gen.ItemUtils;

public class Room {
	public int center_x, center_y, width, height;
	public int moveCounter = 0;
	public String name;
	public String description;
//	public List<Room> neighbors = new ArrayList<Room>();
	public Map<Room, Door> neighbors = new HashMap<Room, Door>();
//	public List<Door> doors = new ArrayList<Door>();
	public List<Room> processedNeighbors;
	public List<Point> points;
	private static int ctr;
	public int id;
	public boolean entryRoom;
	public boolean exitRoom;
	public BufferedImage image;
	
	public static enum RoomNames {LIVINGROOM, BEDROOM, BASEMENT, HALL};
	
	public static final String ENTRY_ROOM = "street";
	public static final String EXIT_ROOM = "backyard";
	
	public Room(int center_x, int center_y, int width, int height, String name) {
		this.center_x = center_x;
		this.center_y = center_y;
		this.width = width;
		this.height = height;
		this.name = name.toLowerCase();
		this.image = createImage();
		id = ctr++;
		processedNeighbors = new ArrayList<Room>();
		points = new ArrayList<Point>();
	}
	
	public boolean collides(Room other) {
		return collision(this, other) || collision(other, this);
	}
	
	private BufferedImage createImage(){
		String FOLDER = new File(".").getAbsolutePath();
		File file = new File(String.format("%s\\graphics\\mansion\\%s.png", FOLDER, name));
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println(file);
			e.printStackTrace();
		}
		return image;
	}
	
	private boolean collision(Room a, Room b) {
		boolean collision_x = false, collision_y = false;
		collision_x = (b.getLeftEdge() >= a.getLeftEdge() && b.getLeftEdge() < a.getRightEdge());
		collision_x = collision_x || (b.getRightEdge() > a.getLeftEdge() && b.getRightEdge() <= a.getRightEdge());
		collision_x = collision_x || (b.getLeftEdge() <= a.getLeftEdge() && b.getRightEdge() >= a.getRightEdge());
		
		collision_y = (b.getTopEdge() >= a.getTopEdge() && b.getTopEdge() < a.getBottomEdge());
		collision_y = collision_y || (b.getBottomEdge() > a.getTopEdge() && b.getBottomEdge() <= a.getBottomEdge());
		collision_y = collision_y || (b.getTopEdge() >= a.getTopEdge() && b.getBottomEdge() >= a.getBottomEdge());
		boolean collision = collision_x && collision_y;
		return collision;
	}
	
	private static final int DELTA = 0;
	public boolean isNeighbor(Room other) {
		boolean neighbor = false;
		neighbor = isLeftNeighbor(other);
		neighbor = neighbor || isRightNeighbor(other);
		neighbor = neighbor || isTopNeighbor(other);
		neighbor = neighbor || isBottomNeighbor(other);
		
		return neighbor;
	}
	
	public boolean isLeftNeighbor(Room other) {
		return Math.abs(other.getLeftEdge() - getRightEdge()) <= DELTA;
	}
	
	public boolean isRightNeighbor(Room other) {
		return Math.abs(other.getRightEdge() - getLeftEdge()) <= DELTA;
	}
	
	public boolean isBottomNeighbor(Room other) {
		return Math.abs(other.getBottomEdge() - getTopEdge()) <= DELTA;
	}
	
	public boolean isTopNeighbor(Room other) {
		return Math.abs(other.getTopEdge() - getBottomEdge()) <= DELTA;
	}

	public int getLeftEdge() {
		int leftEdge = center_x - (width / 2);
		return leftEdge;
	}

	public int getRightEdge() {
		int rightEdge = center_x + (width / 2);
		return rightEdge;
	}

	public int getTopEdge() {
		int leftEdge = center_y - (height / 2);
		return leftEdge;
	}

	public int getBottomEdge() {
		int rightEdge = center_y + (height / 2);
		return rightEdge;
	}

	
	public boolean equals(Object o) {
		if (!(o instanceof Room)){
			return false;
		}
		
		Room other = (Room)o;
		boolean isEqual = false;

		isEqual = other.name.equals(this.name);
		isEqual = isEqual && other.center_x == this.center_x;
		isEqual = isEqual && other.center_y == this.center_y;
		
		return isEqual;
	}
	
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + this.name.hashCode();
		hash = 89 * hash + this.center_x;
		hash = 89 * hash + this.center_y;
		return hash;
	}
	
	public String toString() {
		return String.format("%s [%d]", name, neighbors.size());
	}
	
	private String navigation() {
		/*
		 * 		<navigation>
			<point id="A" x="320" y="480"/>
			<point id="B" x="320" y="416"/>
			<point id="C" x="320" y="224"/>
			<link p1="A" p2="B"/>
			<link p1="B" p2="C"/>
		</navigation>		

		 */
		StringBuilder str = new StringBuilder();
		str.append(String.format("<navigation>"));
		for (Point point : points) {
			str.append(String.format("<point id=\"%s\" x=\"%s\" y=\"%s\"/>", point.id, point.x, point.y));
			for (Point link : point.links) {
				str.append(String.format("<link p1=\"%s\" p2=\"%s\" />", point.id, link.id));
			}
		}
		str.append(String.format("</navigation>"));
		return str.toString();
	}
	
	private String locations() {
		/* <object type="location" id="street" x="320" y="480">
				<description>The main street of Anchorhead.</description>
				<destination room="street" x="96" y="96"/>
			</object> 
	    */
		StringBuilder str = new StringBuilder();
		for (Entry<Room, Door> e : neighbors.entrySet()) {
			str.append(String.format("<object type=\"location\" id=\"%s\" x=\"%d\" y=\"%d\">", e.getKey().name, e.getValue().x, e.getValue().y));
			str.append(String.format("<destination room=\"%s\" x=\"%d\" y=\"%d\"/>", e.getKey().name, e.getValue().x, e.getValue().y));
			str.append("</object>");
		}
		
		return str.toString();
	}
	
	private String character() {
		/*
		 <object type="character" id="player" x="320" y="416">
				<description>I'm an average person</description>
				<inventory>
				</inventory>
				<action>
					<type>idle</type>
				</action>
				<animations>
					<animation action="idle">
						<frame time="125">player</frame>
						<frame time="15">player-happy</frame>
					</animation>
					<animation action="talk">
						<frame time="5">player-talk</frame>
						<frame time="5">player</frame>
					</animation>
				</animations>
				<ai>player.xml</ai>
			</object>
		 */
		StringBuilder str = new StringBuilder();
		Door entryDoor = getDoor(ENTRY_ROOM);
		str.append(String.format("<object type=\"character\" id=\"player\" x=\"%d\" y=\"%d\">", entryDoor.x, entryDoor.y));
		str.append("<description>I'm an average person</description>");
		str.append("<inventory>");
		str.append("</inventory>");
		str.append("<action>");
		str.append("<type>idle</type>");
		str.append("</action>");
		str.append("<animations>");
		str.append("<animation action=\"idle\">");
		str.append("<frame time=\"125\">player</frame>");
		str.append("<frame time=\"15\">player-happy</frame>");
		str.append("</animation>");
		str.append("<animation action=\"talk\">");
		str.append("<frame time=\"5\">player-talk</frame>");
		str.append("<frame time=\"5\">player</frame>");
		str.append("</animation>");
		str.append("</animations>");
		str.append("<ai>player.xml</ai>");
		str.append("</object>");
		
		
		return str.toString();
	}
	
	private Door getDoor(String door_type) {
		for (Map.Entry<Room, Door> e : neighbors.entrySet()) {
			if (e.getKey().name.equals(door_type)) {
				return e.getValue();
			}
		}
		return null;
	}
	
	private String background() {
		/*
		 * <background>
			<object type="tile" id="livingroom" x="0" y="0">  	
				<animation action="idle">
					<frame time="100">livingroom</frame>
				</animation>			
			</object>
		</background>
		 */
		StringBuilder str = new StringBuilder();
		str.append(String.format("<background>"));
		str.append(String.format("<object type=\"tile\" id=\"%s\" x=\"0\" y=\"0\">", name));
		str.append(String.format("<animation action=\"idle\">"));
		str.append(String.format("<frame time=\"100\">%s</frame>", name));
		str.append(String.format("</animation>"));
		str.append(String.format("</object>"));
		str.append(String.format("</background>"));
		
		return str.toString();
	}
	
	public Room getLeftNeighbor() {
		for (Room r : neighbors.keySet()) {
			if (r.isLeftNeighbor(this)){
				return r;
			}
		}
		return null;
	}

	public Room getRightNeighbor() {
		for (Room r : neighbors.keySet()) {
			if (r.isRightNeighbor(this)){
				return r;
			}
		}
		return null;
	}
	
	public Room getTopNeighbor() {
		for (Room r : neighbors.keySet()) {
			if (r.isTopNeighbor(this)){
				return r;
			}
		}
		return null;
	}
	
	public Room getBottomNeighbor() {
		for (Room r : neighbors.keySet()) {
			if (r.isBottomNeighbor(this)){
				return r;
			}
		}
		return null;
	}
	
	public int getRandomX() {
		Random r = new Random();
		int x = r.nextInt(width);
		x += getLeftEdge();
		return x;
	}
	
	public int getRandomY() {
		Random r = new Random();
		int y = r.nextInt(height);
		y += getTopEdge();
		return y;
	}
	
	public String toXML() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("<room id = \"%s\">", name));
		str.append(String.format("<description>%s</description>", description));
		str.append(String.format("%s", navigation()));
		str.append(String.format("%s", background()));
		str.append("<objects>");
		if (entryRoom) {
			str.append(String.format("%s", character()));
		}
		str.append(String.format("%s", locations()));
		str.append(ItemUtils.getRandomItems(this));
		str.append("</objects>");
		str.append("</room>");
		

		/*
		
		<objects>
			<object type="character" id="player" x="320" y="416">
				<description>I'm an average person</description>
				<inventory>
				</inventory>
				<action>
					<type>idle</type>
				</action>
				<animations>
					<animation action="idle">
						<frame time="125">player</frame>
						<frame time="15">player-happy</frame>
					</animation>
					<animation action="talk">
						<frame time="5">player-talk</frame>
						<frame time="5">player</frame>
					</animation>
				</animations>
				<ai>player.xml</ai>
			</object>
		
		</objects>
	</room>
		 */
		
		
		
		return str.toString();
	}
	
	
	public class Point {
		public int x,y,id;
		public List<Point> links;
		public Point(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
			links = new ArrayList<Point>();
		}
	}
	
}