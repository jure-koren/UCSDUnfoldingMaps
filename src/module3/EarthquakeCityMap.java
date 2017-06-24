package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Jure Koren
 * Date: July 24, 2016
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = true;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	// marker radius
	public static final float MARKER_RADIUS_LIGHT = 2;
	public static final float MARKER_RADIUS_MOD = 5;
	public static final float MARKER_RADIUS_HIGH = 10;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);		

	    System.out.println("Create map object...");
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			//map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.AerialProvider() );
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    System.out.println("Parse RSS feed...");
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  

	    
	    //TODO: Add code here as appropriate
	    
	    System.out.println("Add markers...");
	    for (PointFeature eq: earthquakes) {
	    	markers.add(createMarker(eq) );
	    }
	    map.addMarkers(markers);
	    
	    System.out.println("Ready.");
	}
	
	// create marker for magnitude
	private SimplePointMarker StyleMarker (SimplePointMarker myMarker, float mag) {
		
		// set color
		myMarker.setColor(GetColorForMagnitude(mag));	
	    
		// set radius
		if (mag < THRESHOLD_LIGHT) {
			// light eq.
			myMarker.setRadius(MARKER_RADIUS_LIGHT);
		} else if (mag < THRESHOLD_MODERATE) {
			// moderate eq.
			myMarker.setRadius(MARKER_RADIUS_MOD);			
		} else {
			// high
			myMarker.setRadius(MARKER_RADIUS_HIGH);			
		}
	    
		return myMarker;
	}
	
	// return color for magnitude
	private int GetColorForMagnitude(float mag) {
		// define colors for earthquakes
		int MyColor = 0;
	    int ColorForLight = color(255, 255, 0);
	    int ColorForMod = color(0, 0, 255);
	    int ColorForHigh = color(255, 0, 0);
	    
		if (mag < THRESHOLD_LIGHT) {
			// light eq.
			MyColor = ColorForLight;
		} else if (mag < THRESHOLD_MODERATE) {
			// moderate eq.
			MyColor = ColorForMod;
		} else {
			// high
			MyColor = ColorForHigh;
		}	    
		return MyColor;
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		SimplePointMarker myMarker = new SimplePointMarker(feature.getLocation(), feature.getProperties());
		//myMarker.setRadius(MARKER_RADIUS);
		
    	Object magObj = feature.getProperty("magnitude");
    	float mag = Float.parseFloat(magObj.toString());

    	// style the marker according to the magnitude
    	myMarker  = StyleMarker(myMarker, mag);
		
		return myMarker;
	}
	
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		
		// map starts at 200, 50
		
		// draw a rectangle
		//fill(255,255,255);
		rect(10, 50, 180, 500, 10);
		
		// add title
		textSize(14);
		fill(0,0,0);
		text(GetTranslation("Legend:"), 20, 100);
		
		// write text
		textSize(14);
		
		int x;
		int	y;	
		
		// first line		
		x = 30;
		y = 130;

		fill(GetColorForMagnitude(5));
		ellipse(x, y, MARKER_RADIUS_HIGH, MARKER_RADIUS_HIGH);
		fill(0,0,0);
		text("5.0+ " + GetTranslation("Magnitude"), x+20, y+5);
		
		// next line
		y += 20;

		fill(GetColorForMagnitude(4));
		ellipse(x, y, MARKER_RADIUS_MOD, MARKER_RADIUS_MOD);
		fill(0,0,0);		
		text("4.0+ " + GetTranslation("Magnitude"), x+20, y+5);					
		
		// next line
		y += 20;		
		fill(GetColorForMagnitude(3));
		ellipse(x, y, MARKER_RADIUS_LIGHT, MARKER_RADIUS_LIGHT);
		fill(0,0,0);
		text(GetTranslation("Bellow") + " 4.0", x+20, y+5);			
		
		// reset to white
		fill(255,255,255);
	}
	
	// translate English text to local language
	private String GetTranslation(String text) {
		// we could add translations later
		String translated = text;
		return translated;
	}
}
