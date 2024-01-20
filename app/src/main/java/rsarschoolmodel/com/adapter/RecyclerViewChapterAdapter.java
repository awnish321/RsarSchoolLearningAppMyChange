package rsarschoolmodel.com.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import rsarschoolmodel.com.dynamics.RecordDatabase;
import rsarschoolmodel.com.dynamics.VideoPlayer;
import rsarschoolmodel.com.modelClass.response.ChapterResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.ui.ActivityList.ChapterVideoPlayBackActivity;
import rsarschoolmodel.com.utilities.AllStaticMethods;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;
import rsarschoolmodel.com.utilities.UnzipUtil;


public class RecyclerViewChapterAdapter extends RecyclerView.Adapter<RecyclerViewChapterAdapter.ViewHolder> {
    Context context;
    List<ChapterResponseModel.ChapterDatum> chapterData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean isInternetPresent = false;
    private ProgressDialog mProgressDialog;
    String unzipLocation;
    String zipFile, bookName, className, subjectName;
    public static String Value;
    String strZipName, strChapName, strClassName, strSubName, strBookName, schoolFolderName;
    boolean deleted_zip;
    private RecordDatabase database;
    private String saveChapterId;
    File dirDeleteFolder;
    int downloadCount ,downloadCountLeft;
    String bgCode, topBgCode, buttonBg, schoolNameColor;

    public RecyclerViewChapterAdapter(Context context, List<ChapterResponseModel.ChapterDatum> chapterData, String bookName, String className, String subjectName, String schoolFolderName) {
        this.context = context;
        this.chapterData = chapterData;
        this.bookName = bookName;
        this.className = className;
        this.subjectName = subjectName;
        this.schoolFolderName = schoolFolderName;
        database = new RecordDatabase(context);
        sharedPreferences = context.getSharedPreferences("USER_APP_DATA", MODE_PRIVATE);

        UserLoginResponseModel.UserDatum userData = (UserLoginResponseModel.UserDatum) SharedPreferenceManager.getUserData(context);
        bgCode = userData.getBgCode();
        topBgCode = userData.getTopBgCode();
        buttonBg = userData.getButtonBgColor();
        schoolNameColor = userData.getSchoolNameColor();
    }

