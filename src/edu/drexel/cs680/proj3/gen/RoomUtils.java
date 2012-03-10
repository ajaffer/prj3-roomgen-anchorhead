package edu.drexel.cs680.proj3.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.drexel.cs680.proj3.modules.Door;
import edu.drexel.cs680.proj3.modules.Mansion;
import edu.drexel.cs680.proj3.modules.Room;

public class RoomUtils {

	/*
	 * 1. 4 set of images for 4 rooms: living, bed, hall, basement 2. create
	 * random rectangles (how many?) 3. space them apart so none of them
	 * overlaps, and each of them shares a boundary choose x/y direction to
	 * move: x: if distance_x b/w centers is > distance_y or vice versa move B:
	 * A_bottom-B_top OR A_right -B_left 4. give random names 5. give doors to
	 * reach each room, plus doors to reach living room from any room create
	 * nodes in a tree for each adjacent room for each living room, give doors
	 * to all neighbors for each room that does not have a door, give at least
	 * on door to an adjacent room
	 */

	public static List<Room> getRandomRooms(int quantity, int maxx, int maxy) {
		List<Room> randomRooms = new ArrayList<Room>(quantity);
		int i = 0;
		while (i++ < quantity) {
			Room room = createRandomRoom(maxx, maxy);
			randomRooms.add(room);
		}
		
		return randomRooms;
	}
	
	public static void setNeighbors(List<Room> rooms) {
		for (Room room : rooms) {
			Map<Room, Door> neighbors = getNeighbors(rooms, room);
			room.neighbors = neighbors;
		}
	}
	
	public static void removeUngroupedRooms(List<Room> rooms) {
		for (Room room : rooms) {
			if (room.neighbors.isEmpty()) {
				rooms.remove(room);
			}
		}
	}
	
	private static Map<Room, Door> getNeighbors(List<Room> rooms, Room targetRoom) {
		Map<Room, Door> neighbors = new HashMap<Room, Door>();
		for (Room room : rooms) {
			if (room == targetRoom) {
				continue;
			}
			
			if (room.isNeighbor(targetRoom)){
				Door door = getDoor(room, targetRoom);
				neighbors.put(room, door);
				
				room.neighbors.put(targetRoom, door);
			}
		}
		
		return neighbors;
	}
	
//	public static void setDoors(List<Room> rooms) {
//		for (Room room : rooms) {
//			setDoors(room);
//		}
//	}
	
//	private static void setDoors(Room room) {
//		List<Door> doors = new ArrayList<Door>();
//		for (Room neighbor : room.neighbors) {
//			if (!(neighbor.processedNeighbors.contains(room))){
//				Door door = getDoor(room, neighbor);
//				doors.add(door);
//				room.processedNeighbors.add(neighbor);
//			}
//		}
//		room.doors = doors;
//	}
	
	
	public static void setPoints(List<Room> rooms) {
		for (Room room : rooms) {
			setPoints(room);
		}
	}
	
	private static void setPoints(Room room) {
		Room.Point[][] pointsMatrix = getPointsMatrix(room);
		for (int x = 0; x < pointsMatrix.length; x++) {
			for (int y = 0; y < pointsMatrix[x].length; y++) {
				Room.Point point = pointsMatrix[x][y];
				if (pointsMatrix.length == 0 || pointsMatrix[0].length == 0) {
					continue;
				}
				if (y-1 > 0) {
					Room.Point top = pointsMatrix[x][y-1];
					point.links.add(top);
				}
				if (x-1 > 0) {
					Room.Point left = pointsMatrix[x-1][y];
					point.links.add(left);
				}
				if (y+1 < pointsMatrix[0].length) {
					Room.Point bottom = pointsMatrix[x][y+1];
					point.links.add(bottom);
				}
				if (x+1 < pointsMatrix.length) {
					Room.Point right = pointsMatrix[x+1][y];
					point.links.add(right);
				}
				
				room.points.add(point);
			}
		}
	}
	
