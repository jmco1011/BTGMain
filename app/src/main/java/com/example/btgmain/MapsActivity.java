 package com.example.btgmain;

 import android.Manifest;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.graphics.Color;
 import android.graphics.drawable.ColorDrawable;
 import android.location.Address;
 import android.location.Geocoder;
 import android.location.Location;
 import android.location.LocationListener;
 import android.location.LocationManager;
 import android.net.Uri;
 import android.os.AsyncTask;
 import android.os.Build;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.Gravity;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.LinearLayout;
 import android.widget.PopupWindow;
 import android.widget.Spinner;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.fragment.app.FragmentActivity;

 import com.google.android.gms.maps.CameraUpdateFactory;
 import com.google.android.gms.maps.GoogleMap;
 import com.google.android.gms.maps.OnMapReadyCallback;
 import com.google.android.gms.maps.SupportMapFragment;
 import com.google.android.gms.maps.model.LatLng;
 import com.google.android.gms.maps.model.MarkerOptions;
 import com.google.android.gms.maps.model.Polyline;
 import com.google.android.gms.maps.model.PolylineOptions;

 import org.json.JSONObject;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {
    TextView txtSpeed, txtPopUp,txtDuration;
    Spinner et_dest1;
    Button btnAccept;
    ImageButton btnEm;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions mMarkerOptions;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;



    String[] places =new String[]
            {"Burnham Park",
                    "Mines View Park",
                    "Camp John Hay",
                    "Baguio Wright Park",
                    "Baguio The Mansion House",
                    "Baguio Bamboo Sanctuary",
                    "Bell Church",
                    "Baguio Our Lady of Lourdes Grotto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         btnAccept = findViewById(R.id.btnAccept);
         et_dest1 = findViewById(R.id.et_dest);
         txtSpeed = findViewById(R.id.txtSpeed);
         txtPopUp = findViewById(R.id.txtPopup);
         txtDuration = findViewById(R.id.txtDuration);
         btnEm = findViewById(R.id.btnEm);

         btnEm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent callIntent = new Intent(Intent.ACTION_DIAL);
                 callIntent.setData(Uri.parse("tel:(6374)4423939"));
                 startActivity(callIntent);
             }
         });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        getMyLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == 100){
            if (!verifyAllPermissions(grantResults)) {
                Toast.makeText(getApplicationContext(),"No sufficient permissions",Toast.LENGTH_LONG).show();
            }else{
                getMyLocation();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyAllPermissions(int[] grantResults) {

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void getMyLocation(){

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,16));
                    if(mOrigin != null && mDestination != null) {
                        drawRoute();
                        int speed = (int) ((location.getSpeed()*3600)/1000);
                        txtSpeed.setText(speed + " km/h");
                    }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,mLocationListener);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        mDestination = latLng;
                        mMap.clear();
                        mMarkerOptions = new MarkerOptions().position(mDestination).title("Destination");
                        mMap.addMarker(mMarkerOptions);
                        if(mOrigin != null && mDestination != null) {
                                drawRoute();
                            }
                        }


                });

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchLocation();

                     /*   LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popUp = inflater.inflate(R.layout.popupwindow, null);
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popUp, width, height, focusable);
                        popupWindow.showAtLocation(popUp, Gravity.CENTER, 0, 0);drawRoute();*/
                        }

                });


            }else{
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                },100);
            }
        }
    }

    private void searchLocation() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,places);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
         et_dest1.setAdapter(adapter);
         et_dest1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 mMap.clear();
                 String location = et_dest1.getSelectedItem().toString();
                 if (location.equalsIgnoreCase("Burnham Park")) {
                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("Burnham Park is named after Daniel Burnham," +
                                         " an American architect who was the city planner for Baguio." +
                                         " He designed the park and the original plans for the city simultaneously," +
                                         " and construction began around 1904.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });
                     }
                 } else if (location.equalsIgnoreCase("Mines View Park")) {
                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("Mines View is one of the oldest and most famous attraction" +
                                         " in the City of Pines. The park got its name from the Benguet" +
                                         " mountain range where gold, silver and copper were once quarried." +
                                         " It was a mining area for local diggers before the Americans" +
                                         " discovered Baguio City.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });

                     }
                 } else if (location.equalsIgnoreCase("Baguio Wright Park")) {
                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("Wright Park is named after its architect" +
                                         " Governor Luke E. Wright who ordered architect" +
                                         " Daniel Burnham to build the place with recreational" +
                                         " facility for American soldiers and civilians.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });

                     }
                 } else if (location.equalsIgnoreCase("Camp John Hay")) {
                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("In 1903 Camp John Hay was designed for the exclusive use of the" +
                                         " US Military and Department of Defense in the Far East. This U.S." +
                                         " base, named after U.S. President Theodore Roosevelt's Secretary of" +
                                         " State, was used by the Japanese as a concentration camp for American" +
                                         " and British soldiers during WWII.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });

                     }
                 } else if (location.equalsIgnoreCase("Baguio The Mansion House")) {
                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("Formerly called the Mansion House," +
                                         " this stately building was built in 1908" +
                                         " as summer homes for U.S. Governor-generals " +
                                         "who were the American administrators for " +
                                         "the Philippines and was destroyed in 1945" +
                                         " during the battle for the liberation of the " +
                                         "Philippines.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });
                     }
                 } else if (location.equalsIgnoreCase("Bell Church")) {

                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("The Bell Church (Chinese: 钟零善坛) is a Chinese temple of" +
                                         " the Chinese Filipino indigenous religious organization of " +
                                         "the same name in La Trinidad, Benguet, Philippines.\n" +
                                         "\n" +
                                         "It is an important religious and cultural site for the" +
                                         " local Chinese Filipino community and one of the tourist sites" +
                                         " of both La Trinidad and the neighboring city of Baguio featuring" +
                                         " in local films in the 1970s until the 1990s.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });
                     }
                 } else if (location.equalsIgnoreCase("Baguio Bamboo Sanctuary")) {

                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("The Bamboo Eco Park in Baguio is quickly becoming " +
                                         "a popular tourist destination in the City of Pines.");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });
                     }
                 }else if (location.equalsIgnoreCase("Baguio Our Lady of Lourdes Grotto")) {

                     if (location != null || !location.equalsIgnoreCase("")) {
                         List<Address> addressList = null;
                         Geocoder geocoder = new Geocoder(MapsActivity.this);
                         try {
                             addressList = geocoder.getFromLocationName(location, 1);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }

                         Address address = addressList.get(0);
                         LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                         DownloadTask downloadTask = new DownloadTask();
                         downloadTask.execute(getDirectionsUrl(mOrigin, latLng));
                         mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                         LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                         View popupView = inflater.inflate(R.layout.popupwindow, null);

                         int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                         int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                         final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
                         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                         ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                                 .setText("This is a Catholic shrine and pilgrimage site assigned" +
                                         " to Our Lady of Lourdes Grotto, located on Dominican Hill" +
                                         " Road Baguio, Philippines. The site also features a statue" +
                                         " of Virgin Mary and close by is another statue of Jesus Christ." +
                                         " The shrine is reached after climbing 250 steps beautifully " +
                                         "laid down in natural settings");
                         popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                         popupView.setOnTouchListener(new View.OnTouchListener() {
                             @Override
                             public boolean onTouch(View v, MotionEvent event) {
                                 popupWindow.dismiss();
                                 return true;
                             }
                         });
                     }
                 }
             }


             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });

    }


    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key= AIzaSyCR0VdvbweGw1Q4TujqlGlqLAhcnG_Sh1U";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = new ArrayList<>();

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points =  null;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    if (j == 0) {
                        distance = (String)point.get("distance");
                        continue;
                    }else if (j==1){
                        duration = (String)point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }
                txtDuration.setText("Distance:"+distance + ", Duration:"+duration);

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }


    }

 }



