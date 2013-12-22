package com.example.gpstest;

import java.util.ArrayList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btnShowLocation;
    private static int tourStop = 6;
    private static double currentLatitude = 0;
    private static double currentLongitude = 0;
    private static TextView lat;
    private static TextView lon;
    private static TextView nLat;
    private static TextView nLon;
    private static TextView building;
    private static String temp="";
    private static ArrayList<ArrayList<String>> locations;
    private static ImageView arrow;
    private static float currentDegrees=(float) 0.0;
    private static float nextDegrees=(float) 0.0;
    private static MediaPlayer mpAudio;
    static Context appState;
    // GPSTracker class
    GPSTracker gps;
    //private static Context appState;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Chronometer) findViewById(R.id.chronometer1)).start();
        arrow = (ImageView) findViewById(R.id.arrowView);
        System.out.println(arrow);
      appState = getBaseContext();
      
     
        // Adds coordinates of tour locations
        locations = new ArrayList<ArrayList<String>>();
        ArrayList<String> loc = new ArrayList<String>();
        loc.add("38.035632");
        loc.add("-78.503308");
        loc.add("Rotunda");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.033059");
        loc.add("-78.50413");
        loc.add("McIntire School");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.033651");
        loc.add("-78.505753");
        loc.add("Amphitheater");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.036466");
        loc.add("-78.5052");
        loc.add("Alderman Library");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.035696");
        loc.add("-78.510613");
        loc.add("Castle");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.035282");
        loc.add("-78.511495");
        loc.add("Old Dorms (Quad)");
        locations.add(loc);
        loc = new ArrayList<String>();
        loc.add("38.031708");
        loc.add("-78.510849");
        loc.add("Rice Hall");
        locations.add(loc);
       
        // Initializes GPSTracker class
        gps = new GPSTracker(MainActivity.this);

        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longitude);
        nLat = (TextView) findViewById(R.id.nLat);
        nLon = (TextView) findViewById(R.id.nLon);
        building = (TextView) findViewById(R.id.building);
        mpAudio= MediaPlayer.create(this, R.raw.good_ole_song);
        updateNextText();
    }

    // GPSTracker calls this method when it recieves new coordinates. This
    // method sets the current longitude and latitude text on screen. Also calls checkDestination to see if the user
    // has reached the next tour stop
    public void updateText() {
        lat.setText("" + currentLatitude);
        lon.setText("" + currentLongitude);
        
        checkDestination();
    }
    //Updates the textview on screen that shows the coordinates for the next destination
    public static void updateNextText() {
        nLat.setText(locations.get(tourStop).get(0));
        nLon.setText(locations.get(tourStop).get(1));
       building.setText(locations.get(tourStop).get(2));
    }
    //Calculates the distance between two GPS points
    public static double distFrom(double lat1, double lng1, double lat2,
            double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Double(dist * meterConversion).doubleValue();
    }
    private static void updateTemp() {
        if (temp.equals("cold")) {

            Toast t = Toast.makeText(appState, "You're getting cold!", Toast.LENGTH_LONG);

            t.show();

        }

        else if (temp.equals("warm")) {

            Toast t = Toast.makeText(appState, "You're getting warm!", Toast.LENGTH_LONG);

            t.show();

        }

        else if (temp.equals("hot")) {

            Toast t = Toast.makeText(appState, "You're getting hot!", Toast.LENGTH_LONG);

            t.show();
        }
    }
    
    private static double getAngle(double lat1, double lon1, double lat2, double lon2) {

        int MAXITERS = 20;
        // Convert lat/long to radians
        lat1 *= Math.PI / 180.0;
        lat2 *= Math.PI / 180.0;
        lon1 *= Math.PI / 180.0;
        lon2 *= Math.PI / 180.0;

        double a = 6378137.0; // WGS84 major axis
        double b = 6356752.3142; // WGS84 semi-major axis
        double f = (a - b) / a;
        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

        double L = lon2 - lon1;
        double A = 0.0;
        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;

        double sigma = 0.0;
        double deltaSigma = 0.0;
        double cosSqAlpha = 0.0;
        double cos2SM = 0.0;
        double cosSigma = 0.0;
        double sinSigma = 0.0;
        double cosLambda = 0.0;
        double sinLambda = 0.0;

        double lambda = L; // initial guess
        for (int iter = 0; iter < MAXITERS; iter++) {
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
            sinSigma = Math.sqrt(sinSqSigma);
            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
            sigma = Math.atan2(sinSigma, cosSigma); // (16)
            double sinAlpha = (sinSigma == 0) ? 0.0 :
                    cosU1cosU2 * sinLambda / sinSigma; // (17)
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
            cos2SM = (cosSqAlpha == 0) ? 0.0 :
                    cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
            A = 1 + (uSquared / 16384.0) * // (3)
                    (4096.0 + uSquared *
                            (-768 + uSquared * (320.0 - 175.0 * uSquared)));
            double B = (uSquared / 1024.0) * // (4)
                    (256.0 + uSquared *
                            (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
            double C = (f / 16.0) *
                    cosSqAlpha *
                    (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = B * sinSigma * // (6)
                    (cos2SM + (B / 4.0) *
                            (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
                                    (B / 6.0) * cos2SM *
                                            (-3.0 + 4.0 * sinSigma * sinSigma) *
                                            (-3.0 + 4.0 * cos2SMSq)));

            lambda = L +
                    (1.0 - C) * f * sinAlpha *
                            (sigma + C * sinSigma *
                                    (cos2SM + C * cosSigma *
                                            (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

            double delta = (lambda - lambdaOrig) / lambda;
            if (Math.abs(delta) < 1.0e-12) {
                break;
            }
        }

        float distance = (float) (b * A * (sigma - deltaSigma));
        float initialBearing = (float) Math.atan2(cosU2 * sinLambda,
                cosU1 * sinU2 - sinU1 * cosU2 * cosLambda);
        initialBearing *= 180.0 / Math.PI;
        float finalBearing = (float) Math.atan2(cosU1 * sinLambda,
                -sinU1 * cosU2 + cosU1 * sinU2 * cosLambda);
        finalBearing *= 180.0 / Math.PI;

        return finalBearing;
    }
    public static void setArrow() {

        //nextDegrees=(float) Bearing(currentLatitude,currentLatitude,Double.valueOf(locations.get(tourStop).get(0)),
                //Double.valueOf(locations.get(tourStop).get(1)));
        nextDegrees = (float) getAngle(currentLatitude,currentLongitude,Double.valueOf(locations.get(tourStop).get(0)),
                Double.valueOf(locations.get(tourStop).get(1)));
        RotateAnimation anim = new RotateAnimation(currentDegrees, nextDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        System.out.println(currentDegrees);
        System.out.println(nextDegrees);

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        anim.setFillEnabled(true);

        anim.setFillAfter(true);
        arrow.startAnimation(anim);
        currentDegrees = nextDegrees;
    }
    //Checks to see if the user is within 10 meters of the next destination
    public static void checkDestination() {
        double d = distFrom(currentLatitude, currentLongitude, Double.valueOf(locations.get(tourStop).get(0)),
                Double.valueOf(locations.get(tourStop).get(1)));
        if ( d<= 10) {
            Toast.makeText(appState, "You have arrived at your destination", Toast.LENGTH_LONG).show();
           
            if(tourStop<6){
                tourStop++;
                updateNextText();
                checkDestination();
            }
            else{
                Toast.makeText(appState, "Congratulations!You have finished the tour!", Toast.LENGTH_LONG).show();
                mpAudio.start();
                return;
            }
        }
        else if (d<= 50) {
            temp="hot";
        }
        else if ( d<=100){
            temp="warm";
        }
        else {
            temp="cold";
        }
        updateTemp();
        setArrow();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // check if GPS enabled
        if (gps.canGetLocation()) {

            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
           

        }
        else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        updateText();
        checkDestination();


    }
    //Called when the set location button is pressed.
    public void setLoc(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.latInput).getWindowToken(), 0);
        imm.hideSoftInputFromWindow(findViewById(R.id.lonInput).getWindowToken(), 0);
        try {
        currentLatitude =
                Double.valueOf(((EditText) findViewById(R.id.latInput))
                        .getText().toString());
        currentLongitude =
                Double.valueOf(((EditText) findViewById(R.id.lonInput))
                        .getText().toString());
        updateText();
        }
        catch (NumberFormatException e) {
            Toast t = Toast.makeText(getApplicationContext(), "Invalid number input. Please try again.", Toast.LENGTH_SHORT);
            t.show();
        }
        
    }

    public void goMap (View v) {
        Intent myIntent = new Intent(this, gMapActivity.class);
        startActivity(myIntent);
    }
    public static void setCurrentLatitude(double currentLatitude) {
        MainActivity.currentLatitude = currentLatitude;
        
    }

    public static void setCurrentLongitude(double currentLongitude) {
        MainActivity.currentLongitude = currentLongitude;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
