package joytechnologiesltd.com.joyexpressdeliveryservice.APICALL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class HomeDataAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    String value;
    private int count1st = 0;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    Context context;
    ArrayList<HomeDataSet> arrayList;
    String status;
    String delivery_id;


    public HomeDataAdapter(Context context, ArrayList<HomeDataSet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new HomeViewHolder(LayoutInflater.from((parent.getContext())).inflate(R.layout.home_row, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        final HomeDataSet data = arrayList.get(position);

        holder.status.setText(data.getStatus());
        holder.rec_name.setText(data.getRec_name());
        holder.rec_amount.setText(data.getAmount() + " BDT");
        holder.order_date.setText(data.getCreated_at());
        holder.order_id.setText(data.getDelivery_id());
        holder.address.setText(data.getRec_address());
        holder.merchant_name.setText("Merchant Name: " + data.getMerchant_name());
        holder.merchant_id.setText("Merchant Id : " + data.getMerchant_id());
        holder.zone.setText(data.getRec_zone());
        holder.instruction.setText(data.getInstruction());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count1st == 0) {
                    holder.linearLayout_details_card.setVisibility(View.VISIBLE);
                    holder.more_button.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                    count1st++;
                } else {
                    holder.linearLayout_details_card.setVisibility(View.GONE);

                    holder.more_button.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                    count1st = 0;

                }
            }
        });
        final String number = data.getRec_number();

        holder.call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPhonePermission()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    context.startActivity(callIntent);
                } else {

                    Toast.makeText(context, "Need Call Permission !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.compl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "complete";
                delivery_id = data.getDelivery_id();
                SetAlert("complete", data.getDriver_id());
            }
        });
        holder.cance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "cancel";
                delivery_id = data.getDelivery_id();
                SetAlert("cancel", data.getDriver_id());
            }
        });
        holder.ressche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "reschedule";
                delivery_id = data.getDelivery_id();
                SetAlert("reschedule", data.getDriver_id());
            }
        });
        holder.retu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "return";
                delivery_id = data.getDelivery_id();
                SetAlert("return", data.getDriver_id());
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public boolean checkPhonePermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(context)
                        .setTitle("Permission")
                        .setMessage("Please Access Internet")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.INTERNET},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public void SetAlert(final String status, String id) {
        //we will pass this status and driver id

        new AlertDialog.Builder(context)
                .setTitle("Alert !")
                .setMessage("Do You Really Want to " + status + " This Delivery Order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        new jsonCall().execute();
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();

    }

    public class jsonCall extends AsyncTask<Void, Void, String> {

        HttpURLConnection httpURLConnection;
        BufferedReader bufferedReader;
        String mainfile;

        @Override
        protected void onPostExecute(String aVoid) {

        }

        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL url = new URL("http://x.joytechnologieslimited.com/Api/status/status.php?delivery_id=" + delivery_id + "&status=" + status);


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


                return mainfile;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;


        }
    }
}
