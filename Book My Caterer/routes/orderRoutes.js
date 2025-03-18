import express from "express";
import { create, fetchUpcomingOrdersByCatererID, acceptOrderByCatererID, fetchAcceptedOrders, fetchOrderByUserId, rejectOrderByCatererID,getCompletedOrdersByUserID,
    paymentOfAcceptedOrderByOrderID,fetchPaidOrders
} from "../controller/orderController.js";
 const route =express.Router();


 route.get("/accepted_ord/:catererId",fetchAcceptedOrders );
 route.post("/create/:catererId/:userId", create);
 route.get("/getAllOrders/:catererId",fetchUpcomingOrdersByCatererID);
 route.get("/user/getAllOrders/:userId",fetchOrderByUserId);
 route.put("/accept/:orderId", acceptOrderByCatererID);
 route.put("/reject/:orderId", rejectOrderByCatererID);
 route.post("/payment/:orderId", paymentOfAcceptedOrderByOrderID)
 route.get("/completed/:userId", getCompletedOrdersByUserID);
 route.get("/getAllPaid/:catererId",fetchPaidOrders);
 export default route;