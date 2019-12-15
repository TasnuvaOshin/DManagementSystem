package joytechnologiesltd.com.joyexpressdeliveryservice.HomePage;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataAdapter;
import joytechnologiesltd.com.joyexpressdeliveryservice.APICALL.HomeDataSet;
import joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.Pages.CompleteTaskFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;
import joytechnologiesltd.com.joyexpressdeliveryservice.StarterActivity;


public class HomeFragment extends Fragment {
    public RecyclerView recyclerView;
    public ArrayList<HomeDataSet> arrayList;
    private ImageButton drawerbutton, search;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CompleteTaskFragment completeTaskFragment;
    private ImageView date;
    private EditText et_date;
    SharedPreferences sharedPrefs;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String driver_id;
    private  String dlat,dlog;

    public HomeDataAdapter homeDataAdapter;
    Bundle bundle;
    private String currentLang, currentLong;

    int mYear, mMonth, mDay;
    Calendar c;
   private String Cdate;
   private String Ctime;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bundle = new Bundle();
        completeTaskFragment = new CompleteTaskFragment();
        drawerLayout = view.findViewById(R.id.drawerLayout);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        arrayList = new ArrayList<HomeDataSet>();
        recyclerView = view.findViewById(R.id.recyclerView);
        drawerbutton = view.findViewById(R.id.ib_drawer);
        navigationView = view.findViewById(R.id.nav_view);
        View hView = navigationView.inflateHeaderView(R.layout.header);
        CircleImageView ImageView = (CircleImageView) hView.findViewById(R.id.profile_image);
        TextView tv = (TextView) hView.findViewById(R.id.nameText);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = sharedPrefs.getString("driver_name", "defaultValue");
        String image = sharedPrefs.getString("img", "defaultValue");
        driver_id = sharedPrefs.getString("driver_id", "defaultValue");
        currentLang = sharedPrefs.getString("currentLang", "defaultValue");
        currentLong = sharedPrefs.getString("currentLong", "defaultValue");
        Cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Ctime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.d("d",Cdate+ " and time  "+Ctime);
        tv.setText(name);
        Log.d("img", image);
        Picasso.get().load("http://x.joytechnologieslimited.com/" + image).fit().into(ImageView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        jsonCall jsonCall1 = new jsonCall();
        jsonCall1.execute("http://x.joytechnologieslimited.com/Api/orders/get_data.php?driver_id=" + driver_id+"&current_lat="+currentLang+"&current_lon="+currentLong+"&cdate="+Cdate+"&ctime="+Ctime);


        search = view.findViewById(R.id.search);
        et_date = view.findViewById(R.id.search_text);
        date = view.findViewById(R.id.iv_datePicker);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                int month = monthOfYear + 1; // month count given less one e.g. august then give month no 7

                                String dayStr = String.valueOf(dayOfMonth);
                                String monthStr = String.valueOf(month);

                                if (dayStr.length() == 1)
                                    dayStr = "0" + dayStr;

                                if (monthStr.length() == 1)
                                    monthStr = "0" + monthStr;

                                // dd/mm/yyyy format set.
                                //    et_date.setText(dayStr+"/"+monthStr+"/"+year);
                                et_date.setText(year + "-" + monthStr + "-" + dayStr);


                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NewApi")
            @Override
            public void onRefresh() {

                arrayList.clear();
                jsonCall jsonCall1 = new jsonCall();
                jsonCall1.execute("http://x.joytechnologieslimited.com/Api/orders/get_data.php?driver_id=" + driver_id+"&current_lat="+currentLang+"&current_lon="+currentLong+"&cdate="+Cdate+"&ctime="+Ctime);

            }
        });

        drawerbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!drawerLayout.isDrawerOpen(Gravity.START))
                    drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.START);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Searching", Toast.LENGTH_SHORT).show();
                String value = et_date.getText().toString();

                if (TextUtils.isEmpty(value)) {
                    Toast.makeText(getActivity(), "Please Write Something to Search", Toast.LENGTH_SHORT).show();
                } else {
                    arrayList.clear();
                    jsonCall jsonCall1 = new jsonCall();
                    jsonCall1.execute("http://x.joytechnologieslimited.com/Api/orders/get_datewise.php?driver_id=" + driver_id + "&date=" + value);

                }
            }
        });


        SetupNavMenu();
        return view;
    }


    private void SetupNavMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        SetFrame(new HomeFragment());
                        break;

                    case R.id.complete:

                        bundle.putString("status", "complete");
                        completeTaskFragment.setArguments(bundle);
                        SetFrame(completeTaskFragment);
                        break;

                    case R.id.reschedule:

                        bundle.putString("status", "reschedule");
                        completeTaskFragment.setArguments(bundle);
                        SetFrame(completeTaskFragment);
                        break;

                    case R.id.returned:

                        bundle.putString("status", "return");
                        completeTaskFragment.setArguments(bundle);
                        SetFrame(completeTaskFragment);
                        break;
                    case R.id.cancel:

                        bundle.putString("status", "cancel");
                        completeTaskFragment.setArguments(bundle);
                        SetFrame(completeTaskFragment);
                        break;


                    case R.id.logout:

                        Intent intent = new Intent(getActivity(), StarterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        break;


                }

                return false;
            }
        });
    }

    private void SetFrame(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack("my_fragment").commit();
    }

    public class jsonCall extends AsyncTask<String, Void, String> {

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String mainfile;
        String id;
        String driver_id;
        String delivery_id;
        String merchant_id;
        String status;
        String created_at;
        String rec_name;
        String rec_number;
        String rec_address;
        String rec_zone;
        String amount;
        String instruction;
        String merchant_name;
        String merchant_email;
        String merchant_phone;

        @Override
        protected void onPostExecute(String aVoid) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (arrayList.size() > 0) {
                homeDataAdapter = new HomeDataAdapter(getActivity(), arrayList);
                recyclerView.setAdapter(homeDataAdapter);
            } else {
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
        protected String doInBackground(String... strings) {

            String urls = strings[0];
            try {
                URL url = new URL(urls);


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

                    id = child.getString("id");
                    driver_id = child.getString("driver_id");
                    delivery_id = child.getString("delivery_id");
                    merchant_id = child.getString("merchant_id");
                    status = child.getString("status");
                    created_at = child.getString("created_at");
                    rec_name = child.getString("rec_name");
                    rec_number = child.getString("rec_number");
                    rec_address = child.getString("rec_address");
                    rec_zone = child.getString("rec_zone");
                    amount = child.getString("amount");
                    instruction = child.getString("instruction");
                    merchant_name = child.getString("merchant_name");
                    merchant_email = child.getString("merchant_email");
                    merchant_phone = child.getString("merchant_phone");
                    dlat = child.getString("current_lat");
                    dlog = child.getString("current_lon");

                    arrayList.add(new HomeDataSet(id, driver_id, delivery_id, merchant_id, status, created_at, rec_name, rec_number, rec_address, rec_zone, amount, instruction, merchant_name, merchant_email, merchant_phone,dlat,dlog));
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
