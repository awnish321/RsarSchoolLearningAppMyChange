package rsarschoolmodel.com.ui.ActivityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.adapter.GridViewSubjectAdapter;
import rsarschoolmodel.com.adapter.HelpImageAdapter;
import rsarschoolmodel.com.api.RetrofitAPIService;
import rsarschoolmodel.com.modelClass.request.HelpBannerRequestModel;
import rsarschoolmodel.com.modelClass.request.SubjectRequestModel;
import rsarschoolmodel.com.modelClass.response.HelpBannerResponseModel;
import rsarschoolmodel.com.modelClass.response.SubjectResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.AllStaticMethods;
import rsarschoolmodel.com.utilities.AppConstants;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;

public class SubjectListActivity extends AppCompatActivity {

    GridView gvSubject;
    ProgressHUD customProgressBar;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    LinearLayout llBg, llBackgroundDesign;
    String classId, schoolUi, bgCode, topBgCode, buttonBg, schoolName, fdSchoolName, schoolNameColor, userEmail, userMobile;
    Toolbar toolbar;
    ImageView imgHelp, imgBlank;
    TextView txtTittle, txtSubjectName;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    List<SubjectResponseModel.SubjectDatum> subjectData = new ArrayList<SubjectResponseModel.SubjectDatum>();
    ArrayList<HelpBannerResponseModel.BannersDatum> bannersData = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_subject);

        classId = getIntent().getExtras().getString("classId");

        toolbar = findViewById(R.id.toolbarRegister);
        llBg = (LinearLayout) findViewById(R.id.llBg);
        gvSubject = (GridView) findViewById(R.id.gvSubject);
        txtTittle = findViewById(R.id.txtTittle);
        txtSubjectName = findViewById(R.id.txtSubjectName);
        imgHelp = findViewById(R.id.imgHelp);
        imgBlank = findViewById(R.id.imgBlank);
        llBackgroundDesign = (LinearLayout) findViewById(R.id.llBackgroundDesign);

        context = SubjectListActivity.this;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        getUserDetail();

        imgBlank.setVisibility(View.GONE);
        imgHelp.setVisibility(View.VISIBLE);
        llBg.setBackgroundColor(Color.parseColor(bgCode));
        toolbar.setBackgroundColor(Color.parseColor(topBgCode));
        txtSubjectName.setBackgroundColor(Color.parseColor(topBgCode));
        txtTittle.setTextColor(Color.parseColor(schoolNameColor));
        txtSubjectName.setTextColor(Color.parseColor(schoolNameColor));
        imgHelp.setImageTintList(ColorStateList.valueOf(Color.parseColor(schoolNameColor)));
        llBackgroundDesign.setBackgroundColor(Color.parseColor(bgCode));

        if (isInternetPresent) {
            customProgressBar();
            callHelpBannerApi();
            callSubjectApi();
        } else {
            noInternetConnectionDialog();
        }
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetPresent) {
                    final Dialog dialogss = new Dialog(SubjectListActivity.this);
                    dialogss.setContentView(R.layout.popup_help);
                    dialogss.setCancelable(true);
                    LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
                    mPager = (ViewPager) dialogss.findViewById(R.id.pager);
                    mPager.setAdapter(new HelpImageAdapter(SubjectListActivity.this, bannersData));
                    CirclePageIndicator indicator = (CirclePageIndicator) dialogss.findViewById(R.id.indicator);
                    indicator.setFillColor(Color.parseColor(bgCode));
                    indicator.setViewPager(mPager);
                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);
                    NUM_PAGES = bannersData.size();
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }

                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                    dialogss.show();
                } else {
                    noInternetConnectionDialog();                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void noInternetConnectionDialog() {
        final Dialog noInternetDialog = new Dialog(SubjectListActivity.this);
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
        customProgressBar = new ProgressHUD(SubjectListActivity.this, R.style.ProgressHUD);
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

    private void callSubjectApi() {

        SubjectRequestModel subjectRequestModel = new SubjectRequestModel(schoolUi, classId, "Teacher", "subject");

        RetrofitAPIService.getApiClient().getSubjectList(subjectRequestModel).enqueue(new Callback<SubjectResponseModel>() {
            @Override
            public void onResponse(Call<SubjectResponseModel> call, retrofit2.Response<SubjectResponseModel> response) {

                if (customProgressBar.isShowing()) customProgressBar.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            txtTittle.setText(response.body().getClassTitle());
                            subjectData.clear();
                            subjectData.addAll(response.body().getSubjectData());
                            GridViewSubjectAdapter adapter = new GridViewSubjectAdapter(context, subjectData);
                            gvSubject.setAdapter(adapter);
                            gvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String subjectId = subjectData.get(position).getSubjectId().toString();
                                    Intent intent = new Intent(context, BookListActivity.class);
                                    intent.putExtra("classId", classId);
                                    intent.putExtra("subjectId", subjectId);
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
            public void onFailure(Call<SubjectResponseModel> call, Throwable t) {
                if (customProgressBar.isShowing()) customProgressBar.dismiss();
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

    private void callHelpBannerApi() {
        HelpBannerRequestModel helpRequestModel = new HelpBannerRequestModel("banners");
        RetrofitAPIService.getApiClient().getHelpDetail(helpRequestModel).enqueue(new Callback<HelpBannerResponseModel>() {
            @Override
            public void onResponse(Call<HelpBannerResponseModel> call, retrofit2.Response<HelpBannerResponseModel> response) {

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            bannersData.clear();
                            bannersData.addAll(response.body().getBannersData());

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
            public void onFailure(Call<HelpBannerResponseModel> call, Throwable t) {
                Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
