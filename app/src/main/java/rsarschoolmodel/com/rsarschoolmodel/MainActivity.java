package rsarschoolmodel.com.rsarschoolmodel;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.Misc;
import rsarschoolmodel.com.Common.Networking;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.Common.SetterGetter_Sub_Chap;
import rsarschoolmodel.com.ui.ActivityList.UnzipUtil;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{ Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.CAMERA};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model;
    ProgressHUD dialog;
    String message = "Please Wait....";
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    EditText Et_Activation_Code,Et_Stu_Name,Et_Email,Et_Mob_no;
    String Str_activation_Code,Str_Stu_Name,Str_Mobile,Str_Email_Id,Str_Msg,Str_School_UI,Str_Status,Str_School_name,Str_Fd_School_name,Str_School_Path,Str_Otp_Value;
    String Str_Bg_Code,Str_Top_Bg_Code,Str_Button_Bg,Str_Act_Status,Pref_School_UI,Str_Restric_ID,Str_Sch_Name_Color,Str_Otp_value,Pre_Email_Id,Pre_Mob_No;
    Button Btn_Submit;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    private ProgressDialog mProgressDialog;
    String unzipLocation ;
    String zipFile ;
    public static String Value;
    boolean deleted_zip;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    CardView cardView;
    int sVersionCode;
    Spinner Spin;
    LinearLayout Lnr_Reg_Class;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    JSONArray DropClass_Data;
    SetterGetter_Sub_Chap vinsgetter;
    String Details,DClass_ID,RadioUserDetail;
    private ArrayList<String> DClass_Name;
    private RadioGroup radioUserGroup;
    private RadioButton radioUserButton;
    TextView text11,privacy,about,forget_details;
    Dialog otpdialog;
    String scode,sname,semail,smob,s10,s11,s12,s13,getemail;
    Intent getintents;
    ProgressHUD dialogoo;
    EditText ettext;
    Context context;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        getintents=getIntent();
        //  FirebaseApp.initializeApp(MainActivity.this);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Str_Otp_Value = preferences.getString("Rsar_Otp_Value", "");
        if(!Pref_School_UI.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ClassNew.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        ButtonsDetails();
        GetDevicedetails();
        checkAndroidPermission();
        privacy=findViewById(R.id.txtPrivacyPolicy);
        about=findViewById(R.id.txtAboutUs);
        forget_details=findViewById(R.id.txtForgets);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, WebViewPrivacy.class);
                startActivity(i);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, WebViewAbout.class);
                startActivity(i);
            }
        });
        forget_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otpdialog = new Dialog(MainActivity.this);
                otpdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                otpdialog.setContentView(R.layout.forget);
                otpdialog.setCancelable(false);

                LinearLayout ln_outline = (LinearLayout) otpdialog.findViewById(R.id.dia_ln_outline1);
                View view1 = (View) otpdialog.findViewById(R.id.dia_view1);
                TextView Error_text = (TextView) otpdialog.findViewById(R.id.dia_error_title1);
                text11 =  otpdialog.findViewById(R.id.textwrittn1);
                ettext = (EditText) otpdialog.findViewById(R.id.dia_error_msg1);
                getemail=ettext.getText().toString();

                Button btn_yes = (Button) otpdialog.findViewById(R.id.dia_b_yes1);
                Button cencel = (Button) otpdialog.findViewById(R.id.cancels1);

                cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otpdialog.dismiss();
                    }
                });

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Str_Otp= ettext.getText().toString().trim();
                        if (ettext.getText().toString().trim().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please Enter EmailID", Toast.LENGTH_LONG).show();
                        } else {
                            getemail=ettext.getText().toString();
                            Log.d("Responseeee333", getemail);
                            SendMsg();

                            String message = "Please Wait....";
                            dialogoo = new ProgressHUD(MainActivity.this, R.style.ProgressHUD);
                            dialogoo.setTitle("");
                            dialogoo.setContentView(R.layout.progress_hudd);
                            if (message == null || message.length() == 0) {
                                dialogoo.findViewById(R.id.message).setVisibility(View.GONE);
                            } else {
                                TextView txt = (TextView) dialogoo.findViewById(R.id.message);
                                txt.setText(message);
                            }
                            dialogoo.setCancelable(true);

                            dialogoo.getWindow().getAttributes().gravity = Gravity.CENTER;
                            WindowManager.LayoutParams lp = dialogoo.getWindow().getAttributes();
                            lp.dimAmount = 0.2f;
                            dialogoo.getWindow().setAttributes(lp);

                            dialogoo.show();
                        }
                        //dialogss.dismiss();
                    }

                });
                otpdialog.show();
            }
        });
    }
    public void SendMsg() {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String urlmanual = Networking.url+"forgotDetails.php?";

        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        //Toast.makeText(MainActivity.this, "Please Check your Email", Toast.LENGTH_LONG).show();
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;	array = new JSONArray(response);

                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status= object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();

                                System.out.println("messsssss"+"  "+Str_Msg);
                                if(Str_Status.equalsIgnoreCase("True")) {
                                    preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                                    editor= preferences.edit();
                                    editor.putString("Rsar_Otp_Value", "False");
                                    editor.commit();
                                    text11.setText("Please Check your Email");
                                    Toast.makeText(MainActivity.this, "Please Check your Email", Toast.LENGTH_LONG).show();
                                    //CallSubject();
                                    // otpdialog.dismiss();
                                }
                                else {
                                    text11.setText("Not registered with us");
                                    otpdialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Not registered with us", Toast.LENGTH_LONG).show();
                                    Log.d("ttttt33",Str_Msg);
                                }
                                if (dialogoo.isShowing()) {
                                    dialogoo.dismiss();
                                }
                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Log.d("LOGCAT", "" + error.toString());

                        } catch (Exception e) {

                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("School_UI", "MTc=");
                params.put("email", getemail);
                params.put("action", "forgot");

                // params.put("action", "forgot");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void GetDevicedetails() {

        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Mob_Id = Build.ID;
        Mob_Product= Build.PRODUCT;
        Mob_Brand= Build.BRAND;
        Mob_Manufacture= Build.MANUFACTURER;
        Mob_Model = Build.MODEL;
    }
    private void ButtonsDetails() {

        Et_Activation_Code=(EditText)findViewById(R.id.edtActivationCode);
        Et_Stu_Name=(EditText)findViewById(R.id.edtName);
        Et_Email=(EditText)findViewById(R.id.edtEmailId);
        Et_Mob_no=(EditText)findViewById(R.id.edtMobileNumber);
        cardView =findViewById(R.id.cardLogo);
        imageView =findViewById(R.id.imgLogout);
//        Spin= findViewById(R.id.spinner);

        cardView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        scode=Et_Activation_Code.getText().toString();
        sname=Et_Stu_Name.getText().toString();
        semail=Et_Email.getText().toString();
        smob=Et_Mob_no.getText().toString();

        s10=getintents.getStringExtra("pcode");
        s11=getintents.getStringExtra("pname");
        s12=getintents.getStringExtra("pemail");
        s13=getintents.getStringExtra("pmob");

        Et_Activation_Code.setText(s10);
        Et_Stu_Name.setText(s11);
        Et_Email.setText(s12);
        Et_Mob_no.setText(s13);

        //Toast.makeText(this, "n="+s11+"e="+s12+"m="+s13+"", Toast.LENGTH_SHORT).show();
        //  Spin.setOnItemSelectedListener(this);
//        Lnr_Reg_Class=(LinearLayout)findViewById(R.id.Reg_Class);
        Btn_Submit =(Button)findViewById(R.id.btnSubmit);
        Btn_Submit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == Btn_Submit) {
            checkAndroidPermission1();
        }
    }
    private void ActivationCodeURL() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"activation.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                Str_Status=object.getString("Status");
                                Str_Msg =object.getString("Message");
                                System.out.println("abbb"+"    "+Str_Status);
                                if(Str_Status.equalsIgnoreCase("True")) {
                                    Str_School_UI = object.getString("School_UI");
                                    Str_School_name = object.getString("School_Name");
                                    Str_Fd_School_name = object.getString("School_Folder_Name");
                                    Str_School_Path = object.getString("School_Folder_Path");
                                    Str_Bg_Code = object.getString("Bg_Code");
                                    Str_Top_Bg_Code = object.getString("Top_BgCode");
                                    Str_Button_Bg = object.getString("Button_Bg_Color");
                                    Str_Act_Status = object.getString("Activation_Status");
                                    Str_Restric_ID = object.getString("Restrict_SD");
                                    Str_Sch_Name_Color = object.getString("School_Name_Color");
                                    Str_Otp_value=object.getString("OTP_Popup");
                                    Pre_Email_Id = object.getString("Email");
                                    Pre_Mob_No= object.getString("Mobile");
                                    System.out.println("fuckoff"+"    "+object.getString("School_UI"));
                                    preferences = getApplicationContext().getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("Rsar_School_UI",  Str_School_UI);
                                    editor.putString("Rsar_School_Name",  Str_School_name);
                                    editor.putString("Rsar_Fd_School_Name",  Str_Fd_School_name);
                                    editor.putString("Rsar_Bg_Code",  Str_Bg_Code);
                                    editor.putString("Rsar_Top_Bg_Code",  Str_Top_Bg_Code);
                                    editor.putString("Rsar_Button_Bg",  Str_Button_Bg);
                                    editor.putString("Rsar_Act_Status",  Str_Act_Status);
                                    editor.putString("Rsar_Restric_ID",  Str_Restric_ID);
                                    editor.putString("Rsar_Sch_Bgcol_Name",  Str_Sch_Name_Color);
                                    editor.putString("Rsar_Otp_Value",  Str_Otp_value);
                                    editor.putString("Rsar_Email_id",  Str_Otp_value);
                                    editor.putString("Rsar_Pref_Email",  Pre_Email_Id);
                                    editor.putString("Rsar_Pref_Mobile",  Pre_Mob_No);
                                    // Check in Splash screen the Value True
                                    editor.apply();

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                        unzipLocation =  context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/.RSARSchoolModel"+"/";
                                    }else{
                                        unzipLocation =  Environment.getExternalStorageDirectory()+"/.RSARSchoolModel"+"/";
                                    }

                                    Value=null;
                                    DownloadMapAsync mew = new DownloadMapAsync();
                                    mew.execute(Str_School_Path);

                                    Value=Str_Fd_School_name+".zip";
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                        zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+Str_Fd_School_name+".zip";
                                    }else{
                                        zipFile = Environment.getExternalStorageDirectory()+"/"+Str_Fd_School_name+".zip";
                                    }

                                }else
                                {

                                    final Dialog dialoga = new Dialog(MainActivity.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.newdialog);
                                    dialoga.setCancelable(true);

                                    TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
                                    text.setText(Str_Msg);

                                    Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button);
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();
                                        }
                                    });
                                    dialoga.show();
                                    System.out.println("fuckoff"+"    "+Str_Status);
                                }
                            }

                            if (dialog.isShowing())
                                dialog.dismiss();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                System.out.println("accccc"+"  "+sVersionCode+" "+sVersionName+" "+PACKAGE_NAME);
                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("activation_code", Str_activation_Code);
                params.put("mobile", Str_Mobile);
                params.put("email", Str_Email_Id);
                params.put("name", Str_Stu_Name);
                params.put("mid", Mob_Id);
                params.put("mproduct", Mob_Product);
                params.put("mbrand", Mob_Brand);
                params.put("mmanufacture", Mob_Manufacture);
                params.put("mmodel", Mob_Model);
                params.put("mdid", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                return params;
            }
        };
        queue.add(postRequest);

    }
    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result ="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
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
                if(lenghtOfFile == 0)
                {
                    showAlertDialog(context, "Error In Internet Connection", "You don't have proper internet connection.", false);
                }
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
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
            Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            if(result.equalsIgnoreCase("true")){
                try {
                    unzip();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else{

            }
        }
    }
    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(MainActivity.this);
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
                for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }
                System.out.println("saaaaaaa"+"     "+unzipLocation);
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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                filev = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Str_Fd_School_name+".zip");
            }else{
                filev = new File(Environment.getExternalStorageDirectory(), Str_Fd_School_name+".zip");
            }
            sname=Et_Stu_Name.getText().toString();
            scode=Et_Activation_Code.getText().toString();
            deleted_zip = filev.delete();
            Intent intent = new Intent(MainActivity.this, ClassNew.class);
            intent.putExtra("code",scode);
            intent.putExtra("name",sname );
            intent.putExtra("email", Pre_Email_Id);
            intent.putExtra("mobile", Pre_Mob_No);
            startActivity(intent);
            MainActivity.this.finish();

            System.out.println("dfdfdfdfdddddd"+"   "+filev);
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
        }}

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void checkAndroidPermission() {
        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestStorageAndCameraPermission("111");

            }
        } else
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestStorageAndCameraPermission("112");

            }
        }
    }
    public void requestStorageAndCameraPermission( String code){
        if (code.matches("111")){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},1234);

        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.CAMERA},1234);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1234) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                int version = Build.VERSION.SDK_INT;
                if( version <= 32 ) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        SharedPreferences.Editor editor = permissionStatus.edit();
                        editor.putBoolean(permissionsRequired[0], true);
                        editor.apply();
                    }
                } else
                {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
                    {
                        SharedPreferences.Editor editor = permissionStatus.edit();
                        editor.putBoolean(permissionsRequired[0], true);
                        editor.apply();
                    }
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void checkAndroidPermission1() {
        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
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
            }else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
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
            }
            else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                startSubmitActivity();
            }
        } else
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
            {
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
            }else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
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
            }
            else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                startSubmitActivity();
            }
        }
    }
    private void startSubmitActivity() {
        if (Et_Activation_Code.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Activation Code.", Toast.LENGTH_SHORT).show();
        }
        if (Et_Stu_Name.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Name.", Toast.LENGTH_SHORT).show();
        }
        if (Et_Email.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Email Id.", Toast.LENGTH_SHORT).show();
        }
        if (Et_Mob_no.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter the Mobile No.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Str_activation_Code = Et_Activation_Code.getText().toString().trim();
            Str_Stu_Name = Et_Stu_Name.getText().toString().trim();
            Str_Mobile = Et_Mob_no.getText().toString().trim();
            Str_Email_Id = Et_Email.getText().toString().trim();
            if (isInternetPresent) {
                ActivationCodeURL();
                String message = "Please Wait....";
                dialog = new ProgressHUD(MainActivity.this, R.style.ProgressHUD);
                dialog.setTitle("");
                dialog.setContentView(R.layout.progress_hudd);
                if (message == null || message.length() == 0) {
                    dialog.findViewById(R.id.message).setVisibility(View.GONE);
                } else {
                    TextView txt = (TextView) dialog.findViewById(R.id.message);
                    txt.setText(message);
                }
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.dimAmount = 0.2f;
                dialog.getWindow().setAttributes(lp);
                dialog.show();
            } else {
                Misc.showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have internet connection.", false);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}

