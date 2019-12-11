package joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.Pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataAdapter;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataSet;
import joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.HomeFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class CompleteTaskFragment extends Fragment {
    public RecyclerView recyclerView;
    public ArrayList<HomeDataSet> arrayList;
    String status;
    public HomeDataAdapter homeDataAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
   private String driver_id;

    SharedPreferences sharedPrefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_task, container, false);
        //  Bundle bundle = this.getArguments();
        status = getArguments().getString("status");
        arrayList = new ArrayList<HomeDataSet>();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
         driver_id = sharedPrefs.getString("driver_id", "defaultValue");
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        jsonCall jsonCall = new jsonCall();
        jsonCall.execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NewApi")
            @Override
            public void onRefresh() {

                arrayList.clear();
                new jsonCall().execute();

            }
        });

        return view;
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

            if(arrayList.size() > 0){
                homeDataAdapter = new HomeDataAdapter(getActivity(), arrayList);
                recyclerView.setAdapter(homeDataAdapter);
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("No History Available")
                        .setMessage("no data available to show")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                SetFrame(new HomeFragment());
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL("http://x.joytechnologieslimited.com/Api/pages/page.php?driver_id="+driver_id+"&status="+status);


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

                    arrayList.add(new HomeDataSet(id, driver_id, delivery_id, merchant_id, status, created_at, rec_name, rec_number, rec_address, rec_zone, amount, instruction, merchant_name, merchant_email, merchant_phone));
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
    private void SetFrame(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack("my_fragment").commit();
    }
}
