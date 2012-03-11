package edu.drexel.cs680.proj3.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.drexel.cs680.proj3.modules.Room;

public class ItemUtils {
	private static final String key = "<object type=\"item\" id=\"crypt-key\" x=\"%d\" y=\"%d\" synonym = \"key\">"+
			"<description>A small grey and creepy key</description>"+
			"<animation action=\"idle\">"+
				"<frame time=\"100\">crypt-key</frame>"+
			"</animation>"+
		"</object>";
	
	
	private static final String silverLocket = "<object type=\"item\" id=\"silver-locket\" x=\"%d\" y=\"%d\" synonym = \"locket\">"+
				"<description>This must belong to the lady of the house. Wait.. it says Anna. Who is Anna?</description>"+
				"<animation action=\"idle\">"+
					"<frame time=\"100\">silver-locket</frame>"+
				"</animation>"+
			"</object>";
	
	private static final String clippings = "<object type=\"tile\" id=\"clippings\" x=\"608\" y=\"96\" synonym = \"clipping\">"+
				"<description>Some clipings of the family. I do not see anything interesting at all.</description>"+
				"<animation action=\"idle\">"+
					"<frame time=\"100\">clippings</frame>"+
				"</animation>"+
			"</object>";			
	
	private static final String puzzleBox = "<object type=\"item\" id=\"puzzle-box\" x=\"0\" y=\"64\" synonym = \"box\">"+
						"<description>Ah! I have no idea. I really need help from someone with this.</description>"+
						"<animation action=\"idle\">"+
							"<frame time=\"100\">puzzle-box-closed</frame>"+
						"</animation>"+
					"</object>";
	
	private static List<String> items;
	
	static {
		items = new ArrayList<String>();
		items.add(key);
		items.add(silverLocket);
		items.add(clippings);
		items.add(puzzleBox);
	}
	
	
	public static String getRandomItems(Room room) {
		StringBuilder randomItems = new StringBuilder();
		Random r = new Random();
		
		int q = r.nextInt(items.size());
		for (int c=0; c<=q; c++) {
			int i = r.nextInt(items.size());
			int x = room.getRandomX();
			int y = room.getRandomY();
			randomItems.append(String.format(items.get(i), x, y));
		}
		
		
		return randomItems.toString();
	}
	
}
