package com.example.geomap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener,
        TaskLoadedCallback {


    private GoogleMap mMap;

    /////////////USED TO GET THE PERMISSION TO THE DEVICE GPS
    private static final String TAG = "Home";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean mLocationPermissionGranted = false;

    public boolean isLongClick;

    public FusedLocationProviderClient mFusedLocationProviderClient;

    // public PlaceInfo placeInfo = new PlaceInfo();////////////////////////////////////////////////////////////////////////////

    private Marker marker, delayMarker;

    private TextView tvDistance, tvDur, tvName, tvAddress, tvPhone, tvWebsite, tvLat, tvLong;

    public ImageView ivCancel, ivCancel2, ivCancel3;

    private DrawerLayout drawer;

    private NavigationView nav_view;

    private PlacesClient placesClient;

    private List<AutocompletePrediction> predictionList;

    private LocationCallback locationCallBack;

    private Location currentLocation;

    public static MarkerOptions deviceLocation, destination;

    public ArrayList<MarkerOptions> longMarker = new ArrayList<>();

    private MaterialSearchBar searchBar;

    public View mapView, bottomSheetView, bottomSheetViewDirection, bottomSheetViewDelayTaxi;

    public BottomSheetDialog bottomSheetInfo, bottomSheetDirection, bottomSheetDelayTaxi;

    public double distance;

    public SwitchCompat switchDistance;

    public String strUnitDistance, url, myCountry, placeId, favExist;

    public FirebaseDatabase rootNode;

    public Polyline currentPolyline;

    public Button btnSetDelay, btnTaxi;

    public LatLng varLatlng;

    public SpinnerFavLandmark spinnerFavLandmark;

    public MyClass myClass = com.example.geomap.MyClass.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        getLocationPermission();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        searchBar = findViewById(R.id.searchBar);

        nav_view = findViewById(R.id.nav_view);
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(this);

        bottomSheetInfo = new BottomSheetDialog(Home.this, R.style.BottomSheetDialogTheme);

        bottomSheetDirection = new BottomSheetDialog(Home.this, R.style.BottomSheetDialogTheme2);

        bottomSheetDelayTaxi = new BottomSheetDialog(Home.this, R.style.BottomSheetDialogTheme3);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Home.this);


        Places.initialize(Home.this, "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo");
        placesClient = Places.createClient(this);


        setSearchBar();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        MenuItem item = nav_view.getMenu().findItem(R.id.Km_Miles);

        switchDistance = (SwitchCompat) item.getActionView();

        strUnitDistance = "Miles";
        // updateDisInDB(strUnitDistance);////////////////////////////////////////////////////////////////////////////////////////

        KmOrMiles();

        mapView = mapFragment.getView();


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////THIS IS THE ON MAP READY METHOD//////////////////////////////////////////////////////////
///////////////////////////////////////////////THIS IS THE ON MAP READY METHOD//////////////////////////////////////////////////////////
///////////////////////////////////////////////THIS IS THE ON MAP READY METHOD//////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        mMap = googleMap;

        if (myClass.getMapType() != null)
            getMapType();


        //if all the permissions are granted then
        if (mLocationPermissionGranted) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;

            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //mMap.getUiSettings().setZoomControlsEnabled(true);

            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {

                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 50);
            }


            //call method to get the device location
            getDeviceLocation();


            if (myClass.getArrDelaysMakerOp() != null)
                PutDelaysOnMap();

            mMap.setOnMyLocationButtonClickListener(() -> {

                mMap.clear();


                getDeviceLocation();

                //marker = mMap.addMarker(new MarkerOptions().position(varLatlng).visible(false));

                if (myClass.getArrDelaysMakerOp() != null)
                    PutDelaysOnMap();

                if (marker != null) {
                    // marker.remove();
                    marker.setVisible(false);
                }

                if (searchBar.isSuggestionsVisible())

                    searchBar.clearSuggestions();

                if (searchBar.isSearchEnabled())

                    searchBar.disableSearch();


                return true;

            });


            mMap.setOnPoiClickListener(pointOfInterest -> {


                //clean the map
                mMap.clear();

                marker = mMap.addMarker(new MarkerOptions().position(varLatlng).visible(false));
                // marker.setVisible(false);

                if (myClass.getArrDelaysMakerOp() != null)
                    PutDelaysOnMap();

                isLongClick = false;

                //call method to get the place details that is clicked
                getDetailsPlaces(pointOfInterest.placeId);

                //initializing a marker
                marker = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng));

                marker.setVisible(true);
                //call the method to display the the bottom sheet
                displayBottomSheet();

            });


            mMap.setOnMapClickListener(latLng -> {

                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(varLatlng).visible(false));

                if (myClass.getArrDelaysMakerOp() != null)
                    PutDelaysOnMap();

            });


            mMap.setOnMapLongClickListener(latLng -> {

                longMarker.add(new MarkerOptions().position(latLng));
                mMap.addMarker(longMarker.get(longMarker.size() - 1));
                displayDelayTaxiSheet();
            });

        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD TO GET MAP TYPE///////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD TO GET MAP TYPE///////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD TO GET MAP TYPE///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void getMapType() {

        switch (myClass.getMapType()) {

            case "default":
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;

            case "terrain":
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case "satellite":
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET IF ITS KM OR MILES//////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET IF ITS KM OR MILES//////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET IF ITS KM OR MILES//////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void KmOrMiles() {

        switchDistance.setOnClickListener(v -> {
            if (switchDistance.isChecked() == true) {

                strUnitDistance = "Miles";
                Toast.makeText(getApplicationContext(), "Measurements are now in Miles", Toast.LENGTH_SHORT).show();
                updateDisInDB(strUnitDistance);///////////////////////////////////////YOU NEED THIS

            } else {

                strUnitDistance = "Km";
                Toast.makeText(getApplicationContext(), "Measurements are now in Km", Toast.LENGTH_SHORT).show();
                updateDisInDB(strUnitDistance);///////////////////////////////////////YOU NEED THIS
            }
        });

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD FOR UPDATING MEASUREMENT IN DB////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD FOR UPDATING MEASUREMENT IN DB////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD FOR UPDATING MEASUREMENT IN DB////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void updateDisInDB(String unitDistance) {


        rootNode = FirebaseDatabase.getInstance();

        DatabaseReference writetoDb = rootNode.getReference("Users");
        //strUsername = "userTegra";////////////////////////////////////////////////////////////////////////////////
        writetoDb.child(myClass.getUsername()).child("measureUnit").setValue(unitDistance);

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO INITIATE THE MAP///////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO INITIATE THE MAP///////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO INITIATE THE MAP///////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void initializeMap() {
        // Log.d(TAG, "initMap:initializinig map");
        //initializing the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO GET PERMISSION FOR DEVICE LOCATION////////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO GET PERMISSION FOR DEVICE LOCATION////////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO GET PERMISSION FOR DEVICE LOCATION////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void getLocationPermission() {

        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                initializeMap();

            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);

            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO GET DEVICE LOCATION////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO GET DEVICE LOCATION////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHOD TO GET DEVICE LOCATION////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void getDeviceLocation() {


        //initializing the location services from the gps of the device
        mFusedLocationProviderClient.getLastLocation();

        try {
            if (mLocationPermissionGranted) {
                final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        //if the device have been located
                        if (task.isSuccessful()) {

                            currentLocation = task.getResult();

                            if (currentLocation != null) {


                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));

                                varLatlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                                myCountry = getMyCountry();

                                if (myClass.getLandmarkType() != null) {
                                    getLandmarkType();
                                }

                                if (myClass.getFavId() != null) {

                                    Spinner spinFav = findViewById(R.id.spinFavLandmark);

                                    ArrayAdapter<String> adapter = new ArrayAdapter(Home.this,
                                            android.R.layout.simple_spinner_item, myClass.favId);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spinFav.setAdapter(adapter);
                                    ///////////////////////////////////////////////////////////////////////////////////////////////
                                }

                            } else {

                                LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(50000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallBack = new LocationCallback() {

                                    @Override
                                    public void onLocationResult(@NonNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }

                                        currentLocation = locationResult.getLastLocation();
                                        deviceLocation = new MarkerOptions().position(
                                                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("My Location");

                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallBack);
                                    }
                                };

                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);

                            }

                        } else {
                            // Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(Home.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD TO GET DEVICE COUNTRY LOCATION////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD TO GET DEVICE COUNTRY LOCATION////////////////////////////////////////////////////
///////////////////////////////////////////THE METHOD TO GET DEVICE COUNTRY LOCATION////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getMyCountry() {

        Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
        String address = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            Address obj = addresses.get(0);
            String add = obj.getCountryCode();

            address = add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return address;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////THE METHOD FOR NAVIGATION MENU/////////////////////////////////////////////////////////
/////////////////////////////////////////////////THE METHOD FOR NAVIGATION MENU/////////////////////////////////////////////////////////
/////////////////////////////////////////////////THE METHOD FOR NAVIGATION MENU/////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragChangeMap()).commit();
                break;

            case R.id.landmarkType:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragPreferredLandmark()).commit();
                break;

            case R.id.favLandmark:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragFavouriteLandmark()).commit();
                break;

            case R.id.rendezvous:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragRendezvous()).commit();
                break;

            /*case R.id.delay:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragDelay()).commit();
                break;*/

            case R.id.logOut:
                Intent intent = new Intent(Home.this, LogIn.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD FOR SEARCH BAR////////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD FOR SEARCH BAR////////////////////////////////////////////////////////////
///////////////////////////////////////////////////THE METHOD FOR SEARCH BAR////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void setSearchBar() {

        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text.toString(), true, null, true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setTypeFilter(TypeFilter.CITIES)
                        .setTypeFilter(TypeFilter.GEOCODE)
                        .setTypeFilter(TypeFilter.REGIONS)
                        .setCountry(myCountry)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                        if (predictionsResponse != null) {
                            predictionList = predictionsResponse.getAutocompletePredictions();
                            List<String> suggestionsList = new ArrayList<>();
                            for (int i = 0; i < predictionList.size(); i++) {
                                AutocompletePrediction prediction = predictionList.get(i);
                                suggestionsList.add(prediction.getFullText(null).toString());
                            }
                            searchBar.updateLastSuggestions(suggestionsList);
                            if (!searchBar.isSuggestionsVisible()) {
                                searchBar.showSuggestionsList();
                            }
                        }
                    } else {
                        Log.i("mytag", "prediction fetching task unsuccessful");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        searchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;

                }


                AutocompletePrediction selectedPrediction = predictionList.get(position);

                String suggestion = searchBar.getLastSuggestions().get(position).toString();

                searchBar.setText(suggestion);

                new Handler().postDelayed(() -> searchBar.clearSuggestions(), 1000);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if (imm != null) {

                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                final String placeId = selectedPrediction.getPlaceId();

                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();

                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(fetchPlaceResponse -> {

                    Place place = fetchPlaceResponse.getPlace();

                    Log.i("mytag", "Place found: " + place.getName());

                    LatLng latLngOfPlace = place.getLatLng();

                    if (latLngOfPlace != null) {

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));

                        marker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()));

                        getDetailsPlaces(placeId);
                        displayBottomSheet();

                    }
                }).addOnFailureListener(e -> {

                    if (e instanceof ApiException) {

                        ApiException apiException = (ApiException) e;

                        apiException.printStackTrace();

                        int statusCode = apiException.getStatusCode();

                        Log.i("mytag", "place not found: " + e.getMessage());

                        Log.i("mytag", "status code: " + statusCode);

                    }
                });


            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                searchBar.clearSuggestions();
            }
        });
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET TYPE OF LANDMARK////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET TYPE OF LANDMARK////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET TYPE OF LANDMARK////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void getLandmarkType() {

        if (myClass.getLandmarkType().equals("historical")) {

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                    "&radius=5000" +
                    "&types=" + "museum" + "&" + "&" + "art_gallery" + "&" + "mosque" + "&" +
                    "library" + "&" + "painter" + "&" + "courthouse" + "&" + "synagogue" + "&" +
                    "hindu_temple" +
                    "&sensor=true" +
                    "&key=" + "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo";

            new PlaceTask().execute(url);
        }

        if (myClass.getLandmarkType().equals("modern")) {

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                    "&radius=5000" +
                    "&types=" + "lawyer" + "&" + "airport" + "&" + "light_rail_station" + "&" +
                    "liquor_store" + "&" + "local_government_office" + "&" + "atm" + "&" +
                    "locksmith" + "&" + "bakery" + "&" + "lodging" + "&" + "bank" +
                    "&sensor=true" +
                    "&key=" + "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo";


            new PlaceTask().execute(url);

        }

        if (myClass.getLandmarkType().equals("popular")) {

            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                    "?location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                    "&radius=5000" +
                    "&types=" + "aquarium" + "&" + "amusement_park" + "&" + "bar" + "&" +
                    "movie_theater" + "&" + "cafe" + "&" + "night_club" + "&" + "restaurant" + "&" +
                    "shopping_mall" + "&" + "stadium" +
                    "&sensor=true" +
                    "&key=" + "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo";


            new PlaceTask().execute(url);

        }

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHODS TO GET PLACES INFO ON CLICK///////////////////////////////////////////////////
//////////////////////////////////////////////THE METHODS TO GET PLACES INFO ON CLICK///////////////////////////////////////////////////
//////////////////////////////////////////////THE METHODS TO GET PLACES INFO ON CLICK///////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class PlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }


    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line = "";

        while ((line = reader.readLine()) != null) {

            builder.append(line);
        }

        String data = builder.toString();

        reader.close();

        return data;

    }


    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;

            JSONObject object;
            try {
                object = new JSONObject(strings[0]);

                mapList = jsonParser.parseResult(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;
        }


        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {

            mMap.clear();

            for (int i = 0; i < hashMaps.size(); i++) {

                HashMap<String, String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(hashMapList.get("lat"));

                double lng = Double.parseDouble(hashMapList.get("lng"));

                String name = hashMapList.get("name");

                LatLng latLng = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();

                options.position(latLng);

                options.title(name);

                mMap.addMarker(options);

            }

        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////THE METHOD FOR THE BOTTOM SHEET FOR PLACE CLICKED//////////////////////////////////////////////
/////////////////////////////////////////THE METHOD FOR THE BOTTOM SHEET FOR PLACE CLICKED//////////////////////////////////////////////
/////////////////////////////////////////THE METHOD FOR THE BOTTOM SHEET FOR PLACE CLICKED//////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void displayBottomSheet() {

        //Bottom sheet about INFO for selected landmark
        bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_info, findViewById(R.id.bottomSheetInfo)
                );

        //Bottom sheet about DIRECTION to landmark
        bottomSheetViewDirection = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_direction, findViewById(R.id.bottomSheetDirection)
                );


        //initializing the text view
        tvName = bottomSheetView.findViewById(R.id.tvName);
        tvAddress = bottomSheetView.findViewById(R.id.tvAddress);
        tvPhone = bottomSheetView.findViewById(R.id.tvPhone);
        tvWebsite = bottomSheetView.findViewById(R.id.tvWebsite);
        tvLat = bottomSheetView.findViewById(R.id.tvLat);
        tvLong = bottomSheetView.findViewById(R.id.tvLong);

        ivCancel = bottomSheetView.findViewById(R.id.ivCancel);

        tvDistance = bottomSheetViewDirection.findViewById(R.id.tvDis);
        tvDur = bottomSheetViewDirection.findViewById(R.id.tvDur);


        //once the image view is clicked on
        ivCancel.setOnClickListener(v -> {
            //close the bottom sheet Dialog
            bottomSheetInfo.dismiss();

        });


        //set the bottom dialog
        bottomSheetInfo.setContentView(bottomSheetView);

        //show the bottom dialog
        bottomSheetInfo.show();


        //initializing the button direction from the button sheet form
        bottomSheetView.findViewById(R.id.btnDirection).setOnClickListener(v -> {


            deviceLocation = new MarkerOptions().position(
                    new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("My Location");


            //showing the distance
            showDistance();


            //initializing the latitude and longitude bounds
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            //set the device location
            builder.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

            //set destination location
            builder.include(destination.getPosition());
            LatLngBounds bounds = builder.build();

            //move the camera into the middle point between the two location
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 300);

            //move the camera with animation
            mMap.animateCamera(cu);

            //display this message
            Toast.makeText(Home.this, "Giving directions...", Toast.LENGTH_SHORT).show();

            //close the bottom sheet diagram
            bottomSheetInfo.dismiss();

            //calculate the best route and get as parameter device location marker and destination marker
            calculateBestRoute(deviceLocation.getPosition(), destination.getPosition());

            displayDirectionSheet();


        });


        bottomSheetView.findViewById(R.id.btnRendezvous).setOnClickListener(v -> {

            bottomSheetInfo.dismiss();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragRendezvous()).commit();

        });


        //initializing the button ADDLANDMARK from the bottom sheet form and setting click listener
        bottomSheetView.findViewById(R.id.btnAddLandmark).setOnClickListener(v -> {
            MyClass myClass = com.example.geomap.MyClass.getInstance();

            FavouriteLandmark();

        });


        bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_info,
                findViewById(R.id.bottomSheetInfo), false);





    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////THE METHOD TO SHOW THE DIRECTION BOTTOM SHEET/////////////////////////////////////////////////
