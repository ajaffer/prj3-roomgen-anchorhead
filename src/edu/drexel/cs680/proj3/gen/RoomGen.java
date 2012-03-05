package edu.drexel.cs680.proj3.gen;

public class RoomGen {

	
	/*
	 * 1. 4 set of images for 4 rooms: living, bed, hall, basement
	 * 2. create random rectangles (how many?)
	 * 3. space them apart so none of them overlaps, and each of them shares a boundary
	 * 		choose x/y direction to move:
	 * 			x: if distance_x b/w centers is > distance_y or vice versa
	 * 			move B:
	 * 				A_bottom-B_top
	 * 				OR A_right -B_left
	 * 4. give random names
	 * 5. give doors to reach each room, plus doors to reach living room from any room
	 * 		create nodes in a tree for each adjacent room
	 * 		for each living room, give doors to all neighbors
	 * 		for each room that does not have a door, give at least on door to an adjacent room
	 * 
	 */
}
