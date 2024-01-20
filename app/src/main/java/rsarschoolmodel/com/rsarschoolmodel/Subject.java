package rsarschoolmodel.com.rsarschoolmodel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.GridViewAdapterChap1;
import rsarschoolmodel.com.Common.GridViewAdapterSub;
import rsarschoolmodel.com.Common.Misc;
import rsarschoolmodel.com.Common.Networking;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.Common.SetterGetter_Sub_Chap;
import rsarschoolmodel.database.DBHandlerSub;
public class Subject extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabaseSub;
    DBHandlerSub dbHandlersub;
    Cursor cursor;
    ArrayList<String> Sub_ID_Array;
    ArrayList<String> Sub_NAME_Array;
    ArrayList<String> GridViewClickItemArray = new ArrayList<String>();
    GridView gv_subcat;
    GridViewAdapterChap1 adapter1;
    EditText ettext;
    Dialog otpdialog;
    ProgressHUD dialog;
    ProgressHUD dialogoo;
    String message = "Please Wait....";
    ProgressDialog progressDialog;

    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    boolean doubleBackToExitPressedOnce;
    LinearLayout ln_bg,llBackgroundDesign;
    SetterGetter_Sub_Chap vinsgetter;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name, Pref_Restric_Id,Pref_Sch_Name_Color, Str_Notify_Msg_Link, Pref_Email, Pref_Mob;
    String Str_Status, Str_Msg, Details, Sub_Id, Sub_Name, Class_Id, Db_Class_Id, Str_Restrict_SD, Str_Otp_Value, Str_Otp;
    //GridView gv_subcat;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    JSONArray Sub_Data;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName,Class_Name;
    int sVersionCode;
    Toolbar toolbar;
    ImageView imgLogout;
    TextView txtTittle,txtSubjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.subject_activity);

        cd = new ConnectionDetector(getApplicationContext());

        Class_Name = getIntent().getExtras().getString("Rsar_Class_Name");

        preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Pref_School_name = preferences.getString("Rsar_School_Name", "");
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
        Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");
        Str_Otp_Value = preferences.getString("Rsar_Otp_Value", "");
        Pref_Email = preferences.getString("Rsar_Pref_Email", "");
        Pref_Mob = preferences.getString("Rsar_Pref_Mobile", "");
        Pref_Sch_Name_Color = preferences.getString("Rsar_Sch_Bgcol_Name", "");
        // get Internet status
        PACKAGE_NAME = getApplicationContext().getPackageName();

        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sVersionCode = pinfo.versionCode; // 1
        sVersionName = pinfo.versionName; // 1.0
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Db_Class_Id = getIntent().getExtras().getString("Rsar_Class_Id");
            DBHandlerSub.TABLE_NAME = "class_" + Db_Class_Id;
            System.out.println("Database bao" + "  " + Db_Class_Id + "  " + DBHandlerSub.TABLE_NAME);
            Call_Button();
            CallSubject();
            // OtpActication();
        } else {
            Db_Class_Id = getIntent().getExtras().getString("Rsar_Db_Class_Id");
            DBHandlerSub.TABLE_NAME = "class_" + Db_Class_Id;
            System.out.println("Database baf" + "  " + Db_Class_Id + "  " + DBHandlerSub.TABLE_NAME);
            Call_Button();
            CallSubject();
        }
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(Subject.this, MainActivity.class);
                startActivity(i);
                finishAffinity();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }
    private void Call_Button() {
        toolbar = findViewById(R.id.toolbarRegister);
        ln_bg = (LinearLayout) findViewById(R.id.Lnr_Bg);
        gv_subcat = (GridView) findViewById(R.id.gvClass);
        txtTittle=findViewById(R.id.txtTittle);
        txtSubjectName=findViewById(R.id.txtSubjectName);
        imgLogout=findViewById(R.id.imgLogout);
        llBackgroundDesign = (LinearLayout)findViewById(R.id.llBackgroundDesign);

        txtTittle.setText(Class_Name);
        ln_bg.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
        toolbar.setBackgroundColor(Color.parseColor(Pref_Top_Bg_Code));
        txtSubjectName.setBackgroundColor(Color.parseColor(Pref_Top_Bg_Code));
        txtTittle.setTextColor(Color.parseColor(Pref_Sch_Name_Color));
        txtSubjectName.setTextColor(Color.parseColor(Pref_Sch_Name_Color));
        imgLogout.setImageTintList(ColorStateList.valueOf(Color.parseColor(Pref_Sch_Name_Color)));
        llBackgroundDesign.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
    }

    private void CallSubject() {
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (isInternetPresent) {
            SQLiteDataBaseSubBuild();
            SQLiteTableBuild();
            DeletePreviousData();
            Class_Id = getIntent().getExtras().getString("Rsar_Class_Id");
            GetSubjectURl();
            String message = "Please Wait....";
            dialog = new ProgressHUD(Subject.this, R.style.ProgressHUD);
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
            dbHandlersub = new DBHandlerSub(this);
            Sub_ID_Array = new ArrayList<String>();
            Sub_NAME_Array = new ArrayList<String>();
            ShowSQLiteDBdata();
            Misc.showAlertDialog(Subject.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }

    private void GetSubjectURl() {

        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);

        String urlmanual = Networking.url + "subject.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                Str_Restrict_SD = object.get("Restrict_SD").toString();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Sub_Data = object.getJSONArray("SubjectData");
                                    System.out.println("Sub_Data" + "  " + Sub_Data + "  " + object.get("SubjectData").toString());
                                    for (int j = 0; j < Sub_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Sub_Data.getJSONObject(j).toString());
                                        String SubName = ObjectData.getString("Subject_Name");
                                        String SubId = ObjectData.getString("Subject_Id");
                                        vinsgetter = new SetterGetter_Sub_Chap();
                                        vinsgetter.setSubject_Id(ObjectData.getString("Subject_Id"));
                                        vinsgetter.setSubjectName(ObjectData.getString("Subject_Name"));
                                        vinsquesarrayList.add(vinsgetter);
                                        GridViewAdapterSub adapter = new GridViewAdapterSub(Subject.this, vinsquesarrayList);
                                        gv_subcat.setAdapter(adapter);
                                        String SQLiteDataBaseSubQueryHolder = "INSERT INTO " + DBHandlerSub.TABLE_NAME + "" + " (Str_Subject_Name,Str_Subject_Id) VALUES('" + SubName + "', '" + SubId + "')";
                                        sqLiteDatabaseSub.execSQL(SQLiteDataBaseSubQueryHolder);
                                        gv_subcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view,
                                                                    int position, long id) {
                                                // TODO Auto-generated method stub
                                                try {
                                                    Details = Sub_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    Sub_Id = myObject.getString("Subject_Id");
                                                    Sub_Name = myObject.getString("Subject_Name");
                                                    Intent intent = new Intent(Subject.this, Book_Name.class);
                                                    intent.putExtra("Rsar_Subject_Id", Sub_Id);
                                                    intent.putExtra("Rsar_Sub_Name", Sub_Name);
                                                    intent.putExtra("Rsar_Class_Id", Class_Id);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    System.out.println("ONCLIACK" + "  " + Sub_Id + "  " + Sub_Name);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    final Dialog dialogss = new Dialog(Subject.this);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);
                                    // set the custom dialog components - text, image and button
                                    LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
                                    View view = (View) dialogss.findViewById(R.id.dia_view);
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
                                sqLiteDatabaseSub.close();
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("School_UI", Pref_School_UI);
                params.put("cId", Class_Id);// Second one u can change
                params.put("action", "subject");
                params.put("Restrict_SD", Pref_Restric_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void ShowSQLiteDBdata() {
        sqLiteDatabaseSub = dbHandlersub.getWritableDatabase();

        try {
            cursor = sqLiteDatabaseSub.rawQuery("SELECT * FROM " + DBHandlerSub.TABLE_NAME + "", null);
            Sub_ID_Array.clear();
            Sub_NAME_Array.clear();
            if (cursor.moveToFirst()) {
                do {
                    Sub_ID_Array.add(cursor.getString(cursor.getColumnIndex(DBHandlerSub.KEY_RSAR_SUBJECT_ID)));
                    //Inserting Column Name into Array to Use at GridView Click Listener Method.
                    GridViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(DBHandlerSub.KEY_RSAR_SUBJECT_ID)));
                    Sub_NAME_Array.add(cursor.getString(cursor.getColumnIndex(DBHandlerSub.KEY_RSAR_SUBJECT_NAME)));
                } while (cursor.moveToNext());
            }
            adapter1 = new GridViewAdapterChap1(Subject.this, Sub_ID_Array, Sub_NAME_Array);
            gv_subcat.setAdapter(adapter1);
            gv_subcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //    Toast.makeText(Subject.this, GridViewClickItemArray.get(position), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Subject.this, Book_Name.class);
                    intent.putExtra("Rsar_Db_Sub_Id", GridViewClickItemArray.get(position));
                    startActivity(intent);
                }
            });
            cursor.close();
        } catch (Exception e) {

            System.out.println("Table deatails" + "doesn't exist :((()))))");
            final Dialog dialogss = new Dialog(Subject.this);
            dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogss.setContentView(R.layout.alert_dialog);
            dialogss.setCancelable(true);
            // set the custom dialog components - text, image and button
            LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
            View view = (View) dialogss.findViewById(R.id.dia_view);
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

    public void SQLiteDataBaseSubBuild() {

        sqLiteDatabaseSub = openOrCreateDatabase(DBHandlerSub.DB_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild() {

        sqLiteDatabaseSub.execSQL("CREATE TABLE IF NOT EXISTS " + DBHandlerSub.TABLE_NAME + "(" + DBHandlerSub.KEY_RSAR_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DBHandlerSub.KEY_RSAR_SUBJECT_NAME + " Text);");

    }

    public void DeletePreviousData() {

        sqLiteDatabaseSub.execSQL("DELETE FROM " + DBHandlerSub.TABLE_NAME + "");

    }

    private void NotifyUpdate() {


        System.out.println("Notifyupdate" + " " + "aaaaa");
        // TODO Auto-generated method stub

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "smNotifyUpdate.php?";
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
                                String Status = object.getString("Status");
                                String Msg = object.getString("Message");

                                if (Status.equalsIgnoreCase("True")) {

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

                                } else {
                                    //Toast.makeText(Class_Activity.this, Msg, Toast.LENGTH_SHORT).show();
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
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
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
        String urlmanual = Networking.url + "smNotifyMessage.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                String Notify_Status = object.get("Status").toString();
                                String Msg = object.getString("Message");
                                if (Notify_Status.equalsIgnoreCase("True")) {
                                    String Str_Notify_Msg = object.get("Notify_Message").toString();
                                    final Dialog dialoga = new Dialog(Subject.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog);
                                    dialoga.setCancelable(false);

                                    LinearLayout ln_outline = dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur = dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View) dialoga.findViewById(R.id.dia_view);
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));

                                    TextView text = (TextView) dialoga.findViewById(R.id.dia_error_msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.dia_b_error_button);
                                    dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));

                                    text.setText(Str_Notify_Msg);

                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();
                                        }
                                    });
                                    dialoga.show();
                                } else {
                                    Toast.makeText(Subject.this, Msg, Toast.LENGTH_SHORT).show();
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
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
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
        String urlmanual = Networking.url + "smNotifyMessage.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONArray array;
                            array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object;

                                object = new JSONObject(array.getString(i).toString());
                                String Notify_Status = object.get("Status").toString();
                                String Msg = object.getString("Message");

                                if (Notify_Status.equalsIgnoreCase("True")) {

                                    String Str_Notify_Msg = object.get("Notify_Message").toString();

                                    Str_Notify_Msg_Link = object.get("Notify_Link").toString();

                                    final Dialog dialoga = new Dialog(Subject.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_msg_link);
                                    dialoga.setCancelable(false);

                                    LinearLayout ln_outline = dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur = dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View) dialoga.findViewById(R.id.dia_view);
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));

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
                                } else {
                                    Toast.makeText(Subject.this, Msg, Toast.LENGTH_SHORT).show();
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
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
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

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = Networking.url + "smNotifyMessage.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                String Notify_Status = object.get("Status").toString();
                                String Msg = object.getString("Message");

                                if (Notify_Status.equalsIgnoreCase("True")) {

                                    String Str_Notify_Msg = object.get("Notify_Message").toString();
                                    final Dialog dialoga = new Dialog(Subject.this);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.dialog_msg_link);
                                    dialoga.setCancelable(false);

                                    LinearLayout ln_outline = dialoga.findViewById(R.id.dia_ln_outline);
                                    LinearLayout ln_bg_blur = dialoga.findViewById(R.id.dia_ln_bg);
                                    TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
                                    View v_bg_color = (View) dialoga.findViewById(R.id.dia_view);
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
                                            Intent intent = new Intent(Subject.this, MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                                } else {
                                    Toast.makeText(Subject.this, Msg, Toast.LENGTH_SHORT).show();
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
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
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
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
