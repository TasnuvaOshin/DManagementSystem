package joytechnologiesltd.com.joyexpressdeliveryservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import joytechnologiesltd.com.joyexpressdeliveryservice.Auth.AuthFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.Auth.SplashFragment;
import joytechnologiesltd.com.joyexpressdeliveryservice.HomePage.HomeFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.view.WindowManager;

public class StarterActivity extends AppCompatActivity {
    AuthFragment authFragment;
     ProgressDialog progressDialog;
    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starter);

        progressDialog = new ProgressDialog(StarterActivity.this);
        progressDialog.setTitle("No Internet");
        progressDialog.setMessage("Need Internet Service");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if(haveNetworkConnection()) {
            authFragment = new AuthFragment();

            //to set the page
            SetFrame(new SplashFragment());
        }else
        {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


        }
    }
    private void SetFrame(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack("my_fragment").commit();
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
