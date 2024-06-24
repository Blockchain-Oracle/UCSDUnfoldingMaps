package module1;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * HelloWorld
 * An application with two maps side-by-side zoomed in on different locations.
 * Author: UC San Diego Coursera Intermediate Programming team
 * 
 * @author Your name here
 *         Date: July 17, 2015
 */
public class HelloWorld extends PApplet {
	/**
	 * Your goal: add code to display second map, zoom in, and customize the
	 * background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.
	 * Don't forget the import lines above.
	 */

	// You can ignore this. It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// IF YOU ARE WORKING OFFLINE: Change the value of this variable to true
	private static final boolean offline = true;

	/** The map we use to display our home town: La Jolla, CA */
	UnfoldingMap map1;

	/** The map you will use to display your home town */
	UnfoldingMap map2;

	public void setup() {
		size(800, 800, JAVA2D); // Set up the Applet window to be 800x600 with JAVA2D renderer

		this.background(200, 200, 200); // Set background color

		// Select a map provider
		AbstractMapProvider provider = new Microsoft.RoadProvider();
		AbstractMapProvider provider2 = new Microsoft.AerialProvider();
		// Set a zoom level
		int zoomLevel = 10;

		if (offline) {
			// If you are working offline, you need to use this provider
			// to work with the maps that are local on your computer.
			provider = new MBTilesMapProvider(mbTilesString);
			provider2 = new MBTilesMapProvider(mbTilesString); // use the same offline provider for both maps
			// 3 is the maximum zoom level for working offline
			zoomLevel = 3;
		}

		// Create map1
		map1 = new UnfoldingMap(this, 0, 0, 0, 370, provider);
		// Create map2 beside map1
		map2 = new UnfoldingMap(this, 400, 0, 0, 370, provider);

		// Set initial zoom level and location for both maps
		map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		map2.zoomAndPanTo(zoomLevel, new Location(6.564883420726032f, 3.362869719287369f));

		// Make maps interactive
		MapUtils.createDefaultEventDispatcher(this, map1, map2);
	}

	/** Draw the Applet window. */
	public void draw() {
		// Draw both maps
		map1.draw();
		// map2.draw();
	}

	public static void main(String[] args) {
		PApplet.main("module1.HelloWorld");
	}
}
