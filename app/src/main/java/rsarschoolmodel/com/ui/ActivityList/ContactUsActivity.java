package rsarschoolmodel.com.ui.ActivityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.HashMap;

import rsarschoolmodel.com.Common.Networking;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.AllStaticMethods;

public class ContactUsActivity extends AppCompatActivity {

    Context context;
    JSONArray Contact_Data;
    private String Str_Status, Str_Msg,Landline,Mobile,WhatsApp_No,EmailId,Head_Office_Address,Website_Url;
    Boolean isInternetPresent = false;
    ProgressHUD progressHUD;
    ImageView imgHelp,imgSupport;
    TextView txtTittle,txtWeb,txtAddress,txtWhatsUp,txtEmail,txtMobile,txtLandLine;
    LinearLayout llDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        context=ContactUsActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.layoutToolbar);
        setSupportActionBar(toolbar);
        txtTittle =findViewById(R.id.txtTittle);
        txtWeb =findViewById(R.id.txtWeb);
        txtAddress =findViewById(R.id.txtAddress);
        txtWhatsUp =findViewById(R.id.txtWhatsUp);
        txtEmail =findViewById(R.id.txtEmail);
        txtMobile =findViewById(R.id.txtMobile);
        txtLandLine =findViewById(R.id.txtLandLine);
        llDesign =findViewById(R.id.llDesign);

        isInternetPresent = AllStaticMethods.isOnline(context);
        if (isInternetPresent)
        {
            callProgressBar();
            callContactDetailApi();
        }
        else
        {
            AllStaticMethods.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
        }
//        toolbar = findViewById(R.id.dashboardToolbar);
        txtTittle.setText("Contact Us");
        txtWhatsUp.setPaintFlags(txtWhatsUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtWhatsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+WhatsApp_No;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        txtMobile.setPaintFlags(txtMobile.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + txtMobile.getText().toString());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try
                {
                    startActivity(i);
                }
                catch (SecurityException s)
                {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    private void callContactDetailApi()  {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
//        String urlmanual = "https://www.rachnasagar.in/rsarapp.services/contacts.php?";
        String urlmanual = Networking.url + "contacts.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Contact_Data = object.getJSONArray("Contact_Data");
                                    for (int j = 0; j < Contact_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Contact_Data.getJSONObject(j).toString());
                                        Landline = ObjectData.getString("Phone");
                                        Mobile = ObjectData.getString("Mobile");
                                        WhatsApp_No = ObjectData.getString("WhatsApp_No");
                                        EmailId = ObjectData.getString("EmailId");
                                        Head_Office_Address = ObjectData.getString("Head_Office_Address");
                                        Website_Url = ObjectData.getString("Website_Url");
                                        txtLandLine.setText(Landline.trim());
                                        txtMobile.setText(Mobile.trim());
                                        txtWhatsUp.setText(WhatsApp_No.trim());
                                        txtEmail.setText(EmailId.trim());
                                        txtWeb.setText(Website_Url.trim());
                                        txtAddress.setText(Head_Office_Address.trim());
                                        if(Mobile != null){
                                            Linkify.addLinks(txtMobile, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter);
                                            txtMobile.setMovementMethod(LinkMovementMethod.getInstance());
                                        }
                                    }
                                } else {
                                    final Dialog dialogss = new Dialog(ContactUsActivity.this);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);

                                    TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Msg);
                                    Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                        }
                                    });
                                    dialogss.show();
                                }
                                llDesign.setVisibility(View.VISIBLE);
                                if (progressHUD.isShowing())
                                    progressHUD.dismiss();
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
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "contact");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void callProgressBar(){
        String message = "Please Wait....";
        progressHUD = new ProgressHUD(context, R.style.ProgressHUD);
        progressHUD.setTitle("");
        progressHUD.setContentView(R.layout.progress_hudd);
        if (message == null || message.length() == 0) {
            progressHUD.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) progressHUD.findViewById(R.id.message);
            txt.setText(message);
        }
        progressHUD.setCancelable(false);
        progressHUD.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = progressHUD.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        progressHUD.getWindow().setAttributes(lp);
        progressHUD.show();
    }
}