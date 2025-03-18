package com.example.bookmycaterer.api

import OrderHistoryData
import com.example.bookmycaterer.caterer.SampleUpdatePackage
import com.example.bookmycaterer.user.ApproveResponse
import com.example.bookmycaterer.user.AvailabilityRequest
import com.example.bookmycaterer.user.AvailabilityResponse
import com.example.bookmycaterer.user.BookedSlot
import com.example.bookmycaterer.user.CatererData
import com.example.bookmycaterer.user.CatererLoginResponse
import com.example.bookmycaterer.user.CatererProfileResponse
import com.example.bookmycaterer.user.CatererSignUpResponse
import com.example.bookmycaterer.user.LoginRequest
import com.example.bookmycaterer.user.LoginResponse
import com.example.bookmycaterer.user.MenuItem
import com.example.bookmycaterer.user.MenuPackage
import com.example.bookmycaterer.user.MenuRequest
import com.example.bookmycaterer.user.MenuResponse
import com.example.bookmycaterer.user.OrderAcceptResponse
import com.example.bookmycaterer.user.OrderRequest

import com.example.bookmycaterer.user.OrderResponse

import com.example.bookmycaterer.user.PackageRequest
import com.example.bookmycaterer.user.PackageResponse
import com.example.bookmycaterer.user.PaymentRequest
import com.example.bookmycaterer.user.PaymentResponse

