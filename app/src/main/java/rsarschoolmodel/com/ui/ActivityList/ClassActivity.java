package rsarschoolmodel.com.ui.ActivityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.adapter.GridViewClassAdapter;
import rsarschoolmodel.com.api.RetrofitAPIService;
import rsarschoolmodel.com.modelClass.request.ClassListRequestModel;
import rsarschoolmodel.com.modelClass.response.ClassListResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.AllStaticMethods;
import rsarschoolmodel.com.utilities.AppConstants;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;

public class ClassActivity extends AppCompatActivity {
    List<ClassListResponseModel.ClassDatum> classData = new ArrayList<ClassListResponseModel.ClassDatum>();
    GridView gvClass;
    ConnectionDetector cd;
    LinearLayout llBottomBg, llTopBg, llBackgroundDesign;
    ImageView imgSchoolLogo, imgLogout;
    TextView txtSchoolName;
    File schoollogo;
    File logobg;
    Boolean isInternetPresent = false;
    SharedPreferences preferences;
    String bgCode, topBgCode, buttonBg, schoolUi, schoolName, fdSchoolName, schoolNameColor, userEmail, userMobile;
    boolean doubleBackToExitPressedOnce;
    ImageView imgSchoolLogoBg, blank;
    TextView txtTittle;
    Context context;
    Toolbar toolbar;
    File dirDeleteFolder;
    LinearLayout llView;
    ProgressHUD customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.class_activity);

        context = ClassActivity.this;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        toolbar = findViewById(R.id.toolbarRegister);
        txtTittle = findViewById(R.id.txtTittle);
        imgLogout = findViewById(R.id.imgLogout);
        blank = findViewById(R.id.imgBlank);
        imgSchoolLogoBg = (ImageView) findViewById(R.id.Img_School_logo_bg);
        imgSchoolLogo = (ImageView) findViewById(R.id.Img_School_logo);
        llBottomBg = (LinearLayout) findViewById(R.id.Ln_Bottom_Bg);
        llTopBg = (LinearLayout) findViewById(R.id.llView);
        llBackgroundDesign = (LinearLayout) findViewById(R.id.llBackgroundDesign);
        txtSchoolName = (TextView) findViewById(R.id.Txt_School_Name);
        gvClass = (GridView) findViewById(R.id.gvClass);
        llView = (LinearLayout) findViewById(R.id.llView);

        getUserDetail();

        txtTittle.setText("CLASSES");
        imgLogout.setVisibility(View.VISIBLE);
        blank.setVisibility(View.GONE);
        txtTittle.setTextColor(Color.parseColor(schoolNameColor));
        imgLogout.setImageTintList(ColorStateList.valueOf(Color.parseColor(schoolNameColor)));
        toolbar.setBackgroundColor(Color.parseColor(topBgCode));
        txtSchoolName.setTextColor(Color.parseColor(schoolNameColor));
        llBottomBg.setBackgroundColor(Color.parseColor(bgCode));
        llTopBg.setBackgroundColor(Color.parseColor(topBgCode));
        llBackgroundDesign.setBackgroundColor(Color.parseColor(bgCode));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            schoollogo = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.RSARSchoolModel" + "/" + fdSchoolName + "/images/" + "school_logo.png");
            logobg = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.RSARSchoolModel" + "/" + fdSchoolName + "/images/" + "logo_bg.png");
        } else {
            schoollogo = new File(Environment.getExternalStorageDirectory() + fdSchoolName + "/images/" + "school_logo.png");
            logobg = new File(Environment.getExternalStorageDirectory() + "/images/" + "logo_bg.png");
        }

        Bitmap bitmap = BitmapFactory.decodeFile(logobg.getAbsolutePath());
        Bitmap bitsclogo = BitmapFactory.decodeFile(schoollogo.getAbsolutePath());
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(logobg.getAbsoluteFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapDrawable background = new BitmapDrawable(bitmap);
        imgSchoolLogoBg.setBackgroundDrawable(background);
        try {
            bitsclogo = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(schoollogo.getAbsoluteFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapDrawable Sclogo = new BitmapDrawable(bitsclogo);
        imgSchoolLogo.setBackground(Sclogo);
        txtSchoolName.setText(schoolName);

        if (isInternetPresent) {
            customProgressBar();
            callClassApi();
        }
        else
        {
            noInternetConnectionDialog();
        }

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("USER_APP_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                context.deleteDatabase("contactsManager");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    dirDeleteFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.RSARSchoolModel");
                } else {
                    dirDeleteFolder = new File(Environment.getExternalStorageDirectory() + "/.RSARSchoolModel");
                }
                if (dirDeleteFolder.isDirectory()) {
                    try {
                        FileUtils.deleteDirectory(dirDeleteFolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent(ClassActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private void callClassApi() {
        ClassListRequestModel classListRequestModel = new ClassListRequestModel(schoolUi, "Teacher", "class");

        RetrofitAPIService.getApiClient().getClassList(classListRequestModel).enqueue(new Callback<ClassListResponseModel>() {
            @Override
            public void onResponse(Call<ClassListResponseModel> call, retrofit2.Response<ClassListResponseModel> response) {

                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1) ) {
                            classData.clear();
                            classData.addAll(response.body().getClassData());
                            GridViewClassAdapter adapter = new GridViewClassAdapter(context, classData);
                            gvClass.setAdapter(adapter);
                            gvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String classId = classData.get(position).getClassId().toString();
                                    Intent intent = new Intent(context, SubjectListActivity.class);
                                    intent.putExtra("classId", classId);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            });

                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AllStaticMethods.saveException(e);
                }
            }

            @Override
            public void onFailure(Call<ClassListResponseModel> call, Throwable t) {
                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetail() {
        UserLoginResponseModel.UserDatum userData = (UserLoginResponseModel.UserDatum) SharedPreferenceManager.getUserData(context);

        schoolUi = userData.getSchoolUI();
        schoolName = userData.getSchoolName();
        fdSchoolName = userData.getSchoolFolderName();
        bgCode = userData.getBgCode();
        topBgCode = userData.getTopBgCode();
        buttonBg = userData.getButtonBgColor();
        schoolNameColor = userData.getSchoolNameColor();
        userEmail = userData.getEmail();
        userMobile = userData.getMobile();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void noInternetConnectionDialog(){
        final Dialog noInternetDialog = new Dialog(context);
        noInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noInternetDialog.setContentView(R.layout.alert_dialog);
        noInternetDialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        LinearLayout ln_outline = (LinearLayout) noInternetDialog.findViewById(R.id.dia_ln_outline);
        View view = (View) noInternetDialog.findViewById(R.id.dia_view);
        TextView Error_text = (TextView) noInternetDialog.findViewById(R.id.dia_error_title);
        TextView text = (TextView) noInternetDialog.findViewById(R.id.dia_error_msg);
        text.setText("Please Start your internet to load data.");
        Button btn_yes = (Button) noInternetDialog.findViewById(R.id.dia_b_yes);
        ln_outline.setBackgroundColor(Color.parseColor(bgCode));
        view.setBackgroundColor(Color.parseColor(bgCode));
        Error_text.setTextColor(Color.parseColor(bgCode));
        btn_yes.setBackgroundColor(Color.parseColor(bgCode));
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noInternetDialog.dismiss();
            }
        });
        noInternetDialog.show();
    }

    private void customProgressBar() {
        String message = "Please Wait....";
        customProgressBar = new ProgressHUD(context, R.style.ProgressHUD);
        customProgressBar.setTitle("");
        customProgressBar.setContentView(R.layout.progress_hudd);
        if (message == null || message.length() == 0) {
            customProgressBar.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) customProgressBar.findViewById(R.id.message);
            txt.setText(message);
        }
        customProgressBar.setCancelable(false);
        customProgressBar.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = customProgressBar.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        customProgressBar.getWindow().setAttributes(lp);
        customProgressBar.show();
    }


}
