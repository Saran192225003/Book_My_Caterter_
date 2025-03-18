package com.example.bookmycaterer.user

data class UserRequest(
    val name: String,
    val email: String,
    val phone: String,
    val City: String,
    val password: String
)

data class UserResponse(
    val success: Boolean,
    val message: String,
    val data: UserData?
)

data class UserData(
    val name: String,
    val email: String,
    val phone: String,
    val City: String,
    val password: String,
    val _id: String,
    val __v: Int
)

data class LoginRequest(
    val phone: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val User: UserData
)


data class CatererLoginResponse(
    val success: Boolean,
    val message: String,
    val caterer: CatererLoginData?
)

data class CatererLoginData(
    val name: String,
    val Catering_name: String,
    val Business_Size: String,
    val City: String,
    val phone: String,
    val id: String,
)

data class CatererSignUpRequest(
    val name: String,
    val Catering_name: String,
    val phone: String,
    val Business_Size: String,
    val City: String,
    val license: String
)

data class CatererData(
    val name: String,
    val Catering_name: String,
    val phone: String,
    val Business_Size: String,
    val City: String,
    val license: String,
    val _id: String,
    val __v: Int,

)

data class CatererSignUpResponse(
    val success: Boolean,
    val message: String,
    val data: CatererData?
)

data class MenuRequest(
    val item_name: String,
    val price: String
)

data class MenuResponse(
    val message: String,
    val item: MenuItem
)

data class MenuItem(
    val catererId: String,
    val item_name: String,
    val price: String,
    val _id: String,
    val __v: Int
)

data class UpdateMenuRequest(
    val item_name: String,
    val price: String
)


data class MenuPackage(
    val _id: String,
    val catererId: String,
    val package_name: String,
    val items: String,
    val price: String,
    val __v: Int
)

//data class Order(
//    val catering_name: String,
//    val date: String,
//    val time_slot: String,
//    val number_of_heads: String,
//    val contact: String,
//    val event_location: String,
//    val event_type: String,
//    val selected_package: String,
//    val extra_menu: String
//)


data class UserOrdersResponse(
    val success: Boolean,
    val orders: List<OrderResponse>
)

data class OrderResponse(
    val _id: String,
    val userId: String,
    val catererId: String,
    val catering_name: String,
    val date: String,
    val time_slot: String,
    val number_of_heads: String,
    val contact: String,
    val event_location: String,
    val event_type: String,
    val selected_package: String,
    val extra_menu: String,
    val price: String,
    val isAccepted: String,
    val paymentID: String,
    val paymentStatus: String
)





data class OrderAcceptResponse(
    val message: String,
    val updatedCaterer: OrderResponse
)


data class RemoveCatererResponse(
    val message: String
)

data class ApproveResponse(
    val message: String,
    val updatedCaterer: UpdatedCaterer
)

data class UpdatedCaterer(
    val _id: String,
    val name: String,
    val Catering_name: String,
    val phone: String,
    val Business_Size: String,
    val City: String,
    val license: String,
    val isApproved: Boolean
)

data class RejectRequest(
    val reason: String
)


data class RejectResponse(
    val message: String,
    val updatedCaterer: UpdatedCaterer
)




data class ReviewResponse(
    val _id: String,
    val write: String,
    val rate: String,
    val catererId: String,
    val orderId: String,
    val __v: Int
)


data class PackageRequest(
    val package_name: String,
    val items: String,
    val price: String
)

data class PackageResponse(
    val message: String,
    val packages: PackageDetails
)

data class PackageDetails(
    val catererId: String,
    val package_name: String,
    val items: String,
    val price: String,
    val _id: String
)

data class ReviewsResponse(
    val success: Boolean,
    val catererId: String,
    val reviews: List<ReviewData>
)

data class ReviewData(
    val _id: String,
    val catererId: String,
    val orderId: OrderResponse,
    val write: String,
    val rate: String
)


//data class Order(
//    val _id: String,
//    val userId: String,
//    val catererId: String,
//    val catering_name: String,
//    val date: String,
//    val time_slot: String,
//    val number_of_heads: String,
//    val contact: String,
//    val event_location: String,
//    val event_type: String,
//    val selected_package: String,
//    val extra_menu: String,
//    val price: String?,
//    val isAccepted: String,
//    val paymentID: String?,
//    val paymentStatus: String?
//)

data class OrderRequest(
    val catering_name: String,
    val date: String,
    val time_slot: String,
    val number_of_heads: String,
    val contact: String,
    val event_location: String,
    val event_type: String,
    val selected_package: String,
    val extra_menu: String,
    val price: String
)

data class AvailabilityRequest(
    val date: String,
    val time_slot: String
)

data class AvailabilityResponse(
    val _id: String,
    val catererId: String,
    val date: String,
    val time_slot: String
)


data class PaymentRequest(
    val paymentID: String
)

data class PaymentResponse(
    val success: Boolean,
    val message: String,
    val updatedOrder: OrderResponse?
)



data class BookedSlot(
    val _id: String,
    val catererId: String,
    val date: String,
    val time_slot: String
)


data class CatererProfileResponse(
    val _id: String,
    val name: String,
    val Catering_name: String,
    val phone: String,
    val Business_Size: String,
    val City: String,
    val Food_Type: String,   // ✅ Added Food Type
    val Profile: String?,    // ✅ Added Profile Picture URL (Nullable)
    val license: String,
    val isApproved: Boolean
)