import com.example.bookmycaterer.user.RejectRequest
import com.example.bookmycaterer.user.RejectResponse
import com.example.bookmycaterer.user.RemoveCatererResponse
import com.example.bookmycaterer.user.ReviewData
import com.example.bookmycaterer.user.ReviewResponse
import com.example.bookmycaterer.user.ReviewsResponse
import com.example.bookmycaterer.user.SampleUserHome
import com.example.bookmycaterer.user.UpdateMenuRequest
import com.example.bookmycaterer.user.UserOrdersResponse
import com.example.bookmycaterer.user.UserRequest
import com.example.bookmycaterer.user.UserResponse

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("user/create_user")
    fun createUser(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("ulogin/create_user_login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("login/caterers/login")
    fun loginCaterer(@Body request: LoginRequest): Call<CatererLoginResponse>

    @Multipart
    @POST("caterer/cat_create")
    fun createCaterer(
        @Part("name") name: RequestBody,
        @Part("Catering_name") cateringName: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("Business_Size") businessSize: RequestBody,
        @Part("City") city: RequestBody,
        @Part license: MultipartBody.Part
    ): Call<CatererSignUpResponse>

    @GET("caterer/GetAllCaterers")
    fun getCaterers(): Call<List<SampleUserHome>>

    @GET("admin/caterers/approved_cat")
    fun getApprovedCaterers(): Call<List<SampleUserHome>>

    @POST("menu/create/{catererId}")
    fun createMenu(
        @Path("catererId") catererId: String,
        @Body request: MenuRequest
    ): Call<MenuResponse>


    @GET("menu/GetAllMenu/{catererId}")
    fun getAllMenu(@Path("catererId") catererId: String): Call<List<MenuItem>>
    @PUT("menu/update/{menuId}")
    fun updateMenuPrice(
        @Path("menuId") menuId: String,
        @Body updatedMenu: UpdateMenuRequest
    ): Call<Void>

    @GET("packages/GEtAllPackages/{catererId}")
    fun getAllPackages(@Path("catererId") catererId: String): Call<List<MenuPackage>>

    @POST("order/create/{catererId}/{userId}")
    fun createOrder(
        @Path("catererId") catererId: String,
        @Path("userId") userId: String,
        @Body order: OrderRequest
    ): Call<OrderResponse>


    @GET("caterer/caterers/{id}")
    fun getCatererDetails(@Path("id") id: String): Call<SampleUserHome>

    @PUT("admin/caterers/{catererId}/approve")
    fun approveCaterer(@Path("catererId") catererId: String): Call<ApproveResponse>
    @PUT("admin/caterers/{catererId}/reject")
    fun rejectCaterer(
        @Path("catererId") catererId: String,
        @Body rejectReason: RejectRequest
    ): Call<RejectResponse>


        @DELETE("caterer/delete/{id}")
        fun removeCaterer(@Path("id") catererId: String): Call<RemoveCatererResponse>



    @POST("packages/create/{catererId}")
    fun createPackage(
        @Path("catererId") catererId: String,
        @Body request: PackageRequest
    ): Call<PackageResponse>
    @PUT("packages/update/{packageId}")
    fun updatePackagePrice(
        @Path("packageId") packageId: String,
        @Body updateRequest: HashMap<String, Any>
    ): Call<MenuPackage>

    @GET("packages/GEtAPackage/{id}")
    fun getPackageDetails(@Path("id") packageId: String): Call<MenuPackage>
    @GET("api/packages/{packageId}/menuItems")
    fun getMenuItemsForPackage(@Path("packageId") packageId: String): Call<List<SampleUpdatePackage>>
    @PUT("packages/update/{packageId}")
    fun updatePackage(
        @Path("packageId") packageId: String,
        @Body updateRequest: HashMap<String, Any>
    ): Call<MenuPackage>

    @GET("order/getAllOrders/{catererId}")
    fun getAllOrders(@Path("catererId") catererId: String): Call<List<OrderResponse>>

    @PUT("order/accept/{orderId}")
    fun acceptOrder(
        @Path("orderId") orderId: String,
        @Body requestBody: Map<String, String>
    ): Call<OrderAcceptResponse>

    @PUT("order/reject/{orderId}")
    fun rejectOrder(
        @Path("orderId") orderId: String,
        @Body requestBody: Map<String, String>
    ): Call<OrderAcceptResponse>

    @GET("order/accepted_ord/{catererId}")
    fun getAcceptedOrders(@Path("catererId") catererId: String): Call<List<OrderResponse>>

    @GET("order/user/getAllOrders/{userId}")
    fun getUserOrders(
        @Path("userId") userId: String
    ): Call<UserOrdersResponse>

    @GET("order/completed/{userId}")
    fun getCompletedOrders(
        @Path("userId") userId: String
    ): Call<UserOrdersResponse>


/*    @GET("order/user/GetAllOrders/{userId}")
    fun getUserOrderHistory(@Path("userId") userId: String): Call<List<OrderHistoryData>>

    @GET("reviews/GetAllReviews/{catererId}")
    fun getReviews(@Path("catererId") catererId: String): Call<List<ReviewData>>


    @POST("reviews/create/{orderId}/{catererId}")
    fun postReview(
        @Path("orderId") orderId: String,  // Order ID as part of the URL
        @Path("catererId") catererId: String,  // Caterer ID as part of the URL
        @Body reviewData: ReviewData  // Review data in the body
    ): Call<ReviewResponse>*/

    @GET("reviews/getReviewsByCatererID/{catererId}")
    fun getReviewsByCatererID(@Path("catererId") catererId: String): Call<ReviewsResponse>

    @POST("avail/create/{catererId}")
    fun createAvailability(
        @Path("catererId") catererId: String,
        @Body request: AvailabilityRequest
    ): Call<AvailabilityResponse>


    @GET("avail/GetAllAvail/{catererId}")
    fun getAllAvailability(
        @Path("catererId") catererId: String
    ): Call<List<AvailabilityResponse>>



    @GET("avail/GetAllAvail/{catererId}")
    fun getCatererAvailability(@Path("catererId") catererId: String): Call<List<AvailabilityResponse>>

    @GET("avail/GetAllAvail/{catererId}")
    fun getBookedSlots(@Path("catererId") catererId: String): Call<List<BookedSlot>>

    @GET("caterer/caterers/{id}")
    fun getCatererProfile(@Path("id") catererId: String): Call<CatererProfileResponse>

    @Multipart
    @PUT("caterer/update/{id}")
    fun updateCatererProfile(
        @Path("id") catererId: String,
        @Part("Business_Size") businessSize: RequestBody,
        @Part("FoodType") foodType: RequestBody,
        @Part("City") city: RequestBody,
        @Part profile: MultipartBody.Part? // Profile Image (optional)
    ): Call<CatererProfileResponse>


        @GET("caterer/caterers/{id}")
        fun getCatererById(@Path("id") id: String): Call<CatererData>

    @POST("api/order/payment/{orderId}")
    fun updatePaymentStatus(
        @Path("orderId") orderId: String,
        @Body paymentRequest: PaymentRequest
    ): Call<PaymentResponse>

}
