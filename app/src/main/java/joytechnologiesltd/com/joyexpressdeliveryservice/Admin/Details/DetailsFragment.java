package joytechnologiesltd.com.joyexpressdeliveryservice.Admin.Details;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;

public class DetailsFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    private CircleImageView circleImageView;
    private TextView name, id, nid, address, joining, status;
    private ImageButton imageButton;
    String mainfile;
    String driver_nid;
    String driver_id;
    String driver_status;
    String created_at;
    String driver_name;
    String driver_number;
    String driver_address;
    String image;
    String maindriverid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        circleImageView = view.findViewById(R.id.driver_image);
        name = view.findViewById(R.id.driname_name);
        id = view.findViewById(R.id.driname_id);
        nid = view.findViewById(R.id.driver_nid);
        address = view.findViewById(R.id.driver_add);
        joining = view.findViewById(R.id.driver_date);
        status = view.findViewById(R.id.driver_status);
        imageButton = view.findViewById(R.id.call_button);
        maindriverid = getArguments().getString("driver_id");
        id.setText(maindriverid);
        jsonCall jsonCall = new jsonCall();
        jsonCall.execute();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPhonePermission()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + driver_number));
                    getActivity().startActivity(callIntent);
                } else {

                    Toast.makeText(getActivity(), "Need Call Permission !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    public boolean checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(),
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission")
                        .setMessage("Please Access Internet")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions((Activity) getActivity(),
                                        new String[]{Manifest.permission.INTERNET},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    class jsonCall extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String mainfile;
        JSONObject object;

        @Override
        protected void onPostExecute(String s) {
            Log.d("img", driver_number);
            Picasso.get().load("http://x.joytechnologieslimited.com/" + s).fit().into(circleImageView);

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {


            try {

                URL url = new URL("http://x.joytechnologieslimited.com/Api/driver/get_driver.php?driver_id=" + maindriverid);


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


                object = parent.getJSONObject(0);

                name.setText(object.getString("name"));
                joining.setText(object.getString("joining_date"));
                address.setText(object.getString("pre_add"));
                status.setText(object.getString("status"));
                nid.setText(object.getString("n_Id"));
                driver_number = object.getString("phone_no");

                return object.getString("image");

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
