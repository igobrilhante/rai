package com.rai;

import android.os.Bundle;
import android.os.Parcel;
import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolygonOptionsCreator;

import android.view.Menu;

public class MapActivity extends Activity {
	
	private GoogleMap googleMaps;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		googleMaps = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMaps.addMarker(new MarkerOptions()
					.position(new LatLng(-3.68, -41.0))
					.title(""));
		PolygonOptions p = new PolygonOptions();
		p.add(new LatLng(-3.68, -41.0),new LatLng(-3.69, -42.0),new LatLng(-3.699, -42.1),new LatLng(-3.688, -41.0),
				new LatLng(-3.687, -41.2),
				new LatLng(-3.685, -41.3),
				new LatLng(-3.68, -41.0));
		p.fillColor(Color.BLUE);
		p.strokeColor(Color.BLUE);
		
		googleMaps.addPolygon(p);
		googleMaps.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
		googleMaps.setMyLocationEnabled(true);
		googleMaps.getUiSettings().setMyLocationButtonEnabled(true);
		googleMaps.getUiSettings().setCompassEnabled(true);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
