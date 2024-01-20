package rsarschoolmodel.com.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rsarschoolmodel.com.modelClass.request.BookRequestModel;
import rsarschoolmodel.com.modelClass.request.ChapterRequestModel;
import rsarschoolmodel.com.modelClass.request.ClassListRequestModel;
import rsarschoolmodel.com.modelClass.request.ContactUsRequestModel;
import rsarschoolmodel.com.modelClass.request.ForgetDetailRequestModel;
import rsarschoolmodel.com.modelClass.request.HelpBannerRequestModel;
import rsarschoolmodel.com.modelClass.request.OtpSendRequestModel;
import rsarschoolmodel.com.modelClass.request.OtpVerifyRequestModel;
import rsarschoolmodel.com.modelClass.request.SubjectRequestModel;
import rsarschoolmodel.com.modelClass.request.UserLoginRequestModel;
import rsarschoolmodel.com.modelClass.response.BookResponseModel;
import rsarschoolmodel.com.modelClass.response.ChapterResponseModel;
import rsarschoolmodel.com.modelClass.response.ClassListResponseModel;
import rsarschoolmodel.com.modelClass.response.ContactUsResponseModel;
import rsarschoolmodel.com.modelClass.response.ForgetDetailResponseModel;
import rsarschoolmodel.com.modelClass.response.HelpBannerResponseModel;
import rsarschoolmodel.com.modelClass.response.OtpSendResponseModel;
import rsarschoolmodel.com.modelClass.response.OtpVerifyResponseModel;
import rsarschoolmodel.com.modelClass.response.SubjectResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;

public interface MyAPIs

{
    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/activation")
    Call<UserLoginResponseModel> loginUser(@Body UserLoginRequestModel userLoginRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/banners")
    Call<HelpBannerResponseModel> getHelpDetail(@Body HelpBannerRequestModel helpBannerRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/sendOtp")
    Call<OtpSendResponseModel> otpSend(@Body OtpSendRequestModel otpSendRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/contact")
    Call<ContactUsResponseModel> getContactDetail(@Body ContactUsRequestModel contactUsRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/forgotDetails")
    Call<ForgetDetailResponseModel> forgotDetails(@Body ForgetDetailRequestModel forgetDetailRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/class")
    Call<ClassListResponseModel> getClassList(@Body ClassListRequestModel classListRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/otpVerify")
    Call<OtpVerifyResponseModel> otpVerify(@Body OtpVerifyRequestModel otpVerifyRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/subject")
    Call<SubjectResponseModel> getSubjectList(@Body SubjectRequestModel subjectRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/book")
    Call<BookResponseModel> getBookList(@Body BookRequestModel bookRequestModel);

    @Headers("rsarslkey: schoolmodel")
    @POST("rsarsl/chapter")
    Call<ChapterResponseModel> getChapterList(@Body ChapterRequestModel chapterRequestModel);


}
