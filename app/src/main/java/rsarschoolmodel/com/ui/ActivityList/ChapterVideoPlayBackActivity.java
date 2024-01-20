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
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import retrofit2.Call;
import retrofit2.Callback;
import rsarschoolmodel.com.Common.ConnectionDetector;
import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.adapter.RecyclerViewChapterAdapter;
import rsarschoolmodel.com.api.RetrofitAPIService;
import rsarschoolmodel.com.dynamics.ChapterModel;
import rsarschoolmodel.com.modelClass.request.ChapterRequestModel;
import rsarschoolmodel.com.modelClass.response.ChapterResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.AllStaticMethods;
import rsarschoolmodel.com.utilities.AppConstants;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;

public class ChapterVideoPlayBackActivity extends AppCompatActivity {
    private boolean sentToSettings = false;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private RecyclerView recyclerView;

    public static TextView txtScanBook, txtDownloadVideo, txtSubjectName, txtChapName;
    private static String mClassToLaunch;
    private static String mClassToLaunchPackage;
    ConnectionDetector cd;
    String  bookId, classId, subjectId, DataSet_Name, clDiffPlay;
    private ProgressDialog mProgressDialog;
    String unzipLocation;
    String zipFile;
    public static String Value;
    boolean deleted_zip;
    Context context;
    LinearLayout llBg;
    String schoolUi,bgCode, topBgCode, buttonBg, schoolName, fdSchoolName,schoolNameColor, userEmail, userMobile,dataSetName,dataSetLink,bookName;
    ArrayList<ChapterModel> chapterModels =new ArrayList<>() ;
    List<ChapterResponseModel.ChapterDatum> chapterData = new ArrayList<>();
    List<ChapterResponseModel.ChapterDatum> chapterRecyclerViewData = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Boolean isInternetPresent = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressHUD customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_video_play_back);

        classId = getIntent().getExtras().getString("classId");
        subjectId = getIntent().getExtras().getString("subjectId");
        bookId = getIntent().getExtras().getString("bookId");
        clDiffPlay = getIntent().getExtras().getString("differentPlay");
        bookName = getIntent().getExtras().getString("bookName");

        context = ChapterVideoPlayBackActivity.this;
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        sharedPreferences = getSharedPreferences("USER_APP_DATA", Context.MODE_PRIVATE);

        llBg = (LinearLayout) findViewById(R.id.llBg);
        txtScanBook = (TextView) findViewById(R.id.txtScanBook);
        txtDownloadVideo = (TextView) findViewById(R.id.txtDownloadVideo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        txtSubjectName = (TextView) findViewById(R.id.txtSubjectName);
        txtChapName = (TextView) findViewById(R.id.txtChapName);

        getUserDetail();

        txtChapName.setBackgroundColor(Color.parseColor(topBgCode));
        txtChapName.setTextColor(Color.parseColor(schoolNameColor));
        txtScanBook.setBackgroundColor(Color.parseColor(topBgCode));
        txtScanBook.setTextColor(Color.parseColor(schoolNameColor));
        txtDownloadVideo.setBackgroundColor(Color.parseColor(topBgCode));
        txtDownloadVideo.setTextColor(Color.parseColor(schoolNameColor));
        llBg.setBackgroundColor(Color.parseColor(bgCode));

        txtScanBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionBeforeScan();
            }
        });
    }

    @Override
    protected void onResume() {
        checkAndroidPermission();
        super.onResume();
    }

    private void startARActivity() {
        try {
            mClassToLaunchPackage = getPackageName();
            mClassToLaunch = "rsarschoolmodel.com.app.VideoPlayback.VideoPlayback";
            Intent i = new Intent();
            i.putExtra("chapterModels", chapterModels);
            i.putExtra("Rsar_Cl_Diff_Play", clDiffPlay);
            i.setClassName(mClassToLaunchPackage, mClassToLaunch);
            ChapterVideoPlayBackActivity.this.startActivity(i);
            System.out.println("asasasa" + "   " + mClassToLaunch + "   " + mClassToLaunchPackage);
        } catch (Exception e) {
            System.out.println("gfgfgf" + "    " + e);
        }
    }

    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ChapterVideoPlayBackActivity.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
		 		/*mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
		            // Set a click listener for progress dialog cancel button
		            @Override
		            public void onClick(DialogInterface dialog, int which){
		                // dismiss the progress dialog
		            	mProgressDialog.dismiss();
		                // Tell the system about cancellation
		                isCanceled = true;
		            }
		        });*/
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
                System.out.println("tgtgtgttg" + " " + lenghtOfFile);
                if (lenghtOfFile == 0) {
                    showAlertDialog(ChapterVideoPlayBackActivity.this, "Error In Internet Connection",
                            "You don't have proper internet connection.", false);
                }
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    System.out.println("fgfffff" + " " + (int) ((total * 100) / lenghtOfFile));
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
        mProgressDialog = new ProgressDialog(ChapterVideoPlayBackActivity.this);
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

            ///button hide and show
            File filev;
            //  Toast.makeText(ChapterList.this, "Downloading completed...", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                filev = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DataSet_Name + ".zip");
            } else {
                filev = new File(Environment.getExternalStorageDirectory(), DataSet_Name + ".zip");
            }

            deleted_zip = filev.delete();


            System.out.println("dfdfdfdfdddddd" + "   " + filev);
        }


        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            // Log.v("", "Extracting: " + entry);
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

    private void checkAndroidPermission() {

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
                        startActivity(new Intent(context, ClassActivity.class));
                        finishAffinity();
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
                        startActivity(new Intent(context, ClassActivity.class));
                        finishAffinity();
                    }
                });
                builder.show();
            } else {
                loadChapterList();
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
                        startActivity(new Intent(context, ClassActivity.class));
                        finishAffinity();
                    }
                });
                builder.show();
            }
            else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                        startActivity(new Intent(context, ClassActivity.class));
                        finishAffinity();
                    }
                });
                builder.show();
            } else {
                loadChapterList();
            }
        }
    }

    private void loadChapterList() {
        if (isInternetPresent) {
            callChapterListApi();
            customProgressBar();
        } else {
            noInternetConnectionDialog();
        }
    }

    private void checkPermissionBeforeScan() {

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
                startARActivity();
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
                startARActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void callChapterListApi(){
        ChapterRequestModel chapterRequestModel =new ChapterRequestModel(schoolUi,classId,subjectId,bookId,"Teacher","chapter");

        RetrofitAPIService.getApiClient().getChapterList(chapterRequestModel).enqueue(new Callback<ChapterResponseModel>() {
            @Override
            public void onResponse(Call<ChapterResponseModel> call, retrofit2.Response<ChapterResponseModel> response) {
                if (customProgressBar.isShowing())
                    customProgressBar.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus().equals(1)) {
                            chapterData.clear();
                            chapterData.addAll(response.body().getChapterData());
                            String bookName = response.body().getBookName();
                            txtSubjectName.setText(bookName);
                            txtSubjectName.setBackgroundColor(Color.parseColor(topBgCode));
                            txtSubjectName.setTextColor(Color.parseColor(schoolNameColor));

                            fdSchoolName = response.body().getSchoolFolderName();
                            dataSetName = response.body().getDatasetName();
                            dataSetLink = response.body().getDatasetLink();
                            setDataToModel(chapterData, response.body().getClassName(), response.body().getSubjectName(), response.body().getBookName(), response.body().getDatasetName());

                            SharedPreferenceManager.setprefSchoolFbName(context, response.body().getSchoolFolderName());
                            setDataToModel(chapterData, response.body().getClassName(), response.body().getSubjectName(), response.body().getBookName(), response.body().getDatasetName());
                            File file, dir;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                unzipLocation = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                                        + "/.rsarapp" + "/" + fdSchoolName + "/" + response.body().getClassName() + "/" + response.body().getSubjectName() + "/" + response.body().getBookName() + "/" + "DataSet/";
                                Value = null;
                                dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                            } else {
                                unzipLocation = Environment.getExternalStorageDirectory()
                                        + "/.rsarapp" + "/" + fdSchoolName + "/" + response.body().getClassName() + "/" + response.body().getSubjectName() + "/" + response.body().getBookName() + "/" + "DataSet/";
                                Value = null;
                                dir = Environment.getExternalStorageDirectory();
                            }
                            file = new File(dir, "/.rsarapp" + "/" + fdSchoolName + "/" + response.body().getClassName() + "/" + response.body().getSubjectName() + "/" + response.body().getBookName() + "/" + "DataSet/" + dataSetName/*+".xml"*/);
                            if (file.exists()) {
                                System.out.println("FILEEE EXIST" + "  " + file + " " + "True");
                            } else {
                                System.out.println("FILEEE EXIST" + "  " + file + "  " + "False");
                                DownloadMapAsync mew = new DownloadMapAsync();
                                mew.execute(dataSetLink);
                            }
                            Value = dataSetName + ".zip";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + dataSetName + ".zip";
                            } else {
                                zipFile = Environment.getExternalStorageDirectory() + "/" + dataSetName + ".zip";
                            }
                            int k = 0;
                            chapterRecyclerViewData.clear();
                            SharedPreferenceManager.setDownloadCount(context, 0);
                            for (int i = 0; i < chapterData.size(); i++) {
                                ChapterResponseModel.ChapterDatum itemData = chapterData.get(i);
                                if (sharedPreferences.getString(bookName + itemData.getChapterId(), "0").equals("1")) {
                                    itemData.setDownloadStatus("1");
                                    k = k + 1;
                                } else {
                                    itemData.setDownloadStatus("0");
                                }
                                chapterRecyclerViewData.add(itemData);
                            }

                            preferences = getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
                            editor= preferences.edit();
                            editor.putString("Rsar_Fd_School_Name", fdSchoolName);
                            editor.putString("Rsar_Bg_Code", bgCode);
                            editor.putString("Pref_Diff_Play", clDiffPlay);
                            editor.apply();

                            SharedPreferenceManager.setDownloadCount(context, k);
                            RecyclerViewChapterAdapter recyclerViewChapterAdapter = new RecyclerViewChapterAdapter(context, chapterRecyclerViewData, response.body().getBookName(), response.body().getClassName(), response.body().getSubjectName(), response.body().getSchoolFolderName());
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(recyclerViewChapterAdapter);
                            recyclerViewChapterAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, AppConstants.SERVER_ISSUE, Toast.LENGTH_SHORT).show();
                    AllStaticMethods.saveException(e);
                }
            }

            @Override
            public void onFailure(Call<ChapterResponseModel> call, Throwable t) {
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

    private void setDataToModel(List<ChapterResponseModel.ChapterDatum> chapterData, String className, String subjectName, String bookName, String datasetName) {
        chapterModels.clear();

        for (int i = 0; i < chapterData.size(); i++) {
            ChapterModel itemData = new ChapterModel();
            itemData.setZip_Name(chapterData.get(i).getZipName());
            itemData.setVideo_Name(chapterData.get(i).getVideoName());
            itemData.setClass_Name(className);
            itemData.setSubject_Name(subjectName);
            itemData.setBook_Name(bookName);
            itemData.setDataSet_Name(datasetName);
            itemData.setChapter_Id(chapterData.get(i).getChapterId());
            itemData.setChapter_Name(chapterData.get(i).getChapterName());
            itemData.setAssessment_Name(chapterData.get(i).getAssessmentName());
            itemData.setDB_Name(chapterData.get(i).getDbName());
            itemData.setDownload_Link(chapterData.get(i).getDownloadLink());
            itemData.setDownload_Status(chapterData.get(i).getDownloadStatus());
            itemData.setSchool_UI(schoolUi);
            chapterModels.add(itemData);
        }


//        chapterModels.add(new ChapterModel( Class_ID, Message,  Asses_Value));
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