    @Override
    public int getItemCount() {
        return chapterData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.chapter_list_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        ChapterResponseModel.ChapterDatum chapterModel = chapterData.get(position);

        viewHolder.llDownloadBackground.setBackgroundColor(Color.parseColor(topBgCode));
        viewHolder.llChapterBackground.setBackgroundColor(Color.parseColor(topBgCode));
        viewHolder.llBackgroundColour.setBackgroundColor(Color.parseColor(bgCode));
        viewHolder.txtChapterName.setBackgroundColor(Color.parseColor(topBgCode));
        viewHolder.txtChapterName.setTextColor(Color.parseColor(schoolNameColor));
        viewHolder.btnDownload.setBackgroundColor(Color.parseColor(topBgCode));
        viewHolder.btnDownload.setTextColor(Color.parseColor(schoolNameColor));
        viewHolder.btnDelete.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bgCode)));

        if (chapterModel.getDownloadStatus().contains("1")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ChapterVideoPlayBackActivity.txtDownloadVideo.setVisibility(View.GONE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ChapterVideoPlayBackActivity.txtScanBook.setVisibility(View.VISIBLE);
            }
        }
        if (chapterModel.getDownloadStatus().equalsIgnoreCase("0")) {
            viewHolder.linearLayout.setVisibility(View.GONE);
            viewHolder.btnDownload.setVisibility(View.VISIBLE);
        } else {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.btnDownload.setVisibility(View.GONE);
            isInternetPresent = AllStaticMethods.isOnline(context);
            if (isInternetPresent) {
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btnDelete.setVisibility(View.GONE);
            }
        }

        viewHolder.txtChapterName.setText(chapterModel.getChapterName());
        viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChapterResponseModel.ChapterDatum model = chapterData.get(position);

                strZipName = model.getZipName();
                strChapName = model.getChapterName();
                strClassName = className;
                strSubName = subjectName;
                strBookName = bookName;
                Intent intent = new Intent(context, VideoPlayer.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent.putExtra("VideoUrl", context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            + "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/" + model.getZipName()
                            + "/videos" + "/" + model.getVideoName() + ".mp4");
                    SharedPreferenceManager.setDownloadCount(context, 0);
                    context.startActivity(intent);
                } else {
                    intent.putExtra("VideoUrl", Environment.getExternalStorageDirectory()
                            + "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/" + model.getZipName()
                            + "/videos" + "/" + model.getVideoName() + ".mp4");
                    SharedPreferenceManager.setDownloadCount(context, 0);
                    context.startActivity(intent);

                }
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ChapterResponseModel.ChapterDatum model = chapterData.get(position);
                strChapName = model.getChapterName();
                strClassName = className;
                strSubName = subjectName;
                strBookName = bookName;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    dirDeleteFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/" + model.getZipName() + "/");
                } else {
                    dirDeleteFolder = new File(Environment.getExternalStorageDirectory() + "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/" + model.getZipName() + "/");
                }
                if (dirDeleteFolder.isDirectory()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Confirm Delete...");
                    alertDialog.setMessage("Are you sure you want to delete ?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                FileUtils.deleteDirectory(dirDeleteFolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            database.updateDownloadStatus(bookName, model.getChapterId().toString(), "0");
                            editor = sharedPreferences.edit();
                            editor.putString(bookName + model.getChapterId(), "0");
                            editor.commit();
                            editor.apply();
                            model.setDownloadStatus("0");
                            downloadCount = SharedPreferenceManager.getDownloadCount(context);
                            downloadCountLeft = downloadCount - 1;
                            SharedPreferenceManager.setDownloadCount(context, downloadCountLeft);
                            if (downloadCountLeft <= 0) {
                                ChapterVideoPlayBackActivity.txtScanBook.setVisibility(View.GONE);
                                ChapterVideoPlayBackActivity.txtDownloadVideo.setVisibility(View.VISIBLE);
                            }
                            notifyDataSetChanged();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else {
                    Toast.makeText(context, "No folder available.... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterResponseModel.ChapterDatum model = chapterData.get(position);
                String DwlndLink = model.getDownloadLink();
                strZipName = model.getZipName();
                strChapName = model.getChapterName();
                strClassName = className;
                strSubName = subjectName;
                strBookName = bookName;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    unzipLocation = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) +
                            "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/";
                } else {
                    unzipLocation = Environment.getExternalStorageDirectory()
                            + "/.rsarapp" + "/" + schoolFolderName + "/" + strClassName + "/" + strSubName + "/" + strBookName + "/";
                }
                Value = null;
                isInternetPresent = AllStaticMethods.isOnline(context);
                if (isInternetPresent) {
                    saveChapterId = String.valueOf(model.getChapterId());
                    DownloadMapAsync mew = new DownloadMapAsync(viewHolder, chapterModel);
                    mew.execute(DwlndLink);
                    Value = strZipName + ".zip";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        zipFile = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + strZipName + ".zip";
                    } else {
                        zipFile = Environment.getExternalStorageDirectory() + "/" + strZipName + ".zip";
                    }

                } else {
                    AllStaticMethods.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout,llBackgroundColour,llChapterBackground,llDownloadBackground;

        public TextView txtChapterName;
        public Button btnDownload;
        public Button btnPlay;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            llBackgroundColour=(LinearLayout)itemView.findViewById(R.id.llBackgroundColour);
            llChapterBackground=(LinearLayout)itemView.findViewById(R.id.llChapterBackground);
            llDownloadBackground=(LinearLayout)itemView.findViewById(R.id.llDownloadBackground);
            txtChapterName = (TextView) itemView.findViewById(R.id.txtChapterName);
            btnDownload = (Button) itemView.findViewById(R.id.btnDownload);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }

    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";
        ViewHolder holder;
        ChapterResponseModel.ChapterDatum chapterModel;

        public DownloadMapAsync(ViewHolder viewHolder, ChapterResponseModel.ChapterDatum model) {
            this.holder = viewHolder;
            this.chapterModel = model;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading File..");
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
                    AllStaticMethods.showAlertDialog(context, "Error In Internet Connection",
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
                    unzip(holder, chapterModel);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void unzip(final ViewHolder holder, ChapterResponseModel.ChapterDatum model) throws IOException {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask(holder, model).execute(zipFile, unzipLocation);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        public ViewHolder holder;
        private final ChapterResponseModel.ChapterDatum chapterModel;

        public UnZipTask(ViewHolder viewHolder, ChapterResponseModel.ChapterDatum model) {
            this.holder = viewHolder;
            this.chapterModel = model;
        }

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
            Toast.makeText(context, "Downloading completed...", Toast.LENGTH_LONG).show();
            File filev;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                filev = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), strZipName + ".zip");
            } else {
                filev = new File(Environment.getExternalStorageDirectory(), strZipName + ".zip");
            }
            deleted_zip = filev.delete();
            database.updateDownloadStatus(bookName, saveChapterId, "1");
            editor = sharedPreferences.edit();
            editor.putString(bookName + saveChapterId, "1");
            editor.commit();
            editor.apply();
            chapterModel.setDownloadStatus("1");

            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.btnDownload.setVisibility(View.GONE);
            ChapterVideoPlayBackActivity.txtScanBook.setVisibility(View.VISIBLE);
            ChapterVideoPlayBackActivity.txtDownloadVideo.setVisibility(View.GONE);
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

}