	private static Room.Point[][] getPointsMatrix(Room room) {
		int id = 1;
		Room.Point[][] points = new Room.Point[room.getRightEdge()-room.getLeftEdge()+1][room.getBottomEdge()-room.getTopEdge()+1];
		for (int x = room.getLeftEdge(); x <= room.getRightEdge() ; x++) {
			for (int y = room.getTopEdge(); y <= room.getBottomEdge(); y++) {
				points[x-room.getLeftEdge()][y-room.getTopEdge()] = room.new Point(id++, x, y);
			}
		}
		return points;
	}
	
	
	private static Door getDoor(Room a, Room b) {
		int x=0, y=0;

		if (a.isTopNeighbor(b)) {
			y = a.getBottomEdge();
		}
		
		if (a.isBottomNeighbor(b)) {
			y = a.getTopEdge();
		}
		
		if (a.isTopNeighbor(b) || a.isBottomNeighbor(b)) {
			int leftEdge = Math.max(a.getLeftEdge(), b.getLeftEdge());
			int rightEdge = Math.min(a.getRightEdge(), b.getRightEdge());
			
			x = leftEdge + (rightEdge - leftEdge) / 2;
		}
		
		if (a.isLeftNeighbor(b)) {
			x = a.getRightEdge();
		}
		
		if (a.isRightNeighbor(b)) {
			x = a.getLeftEdge();
		}

		if (a.isLeftNeighbor(b) || a.isRightNeighbor(b)) {
			int topEdge = Math.max(a.getTopEdge(), b.getTopEdge());
			int bottomEdge = Math.min(a.getBottomEdge(), b.getBottomEdge());
			
			y = topEdge + (bottomEdge - topEdge) / 2;
		}
		
		Door door = new Door(x, y);
		return door;
	}
	
//	public static void moveRoomsClose(List<Room> rooms) {
//		for (int i=0; i< rooms.size(); i++) {
//			Room currentRoom = rooms.get(i);
//			List<Room> collidingRooms = getCollidingRooms(rooms, currentRoom);
//			moveCollidingRooms(collidingRooms, currentRoom);
//		}
//	}
	
	public static List<Room> moveRoomsApart(List<Room> rooms) {
		List<Room> arrangedRooms = new ArrayList<Room>(rooms.size());

		for (Room room : rooms) {
			addAndSortOutCollissions(arrangedRooms, room);
		}
		
		/*
//		Room centerRoom = getCenterRoom(rooms);
//		moveCollidingRoomApart(centerRoom, rooms);	
		for (Room room : rooms){
			for (int i=0; i< rooms.size(); i++) {
				Room currentRoom = rooms.get(i);
				List<Room> collidingRooms = getCollidingRooms(rooms, currentRoom);
				moveCollidingRooms(collidingRooms, currentRoom);
			}
		}
		*/
		
		return arrangedRooms;
	}
	
	public static List<Room> filterCollidingRooms(List<Room> rooms) {
		List<Room> filteredRooms = new ArrayList<Room>(rooms.size());
		
		for (Room room : rooms) {
			if (noOfCollisions(rooms, room) == 0) {
				filteredRooms.add(room);
			}
		}
		
		return filteredRooms;
	}
	
	public static int noOfCollisions(List<Room> rooms, Room currentRoom) {
		int collisions = 0;
		for (Room room : rooms) {
			if (!room.equals(currentRoom) && room.collides(currentRoom)) {
				collisions++;
			}
		}
		
		return collisions;
	}
	
	private static void addAndSortOutCollissions(List<Room> arrangedRooms, Room newRoom) {
		int counter = 0;
		while (roomCollides(arrangedRooms, newRoom) && ++counter < 500) {
			for (Room targetRoom : arrangedRooms) {
				if (targetRoom.collides(newRoom)) {
					moveCollidingRoom(newRoom, targetRoom);
					if (targetRoom.collides(newRoom)) { 
//						System.err.println("uncollide didn't work! for " + newRoom);
					}
				}
			}
		}
		
//		arrangedRooms.add(newRoom);
		if (roomCollides(arrangedRooms, newRoom)) {
			System.err.println("oops couldn't find an arrangment for room: " + newRoom.name);
		} else {
			System.out.println("counter: "+counter);
			arrangedRooms.add(newRoom);
		}
		
	}
	
