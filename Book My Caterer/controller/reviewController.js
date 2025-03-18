import reviews from "../models/reviewModels.js"
import caterers from '../models/catererModels.js'
import order from '../models/orderModels.js';

import mongoose from "mongoose";
import Review from "../models/reviewModels.js";
import Order from "../models/orderModels.js";
import Caterer from "../models/catererModels.js";



export const create = async (req, res) => {
    try {
        const catererId = req.params.id; 
        const orderId = req.params.orderId;
        const { write, rate } = req.body;

        const catererExists = await caterers.findById(catererId);
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        const orderExists = await order.findById(orderId);
        if (!orderExists) {
            return res.status(404).json({ message: "Order not found." });
        }

        const existingReview = await reviews.findOne({ orderId, catererId });
        if (existingReview) {
            return res.status(400).json({ message: "Review for this order and caterer already exists." });
        }

        const newReview = new reviews({
            write,
            rate,
            catererId,  
            orderId   
        });

        await newReview.save();

        res.status(201).json({
            _id: newReview._id,
            write: newReview.write,
            rate: newReview.rate,
            catererId: newReview.catererId,
            orderId: newReview.orderId,
            __v: newReview.__v
        });
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};




export const fetch = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL

        // Fetch reviews associated with the specific caterer
        const reviewsForCaterer = await reviews.find({ catererId: catererId });

        if (reviewsForCaterer.length === 0) {
            return res.status(404).json({ message: "No reviews found for this caterer." });
        }

        res.status(200).json(reviewsForCaterer);  // Return the list of reviews
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};


export const getReviewsForCaterer = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL

        // Check if the caterer exists
        const catererExists = await order.findById(catererId);
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Fetch all reviews for the caterer
        const reviewsForCaterer = await reviews.find({ catererId });

        // Return the reviews
        res.status(200).json(reviewsForCaterer);
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};




export const createReviewForOrderIDByUserID = async (req, res) => {
    try {
        const { userId, orderId } = req.params;
        const { write, rate } = req.body;

        if (!mongoose.Types.ObjectId.isValid(userId) || !mongoose.Types.ObjectId.isValid(orderId)) {
            return res.status(400).json({ message: "Invalid User ID or Order ID." });
        }

        // Check if order exists and belongs to the user, and is completed
        const order = await Order.findOne({ _id: orderId, userId: userId, paymentStatus: "true" }).lean();

        if (!order) {
            return res.status(404).json({ message: "Order not found or not completed." });
        }

        // Check if a review already exists for this order
        const existingReview = await Review.findOne({ orderId: orderId });
        if (existingReview) {
            return res.status(400).json({ message: "Review already submitted for this order." });
        }

        // Create a new review
        const newReview = new Review({
            catererId: order.catererId,
            orderId: orderId,
            write,
            rate
        });

        await newReview.save();

        // Fetch all reviews for this order
        const reviews = await Review.find({ orderId: orderId }).lean();

        // Attach reviews to the order
        const updatedOrder = {
            ...order,
            reviews
        };

        res.status(201).json({
            success: true,
            message: "Review submitted successfully.",
            order: updatedOrder
        });

    } catch (error) {
        console.error("Error creating review:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};


export const getReviewsForCatererByCatererID = async (req, res) => {
    try {
        const { catererId } = req.params;

        if (!mongoose.Types.ObjectId.isValid(catererId)) {
            return res.status(400).json({ message: "Invalid Caterer ID." });
        }

        // Ensure the caterer exists
        const catererExists = await Caterer.findById(catererId).lean();
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Fetch reviews with full order details
        const reviews = await Review.find({ catererId })
            .populate({
                path: "orderId",
                model: Order,
                select: "-__v" // Exclude version key
            })
            .lean();

        if (!reviews.length) {
            return res.status(404).json({ message: "No reviews found for this caterer." });
        }

        res.status(200).json({
            success: true,
            catererId: catererId,
            reviews: reviews
        });

    } catch (error) {
        console.error("Error fetching reviews:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};
