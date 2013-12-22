package com.example.gpstest;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;
import android.graphics.*;
import com.google.android.maps.Overlay;

public class gMapActivity extends MapActivity {

    MapView mapView;
    GeoPoint p;
    MapController mc;

    // extend Overlay class and override draw method to add our pin to the map
    class MapOverlay extends com.google.android.maps.Overlay {
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
                long when) {
            super.draw(canvas, mapView, shadow);
            // translate the GeoPoint to screen pixels
            Point screenPts = new Point();
            mapView.getProjection().toPixels(p, screenPts);

            // add push-pin
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    R.drawable.pin);// pin is the reference to the image of our
                                    // push-pin
            canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
            return true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mc = mapView.getController();
        // mapView.setSatellite(true);
        showMap(38.02868, -78.51661);

    }

    // This is the method that GPS method will call.
    // showMap() accepts two double values as parameters representing latitude
    // and longitude of the current location respectively. The method then
    // translate the GPS coordinates to "macrodegrees" to animate the map on the
    // screen towards given point with appropriate zoom level.
    // Finally,showMap() calls the MapOverlay and draws a push-pin at the giving
    // point on the map

    public void showMap(double lat, double lng) {

        double coordinates[] = { lat, lng }; // get from
        // http://www.travelgis.com/geocode/default.aspx
        double lat1 = coordinates[0];
        double lng1 = coordinates[1];

        p = new GeoPoint((int) (lat1 * 1E6), (int) (lng1 * 1E6)); // convert to
                                                                    // macrodegrees
        mc.animateTo(p); // animating the map toward the given point
        mc.setZoom(9);
        // mapView.invalidate();

        // add the push-pin marker on the map
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        mapView.invalidate();
    }
    

    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // MapController mc = mapView.getController();
    // switch (keyCode) {
    // case KeyEvent.KEYCODE_3:
    // mc.zoomIn();
    // break;
    // case KeyEvent.KEYCODE_4:
    // mc.zoomOut();
    // break;
    // }
    // return super.onKeyDown(keyCode, event);
    //
    // }
public void Back(View v) {
   this.finish();
}
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

    /* Request updates at startup */

}