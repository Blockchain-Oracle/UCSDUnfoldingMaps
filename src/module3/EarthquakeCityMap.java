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

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * 
 * @author Abubakr JIm0h
 *         Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = true;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;
	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public void setup() {
		hint(ENABLE_DEPTH_SORT);

		size(950, 600);

		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015, for working offline
		} else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.AerialProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// The List you will populate with new SimplePointMarkers
		List<Marker> markers = new ArrayList<Marker>();

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// TODO (Step 3): Add a loop here that calls createMarker (see below)
		// to create a new SimplePointMarker for each PointFeature in
		// earthquakes. Then add each new SimplePointMarker to the
		// List markers (so that it will be added to the map in the line below)
		for (PointFeature pointFeature : earthquakes) {
			SimplePointMarker simplePointMarker = createMarker(pointFeature);
			markers.add(simplePointMarker);
		}
		// Add the markers to the map so that they are displayed
		map.addMarkers(markers);
	}

	/*
	 * createMarker: A suggested helper method that takes in an earthquake
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is. Call it from a loop in the
	 * setp method.
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper
	 * styling to each marker based on the magnitude of the earthquake.
	 */
	private SimplePointMarker createMarker(PointFeature feature) {
		System.out.println(feature.getProperties());

		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());

		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());

		// Define colors for different magnitudes
		int blue = color(0, 0, 255); // Light earthquakes
		int yellow = color(255, 255, 0); // Moderate earthquakes
		int red = color(255, 0, 0); // Strong earthquakes

		if (mag < THRESHOLD_LIGHT) {
			marker.setColor(blue);
			marker.setRadius(5); // Small circle for light earthquakes
		} else if (mag < THRESHOLD_MODERATE) {
			marker.setColor(yellow);
			marker.setRadius(10); // Medium circle for moderate earthquakes
		} else {
			marker.setColor(red);
			marker.setRadius(15); // Large circle for strong earthquakes
		}

		return marker;
	}

	public void draw() {
		background(0);
		map.draw();
		addKey();
	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() {
		// Draw the key background
		fill(255, 250, 240);
		rect(25, 50, 150, 250);

		// Add title
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);

		// Add markers with corresponding labels
		fill(255, 0, 0);
		ellipse(50, 125, 15, 15);
		fill(0, 0, 0);
		text("5.0+ Magnitude", 75, 125);

		fill(255, 255, 0);
		ellipse(50, 175, 10, 10);
		fill(0, 0, 0);
		text("4.0+ Magnitude", 75, 175);

		fill(0, 0, 255);
		ellipse(50, 225, 5, 5);
		fill(0, 0, 0);
		text("Below 4.0", 75, 225);
	}

	public static void main(String[] args) {
		PApplet.main("module3.EarthquakeCityMap");
	}
}
