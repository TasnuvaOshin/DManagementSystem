package joytechnologiesltd.com.joyexpressdeliveryservice.Auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.Objects;

import androidx.fragment.app.FragmentTransaction;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataAdapter;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataSet;
import joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.HomeFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.MainActivity;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;
import joytechnologiesltd.com.joyexpressdeliveryservice.StarterActivity;


public class AuthFragment extends Fragment implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FloatingActionButton next;
    private EditText editText;
    private String phoneno;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String id, name, img;
    String driver_id;
    private ProgressDialog progressDialog;
    GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;
    private Double currentLang, currentLong;

    @SuppressLint({"ObsoleteSdkInt", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.f_map);
        supportMapFragment.getMapAsync(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please Wait");

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPrefs.edit();

        next = view.findViewById(R.id.next);
        editText = view.findViewById(R.id.phoneno);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                if (TextUtils.isEmpty(editText.getText())) {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error!!")
                            .setMessage("Please Enter the Phone number")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {


                    jsonCall json = new jsonCall();
                    json.execute();

                }

            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (checkLocationPermission()) {
            gMap = googleMap;
            gMap.setMyLocationEnabled(true);
            //for getting the current Location for my place
            GetCurrentLocation();
        }

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
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15f));
                        Log.d("lat", String.valueOf(currentLang));
                        Log.d("long", String.valueOf(currentLong));
                        editor.putString("currentLang", String.valueOf(currentLang));
                        editor.putString("currentLong", String.valueOf(currentLong));

                    }
                }
            });
        }

    }

    //for checking the location Permission

//checking the permission before the full process starts


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
        protected String doInBackground(Void... voids) {


            try {
                phoneno = editText.getText().toString();
                URL url = new URL("http://x.joytechnologieslimited.com/Api/Auth/authapi.php?phoneno=" + phoneno);


                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                //     httpURLConnection.setInstanceFollowRedirects(false); /* added line */
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

                    id = child.getString("id");
                    name = child.getString("name");
                    img = child.getString("image");
                    i++;
                }


                return id;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return id;


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("id", s);
            if (s.equals("no")) {
                //donotgo
                progressDialog.dismiss();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Invalid Number!")
                        .setMessage("This Number is not Yet Registered")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {

                editor.putString("driver_name", name);
                editor.putString("driver_id", s);
                editor.putString("driver_number", phoneno);
                editor.putString("img", img);
                editor.apply();
                CallUpToDate(s);
                progressDialog.dismiss();
                SetFrame(new HomeFragment());
            }
        }
    }

    private void CallUpToDate(final String s) {


        driver_id = s;
        Log.d("d", driver_id);
        jsonCalltoupdate jsonCalltoupdate = new jsonCalltoupdate();
        jsonCalltoupdate.execute();


    }

    private void SetFrame(Fragment fragment) {

        @SuppressLint({"NewApi", "LocalSuppress"}) FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    class jsonCalltoupdate extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String mainfile;


        @Override
        protected String doInBackground(Void... voids) {


            try {
                phoneno = editText.getText().toString();
                URL url = new URL("http://x.joytechnologieslimited.com/Api/orders/get_orders.php?driver_id=" + driver_id + "&current_lat=" + currentLang + "&current_lon=" + currentLong);


                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                //     httpURLConnection.setInstanceFollowRedirects(false); /* added line */
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = " ";
                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line);


                }

                mainfile = stringBuffer.toString();


                return id;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return id;


        }


    }


}
