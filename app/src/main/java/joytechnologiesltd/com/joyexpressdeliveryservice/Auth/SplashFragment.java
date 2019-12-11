package joytechnologiesltd.com.joyexpressdeliveryservice.Auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Objects;

import androidx.fragment.app.FragmentTransaction;
import joytechnologiesltd.com.joyexpressdeliveryservice.R;


public class SplashFragment extends Fragment {
private ProgressBar progressBar;
private int progressStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_splash, container, false);


final Handler handler = new Handler();
progressBar = view.findViewById(R.id.progressBar1);
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100)
                {
                    progressStatus += 5;
                     handler.post(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setProgress(progressStatus);

                        }
                    });
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (progressStatus==100)
                {
                    SetFrame(new AuthFragment());
                }
            }
        }).start();
return view;
    }
    private void SetFrame(Fragment fragment) {

        @SuppressLint({"NewApi", "LocalSuppress"}) FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
