package rsarschoolmodel.com.rsarschoolmodel;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.GridViewAdapterChap;
import rsarschoolmodel.com.Common.GridViewAdapterClass;
import rsarschoolmodel.com.Common.Misc;
import rsarschoolmodel.com.Common.Networking;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.Common.SetterGetter_Sub_Chap;
import rsarschoolmodel.database.DBHandlerClass;


public class ClassNew extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabaseClass;
    DBHandlerClass dbHandlerClass;
    Cursor cursor;
    ArrayList<String> Class_ID_Array;
    ArrayList<String> Class_NAME_Array;
    ArrayList<String> GridViewClickItemArray = new ArrayList<String>();
    GridView gv_subcat;
    GridViewAdapterClass adapter;
    ProgressHUD dialog;
    ProgressHUD dialogoo;
    String message = "Please Wait....";
    ProgressDialog progressDialog;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    LinearLayout ln_logo_bg, ln_bg, ln_top_bg;
    ImageView Img_Sch_Logo;
    TextView Txt_School_Name;
    EditText ettext;
    Dialog otpdialog;
    File schoollogo;
    File logobg;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name,
            Pref_Fd_School_name, Pref_Class_List, Pref_Restric_Id, Pref_Sch_Name_Color, Str_Notify_Msg_Link, Pref_Email, Pref_Mob;
    SetterGetter_Sub_Chap vinsgetter;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    String Str_Status, Str_Msg, Details, Class_Name, Class_Id, Str_Otp_Value, Str_Otp;
    JSONArray Class_Data;
    boolean doubleBackToExitPressedOnce;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model, Db_Main_SUId;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;

    Intent intent_get;
    String vcode,vname, vemail, vmob;
    ImageView imageViewlog,refresh;
    TextView ty1, ty2, ty3;
    boolean vishu = false;
    String vishu1 = "boolKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.class_activity);
        Log.d("okkkk", "Welcome");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myNotifications", "myNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg;
                        if (task.isSuccessful()) {
                            msg = "okk";
                        } else {
                            msg = "fail";
                        }
                        // Toast.makeText(ClassNew.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        // gv_subcat=(GridView)findViewById(R.id.Gv_subcat);

        preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Pref_School_name = preferences.getString("Rsar_School_Name", "");
        Pref_Fd_School_name = preferences.getString("Rsar_Fd_School_Name", "");
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
        Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
        Pref_Class_List = preferences.getString("Rsar_Class_Arraylst", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");
        Pref_Sch_Name_Color = preferences.getString("Rsar_Sch_Bgcol_Name", "");
        Str_Otp_Value = preferences.getString("Rsar_Otp_Value", "");//Rsar_Otp_Value
        Pref_Email = preferences.getString("Rsar_Pref_Email", "");
        Pref_Mob= preferences.getString("Rsar_Pref_Mobile", "");
        System.out.println("Dooooo"+Str_Otp_Value);
        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        //  Pref_Top_Bg_Code ="#138ab2";
        //  Pref_Bg_Code="#053a5a";
        //GetAndroidPermission();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            schoollogo= new  File("/storage/emulated/0/Android/data/rsarschoolmodel.com.rsarschoolmodel/files/Documents/"+Pref_Fd_School_name+"/images/"+"school_logo.png");
            logobg= new  File("/storage/emulated/0/Android/data/rsarschoolmodel.com.rsarschoolmodel/files/Documents/"+Pref_Fd_School_name+"/images/"+"logo_bg.png");
        }else{
            schoollogo= new  File("/sdcard/.RSARSchoolModel/"+Pref_Fd_School_name+"/images/"+"school_logo.png");
            logobg= new  File("/sdcard/.RSARSchoolModel/"+Pref_Fd_School_name+"/images/"+"logo_bg.png");
        }

        GetDevicedetails();
        Call_Button();
        intent_get = getIntent();
        vcode = intent_get.getStringExtra("code");
        vname = intent_get.getStringExtra("name");
        vemail = intent_get.getStringExtra("email");
        vmob = intent_get.getStringExtra("mobile");
        //   Toast.makeText(ClassNew.this, "n="+vname+"e="+vemail+"m="+vmob+"", Toast.LENGTH_SHORT).show();
        imageViewlog = findViewById(R.id.logout1);
        imageViewlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(ClassNew.this, MainActivity.class);
                i.putExtra("pcode", vcode);
                i.putExtra("pname", vname);
                i.putExtra("pemail", vemail);
                i.putExtra("pmob", vmob);
                startActivity(i);
                //Toast.makeText(ClassNew.this, "n="+vname+"e="+vemail+"m="+vmob+"", Toast.LENGTH_SHORT).show();
            }
        });
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0
        System.out.println("details version"+"  "+sVersionCode+" "+sVersionName+" "+PACKAGE_NAME);
        if(Str_Otp_Value.equalsIgnoreCase("True"))
        {
            DBHandlerClass.TABLE_NAME="main_"+"1";
            System.out.println("Database bao"+"  "+"1"+"  "+DBHandlerClass.TABLE_NAME);
            otpdialog = new Dialog(ClassNew.this);
            otpdialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
            otpdialog.setContentView(R.layout.otp_dialog);
            otpdialog.setCancelable(false);
            // set the custom dialog components - text, image and button
            LinearLayout ln_outline=(LinearLayout)otpdialog.findViewById(R.id.dia_ln_outline);
            View view=(View) otpdialog.findViewById(R.id.dia_view);
            TextView Error_text = (TextView) otpdialog.findViewById(R.id.dia_error_title);
            ettext = (EditText) otpdialog.findViewById(R.id.dia_error_msg);
            Button btn_yes = (Button) otpdialog.findViewById(R.id.dia_b_yes);
            ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
            btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            Button cancel = (Button) otpdialog.findViewById(R.id.cancels);
            refresh=otpdialog.findViewById(R.id.refresh);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent i = new Intent(ClassNew.this, MainActivity.class);
                    i.putExtra("pname", vname);
                    i.putExtra("pemail", vemail);
                    i.putExtra("pmob", vmob);
                    startActivity(i);
                }
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OtpRefresh();
                    Toast.makeText(ClassNew
                            .this, "Refreshing....Wait", Toast.LENGTH_SHORT).show();


                }
            });
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Str_Otp= ettext.getText().toString().trim();
                    if (ettext.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(ClassNew.this, "Please Enter OTP .", Toast.LENGTH_LONG).show();
                    }
                    else {
                        OtpVerify();
                        String message = "Please Wait....";
                        dialogoo = new ProgressHUD(ClassNew.this, R.style.ProgressHUD);
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
        else
        {
            DBHandlerClass.TABLE_NAME="main_"+"1";
            System.out.println("Database bao"+"  "+"1"+"  "+DBHandlerClass.TABLE_NAME);
            CallClassUrl();
        }
    }
    private void OtpRefresh() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"OtpResend.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
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
                                    //CallSubject();
                                    //otpdialog.dismiss();
                                }
                                else {
                                    Toast.makeText(ClassNew.this, Str_Msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("School_UI", "MTc=");
                params.put("email", Pref_Email);
                params.put("mobile", Pref_Mob);
                params.put("action", "resendotp");//action=resendotp
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void OtpVerify() {

        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"checkOtp.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;	array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status= object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                System.out.println("messsssss"+"  "+Str_Msg);
                                if(Str_Status.equalsIgnoreCase("True"))
                                {
                                    preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                                    editor= preferences.edit();
                                    editor.putString("Rsar_Otp_Value", "False");
                                    editor.commit();
                                    CallClassUrl();
                                    otpdialog.dismiss();
                                }
                                else {
                                    Toast.makeText(ClassNew.this, Str_Msg, Toast.LENGTH_LONG).show();
                                }
                                if (dialogoo.isShowing()) {
                                    dialogoo.dismiss();
                                }
                            }
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

                        try {
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("School_UI", Pref_School_UI);
                params.put("otp", Str_Otp);// Second one u can changePref_Restric_Id
                params.put("mobile", Pref_Mob);
                params.put("email", Pref_Email);
                return params;
            }
        };
        queue.add(postRequest);


    }{
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
    private void Call_Button()  {
        ln_logo_bg = (LinearLayout)findViewById(R.id.Ln_Logo_Bg);
        Img_Sch_Logo=(ImageView)findViewById(R.id.Img_School_logo);
        ln_bg = (LinearLayout)findViewById(R.id.Lnr_Bg);
        ln_top_bg = (LinearLayout)findViewById(R.id.Lnr_Top_bg);
        Txt_School_Name=(TextView)findViewById(R.id.Txt_School_Name);
        Txt_School_Name.setTextColor(Color.parseColor(Pref_Sch_Name_Color));
        gv_subcat=(GridView)findViewById(R.id.Gv_subcat);
        ln_bg.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
        ln_top_bg.setBackgroundColor(Color.parseColor(Pref_Top_Bg_Code));
        Bitmap bitmap = BitmapFactory.decodeFile(logobg.getAbsolutePath());
        Bitmap bitsclogo = BitmapFactory.decodeFile(schoollogo.getAbsolutePath());
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(logobg.getAbsoluteFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapDrawable background = new BitmapDrawable(bitmap);
        ln_logo_bg.setBackgroundDrawable(background);
        try {
            bitsclogo = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(schoollogo.getAbsoluteFile())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapDrawable Sclogo = new BitmapDrawable(bitsclogo);
        Img_Sch_Logo.setBackground(Sclogo);
        Txt_School_Name.setText(Pref_School_name);
    }

    private void CallClassUrl() {
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (isInternetPresent) {
            SQLiteDataBaseBuild();
            SQLiteTableBuild();
            DeletePreviousData();
            ClassUrl();
            NotifyUpdate();
            String message = "Please Wait....";
            dialog = new ProgressHUD(ClassNew.this, R.style.ProgressHUD);
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
            dbHandlerClass = new DBHandlerClass(this);
            Class_ID_Array = new ArrayList<String>();
            Class_NAME_Array = new ArrayList<String>();
            ShowSQLiteDBdata() ;
            //Show_Gridview_Offline();
            Misc.showAlertDialog(ClassNew.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }
    private void ClassUrl() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"class.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;	array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status= object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                System.out.println("messsssss"+"  "+Str_Msg);
                                if(Str_Status.equalsIgnoreCase("True"))

                                {

                                    Class_Data=object.getJSONArray("ClassData");
                                    System.out.println("dinchiaaa"+"  "+object.get("ClassData").toString());

                                    for(int j=0; j<Class_Data.length(); j++)
                                    {
                                        JSONObject ObjectData ;
                                        ObjectData= new JSONObject(Class_Data.getJSONObject(j).toString()) ;
                                        String ClassName = ObjectData.getString("Class_Name");
                                        String ClassId = ObjectData.getString("Class_Id");
                                        vinsgetter= new SetterGetter_Sub_Chap();
                                        vinsgetter.setClassName(ObjectData.getString("Class_Name"));
                                        vinsgetter.setClass_Id(ObjectData.getString("Class_Id"));
                                        vinsquesarrayList.add(vinsgetter);
                                        /*   preferences = getApplicationContext().getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                                        editor = preferences.edit();
                                        editor.putString("Rsar_Class_Arraylst",  vinsquesarrayList);

                                    editor.commit();
                                        editor.apply();*/
                                        GridViewAdapterChap adapter = new GridViewAdapterChap(ClassNew.this,vinsquesarrayList );
                                        gv_subcat.setAdapter(adapter);
                                        String SQLiteDataBaseQueryHolder = "INSERT INTO "+ DBHandlerClass.TABLE_NAME+"" +
                                                " (Str_Class_Name,Str_Class_Id) VALUES('"+ClassName+"', '"+ClassId+"')";
                                        sqLiteDatabaseClass.execSQL(SQLiteDataBaseQueryHolder);
                                        gv_subcat.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view,
                                                                    int position, long id) {
                                                // TODO Auto-generated method stub
                                                // Toast.makeText(ClassNew.this, GridViewClickItemArray.get(position).toString(), Toast.LENGTH_LONG).show();
                                                try {
                                                    Details= Class_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    Class_Id= myObject.getString("Class_Id");
                                                    Intent intent = new Intent(ClassNew.this, Subject.class);
                                                    intent.putExtra("Rsar_Class_Id", Class_Id);
                                                    startActivity(intent);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }}
                                else
                                {
                                    final Dialog dialogss = new Dialog(ClassNew.this);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);
                                    // set the custom dialog components - text, image and button
                                    LinearLayout ln_outline=(LinearLayout)dialogss.findViewById(R.id.dia_ln_outline);
                                    View view=(View) dialogss.findViewById(R.id.dia_view);
                                    TextView Error_text = (TextView) dialogss.findViewById(R.id.dia_error_title);
                                    TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Msg);
                                    Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                        }

                                    });
                                    dialogss.show();
                                }

                                if (dialog.isShowing())
                                    dialog.dismiss();
                                sqLiteDatabaseClass.close();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams()
            {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("School_UI", Pref_School_UI);
                params.put("action", "class");// Second one u can changePref_Restric_Id
                params.put("Restrict_SD", Pref_Restric_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void ShowSQLiteDBdata() {
        //  dbHandler = new DBHandler(this);
        sqLiteDatabaseClass = dbHandlerClass.getWritableDatabase();
        try {
            cursor = dbHandlerClass.getWritableDatabase().rawQuery("SELECT * FROM " + "main_1" + "", null);
            Class_ID_Array.clear();
            Class_NAME_Array.clear();
            if (cursor.moveToFirst()) {
                do {
                    Class_ID_Array.add(cursor.getString(cursor.getColumnIndex(DBHandlerClass.KEY_RSAR_CLASS_ID)));
                    GridViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(DBHandlerClass.KEY_RSAR_CLASS_ID)));
                    Class_NAME_Array.add(cursor.getString(cursor.getColumnIndex(DBHandlerClass.KEY_RSAR_CLASS_NAME)));
                } while (cursor.moveToNext());
            }
            adapter = new GridViewAdapterClass(ClassNew.this, Class_ID_Array, Class_NAME_Array);
            System.out.println("data db"+"  "+Class_ID_Array+"  "+Class_NAME_Array);
            gv_subcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //   Toast.makeText(ClassNew.this, GridViewClickItemArray.get(position), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClassNew.this, Subject.class);
                    intent.putExtra("Rsar_Db_Class_Id", GridViewClickItemArray.get(position));
                    startActivity(intent);

                }
            });
            gv_subcat.setAdapter(adapter);
            cursor.close();
        }catch (Exception e){
            System.out.println("Table deatails"+ "doesn't exist :(((");
            final Dialog dialogss = new Dialog(ClassNew.this);
            dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogss.setContentView(R.layout.alert_dialog);
            dialogss.setCancelable(true);

            // set the custom dialog components - text, image and button
            LinearLayout ln_outline=(LinearLayout)dialogss.findViewById(R.id.dia_ln_outline);
            View view=(View) dialogss.findViewById(R.id.dia_view);
            TextView Error_text = (TextView) dialogss.findViewById(R.id.dia_error_title);
            TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
            text.setText("Please Start your internet to load data.");
            Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);
            ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
            btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogss.dismiss();
                }
            });
            dialogss.show();
        }
    }
    public void SQLiteDataBaseBuild(){
        sqLiteDatabaseClass = openOrCreateDatabase(DBHandlerClass.DB_NAME, Context.MODE_PRIVATE, null);
    }
    public void SQLiteTableBuild(){

        sqLiteDatabaseClass.execSQL("CREATE TABLE IF NOT EXISTS "+ DBHandlerClass.TABLE_NAME+"("+DBHandlerClass.KEY_RSAR_CLASS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+DBHandlerClass.KEY_RSAR_CLASS_NAME+" Text);");
    }
    public void DeletePreviousData(){

        sqLiteDatabaseClass.execSQL("DELETE FROM "+ DBHandlerClass.TABLE_NAME+"");
    }
    private void NotifyUpdate() {
        System.out.println("Notifyupdate" + " " + "aaaaa");
        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"smNotifyUpdate.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
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
                                /* User_Name,User_Email_id,User_Mobile_No,User_Shipping_City,User_Shipping_State,User_Shipping_Country,User_Pin_Code,
    			   	          User_Land_Mark,User_Shipping_Address,;*/
                                int notifyupdate;
                                String Status=object.getString("Status");
                                String Msg=object.getString("Message");
                                if(Status.equalsIgnoreCase("True")) {
                                    notifyupdate = Integer.parseInt((String) object.get("Notify_Status"));
                                    if (notifyupdate == 1) {
                                        NotifyUpdateMsg();
                                    }
                                    if (notifyupdate == 2) {
                                        NotifyUpdateMsglink();
                                    }
                                    if (notifyupdate == 3) {
                                        NotifyUpdateMsgActivity();
                                    }
                                    if (notifyupdate == 4) {
                                        NotifyUpdateMsglink();
                                    }
                                    System.out.println("Notifyupdate" + " " + notifyupdate);

                                }else
                                {
                                    //Toast.makeText(ClassNew.this, Msg, Toast.LENGTH_SHORT).show();
                                }

                            }
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
            protected HashMap<String, String> getParams()
            {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("notify", "notify");
                params.put("Device_Id", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                params.put("School_UI", Pref_School_UI);

                return params;
            }
        };
        queue.add(postRequest);

    }
    private void NotifyUpdateMsg() {


        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"smNotifyMessage.php?";
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
                                String Notify_Status= object.get("Status").toString();
                                String Msg=object.getString("Message");
                                if(Notify_Status.equalsIgnoreCase("True"))
                                {
                                    String	Str_Notify_Msg=object.get("Notify_Message").toString();
                                    System.out.println("Deegggg"+" "+object.get("Notify_Message").toString());
                                    final Dialog dialoga = new Dialog(ClassNew.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog);
                                    dialoga.setCancelable(false);

                                    LinearLayout ln_outline= dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur= dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View)dialoga.findViewById(R.id.dia_view) ;
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));

                                    TextView text = (TextView) dialoga.findViewById(R.id.dia_error_msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.dia_b_error_button);
                                    dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));

                                    text.setText(Str_Notify_Msg);
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();

                                        }
                                    });

                                    dialoga.show();


                                }else
                                {
                                    Toast.makeText(ClassNew.this, Msg, Toast.LENGTH_SHORT).show();
                                }
                            }
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
            protected HashMap<String, String> getParams()
            {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("notifymsg", "notifymsg");
                params.put("Device_Id", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                params.put("School_UI", Pref_School_UI);

                return params;
            }
        };
        queue.add(postRequest);

    }
    private void NotifyUpdateMsglink() {
        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"smNotifyMessage.php?";
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
                                String Notify_Status= object.get("Status").toString();
                                String Msg=object.getString("Message");

                                if(Notify_Status.equalsIgnoreCase("True"))
                                {

                                    String	Str_Notify_Msg=object.get("Notify_Message").toString();
                                    Str_Notify_Msg_Link=object.get("Notify_Link").toString();
                                    System.out.println("Deegggg"+" "+object.get("Notify_Message").toString());
                                    final Dialog dialoga = new Dialog(ClassNew.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_msg_link);
                                    dialoga.setCancelable(false);

                                    LinearLayout ln_outline= dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur= dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View)dialoga.findViewById(R.id.dia_view) ;
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialoga.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Notify_Msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.dia_b_error_button_link);
                                    dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(Str_Notify_Msg_Link));
                                            startActivity(i);
                                            dialoga.dismiss();
                                        }
                                    });

                                    Button dialogButtonCan = (Button) dialoga.findViewById(R.id.dia_b_error_button);
                                    dialogButtonCan.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // if button is clicked, close the custom dialog
                                    dialogButtonCan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();

                                        }
                                    });

                                    dialoga.show();

                                }else
                                {
                                    Toast.makeText(ClassNew.this, Msg, Toast.LENGTH_SHORT).show();
                                }

                            }
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
            protected HashMap<String, String> getParams()
            {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("notifymsg", "notifymsg");
                params.put("Device_Id", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                params.put("School_UI", Pref_School_UI);

                return params;
            }
        };
        queue.add(postRequest);
    }
    private void NotifyUpdateMsgActivity() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url+"smNotifyMessage.php?";
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
                                String Notify_Status= object.get("Status").toString();
                                String Msg=object.getString("Message");

                                if(Notify_Status.equalsIgnoreCase("True")) {

                                    String	Str_Notify_Msg=object.get("Notify_Message").toString();
                                    System.out.println("Deegggg"+" "+object.get("Notify_Message").toString());
                                    final Dialog dialoga = new Dialog(ClassNew.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_msg_link);
                                    dialoga.setCancelable(false);
                                    LinearLayout ln_outline= dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur= dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View)dialoga.findViewById(R.id.dia_view) ;
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialoga.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Notify_Msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.dia_b_error_button_link);
                                    dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            preferences = getSharedPreferences("RSAR_School_Model", 0);
                                            editor = preferences.edit();
                                            editor.clear();
                                            editor.commit();
                                            Intent intent = new Intent(ClassNew.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    Button dialogButtonCan = (Button) dialoga.findViewById(R.id.dia_b_error_button);
                                    dialogButtonCan.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    // if button is clicked, close the custom dialog
                                    dialogButtonCan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();

                                        }
                                    });

                                    dialoga.show();


                                }else
                                {
                                    Toast.makeText(ClassNew.this, Msg, Toast.LENGTH_SHORT).show();
                                }
                            }
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
            protected HashMap<String, String> getParams()
            {

                HashMap<String, String>  params = new HashMap<String, String>();
                params.put("notifymsg", "notifymsg");
                params.put("Device_Id", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                params.put("School_UI", Pref_School_UI);

                return params;
            }
        };
        queue.add(postRequest);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();

            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

		    /*   MenuSlider menu= new MenuSlider(this);

		      menu.showMenu();*/
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}






