package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

public class EarthquakeCityMap extends PApplet {
	private static final long serialVersionUID = 1L;
	private static final boolean offline = true;
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	private UnfoldingMap map;
	private List<Marker> cityMarkers;
	private List<Marker> quakeMarkers;
	private List<Marker> countryMarkers;

	public void setup() {
		size(900, 700);
		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // The same feed, but saved August 7, 2015
		} else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		}
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities) {
			cityMarkers.add(new CityMarker(city));
		}

		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<Marker>();
		for (PointFeature feature : earthquakes) {
			if (isLand(feature)) {
				quakeMarkers.add(new LandQuakeMarker(feature));
			} else {
				quakeMarkers.add(new OceanQuakeMarker(feature));
			}
		}

		printQuakes();

		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);
	}

	public void draw() {
		background(0);
		map.draw();
		addKey();
	}

	private void addKey() {
		fill(255, 250, 240);
		rect(25, 50, 150, 250);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", 50, 75);

		fill(color(255, 0, 0));
		ellipse(50, 125, 15, 15);
		fill(color(255, 255, 0));
		ellipse(50, 175, 10, 10);
		fill(color(0, 0, 255));
		ellipse(50, 225, 5, 5);

		fill(0, 0, 0);
		text("5.0+ Magnitude", 75, 125);
		text("4.0+ Magnitude", 75, 175);
		text("Below 4.0", 75, 225);
	}

	private boolean isLand(PointFeature earthquake) {
		for (Marker m : countryMarkers) {
			if (isInCountry(earthquake, m)) {
				return true;
			}
		}
		return false;
	}

	private void printQuakes() {
		HashMap<String, Integer> countryQuakeCounts = new HashMap<>();
		int oceanQuakeCount = 0;

		for (Marker cm : countryMarkers) {
			String countryName = (String) cm.getProperty("name");
			countryQuakeCounts.put(countryName, 0);
		}

		for (Marker qm : quakeMarkers) {
			EarthquakeMarker em = (EarthquakeMarker) qm;
			if (em.isOnLand()) {
				String countryName = (String) em.getProperty("country");
				countryQuakeCounts.put(countryName, countryQuakeCounts.get(countryName) + 1);
			} else {
				oceanQuakeCount++;
			}
		}

		for (String country : countryQuakeCounts.keySet()) {
			int quakeCount = countryQuakeCounts.get(country);
			if (quakeCount > 0) {
				System.out.println(country + ": " + quakeCount);
			}
		}
		System.out.println("OCEAN QUAKES: " + oceanQuakeCount);
	}

	private boolean isInCountry(PointFeature earthquake, Marker country) {
		Location checkLoc = earthquake.getLocation();

		if (country.getClass() == MultiMarker.class) {
			for (Marker marker : ((MultiMarker) country).getMarkers()) {
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
					return true;
				}
			}
		} else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		PApplet.main("module4.EarthquakeCityMap");
	}
}
