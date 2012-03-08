package edu.drexel.cs680.proj3.modules;

import java.util.ArrayList;
import java.util.List;

public class Room {
	public int center_x, center_y, width, height;
	public int moveCounter = 0;
	public String name;
	public List<Room> neighbors;
	public List<Door> doors;
	public List<Room> processedNeighbors;
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
	}
	
	public boolean collides(Room other) {
		return collision(this, other) || collision(other, this);
	}
	
	private boolean collision(Room a, Room b) {
		boolean collision_x = false, collision_y = false;
		
		collision_x = (b.getLeftEdge() >= a.getLeftEdge() && b.getLeftEdge() < a.getRightEdge());
		collision_x = collision_x || (b.getRightEdge() > a.getLeftEdge() && b.getRightEdge() <= a.getRightEdge());
		
		collision_y = (b.getTopEdge() >= a.getTopEdge() && b.getTopEdge() < a.getBottomEdge());
		collision_y = collision_y || (b.getBottomEdge() > a.getTopEdge() && b.getBottomEdge() <= a.getBottomEdge());
		boolean collision = collision_x && collision_y;
		return collision;
	}
	
	private static final int DELTA = 1;
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
}