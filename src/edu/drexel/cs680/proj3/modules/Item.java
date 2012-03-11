//package edu.drexel.cs680.proj3.modules;
//
//import java.util.List;
//
//public class Item {
//	public String type = "item";
//	public String id;
//	public int x,y;
//	public String synonym;
//	public String description;
//	public Animation animation;
//	
//	public class Animation {
//		public String action;
//		public String frameTime;
//		public String frame;
//	}
//	
//	/*
//	 * 			<object type="item" id="crypt-key" x="544" y="64" synonym = "key">
//					<description>A small grey and creepy key</description>
//					<animation action="idle">
//						<frame time="100">crypt-key</frame>
//					</animation>
//				</object>
//	 */
//	public String toXML(){
//		StringBuilder str = new StringBuilder();
//		str.append(String.format("<object type=\"%s\" id=\"%d\" x=\"544\" y=\"64\" synonym = \"key\">"), type, );
//					str.append(String.format("<description>A small grey and creepy key</description>"));
//					str.append(String.format("<animation action=\"idle\">"));
//						str.append(String.format("<frame time=\"100\">crypt-key</frame>"));
//					str.append(String.format("</animation>"));
//				str.append(String.format("</object>\")"));
//		
//		
//		return str.toString();
//	}
//}
