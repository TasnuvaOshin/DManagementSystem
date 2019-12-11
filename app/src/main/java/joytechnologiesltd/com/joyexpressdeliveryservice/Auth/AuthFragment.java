package joytechnologiesltd.com.joyexpressdeliveryservice.Auth;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class AuthFragment extends Fragment {
    private FloatingActionButton next;
    private EditText editText;
    private String phoneno;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    String id, name, img;
    String driver_id;
    private ProgressDialog progressDialog;

    @SuppressLint({"ObsoleteSdkInt", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);

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
                URL url = new URL("http://x.joytechnologieslimited.com/Api/orders/get_orders.php?driver_id="+driver_id);


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
