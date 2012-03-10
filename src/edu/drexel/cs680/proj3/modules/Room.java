package edu.drexel.cs680.proj3.modules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Room {
	public int center_x, center_y, width, height;
	public int moveCounter = 0;
	public String name;
//	public List<Room> neighbors = new ArrayList<Room>();
	public Map<Room, Door> neighbors = new HashMap<Room, Door>();
	public List<Door> doors = new ArrayList<Door>();
	public List<Room> processedNeighbors;
	public List<Point> points;
	private static int ctr;
	public int id;
	
	public Room(int center_x, int center_y, int width, int height, String name) {
		this.center_x = center_x;
		this.center_y = center_y;
		this.width = width;
		this.height = height;
		this.name = name;
		id = ctr++;
		processedNeighbors = new ArrayList<Room>();
		points = new ArrayList<Point>();
	}
	
	public boolean collides(Room other) {
		return collision(this, other) || collision(other, this);
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
		return String.format("%s [%d/%d]", name, neighbors.size(), doors.size());
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
			str.append(String.format("<object type=\"%s\" id=\"%s\" x=\"%d\" y=\"%d\">", name, e.getKey().name, e.getValue().x, e.getValue().y));
			str.append(String.format("<destination room=\"%s\" x=\"%d\" y=\"%d\"/>", e.getKey().name, e.getValue().x, e.getValue().y));
		}
		
		return str.toString();
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
	
	public String toXML() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("<room id = \"%s\">", name));
		str.append(String.format("<description>%s</description>", ""));
		str.append(String.format("%s", navigation()));
		str.append(String.format("%s", background()));
		str.append(String.format("%s", locations()));
		

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