package module5;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/** Implements a common marker for cities and earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class CommonMarker extends SimplePointMarker {

	// Records whether this marker has been clicked (most recently)
	protected boolean clicked = false;
	
	public CommonMarker(Location location) {
		super(location);
	}
	
	public CommonMarker(Location location, java.util.HashMap<java.lang.String,java.lang.Object> properties) {
		super(location, properties);
	}
	
	// Getter method for clicked field
	public boolean getClicked() {
		return clicked;
	}
	
	// Setter method for clicked field
	public void setClicked(boolean state) {
		clicked = state;
	}
	
	// Common piece of drawing method for markers; 
	// Note that you should implement this by making calls 
	// drawMarker and showTitle, which are abstract methods 
	// implemented in subclasses
	public void draw(PGraphics pg, float x, float y) {
		// For starter code just drawMaker(...)
		if (!hidden) {
			drawMarker(pg, x, y);
			if (selected) {
				showTitle(pg, x, y);  // You will implement this in the subclasses
			}
		}
	}
	public abstract void drawMarker(PGraphics pg, float x, float y);
	public abstract void showTitle(PGraphics pg, float x, float y);
	
	public void drawTitle(PGraphics pg, float x, float y, String myTitle) {
		// save previous styling
		pg.pushStyle();
		
		pg.fill(255, 255, 255);
		pg.rect(x, y-2, 300, 20);		
		pg.fill(0, 0 , 0);
		pg.textSize(12);
		pg.text(myTitle, x+5, y, 300-5, 20);
		
		//pg.text(myTitle, x, y, 100, 10);
		//System.out.println("Title: "+ myTitle + " at " + x + "," + y);
		
		// reset to previous styling
		pg.popStyle();			
	}	
}