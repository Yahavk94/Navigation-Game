package gameClient;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * This class represents a KML file creation after finishing a game.
 * @author Yahav Karpel and Daniel Korotine.
 */

public class KML_Logger {
	private int levelNumber;
	private StringBuilder content;

	private static final String NodeStyle = "node";
	private static final String BananaStyle = "Yellow";
	private static final String AppleStyle = "Red";
	private static final String RobotStyle = "Mario";
	
	/**
	 * Constructor
	 * This method constructs a KML file after finishing a given level number.
	 * @param levelNumber
	 */
	
	public KML_Logger(int levelNumber) {
		this.levelNumber = levelNumber;
		content = new StringBuilder();
		
		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		content.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n");
		content.append("  <Document>\r\n");
		content.append("    <name>level: " + levelNumber + " Maze of Waze" + "</name>\r\n");
		content.append("	 <Style id=\"" + NodeStyle + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/pushpin/grn-pushpin.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + BananaStyle + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + AppleStyle + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/push[in/red-pushpin.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>");
		content.append("	 <Style id=\"" + RobotStyle + "\">\r\n");
		content.append("      <IconStyle>\r\n");
		content.append("        <Icon>\r\n");
		content.append("          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png</href>\r\n");
		content.append("        </Icon>\r\n");
		content.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
		content.append("      </IconStyle>\r\n");
		content.append("    </Style>\r\n");
	}

	private void addPlaceMark(String type, String pos) {
		LocalDateTime now = LocalDateTime.now();
		content.append("    <Placemark>\r\n");
		content.append("      <TimeStamp>\r\n");
		content.append("        <when>" + now + "</when>\r\n");
		content.append("      </TimeStamp>\r\n");
		content.append("      <styleUrl>#" + type + "</styleUrl>\r\n");
		content.append("      <Point>\r\n");
		content.append("        <coordinates>" + pos + "</coordinates>\r\n");
		content.append("      </Point>\r\n");
		content.append("    </Placemark>\r\n");
	}

	public void addNodePlaceMark(String pos) {
		addPlaceMark(NodeStyle, pos);
	}

	public void addRobotPlaceMark(String pos) {
		addPlaceMark(RobotStyle, pos);
	}

	public void addFruitPlaceMark(int type, String pos) {
		addPlaceMark(type == 1 ? AppleStyle : BananaStyle, pos);
	}
	
	/**
	 * This method closes the document after writing the entire data into it.
	 */

	public void closeDocument() {
		content.append("  </Document>\r\n");
		content.append("</kml>");
		try {
			PrintWriter pw = new PrintWriter(new File("data//KML/" + levelNumber + ".kml"));
			pw.write(content.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
}
