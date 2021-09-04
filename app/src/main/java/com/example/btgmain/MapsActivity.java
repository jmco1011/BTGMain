 package com.example.btgmain;

 import android.Manifest;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.graphics.Color;
 import android.graphics.drawable.ColorDrawable;
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
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.LinearLayout;
 import android.widget.PopupWindow;
 import android.widget.RadioButton;
 import android.widget.RadioGroup;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.fragment.app.FragmentActivity;

 import com.google.android.gms.common.api.Status;
 import com.google.android.gms.maps.GoogleMap;
 import com.google.android.gms.maps.OnMapReadyCallback;
 import com.google.android.gms.maps.SupportMapFragment;
 import com.google.android.gms.maps.model.Dash;
 import com.google.android.gms.maps.model.Dot;
 import com.google.android.gms.maps.model.Gap;
 import com.google.android.gms.maps.model.LatLng;
 import com.google.android.gms.maps.model.MarkerOptions;
 import com.google.android.gms.maps.model.PatternItem;
 import com.google.android.gms.maps.model.Polyline;
 import com.google.android.gms.maps.model.PolylineOptions;
 import com.google.android.libraries.places.api.Places;
 import com.google.android.libraries.places.api.model.Place;
 import com.google.android.libraries.places.api.model.RectangularBounds;
 import com.google.android.libraries.places.api.model.TypeFilter;
 import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
 import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

 import org.jetbrains.annotations.NotNull;
 import org.json.JSONObject;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {
    TextView txtSpeed, txtPopUp,txtDuration;
    Button btnLink;
    ImageButton btnEm;
    RadioButton rbDriving, rbWalking, rbBicycling;
    RadioGroup rgModes;
    int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_TRANSIT=1;
    final int MODE_WALKING=2;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions mMarkerOptions;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    public  String key = "key=AIzaSyCR0VdvbweGw1Q4TujqlGlqLAhcnG_Sh1U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);


         txtSpeed = findViewById(R.id.txtSpeed);
         txtPopUp = findViewById(R.id.txtPopup);
         txtDuration = findViewById(R.id.txtDuration);
         btnLink = findViewById(R.id.btnLink);
         btnEm = findViewById(R.id.btnEm);

         rbDriving = findViewById(R.id.rb_driving);
         rbBicycling = findViewById(R.id.rb_bicycling);
         rbWalking = findViewById(R.id.rb_walking);
         rgModes = findViewById(R.id.rg_modes);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),"AIzaSyCR0VdvbweGw1Q4TujqlGlqLAhcnG_Sh1U");
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(16.4023,120.5960),
                 new LatLng(16.5577,120.8039)));

         autocompleteFragment.setCountries("Ph");

         autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull @NotNull Place place) {
                mDestination = place.getLatLng();
                drawRoute();
            }

            @Override
            public void onError(@NonNull @NotNull Status status) {
                Toast.makeText(MapsActivity.this,"Place not Found",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.isMyLocationEnabled();
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

    private void getMyLocation() {
        //Initialize landmarks

        Location maharlika = new Location(LocationManager.GPS_PROVIDER);
        maharlika.setLatitude(16.41406);
        maharlika.setLongitude(120.590507);

        Location burnham = new Location(LocationManager.GPS_PROVIDER);
        burnham.setLatitude(16.41252);
        burnham.setLongitude(120.59299);

        Location minesview = new Location(LocationManager.GPS_PROVIDER);
        minesview.setLatitude(16.419661);
        minesview.setLongitude(120.62788);

        Location grotto = new Location(LocationManager.GPS_PROVIDER);
        grotto.setLatitude(16.40961);
        grotto.setLongitude(120.58056);

        Location mansion = new Location(LocationManager.GPS_PROVIDER);
        mansion.setLatitude(16.41369);
        mansion.setLongitude(120.62038);

        Location wright = new Location(LocationManager.GPS_PROVIDER);
        wright.setLatitude(16.41593);
        wright.setLongitude(120.61671);

        Location teacherscamp = new Location(LocationManager.GPS_PROVIDER);
        teacherscamp.setLatitude(16.41193);
        teacherscamp.setLongitude(120.60641);

        Location kennonroad = new Location(LocationManager.GPS_PROVIDER);
        kennonroad.setLatitude(16.37516);
        kennonroad.setLongitude(120.60706);

        Location bellamph = new Location(LocationManager.GPS_PROVIDER);
        bellamph.setLatitude(16.39880);
        bellamph.setLongitude(120.61779);

        Location govpack = new Location(LocationManager.GPS_PROVIDER);
        govpack.setLatitude(16.41099);
        govpack.setLongitude(120.59902);

        Location campjh = new Location(LocationManager.GPS_PROVIDER);
        campjh.setLatitude(16.39708);
        campjh.setLongitude(120.61148);

        Location botanical = new Location(LocationManager.GPS_PROVIDER);
        botanical.setLatitude(16.41503);
        botanical.setLongitude(120.61338);

        Location cathedral = new Location(LocationManager.GPS_PROVIDER);
        cathedral.setLatitude(16.42345);
        cathedral.setLongitude(120.59346);

        Location bellchurch = new Location(LocationManager.GPS_PROVIDER);
        bellchurch.setLatitude(16.43158);
        bellchurch.setLongitude(120.59883);

        Location tamawan = new Location(LocationManager.GPS_PROVIDER);
        tamawan.setLatitude(16.42949);
        tamawan.setLongitude(120.57625);


        Location diplomat =  new Location(LocationManager.GPS_PROVIDER);
        diplomat.setLatitude(16.40376);
        diplomat.setLongitude(120.58666);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location ps1e = new Location(LocationManager.GPS_PROVIDER);
        Location ps2e = new Location(LocationManager.GPS_PROVIDER);
        Location ps3e = new Location(LocationManager.GPS_PROVIDER);
        Location ps4e = new Location(LocationManager.GPS_PROVIDER);
        Location ps5e = new Location(LocationManager.GPS_PROVIDER);
        Location ps6e = new Location(LocationManager.GPS_PROVIDER);
        Location ps7e = new Location(LocationManager.GPS_PROVIDER);
        Location ps8e = new Location(LocationManager.GPS_PROVIDER);
        Location ps9e = new Location(LocationManager.GPS_PROVIDER);
        Location ps10e = new Location(LocationManager.GPS_PROVIDER);

        ps1e.setLatitude(16.41224);
        ps1e.setLongitude(120.57933);

        ps2e.setLatitude(16.42569);
        ps2e.setLongitude(120.59344);

        ps3e.setLatitude(16.41664);
        ps3e.setLongitude(120.61550);

        ps4e.setLatitude(16.37959);
        ps4e.setLongitude(120.61934);

        ps5e.setLatitude(16.40153);
        ps5e.setLongitude(120.5932);

        ps6e.setLatitude(16.42382);
        ps6e.setLongitude(120.60573);

        ps7e.setLatitude(16.41421);
        ps7e.setLongitude(120.59223);

        ps8e.setLatitude(16.39191);
        ps8e.setLongitude(120.59997);

        ps9e.setLatitude(16.43007);
        ps9e.setLongitude(120.54865);

        ps10e.setLatitude(16.38891);
        ps10e.setLongitude(120.57544);


        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double distance = location.distanceTo(ps1e) / 1000;
                double distance2 = location.distanceTo(ps2e) / 1000;
                double distance3 = location.distanceTo(ps3e) / 1000;
                double distance4 = location.distanceTo(ps4e) / 1000;
                double distance5 = location.distanceTo(ps5e) / 1000;
                double distance6 = location.distanceTo(ps6e) / 1000;
                double distance7 = location.distanceTo(ps7e) / 1000;
                double distance8 = location.distanceTo(ps8e) / 1000;
                double distance9 = location.distanceTo(ps9e) / 1000;
                double distance10 = location.distanceTo(ps10e) / 1000;

                double dist1 = location.distanceTo(maharlika) / 1000;
                double dburnham = location.distanceTo(burnham)/1000;
                double dmines = location.distanceTo(minesview)/1000;
                double dgrotto = location.distanceTo(grotto)/1000;
                double dmansion = location.distanceTo(mansion)/1000;
                double dgovpack = location.distanceTo(govpack)/1000;
                double dtamawan = location.distanceTo(tamawan)/1000;
                double dbellchurch = location.distanceTo(bellchurch)/1000;
                double dbellamph = location.distanceTo(bellamph)/1000;
                double dcjh = location.distanceTo(campjh)/1000;
                double dtc = location.distanceTo(teacherscamp)/1000;
                double dcath = location.distanceTo(cathedral)/1000;
                double ddiplomat = location.distanceTo(diplomat)/1000;
                double dbotanical = location.distanceTo(botanical)/1000;
                double dkennon = location.distanceTo(kennonroad)/1000;
                double dwright = location.distanceTo(wright)/1000;


                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());

                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 16));

                if (mOrigin != null && mDestination != null) {
                    drawRoute();
                    int speed = (int) ((location.getSpeed() * 3600) / 1000);
                    txtSpeed.setText(speed + " km/h");
                    //mMap.setTrafficEnabled(true);
                }

                if (distance <= .5) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 4242697));
                            startActivity(callintent);

                        }
                    });

                } else if (distance2 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 6611255));
                            startActivity(callintent);

                        }
                    });
                } else if (distance3 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 3001993));
                            startActivity(callintent);

                        }
                    });
                } else if (distance4 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 3059554));
                        }
                    });
                } else if (distance5 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 4420629));
                        }
                    });
                } else if (distance6 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 3009116));
                        }
                    });
                } else if (distance7 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 6611489));
                        }
                    });
                } else if (distance8 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 4242681));
                        }
                    });
                } else if (distance9 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 4248834));
                        }
                    });
                } else if (distance10 <= 1) {
                    btnEm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callintent = new Intent(Intent.ACTION_DIAL);
                            callintent.setData(Uri.parse("tel: " + 4222662));
                        }
                    });
                }
                if (dist1 <=.5) {
                    LatLng mh = new LatLng(maharlika.getLatitude(),maharlika.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(mh).title("Maharlika"));
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("The Maharlika Livelihood Center stands" +
                                    " on the former site of the Baguio Stone Market which was gutted" +
                                    " by a major fire in 1970 and was demolished in the mid-1970s." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Maharlika_Livelihood_Center"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //bell church
                }else if (dbellchurch <=.5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("The Bell Church is a magnificent Taoist Church that sits between Baguio City and La Trinidad, Benguet." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://outoftownblog.com/bell-church-of-baguio-city/"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //burnham
                }else if (dburnham <=.5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Burnham Park is named after Daniel Burnham, an American architect who was the city planner for Baguio. " + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://traveltips.usatoday.com/burnham-park-baguio-16190.html"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //grotto
                }else if (dgrotto <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Reach the pilgrimage spot Our Lady of Lourdes Grotto by climbing 252 steps to the top of the hill where the grotto sits. " + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.inspirock.com/philippines/baguio/our-lady-of-lourdes-grotto-a446228179"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //minesview
                }else if (dmines <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Mines View is one of the oldest and most famous attraction in the City of Pines. " + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.lakadpilipinas.com/2010/02/baguio-mines-view-park.html"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //tamawan
                }else if (dtamawan <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Located at the outskirts of the city, it is an artists colony set amid a charming collection of Ifugao and Kalinga huts." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.gobaguio.com/tam-awan-village.html#.YTGreY5Kjcs"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //mansion
                }else if (dbellchurch <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("The Bell Church is a magnificent Taoist Church that sits between Baguio City and La Trinidad, Benguet." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://outoftownblog.com/bell-church-of-baguio-city/"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //wright
                }else if (dwright <=.5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Wright Park is named after its architect Governor Luke E. Wright who ordered architect Daniel Burnham to build the place with recreational facility for American soldiers and civilians." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://thedailyroar.com/asia/philippines/the-wright-park-baguio-city/"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //cathedral 
                }else if (dcath <=.5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("The site where the cathedral currently stands was a hill referred" +
                                    " to as \"Kampo\" by the Ibaloi people. In 1907, a Catholic mission " +
                                    "was established by Belgian missionaries from the Congregatio Immaculati Cordis Mariae," +
                                    " who named the site Mount Mary." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.vigattintourism.com/tourism/articles/Baguio-Cathedral"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //botanical
                }else if (dbotanical <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("A huge piece of land that is owned by the Philippine government," +
                                    " Botanical Garden, like Burnham Park is one of those prime pieces of" +
                                    " real estate that provides priceless peace and tranquility to a city that" +
                                    " is in danger of becoming an urban jungle." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.gobaguio.com/botanical-garden-baguio-city.html#.YTGsMY5Kjcs"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //johnhay
                }else if (dcjh <=.5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("n 1903 Camp John Hay was designed for the exclusive use of" +
                                    " the US Military and Department of Defense in the Far East." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.gobaguio.com/camp-john-hay.html    "));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //bell amph
                }else if (dbellamph <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Walking distance from The Manor Hotel and inside the Historic " +
                                    "core of Camp John Hay is the charming Bell house, the original " +
                                    "vacation home of the Commanding General of the Philippines. " +
                                    "It was named after General J. Franklin Bell who's credited for " +
                                    "transforming Camp John Hay into a major military resort." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.backpackingphilippines.com/2009/09/bell-house-camp-john-hay-baguio-history.html?m=0"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                //diplomat
                }else if (ddiplomat <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("In the 1970s, the wartorn building was converted to the " +
                                    "sophisticated and beautiful Diplomat Hotel, but the hotel was " +
                                    "shut down by the ’80s, and left abandoned. The deserted," +
                                    " deteriorating building became infamous for being one of the " +
                                    "most haunted ruins in the country." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://www.atlasobscura.com/places/haunted-diplomat-hotel"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //gov pack
                }else if (dgovpack <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("In the 1970s, the wartorn building was converted to the " +
                                    "sophisticated and beautiful Diplomat Hotel, but the hotel was " +
                                    "shut down by the ’80s, and left abandoned. The deserted," +
                                    " deteriorating building became infamous for being one of the " +
                                    "most haunted ruins in the country." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("https://baguiocityguide.com/baguio-landmarks-and-their-history/#Governor_Pack_Road"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //teachers camp
                }else if (dtc <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("Teachers' Camp has, for the past 100 years, served as a training" +
                                    " center and venue for teachers from all over the Philippines who" +
                                    " come during the summer break to attend special courses in education." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.gobaguio.com/teachers-camp-baguio-city.html#.YTGvgo5Kjcs"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    //kennon
                }else if (ddiplomat <= .5) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popupwindow, null);

                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

                    ((TextView) popupWindow.getContentView().findViewById(R.id.txtPopup))
                            .setText("The epic of Kennon Road is a part of the story of Baguio.\n" +
                                    "Without it, Baguio would not have survived." + "\n" +
                                    "To learn more, click the button below.");
                    ((Button) popupWindow.getContentView().findViewById(R.id.btnLink)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse("http://www.gobaguio.com/files/Pages/articles-kennon.html"));
                            startActivity(intent);
                        }
                    });
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
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

            }else{
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                },100);
            }
        }
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

        // Sensor enabled
        String sensor = "sensor=false";

        // Travelling Mode
        String mode = "mode=driving";

        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }else if(rbBicycling.isChecked()){
            mode = "mode=transit";
            mMode = 1;
        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&"+key;

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
            List<PatternItem> pattern = Arrays.asList(
                    new Dot(), new Gap(20), new Dot(), new Gap(20));
            List<PatternItem> pattern2 = Arrays.asList(
                    new Dash(20),new Gap(20),new Dash(20 ),new Gap(20));
            ArrayList<LatLng> points;
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
                    lineOptions.width(10);
                // Adding all the points in the route to LineOptions
                if(mMode==MODE_DRIVING){
                    lineOptions.addAll(points);
                    lineOptions.color(Color.RED);
                    txtDuration.setText("Distance:"+ distance + ", Duration:"+duration);
                }else if(mMode==MODE_TRANSIT){
                    lineOptions.addAll(points);
                    lineOptions.pattern(pattern2);
                    lineOptions.color(Color.GREEN);
                    txtDuration.setText("Distance:"+ distance + ", Duration:"+duration);
                }else if (mMode==MODE_WALKING){
                    lineOptions.addAll(points);
                    lineOptions.pattern(pattern);
                    lineOptions.color(Color.BLUE);
                    txtDuration.setText("Distance:"+ distance + ", Duration:"+duration);
                }
            }
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