//////////////////////////////////////////THE METHOD TO SHOW THE DIRECTION BOTTOM SHEET/////////////////////////////////////////////////
//////////////////////////////////////////THE METHOD TO SHOW THE DIRECTION BOTTOM SHEET/////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void displayDirectionSheet() {


        bottomSheetDirection.setContentView(bottomSheetViewDirection);

        bottomSheetDirection.show();

        ivCancel2 = bottomSheetViewDirection.findViewById(R.id.ivCancel2);

        ivCancel2.setOnClickListener(v -> {


            bottomSheetDirection.dismiss();

        });

        bottomSheetViewDirection = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_direction, findViewById(R.id.bottomSheetDirection)
                        , false);

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////THE METHOD TO SHOW THE DELAY & TAXI BOTTOM SHEET///////////////////////////////////////////////
/////////////////////////////////////////THE METHOD TO SHOW THE DELAY & TAXI BOTTOM SHEET///////////////////////////////////////////////
/////////////////////////////////////////THE METHOD TO SHOW THE DELAY & TAXI BOTTOM SHEET///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void displayDelayTaxiSheet() {

        //Bottom sheet about SETTING UP DELAY ALERT OR Taxi
        bottomSheetViewDelayTaxi = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_delay_taxi, findViewById(R.id.bottom_sheet_delay_taxi)
                );

        bottomSheetDelayTaxi.setContentView(bottomSheetViewDelayTaxi);

        bottomSheetDelayTaxi.show();

        ivCancel3 = bottomSheetViewDelayTaxi.findViewById(R.id.ivCancel3);
        btnSetDelay = bottomSheetViewDelayTaxi.findViewById(R.id.btnSetDelay);
        btnTaxi = bottomSheetViewDelayTaxi.findViewById(R.id.btnTaxi);


        ivCancel3.setOnClickListener(v -> {

            //close the bottomsheetDialog
            bottomSheetDelayTaxi.dismiss();

            longMarker.remove(longMarker.size() - 1);


        });


        btnTaxi.setOnClickListener(v -> {


            Toast.makeText(Home.this, "A taxi is being requested...", Toast.LENGTH_LONG).show();

        });


        btnSetDelay.setOnClickListener(v -> {

            MyClass myClass = com.example.geomap.MyClass.getInstance();
            myClass.setArrDelaysMakerOp(longMarker);

            bottomSheetDelayTaxi.dismiss();


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragDelay()).commit();

        });


        bottomSheetViewDelayTaxi = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.bottom_sheet_delay_taxi, findViewById(R.id.bottom_sheet_delay_taxi)
                        , false);

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO CALCULATE DISTANCE BETWEEN LOCATIONS//////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO CALCULATE DISTANCE BETWEEN LOCATIONS//////////////////////////////////////////////
////////////////////////////////////////THE METHOD TO CALCULATE DISTANCE BETWEEN LOCATIONS//////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //calculate and display the distance of between the device location and the selected destination
    private void showDistance() {

        //check if the distance must be in Miles or Kilometers
        if (strUnitDistance.equals("Km")) {
            tvDistance.setText("0 Km");
        } else if (strUnitDistance.equals("Miles")) {
            tvDistance.setText("0 Miles");
        }

        //initializing the destination location variable
        Location des = new Location("Destination");

        //set the latitude of the destination getting it from the marker destination
        des.setLatitude(destination.getPosition().latitude);

        //set the longitude of the destination getting it from the marker destination
        des.setLongitude(destination.getPosition().longitude);

        //initializing the device location variable
        Location myLocation = new Location("Current location");

        //set the latitude of the device location getting it from the marker destination
        myLocation.setLatitude(currentLocation.getLatitude());

        //set the longitude of the destination getting it from the marker destination
        myLocation.setLongitude(currentLocation.getLongitude());


        //get the distance
        distance = des.distanceTo(myLocation);
        tvDur.setText(getTimeTaken(distance));

        if (strUnitDistance.equals("Km")) {
            //if the distance between the device location and the destination is less then a 1000 meters
            if (distance < 1000) {
                //set the textView text to the distance in kilometers
                tvDistance.setText(new DecimalFormat("##.##").format(distance) + " M");

            } else {
                //set the textView text to the distance in kilometers
                tvDistance.setText(new DecimalFormat("##.##").format((distance) / 1000) + " Km");
            }
        } else if (strUnitDistance.equals("Miles")) {

            //convert the distance in Miles
            double distMi = (distance / 1000) * 0.62137;

            //set the textView text to the distance in miles
            tvDistance.setText(new DecimalFormat("##.##").format(distMi) + " Miles");

        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHODS TO CALCULATE BEST ROUTE//////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHODS TO CALCULATE BEST ROUTE//////////////////////////////////////////////////////
///////////////////////////////////////////////THE METHODS TO CALCULATE BEST ROUTE//////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //calculate the best route to the destination
    private void calculateBestRoute(LatLng Start, LatLng End) {
        //get the position of the destination and device location
        url = getUrl(deviceLocation.getPosition(), destination.getPosition(), "driving");//////////////////
        new FetchURL(Home.this).execute(url, "driving");////////////////////////
        //DownloadTask downloadTask = new DownloadTask();

        //downloadTask.execute(url);


    }


    //get the information about the best route between the device location and the destination from the direction API
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {

        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;

        // Mode
        String mode = "mode=" + directionMode;

        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;

        // Output format
        String output = "json";
        String apiKey = "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + apiKey;
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////THE METHOD TO CALCULATE TIME TO LOCATION////////////////////////////////////////////////////
////////////////////////////////////////////THE METHOD TO CALCULATE TIME TO LOCATION////////////////////////////////////////////////////
////////////////////////////////////////////THE METHOD TO CALCULATE TIME TO LOCATION////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //calculate the duration of the trip
    public String getTimeTaken(double dis) {

        //Log.d("ResponseT","meter :"+meter+ " kms : "+kms+" mins :"+mins_taken);
        int totalMinutes = (int) ((dis / 1000) / 0.5);

        //if the time of the trip is less then 60
        if (totalMinutes < 60) {
            //return the time in minutes
            return "" + totalMinutes + " mins";

            //else if the time of the trip is bigger then 24 hours
        } else if ((totalMinutes / 60) > 24) {

            String minutes = Integer.toString(totalMinutes % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;

            //returns time sin days, hours and minutes
            return ((totalMinutes / 60) / 24) + " Day " + (totalMinutes / 60) + " hour " + minutes + "mins";
        } else {

            String minutes = Integer.toString(totalMinutes % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;

            //return the time in hours and minutes
            return (totalMinutes / 60) + " hour " + minutes + "mins";

        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET DETAILS ABOUT PLACES////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET DETAILS ABOUT PLACES////////////////////////////////////////////////////
//////////////////////////////////////////////THE METHOD TO GET DETAILS ABOUT PLACES////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //get the details of the selected place
    private void getDetailsPlaces(String pPlaceId) {
        //initializing the Places API with the API key
        Places.initialize(getApplicationContext(), "AIzaSyAoIjOJDwIT4DJaMih0puhM-1m_U44cGlo");

        // Define a Place ID.
        placeId = pPlaceId;


        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,
                Place.Field.NAME, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI, Place.Field.VIEWPORT, Place.Field.RATING);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient = Places.createClient(getApplicationContext());


        //set the the event listener
        placesClient.fetchPlace(request).addOnCompleteListener(task -> {


            //set the place object
            Place place = task.getResult().getPlace();

            Log.i(TAG, "Place found: " + place.getName());

            try {

                //iniatializing the class


                //set the place information in the class
               /* placeInfo.setId(place.getId());
                placeInfo.setLatLng(place.getLatLng());
                placeInfo.setName(place.getName());
                placeInfo.setAddress(place.getAddress());
                placeInfo.setPhoneNumber(place.getPhoneNumber());
                placeInfo.setWebsiteUri(place.getWebsiteUri());
                placeInfo.setRating(place.getRating());*/

                myClass.setId(place.getId());
                myClass.setLatLng(place.getLatLng());
                myClass.setName(place.getName());
                myClass.setAddress(place.getAddress());
                myClass.setPhoneNumber(place.getPhoneNumber());
                myClass.setWebsiteUri(place.getWebsiteUri());
                myClass.setRating(place.getRating());


                //set the marker destination position with the current information collected from the API
                /*destination = new MarkerOptions().position(
                        new LatLng(placeInfo.getLatLng().latitude, placeInfo.getLatLng().longitude)).title("Destination");*//////////////

                destination = new MarkerOptions().position(
                        new LatLng(myClass.getLatLng().latitude, myClass.getLatLng().longitude)).title("Destination");


                //set the text view with the collected information
               /* tvName.setText(placeInfo.getName());
                tvAddress.setText(placeInfo.getAddress());
                tvPhone.setText(placeInfo.getPhoneNumber());
                tvWebsite.setText(placeInfo.getWebsiteUri().toString());
                tvLat.setText(String.valueOf(placeInfo.getLatLng().latitude));
                tvLong.setText(String.valueOf(placeInfo.getLatLng().longitude));*/////////////////////////////////////////////

                tvName.setText(myClass.getName());
                tvAddress.setText(myClass.getAddress());
                tvPhone.setText(myClass.getPhoneNumber());
                tvWebsite.setText(myClass.getWebsiteUri().toString());
                tvLat.setText(String.valueOf(myClass.getLatLng().latitude));
                tvLong.setText(String.valueOf(myClass.getLatLng().longitude));

            } catch (NullPointerException e) {
                Log.i(TAG, "onResult: NullPointerException" + e.getMessage());

            }


        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////METHOD TO ADD FAVOURITE LANDMARK TO DB///////////////////////////////////////////////////
///////////////////////////////////////////////METHOD TO ADD FAVOURITE LANDMARK TO DB///////////////////////////////////////////////////
///////////////////////////////////////////////METHOD TO ADD FAVOURITE LANDMARK TO DB///////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void FavouriteLandmark() {

        //Toast.makeText(Home.this, "Favourite landmark seeeeeeeeeeeeeet", Toast.LENGTH_SHORT);
        ArrayList<String> arrFavId = new ArrayList<>();

        arrFavId.add(myClass.getId());

        myClass.setFavId(arrFavId);

        //MyClass myClass = com.example.geomap.MyClass.getInstance();
        rootNode = FirebaseDatabase.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference UsernameRef = rootRef.child("Users").child(myClass.getUsername()).child("FavouriteLandmarks");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if (!snapshot.exists()) {


                    DatabaseReference writetoDb = rootNode.getReference("Users");


                    writetoDb.child(myClass.getUsername()).child("FavouriteLandmarks").child(myClass.getName())
                            .child(myClass.getName()).setValue(myClass.getName());

                    writetoDb.child(myClass.getUsername()).child("FavouriteLandmarks")
                            .child("ID").setValue(myClass.getId());

                    writetoDb.child(myClass.getUsername()).child("FavouriteLandmarks")
                            .child("Website").setValue(myClass.getWebsiteUri().toString());


                    Toast.makeText(Home.this, "This landmark has been added to your favourites!", Toast.LENGTH_LONG).show();

                    bottomSheetInfo.dismiss();




                } else {


                    DatabaseReference UsernameRef = rootRef.child("Users").child(myClass.getUsername())
                            .child("FavouriteLandmarks").child(myClass.getName());

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (!snapshot.exists()) {

                                DatabaseReference writetoDb = rootNode.getReference("Users");


                                writetoDb.child(myClass.getName())
                                        .child(myClass.getName()).setValue(myClass.getName());

                                writetoDb.child(myClass.getName())
                                        .child("ID").setValue(myClass.getId());

                                writetoDb.child(myClass.getName())
                                        .child("Website").setValue(myClass.getWebsiteUri().toString());


                                Toast.makeText(Home.this, "This landmark has been added to your favourites!", Toast.LENGTH_LONG).show();

                                bottomSheetInfo.dismiss();




                            } else {


                                Toast.makeText(Home.this, "This landmark is already in your favourites!", Toast.LENGTH_LONG).show();

                                bottomSheetInfo.dismiss();

                              }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    };
                    UsernameRef.addListenerForSingleValueEvent(eventListener);


                }


            }

            public void onCancelled(DatabaseError error) {

            }
        };
        UsernameRef.addListenerForSingleValueEvent(eventListener);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////THE METHODS TO SHOW DELAYS MARKERS ON MAP///////////////////////////////////////////////////
////////////////////////////////////////////THE METHODS TO SHOW DELAYS MARKERS ON MAP///////////////////////////////////////////////////
////////////////////////////////////////////THE METHODS TO SHOW DELAYS MARKERS ON MAP///////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void PutDelaysOnMap() {

        MyClass myClass = com.example.geomap.MyClass.getInstance();

        for (int i = 0; i < myClass.getArrDelaysMakerOp().size(); i++) {

            mMap.addMarker(myClass.getArrDelaysMakerOp().get(i));

        }

    }


}





















