import express from "express";
import { create, createReviewForOrderIDByUserID, fetch,getReviewsForCaterer, getReviewsForCatererByCatererID} from "../controller/reviewController.js";
 const route =express.Router();


 
 route.post("/create/:orderId/:id", create);
 route.get("/reviews/:id", getReviewsForCaterer);
 route.get("/getAllReviews/:id",fetch);

 route.post("/createReview/:userId/:orderId", createReviewForOrderIDByUserID)
 route.get("/getReviewsByCatererID/:catererId", getReviewsForCatererByCatererID)
 
 export default route;