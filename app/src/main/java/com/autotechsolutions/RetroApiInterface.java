package com.autotechsolutions;


import com.autotechsolutions.Response.Common;
import com.autotechsolutions.Response.SplashResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetroApiInterface {

    @FormUrlEncoded
    @POST("splash")
    public Call<SplashResponse> getDataOnSplash(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/register_user")
    public Call<Common> sendMobile(@Field("mobileNumber") String mobileNumber, @Field("deviceType") String deviceType, @Field("deviceToken") String deviceToken);


    @FormUrlEncoded
    @POST("api/verify_user")
    public Call<Common> verifyUser(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("api/delete_user_car")
    public Call<Common> delete_user_car(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID);

    @FormUrlEncoded
    @POST("api/logout")
    public Call<Common> logout(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/get_user_profile_details")
    public Call<Common> get_user_profile_details(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/get_all_products_by_carID")
    public Call<Common> get_all_products_by_carID(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID);

    @FormUrlEncoded
    @POST("api/get_user_amc_details_by_car")
    public Call<Common> get_user_amc_details_by_car(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID);

    @FormUrlEncoded
    @POST("api/get_user_register_car_list")
    public Call<Common> getUserRegisterCarList(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/get_user_all_notifications")
    public Call<Common> get_user_all_notifications(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/update_user_register_details")
    public Call<Common> Registration(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("firstName") String firstName,
                                     @Field("lastName") String lastName, @Field("email") String email, @Field("areaName") String areaName, @Field("cityID") String cityID, @Field("referralCode") String referralCode);


    @FormUrlEncoded
    @POST("api/add_user_car")
    public Call<Common> addUserCar(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("makeID") String makeID,
                                   @Field("modelID") String modelID, @Field("registrationNumber") String registrationNumber,@Field("fuelType") String fuelType);

    @FormUrlEncoded
    @POST("api/add_user_car_service")
    public Call<Common> add_user_car_service(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID,
                                             @Field("branchID") String branchID, @Field("serviceDate") String serviceDate, @Field("reachability") String reachability, @Field("pickupAddress") String pickupAddress, @Field("latitude") String latitude,
                                             @Field("longitude") String longitude,@Field("serviceTime") String serviceTime);


    @Multipart
    @POST("api/add_user_car_bodyshop")
    public Call<Common> add_user_car_bodyshop(@Part("userID") String userID, @Part("accessToken") String accessToken, @Part("carID") String carID,
                                             @Part("branchID") String branchID, @Part("serviceDate") String serviceDate, @Part("reachability") String reachability, @Part("pickupAddress") String pickupAddress, @Part("latitude") String latitude,
                                             @Part("longitude") String longitude,@Part("image_data_count") String image_data_count,@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/car_insurance_quote")
    public Call<Common> car_insurance_quote(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID,
                                             @Field("insuranceCompanyID") String insuranceCompanyID, @Field("policyNumber") String policyNumber, @Field("expiryDate") String expiryDate, @Field("yearOfManufacture") String yearOfManufacture,@Field("haveInsurance") String haveInsurance);

    @FormUrlEncoded
    @POST("api/add_amc_inquiry")
    public Call<Common> add_amc_inquiry(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID,
                                             @Field("amcID") String amcID);

    @FormUrlEncoded
    @POST("api/redeem_amc_coupan")
    public Call<Common> redeem_amc_coupan(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("amcCoupanID") String amcCoupanID,
                                             @Field("pin") String pin,@Field("amcUserID") String amcUserID,@Field("amcType") String amcType);

    @FormUrlEncoded
    @POST("api/add_product_inquiry")
    public Call<Common> add_product_inquiry(@Field("userID") String userID, @Field("accessToken") String accessToken, @Field("carID") String carID,
                                             @Field("productModelID") String productModelID);

    @FormUrlEncoded
    @POST("api/add_user_address_book")
    public Call<Common> add_user_address_book(@Field("userID") String userID, @Field("accessToken") String accessToken ,@Field("title") String title, @Field("text") String text ,@Field("address") String address);

    @FormUrlEncoded
    @POST("api/get_all_user_address_book")
    public Call<Common> get_all_user_address_book(@Field("userID") String userID, @Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST("api/delete_user_address_book")
    public Call<Common> delete_user_address_book(@Field("userID") String userID, @Field("accessToken") String accessToken ,@Field("userAddressBookID") String userAddressBookID);
}
