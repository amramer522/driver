package com.amoor.driver.ui.fragment.homeCycle;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.data.model.map_students.MapStudents;
import com.amoor.driver.data.rest.ApiServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.amoor.driver.data.rest.RetrofitClient.getClient;

public class MapFragment extends Fragment implements OnMapReadyCallback {


    Unbinder unbinder;
    private ApiServices apiServices;
    private SharedPreferences preferences;
    private GoogleMap mMap;
    private LatLng currentLatlang;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    private String driver_name;
    private boolean granted = false;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        givePermission();

        preferences = getContext().getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        String driver_id = preferences.getString("driver_id", "");
        driver_name = preferences.getString("driver_name", "");
        apiServices = getClient().create(ApiServices.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.navigation_map);
        mapFragment.getMapAsync(this);

        getCurrentLocation();
        getStudents(driver_id);


        return view;
    }

    private void getStudents(String driver_id)
    {
        apiServices.getStudentsOnMap(driver_id).enqueue(new Callback<List<MapStudents>>() {
            @Override
            public void onResponse(Call<List<MapStudents>> call, Response<List<MapStudents>> response) {
                List<MapStudents> mapStudentsList = response.body();
                if (mapStudentsList != null) {
                    for (int i = 0; i < mapStudentsList.size(); i++) {
                        MapStudents student = mapStudentsList.get(i);
                        double lat = Double.parseDouble(student.getLangtude());
                        double lon = Double.parseDouble(student.getLongtude());
                        String name = student.getName();
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat, lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String cityName = addresses.get(0).getAddressLine(0);
                        setMarkerOnMap(new LatLng(lat, lon), name, cityName);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<MapStudents>> call, Throwable t) {
            }
        });
    }

    private void setMarkerOnMap(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("الاتوبيس")
                .snippet(driver_name)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_bus))
        );
//        Toast.makeText(getContext(), ""+driver_name, Toast.LENGTH_SHORT).show();
        currentLatlang = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f);
        mMap.moveCamera(cameraUpdate);
    }

    private void setMarkerOnMap(LatLng latLng, String name, String cityName) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(name)
                .snippet(cityName)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_student))
        );
        currentLatlang = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatlang, 15f);
        mMap.moveCamera(cameraUpdate);
    }

    public boolean isMapsEnabled()
    {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps()
    {
        if (!granted)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            granted = true;
                            Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void getCurrentLocation()
    {

        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{FINE_LOCATION, COURSE_LOCATION}, 10);
            return;
        }
        LocationManager mngr = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationProvider provider = mngr.getProvider(LocationManager.GPS_PROVIDER);

        String providerName = provider.getName();
        mngr.requestLocationUpdates(providerName, 10, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                setMarkerOnMap(latLng);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Toast.makeText(getContext(), ""+provider+status, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onProviderEnabled(String provider) {
//                Toast.makeText(getContext(), ""+provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
//                Toast.makeText(getContext(), ""+provider, Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void givePermission() {
        if (isMapsEnabled())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(getContext(), FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), COURSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{FINE_LOCATION, COURSE_LOCATION}, 10);
                }
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults != null && grantResults.length > 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{FINE_LOCATION, COURSE_LOCATION}, 10);
                }

            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


}
