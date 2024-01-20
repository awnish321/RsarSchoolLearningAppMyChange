package rsarschoolmodel.com.ui.ActivityList;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import rsarschoolmodel.com.modelClass.request.UserDeviceDetailRequestModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int TIME = 4 * 1000;// 4 seconds
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        context = SplashScreenActivity.this;

        if (SharedPreferenceManager.getUserDeviceDetail(context) == null) {
            GetDeviceDetails();
        }

        UserLoginResponseModel.UserDatum userData = (UserLoginResponseModel.UserDatum) SharedPreferenceManager.getUserData(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if (userData!=null) {
                    intent = new Intent(SplashScreenActivity.this, ClassActivity.class);
                } else {
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                SplashScreenActivity.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, TIME);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    private void GetDeviceDetails() {
        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Mob_Id = Build.ID;
        Mob_Product = Build.PRODUCT;
        Mob_Brand = Build.BRAND;
        Mob_Manufacture = Build.MANUFACTURER;
        Mob_Model = Build.MODEL;

        UserDeviceDetailRequestModel userDeviceDetailRequestModel = new UserDeviceDetailRequestModel(Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model);
        SharedPreferenceManager.setUserDeviceDetail(context, userDeviceDetailRequestModel);
    }

}
