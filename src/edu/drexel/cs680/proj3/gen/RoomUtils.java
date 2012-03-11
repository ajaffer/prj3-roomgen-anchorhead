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
		int i = -1;
		while (++i < quantity) {
			Room room = createRandomRoom(maxx, maxy, Room.RoomNames.values()[i].toString().toLowerCase());
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
	
	public static void markEntryExitRooms(List<Room> rooms) {
		Room entryRoom = getRoom(rooms, Room.RoomNames.LIVINGROOM.toString().toLowerCase());
		if (entryRoom != null) {
			entryRoom.entryRoom = true;
		}
		Room exitRoom = getRoom(rooms, Room.RoomNames.HALL.toString().toLowerCase());
		if (exitRoom != null) {
			exitRoom.exitRoom = true;
		}
	}
	
	private static Room getRandomRoomPreculdingGivenRoom(List<Room> rooms, Room givenRoom) {
		Room randomRoom = givenRoom;
		Random rand = new Random();
		while (randomRoom == givenRoom) {
			randomRoom = rooms.toArray(new Room[0])[rand.nextInt(rooms.size())];
		}
		
		return randomRoom;
	}
	
	private static Room getRoom(List<Room> rooms, String roomType) {
		for (Room room : rooms) {
			if (room.name.equalsIgnoreCase(roomType)) {
				return room; 
			}
		}
		return null;
	}
	
	public static void createEntryExitDoors(List<Room> rooms) {
		for (Room room : rooms) {
			if (room.entryRoom) {
				Door entryDoor = getRandDoor(room);
				Room entryRoom =  getEntryRoom();
				room.neighbors.put(entryRoom, entryDoor);
			}
			else if (room.entryRoom) {
				Door exitDoor = getRandDoor(room);
				Room exitRoom =  getExitRoom();
				room.neighbors.put(exitRoom, exitDoor);
			}
		}
	}
	
	private static Room getEntryRoom() {
		Room room = new Room(0, 0, 0, 0, Room.ENTRY_ROOM);
		room.description = "The main street of Anchorhead.";
		return room;
	}
	
	private static Room getExitRoom() {
		Room room = new Room(0, 0, 0, 0, Room.EXIT_ROOM);
		room.description = "The nice backyard of Verlag Mansion.";
		return room;
	}
	
	private static Door getRandDoor(Room room) {
		Door randDoor = getDoorWhenEmptyWall(room);
		if (randDoor == null) {
			randDoor = findDoorBWNeighbors(room);
		}
		
		if (randDoor == null) {
			System.out.println("Could not find a door! for room: " + room);
		}
		return randDoor;
	}
	
	private static Door findDoorBWNeighbors(Room room) {
		Door door = null;
		
		Room neighbor = room.getLeftNeighbor();
		if (neighbor.getBottomEdge() < room.getBottomEdge()) {
			int y = neighbor.getBottomEdge() + (room.getBottomEdge() - neighbor.getBottomEdge()) / 2;
			int x = room.getLeftEdge();
			door = new Door(x, y);
		} else if (neighbor.getTopEdge() > room.getTopEdge()) {
			int y = room.getTopEdge() + (neighbor.getTopEdge() - room.getTopEdge()) / 2;
			int x = room.getLeftEdge();
			door = new Door(x, y);
		}
		
		if (door == null) {
			neighbor = room.getRightNeighbor();
			if (neighbor.getBottomEdge() < room.getBottomEdge()) {
				int y = neighbor.getBottomEdge() + (room.getBottomEdge() - neighbor.getBottomEdge()) / 2;
				int x = room.getRightEdge();
				door = new Door(x, y);
			} else if (neighbor.getTopEdge() > room.getTopEdge()) {
				int y = room.getTopEdge() + (neighbor.getTopEdge() - room.getTopEdge()) / 2;
				int x = room.getRightEdge();
				door = new Door(x, y);
			}
		}
		
		if (door == null) {
			neighbor = room.getTopNeighbor();
			if (neighbor.getLeftEdge() > room.getLeftEdge()) {
				int y = room.getTopEdge();
				int x = room.getLeftEdge() + (neighbor.getLeftEdge() - room.getLeftEdge()) / 2;
				door = new Door(x, y);
			} else if (neighbor.getRightEdge() < room.getRightEdge()) {
				int y = room.getTopEdge();
				int x = neighbor.getRightEdge() + (room.getRightEdge() - neighbor.getRightEdge()) / 2;
				door = new Door(x, y);
			}
		}

		if (door == null) {
			neighbor = room.getBottomNeighbor();
			if (neighbor.getLeftEdge() > room.getLeftEdge()) {
				int y = room.getBottomEdge();
				int x = room.getLeftEdge() + (neighbor.getLeftEdge() - room.getLeftEdge()) / 2;
				door = new Door(x, y);
			} else if (neighbor.getRightEdge() < room.getRightEdge()) {
				int y = room.getBottomEdge();
				int x = neighbor.getRightEdge() + (room.getRightEdge() - neighbor.getRightEdge()) / 2;
				door = new Door(x, y);
			}
		}
		

		return door;
	}
	
	private static Door getDoorWhenEmptyWall(Room room){
		Door randDoor = null;
		if (room.getRightNeighbor() == null ){
			int x = room.getRightEdge();
			int y = room.getTopEdge() + (room.getBottomEdge() - room.getTopEdge())/2;
			randDoor = new Door(x , y);
			return randDoor;
		} else if (room.getLeftNeighbor() == null ){
			int x = room.getLeftEdge();
			int y = room.getTopEdge() + (room.getBottomEdge() - room.getTopEdge())/2;
			randDoor = new Door(x , y);
			return randDoor;
		} else if (room.getTopNeighbor() == null ){
			int x = room.getLeftEdge() + (room.getRightEdge() - room.getLeftEdge())/2;
			int y = room.getTopEdge();
			randDoor = new Door(x , y);
			return randDoor;
		} else if (room.getBottomNeighbor() == null ){
			int x = room.getLeftEdge() + (room.getRightEdge() - room.getLeftEdge())/2;
			int y = room.getBottomEdge();
			randDoor = new Door(x , y);
			return randDoor;
		}
		
		return randDoor;
	}
	
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
				if (pointsMatrix.length == 0 || pointsMatrix[0].length == 0 || point == null) {
					continue;
				}
				if (y-1 > 0) {
					Room.Point top = pointsMatrix[x][y-1];
					if (top!=null) {
						point.links.add(top);
					}
				}
				if (x-1 > 0) {
					Room.Point left = pointsMatrix[x-1][y];
					if (left!=null) {
						point.links.add(left);
					}
				}
				if (x-1 > 0 && y-1 > 0) {
					Room.Point topLeft = pointsMatrix[x-1][y-1];
					if (topLeft!=null) {
						point.links.add(topLeft);
					}
				}
				if (x+1 < pointsMatrix.length && y-1 > 0) {
					Room.Point topRight = pointsMatrix[x+1][y-1];
					if (topRight!=null) {
						point.links.add(topRight);
					}
				}
				if (y+1 < pointsMatrix[0].length) {
					Room.Point bottom = pointsMatrix[x][y+1];
					if (bottom!=null) {
						point.links.add(bottom);
					}
				}
				if (x+1 < pointsMatrix.length) {
					Room.Point right = pointsMatrix[x+1][y];
					if (right!=null) {
						point.links.add(right);
					}
				}
				if (y+1 < pointsMatrix[0].length && x+1 < pointsMatrix.length) {
					Room.Point bottomRight = pointsMatrix[x+1][y+1];
					if (bottomRight!=null) {
						point.links.add(bottomRight);
					}
				}
				if (y+1 < pointsMatrix[0].length && x-1 > 0) {
					Room.Point bottomLeft = pointsMatrix[x+1][y+1];
					if (bottomLeft!=null) {
						point.links.add(bottomLeft);
					}
				}
				
				room.points.add(point);
			}
		}
	}
	
	private static Room.Point[][] getPointsMatrix(Room room) {
		int id = 1;
		Room.Point[][] points = new Room.Point[room.getRightEdge()-room.getLeftEdge()+1][room.getBottomEdge()-room.getTopEdge()+1];
		
		for (Door doorA : room.neighbors.values()) {
			for (Door doorB : room.neighbors.values()) {
				if (doorA.equals(doorB)) {
					continue;
				}
				
				int yy = (doorB.y - doorA.y);
				int xx = (doorB.x - doorA.x);
				double m = yy/xx;
				int c = doorA.y;
				
				for (int i = Math.min(doorB.x, doorA.x); i < Math.max(doorB.x, doorA.x); i++){
					int x = i;
					int y = (int) ((m*x) + c);
					int ii = x-room.getLeftEdge();
					int jj = y-room.getTopEdge();
					try {
						points[ii][jj] = room.new Point(id++, x, y);
					}catch (IndexOutOfBoundsException e) {
						System.err.println(e.getMessage());
						System.err.println(String.format("yy%d/xx%d/m%f/c%d/x%d/y%d/ii%d/jj%d", yy, xx, m, c, x, y, ii ,jj));
					}
				}
			}			
		}
		
		
//		for (int x = room.getLeftEdge(); x <= room.getRightEdge() ; x++) {
//			for (int y = room.getTopEdge(); y <= room.getBottomEdge(); y++) {
//				points[x-room.getLeftEdge()][y-room.getTopEdge()] = room.new Point(id++, x, y);
//			}
//		}
		return points;
	}
	
