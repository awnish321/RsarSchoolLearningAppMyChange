package rsarschoolmodel.com.ui.ActivityList;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import retrofit2.Call;
import retrofit2.Callback;
import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.Misc;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.api.RetrofitAPIService;
import rsarschoolmodel.com.modelClass.request.ForgetDetailRequestModel;
import rsarschoolmodel.com.modelClass.request.OtpSendRequestModel;
import rsarschoolmodel.com.modelClass.request.OtpVerifyRequestModel;
import rsarschoolmodel.com.modelClass.request.UserDeviceDetailRequestModel;
import rsarschoolmodel.com.modelClass.request.UserLoginRequestModel;
import rsarschoolmodel.com.modelClass.response.ForgetDetailResponseModel;
import rsarschoolmodel.com.modelClass.response.OtpSendResponseModel;
import rsarschoolmodel.com.modelClass.response.OtpVerifyResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.rsarschoolmodel.WebViewAbout;
import rsarschoolmodel.com.rsarschoolmodel.WebViewPrivacy;
import rsarschoolmodel.com.utilities.AllStaticFields;
import rsarschoolmodel.com.utilities.AllStaticMethods;
import rsarschoolmodel.com.utilities.AppConstants;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;
import rsarschoolmodel.database.DBHandlerClass;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    ProgressHUD customProgressBar;
    SharedPreferences preferences;
    EditText etActivationCode, etStuName, etEmail, etMobNo ,ettext;
    String deviceId, mobId, mobProduct, mobBrand, mobManufacture, mobModel,unzipLocation,zipFile;
    String activationCode, userName, userMobile, userEmailId, fdSchoolName, schoolFolderPath;
    Button btnSubmit;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    private ProgressDialog mProgressDialog;
    public static String Value;
    boolean deleted_zip;
    CardView cardView;
    TextView text11, privacy, about, forget_details;
    Dialog otpdialog;
    Dialog forgetDetailDialog;
    Context context;
    ImageView help, support, blank, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (SharedPreferenceManager.getUserDeviceDetail(context) == null) {
            saveDeviceDetail();
        } else {
            GetDeviceDetails();
        }

        etActivationCode = (EditText) findViewById(R.id.edtActivationCode);
        etStuName = (EditText) findViewById(R.id.edtName);
        etEmail = (EditText) findViewById(R.id.edtEmailId);
        etMobNo = (EditText) findViewById(R.id.edtMobileNumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        cardView = findViewById(R.id.cardLogo);
        help = findViewById(R.id.imgHelp);
        support = findViewById(R.id.imgSupport);
        blank = findViewById(R.id.imgBlank);
        privacy = findViewById(R.id.txtPrivacyPolicy);
        about = findViewById(R.id.txtAboutUs);
        forget_details = findViewById(R.id.txtForgets);

        cardView.setVisibility(View.GONE);
        blank.setVisibility(View.GONE);
        support.setVisibility(View.VISIBLE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndroidPermission();
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ContactUsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, WebViewPrivacy.class);
                startActivity(i);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, WebViewAbout.class);
                startActivity(i);
            }
        });
        forget_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgetDetailDialog = new Dialog(LoginActivity.this);
                forgetDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                forgetDetailDialog.setContentView(R.layout.forget);
                forgetDetailDialog.setCancelable(false);
                LinearLayout ln_outline = (LinearLayout) forgetDetailDialog.findViewById(R.id.dia_ln_outline1);
                View view1 = (View) forgetDetailDialog.findViewById(R.id.dia_view1);
                TextView Error_text = (TextView) forgetDetailDialog.findViewById(R.id.dia_error_title1);
                text11 = forgetDetailDialog.findViewById(R.id.textwrittn1);
                ettext = (EditText) forgetDetailDialog.findViewById(R.id.dia_error_msg1);
                Button btn_yes = (Button) forgetDetailDialog.findViewById(R.id.dia_b_yes1);
                Button cencel = (Button) forgetDetailDialog.findViewById(R.id.cancels1);
                cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forgetDetailDialog.dismiss();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ettext.getText().toString().trim().isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Enter EmailID", Toast.LENGTH_LONG).show();
                        } else {
                            callForgetDetailApi(ettext.getText().toString().trim());
                            customProgressBar();
                        }
                    }
                });
                forgetDetailDialog.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(zipFile);
                if (lenghtOfFile == 0) {
                    Misc.showAlertDialog(LoginActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                }
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.close();
                input.close();
                result = "true";
            } catch (Exception e) {
                result = "false";
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
                try {
                    unzip();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(zipFile, unzipLocation);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];
            File archive = new File(filePath);
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }
                UnzipUtil d = new UnzipUtil(zipFile, unzipLocation);
                d.unzip();

            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            File filev;
            ///button hide and show
            //Toast.makeText(MainActivity.this, "Downloading completed...", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                filev = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fdSchoolName + ".zip");
            } else {
                filev = new File(Environment.getExternalStorageDirectory(), fdSchoolName + ".zip");
            }
            deleted_zip = filev.delete();

            Intent intent = new Intent(LoginActivity.this, ClassActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }
            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            } finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    public void checkAndroidPermission() {
        int version = Build.VERSION.SDK_INT;
        if (version <= 32) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            } else {
                startSubmitActivity();
            }
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            } else {
                startSubmitActivity();
            }
        }
    }

    private void startSubmitActivity() {
        if (etActivationCode.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Activation Code.", Toast.LENGTH_SHORT).show();
        }
        if (etStuName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Name.", Toast.LENGTH_SHORT).show();
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Email Id.", Toast.LENGTH_SHORT).show();
        }
        if (etMobNo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Mobile No.", Toast.LENGTH_SHORT).show();
        } else {
            activationCode = etActivationCode.getText().toString().trim();
            userName = etStuName.getText().toString().trim();
            userMobile = etMobNo.getText().toString().trim();
            userEmailId = etEmail.getText().toString().trim();
            if (isInternetPresent) {
//                ActivationCodeURL();
                callActivationApi();
                customProgressBar();
            } else {
                Misc.showAlertDialog(LoginActivity.this, "No Internet Connection", "You don't have internet connection.", false);
            }
        }
    }

    private void saveSchoolImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            unzipLocation = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.RSARSchoolModel" + "/";
        } else {
            unzipLocation = Environment.getExternalStorageDirectory() + "/.RSARSchoolModel" + "/";
        }

        Value = null;
        DownloadMapAsync mew = new DownloadMapAsync();
        mew.execute(schoolFolderPath);
        Value = fdSchoolName + ".zip";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + fdSchoolName + ".zip";
        } else {
            zipFile = Environment.getExternalStorageDirectory() + "/" + fdSchoolName + ".zip";
        }
    }

    private void callActivationApi() {

        UserLoginRequestModel userLoginRequestModel = new UserLoginRequestModel(activationCode, userName, userEmailId, userMobile,
                "Teacher", "2", "rsarslapp", deviceId, mobId, mobProduct, mobBrand, mobManufacture, mobModel, "login");
        RetrofitAPIService.getApiClient().loginUser(userLoginRequestModel).enqueue(new Callback<UserLoginResponseModel>() {
            @Override
            public void onResponse(Call<UserLoginResponseModel> call, retrofit2.Response<UserLoginResponseModel> response) {

                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1) && response.body().getOtpVerifyStatus().equals(1)) {
                            SharedPreferenceManager.setUserData(context, response.body().getUserData().get(0));
                            AllStaticFields.userData = response.body().getUserData().get(0);
                            fdSchoolName =response.body().getUserData().get(0).getSchoolFolderName();
                            schoolFolderPath =response.body().getUserData().get(0).getSchoolFolderPath();
                            saveSchoolImage();
                        } else {
                            callOtpDialog(userEmailId);
                        }
                    } else {
                        Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AllStaticMethods.saveException(e);
                    Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserLoginResponseModel> call, Throwable t) {
                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callOtpVerifyApi(String userEmailId, String otp) {

        OtpVerifyRequestModel otpVerifyRequestModel = new OtpVerifyRequestModel(userEmailId, otp, "otpVerify");

        RetrofitAPIService.getApiClient().otpVerify(otpVerifyRequestModel).enqueue(new Callback<OtpVerifyResponseModel>() {
            @Override
            public void onResponse(Call<OtpVerifyResponseModel> call, retrofit2.Response<OtpVerifyResponseModel> response) {

                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            SharedPreferenceManager.setNewUserData(context, response.body().getUserData().get(0));
                            fdSchoolName =response.body().getUserData().get(0).getSchoolFolderName();
                            schoolFolderPath =response.body().getUserData().get(0).getSchoolFolderPath();
                            saveSchoolImage();
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AllStaticMethods.saveException(e);
                    Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerifyResponseModel> call, Throwable t) {
                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callResendOtpApi(String email) {
        OtpSendRequestModel otpSendRequestModel = new OtpSendRequestModel(email, "sendOtp");

        RetrofitAPIService.getApiClient().otpSend(otpSendRequestModel).enqueue(new Callback<OtpSendResponseModel>() {
            @Override
            public void onResponse(Call<OtpSendResponseModel> call, retrofit2.Response<OtpSendResponseModel> response) {

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            Toast.makeText(context, "Otp resend successfully please check your email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AllStaticMethods.saveException(e);
                    Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpSendResponseModel> call, Throwable t) {
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDeviceDetails() {
        UserDeviceDetailRequestModel UserDeviceDetailRequestModel = (UserDeviceDetailRequestModel) SharedPreferenceManager.getUserDeviceDetail(context);
        deviceId = UserDeviceDetailRequestModel.getDeviceId();
        mobId = UserDeviceDetailRequestModel.getMobId();
        mobProduct = UserDeviceDetailRequestModel.getMobProduct();
        mobBrand = UserDeviceDetailRequestModel.getMobBrand();
        mobManufacture = UserDeviceDetailRequestModel.getMobManufacture();
        mobModel = UserDeviceDetailRequestModel.getMobModel();
    }

    private void saveDeviceDetail() {
        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Mob_Id = Build.ID;
        Mob_Product = Build.PRODUCT;
        Mob_Brand = Build.BRAND;
        Mob_Manufacture = Build.MANUFACTURER;
        Mob_Model = Build.MODEL;

        UserDeviceDetailRequestModel userDeviceDetailRequestModel = new UserDeviceDetailRequestModel(Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model);
        SharedPreferenceManager.setUserDeviceDetail(context, userDeviceDetailRequestModel);
    }

    private void callOtpDialog(String userEmailId) {
        DBHandlerClass.TABLE_NAME = "main_" + "1";
        otpdialog = new Dialog(LoginActivity.this);
        otpdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpdialog.setContentView(R.layout.otp_dialog);
        otpdialog.setCancelable(false);
        LinearLayout ln_outline = (LinearLayout) otpdialog.findViewById(R.id.dia_ln_outline);
        View view = (View) otpdialog.findViewById(R.id.dia_view);
        TextView Error_text = (TextView) otpdialog.findViewById(R.id.dia_error_title);
        ettext = (EditText) otpdialog.findViewById(R.id.dia_error_msg);
        Button btn_yes = (Button) otpdialog.findViewById(R.id.dia_b_yes);
        Button cancel = (Button) otpdialog.findViewById(R.id.cancels);
        refresh = otpdialog.findViewById(R.id.refresh);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                otpdialog.dismiss();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callResendOtpApi(userEmailId);
//                    OtpRefresh();
                Toast.makeText(LoginActivity.this, "Refreshing.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ettext.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter OTP .", Toast.LENGTH_LONG).show();
                } else {
//                        OtpVerify();
                    callOtpVerifyApi(userEmailId, ettext.getText().toString().trim());
                    customProgressBar();
                }
            }

        });
        otpdialog.show();

    }

    private void callForgetDetailApi(String userEmailId) {
        ForgetDetailRequestModel forgetDetailRequestModel = new ForgetDetailRequestModel(userEmailId, "forgotDetails");

        RetrofitAPIService.getApiClient().forgotDetails(forgetDetailRequestModel).enqueue(new Callback<ForgetDetailResponseModel>() {
            @Override
            public void onResponse(Call<ForgetDetailResponseModel> call, retrofit2.Response<ForgetDetailResponseModel> response) {

                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();
                if (forgetDetailDialog.isShowing())
                    forgetDetailDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            Toast.makeText(context, "Please check your mail", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AllStaticMethods.saveException(e);
                    Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgetDetailResponseModel> call, Throwable t) {
                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();

                if (forgetDetailDialog.isShowing())
                    forgetDetailDialog.dismiss();
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void customProgressBar() {
        String message = "Please Wait....";
        customProgressBar = new ProgressHUD(LoginActivity.this, R.style.ProgressHUD);
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

