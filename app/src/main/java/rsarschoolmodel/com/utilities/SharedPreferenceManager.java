package rsarschoolmodel.com.utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import rsarschoolmodel.com.modelClass.request.UserDeviceDetailRequestModel;
import rsarschoolmodel.com.modelClass.response.OtpVerifyResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;

public class SharedPreferenceManager {
    private static final String SCHOOL_LEARNING_USER_DATA ="USER_APP_DATA";
    public static String USER_DATA = "USER_DATA";

    public static void setUserData(Context context, UserLoginResponseModel.UserDatum userData) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(USER_DATA,new Gson().toJson(userData));
        prefsEditor.apply();
//        AllStaticFields.userData = userData;
    }
    public static void setNewUserData(Context context, OtpVerifyResponseModel.UserDatum userData) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(USER_DATA,new Gson().toJson(userData));
        prefsEditor.apply();
//        AllStaticFields.existingUserData = userData;
    }
    public static UserLoginResponseModel.UserDatum  getUserData(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA, MODE_PRIVATE);
        UserLoginResponseModel.UserDatum userDatum = new Gson().fromJson(sharedPreferences.getString(USER_DATA,null), UserLoginResponseModel.UserDatum.class);
        return userDatum;
    }

    public static void setUserStatus(Context context,String RsarUserStatus) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("userStatus", RsarUserStatus);
//        myEdit.putInt("age", Integer.parseInt(age));
        myEdit.apply();
    }
    public static String getUserStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        String name=sharedPreferences.getString("userStatus",null);
        return name;
    }

    public static void setprefSchoolFbName(Context context,String prefSchoolFbName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("prefSchoolFbName", prefSchoolFbName);
//        myEdit.putInt("age", Integer.parseInt(age));
        myEdit.apply();
    }
    public static String getprefSchoolFbName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        String name=sharedPreferences.getString("prefSchoolFbName",null);
        return name;
    }
    public static void setDownloadCount(Context context,int count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("downloadCount", count);
        myEdit.apply();
    }
    public static int getDownloadCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        int downloadCount=sharedPreferences.getInt("downloadCount",0);
        return (downloadCount);
    }
    public static void setUserDeviceDetail(Context context, UserDeviceDetailRequestModel UserDeviceDetailRequestModel) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(UserDeviceDetailRequestModel);
        prefsEditor.putString("userDeviceDetail",json);
        prefsEditor.apply();
    }
    public static UserDeviceDetailRequestModel getUserDeviceDetail(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userDeviceDetail", "");
        UserDeviceDetailRequestModel userDeviceDetailRequestModel = gson.fromJson(json, UserDeviceDetailRequestModel.class);
        return userDeviceDetailRequestModel;
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SCHOOL_LEARNING_USER_DATA,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