//	private static Room.Point[][] getPointsMatrix(Room room) {
//		int id = 1;
//		Room.Point[][] points = new Room.Point[room.getRightEdge()-room.getLeftEdge()+1][room.getBottomEdge()-room.getTopEdge()+1];
//		for (int x = room.getLeftEdge(); x <= room.getRightEdge() ; x++) {
//			for (int y = room.getTopEdge(); y <= room.getBottomEdge(); y++) {
//				points[x-room.getLeftEdge()][y-room.getTopEdge()] = room.new Point(id++, x, y);
//			}
//		}
//		return points;
//	}
	
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
	
	
	private static Room createRandomRoom(int maxx, int maxy, String name) {
		Random random = new Random();
		
		int random_width = maxx/12 + random.nextInt(maxx/4);
		int random_height = maxy/12 + random.nextInt(maxy/4);
		int random_center_x = maxx/2 + random.nextInt(maxx/50);
		int random_center_y = maxy/2 + random.nextInt(maxy/50);
//		String random_name = Room.RoomNames.values()[random.nextInt(Room.RoomNames.values().length)].toString();
		
		Room room = new Room(random_center_x, random_center_y, random_width, random_height, name);
		return room;
	}
	
//	public static void main(String[] args) {
//		List<Room> rooms = RoomUtils.getRandomRooms(5, 500, 500);
//		RoomUtils.moveRoomsApart(rooms);
//	}

}