	private static boolean roomCollides(List<Room> arrangedRooms, Room newRoom) {
		for (Room arrangedRoom : arrangedRooms) {
			if (arrangedRoom.collides(newRoom)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	private void moveCollidingRoomApart(Room targetRoom, List<Room> rooms) {
		Room collidingRoom = getACollidingRoom(rooms, targetRoom);
		if (collidingRoom == null) {
			return;
		}
		moveCollidingRoom(collidingRoom, targetRoom);
		moveCollidingRoomApart(collidingRoom, rooms);
	}*/
	
	private static void moveCollidingRooms(List<Room> collidingRooms, Room targetRoom) {
		for (Room collidingRoom : collidingRooms) {
			moveCollidingRoom(collidingRoom, targetRoom);
		}
	}
	
	private static void moveCollidingRoom(Room collidingRoom, Room targetRoom) {
		int move_up = Math.abs(collidingRoom.getBottomEdge() - targetRoom.getTopEdge());
		int move_down = Math.abs(targetRoom.getBottomEdge() - collidingRoom.getTopEdge());
		int move_y = move_up < move_down ? -(move_up) : move_down; 

		int move_right = Math.abs(targetRoom.getRightEdge() - collidingRoom.getLeftEdge());
		int move_left = Math.abs(collidingRoom.getRightEdge() - targetRoom.getLeftEdge());
		int move_x = move_right > move_left ? -(move_left) : move_right;
		
		if (new Random().nextBoolean()) {
			collidingRoom.center_x = collidingRoom.center_x + move_x;
		}else {
			collidingRoom.center_y = collidingRoom.center_y + move_y;
		}
		
		collidingRoom.moveCounter++;
	}
	
	private static List<Room> getCollidingRooms(List<Room> rooms, Room targetRoom) {
		List<Room> collidingRooms = new ArrayList<Room>();
		for (Room room : rooms) {
			if (room.equals(targetRoom)) {
				continue;
			}
			if (room.collides(targetRoom)) {
				collidingRooms.add(room);
			}
		}
		
		return collidingRooms;
	}
//	
//	private static Room getCenterRoom(List<Room> rooms) {
//		Room centerRoom = rooms.get(0);
//		Room[] topLeftBottomRightRooms = getTopLeftBottomRightRooms(rooms);
//		Room topLeft = topLeftBottomRightRooms[0], bottomRight = topLeftBottomRightRooms[1];
//		
//		
//		for (Room room : rooms) {
//			if (distance(room, topLeft) > distance(centerRoom, topLeft) && 
//					distance(room, bottomRight) > distance(centerRoom, bottomRight)) {
//				centerRoom = room;
//			}
//		}
//		
//		return centerRoom;
//	}
	
//	private static double distance(Room roomA, Room roomB) {
//		double d = Math.sqrt(Math.pow(roomA.center_x-roomB.center_x, 2) + Math.pow(roomA.center_y-roomB.center_y, 2) );
//		return d;
//	}
//	
	public static int[] getMinMaxXY(List<Room> rooms) {
		Room r = rooms.get(0);
		int minx=r.getLeftEdge(),miny=r.getTopEdge(),maxx=r.getRightEdge(),maxy=r.getBottomEdge();
		
		for (Room room : rooms) {
			if (room.getLeftEdge() < minx) {
				minx = room.getLeftEdge();
			}
			if (room.getTopEdge() < miny) {
				miny = room.getTopEdge();
			}
			if (room.getRightEdge() > maxx) {
				maxx = room.getRightEdge();
			}
			if (room.getBottomEdge() > maxy) {
				maxy = room.getBottomEdge();
			}
		}
		int[] minMaxXY = new int[] {minx, miny, maxx, maxy};
		
		return minMaxXY;
	}
	
//	public static Room[] getTopLeftBottomRightRooms(List<Room> rooms) {
//		Room topLeft=rooms.get(0), bottomRight=rooms.get(0);
//		
//		for (Room room : rooms) {
//			if (room.center_x < topLeft.center_x && room.center_y < topLeft.center_y) {
//				topLeft = room;
//			}
//			if (room.center_x > bottomRight.center_x && room.center_y > bottomRight.center_y) {
//				bottomRight = room;
//			}
//		}
//		Room[] topLeftBottomRight = new Room[] {topLeft, bottomRight};
//		
//		return topLeftBottomRight;
//	}
	
	private static enum RoomNames {LIVINGROOM, BEDROOM, BASEMENT, HALL};
	
	private static Room createRandomRoom(int maxx, int maxy) {
		Random random = new Random();
		
		int random_width = maxx/50 + random.nextInt(maxx/10);
		int random_height = maxy/25 + random.nextInt(maxy/5);
		int random_center_x = maxx/2 + random.nextInt(maxx/50);
		int random_center_y = maxy/2 + random.nextInt(maxy/50);
		String random_name = RoomNames.values()[random.nextInt(RoomNames.values().length)].toString();
		
		Room room = new Room(random_center_x, random_center_y, random_width, random_height, random_name);
		return room;
	}
	
	public static void main(String[] args) {
		List<Room> rooms = RoomUtils.getRandomRooms(5, 500, 500);
		RoomUtils.moveRoomsApart(rooms);
	}

}
