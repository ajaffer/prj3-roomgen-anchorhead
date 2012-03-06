package edu.drexel.cs680.proj3.modules;

import java.util.List;

public class Room {
	public int center_x, center_y, width, height;
	public String name;
	List<Room> neighbors;
	List<Door> doors;
	
	public Room(int center_x, int center_y, int width, int height, String name) {
		this.center_x = center_x;
		this.center_y = center_y;
		this.width = width;
		this.height = height;
		this.name = name;
	}
	
	public boolean collides(Room other) {
		boolean collision_x = false, collision_y = false;
		
		collision_x = (other.getLeftEdge() > getLeftEdge() && other.getLeftEdge() < getRightEdge());
		collision_x = collision_x || (other.getRightEdge() > getLeftEdge() && other.getRightEdge() < getRightEdge());
		
		collision_y = (other.getTopEdge() > getTopEdge() && other.getTopEdge() < getBottomEdge());
		collision_y = collision_y || (other.getBottomEdge() > getTopEdge() && other.getBottomEdge() < getBottomEdge());
		
		return collision_x && collision_y;
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

	public class Door {
		int x, y;
	}
}