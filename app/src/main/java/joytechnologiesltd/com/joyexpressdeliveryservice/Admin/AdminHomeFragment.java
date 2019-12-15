package joytechnologiesltd.com.joyexpressdeliveryservice.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataAdapter;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataSet;
import joytechnologiesltd.com.joyexpressdeliveryservice.Admin.Details.DetailsFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.HomeFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.MainActivity;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class AdminHomeFragment extends Fragment implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;
    private Double currentLang, currentLong;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ArrayList<HomeDataSet> arrayList;
    private Bundle bundle;
    // Creating a marker
    MarkerOptions markerOptions = new MarkerOptions();
    Marker m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        bundle = new Bundle();
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        arrayList = new ArrayList<HomeDataSet>();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.f_map);
        supportMapFragment.getMapAsync(this);


        jsonCall jsonCall = new jsonCall();
        jsonCall.execute();


        return view;
    }


    private void GetCurrentLocation() {
        if (checkLocationPermission()) {
            Log.d("debug", "Now We want to show our current location");
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            @SuppressLint("MissingPermission") Task LocationTask = fusedLocationProviderClient.getLastLocation();

            LocationTask.addOnCompleteListener(new OnCompleteListener() {

                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Log.d("debug", "Now We get our Current Location");

                        location = (Location) task.getResult();

                        currentLang = location.getLatitude();
                        currentLong = location.getLongitude();
                        // gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
                        Log.d("lat", String.valueOf(currentLang));
                        Log.d("long", String.valueOf(currentLong));
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11f));
                    }
                }
            });
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (checkLocationPermission()) {
            gMap = googleMap;
            gMap.setMyLocationEnabled(true);
            GetCurrentLocation();
            //get all drivers last position call json to get the value
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    DetailsFragment detailsFragment = new DetailsFragment();
                    Toast.makeText(getActivity(), "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                    bundle.putString("driver_id", marker.getSnippet());
                    detailsFragment.setArguments(bundle);
                    SetFrame(detailsFragment);
                    return false;
                }
            });
        }

    }


    private void SetFrame(Fragment fragment) {

        @SuppressLint({"NewApi", "LocalSuppress"}) FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack("fragment").commit();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Please Share/on Your Location")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }

            return false;
        } else {

            return true;

        }
    }


    public class jsonCall extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String mainfile;

        @Override
        protected void onPostExecute(String aVoid) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    HomeDataSet currentHds = arrayList.get(i);
                    double lat = Double.parseDouble(currentHds.getCurrent_lat());
                    double lng = Double.parseDouble(currentHds.getCurrent_lon());
                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);
                    markerOptions.snippet(currentHds.getDriver_id());
                    markerOptions.title("Driver Id -> " + currentHds.getDriver_id());
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    // Placing a marker on the touched position
                    m = gMap.addMarker(markerOptions);
                }
            }
        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL("http://x.joytechnologieslimited.com/Api/location/loc.php");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                //     httpURLConnection.setInstanceFollowRedirects(false); /* added line */

                Log.d("id", String.valueOf(httpURLConnection.getResponseCode()));

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = " ";
                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line);


                }

                mainfile = stringBuffer.toString();

                JSONArray parent = new JSONArray(mainfile);
                int i = 0;
                while (i <= parent.length()) {

                    JSONObject child = parent.getJSONObject(i);

                    String id = child.getString("id");
                    Log.d("id", id);
                    String driver_id = child.getString("driver_id");
                    String delivery_id = child.getString("delivery_id");
                    String merchant_id = child.getString("merchant_id");
                    String status = child.getString("status");
                    String created_at = child.getString("created_at");
                    String rec_name = child.getString("rec_name");
                    String rec_number = child.getString("rec_number");
                    String rec_address = child.getString("rec_address");
                    String rec_zone = child.getString("rec_zone");
                    String amount = child.getString("amount");
                    String instruction = child.getString("instruction");
                    String merchant_name = child.getString("merchant_name");
                    String merchant_email = child.getString("merchant_email");
                    String merchant_phone = child.getString("merchant_phone");
                    String dlat = child.getString("current_lat");
                    String dlon = child.getString("current_lon");


                    arrayList.add(new HomeDataSet(id, driver_id, delivery_id, merchant_id, status, created_at, rec_name, rec_number, rec_address, rec_zone, amount, instruction, merchant_name, merchant_email, merchant_phone, dlat, dlon));
                    i++;
                }


                return mainfile;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;


        }
    }
}
