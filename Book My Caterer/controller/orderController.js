import mongoose from "mongoose";  // ✅ Keep this only once
import Order from "../models/orderModels.js";
import Caterer from '../models/catererModels.js';
import User from '../models/userModels.js';
import reviews from "../models/reviewModels.js";
import moment from "moment"; 

export const create = async (req, res) => {
    try {
        const { catererId, userId } = req.params; 
        const {catering_name, date, time_slot, number_of_heads, contact, event_location, event_type, selected_package, extra_menu, price } = req.body;

        if (!userId) {
            return res.status(400).json({ message: "User ID is required." });
        }

        const catererExists = await Caterer.findById(catererId).lean();
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        const userExists = await User.findById(userId).lean();
        if (!userExists) {
            return res.status(404).json({ message: "User not found." });
        }

        const newOrder = new Order({
            catering_name,
            date,
            time_slot,
            number_of_heads,
            contact,
            event_location,
            event_type,
            selected_package,
            extra_menu,
            catererId,
            userId,
            price,
            isAccepted: "false"  // ✅ Default status on creation
        });

        const savedOrder = await newOrder.save();

        res.status(201).json({
            ...savedOrder.toObject(),
            userId: savedOrder.userId
        });

    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};






export const fetchOrderByUserId = async (req, res) => {
    try {
        const { userId } = req.params;

        if (!userId || !mongoose.Types.ObjectId.isValid(userId)) {
            return res.status(400).json({ message: "Invalid or missing User ID." });
        }

        const userOrders = await Order.find({ userId }).lean().exec();

        if (!userOrders || userOrders.length === 0) {
            return res.status(404).json({ message: "No orders found for this user." });
        }

        // Get today's date (without time) for accurate comparison
        const currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0);

        // Filter orders to include only today and future orders
        const upcomingOrders = userOrders.filter(order => {
            if (!order.date) return false;

            const [day, month, year] = order.date.split("/").map(Number);
            const orderDate = new Date(year, month - 1, day); // Convert to Date object

            return orderDate >= currentDate; // Keep only upcoming orders
        });

        if (upcomingOrders.length === 0) {
            return res.status(404).json({ message: "No upcoming orders found." });
        }

        res.status(200).json({ success: true, orders: upcomingOrders });
    } catch (error) {
        console.error("Error fetching orders:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};



export const fetchUpcomingOrdersByCatererID = async (req, res) => {
    try {
        const catererId = req.params.catererId;
        const currentDate = new Date(); // Get today's date

        const allOrders = await Order.find({ catererId: catererId });

        // Convert "DD/MM/YYYY" string to actual Date object and filter expired orders
        const currentOrders = allOrders.filter(order => {
            const [day, month, year] = order.date.split("/").map(Number);
            const orderDate = new Date(year, month - 1, day); // Convert to JavaScript Date

            return orderDate >= currentDate; // Only keep future orders
        });

        if (currentOrders.length === 0) {
            return res.status(404).json({ message: "No current orders found for this caterer." });
        }

        res.status(200).json(currentOrders);
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};





export const acceptOrderByCatererID = async (req, res) => {
    try {
        const { orderId } = req.params;

        // Check if the order exists
        const existingOrder = await Order.findById(orderId);
        if (!existingOrder) {
            return res.status(404).json({ message: "Order not found." });
        }

        // Check if the order is already accepted
        if (existingOrder.isAccepted === "accepted") {
            return res.status(400).json({ message: "Order already accepted." });
        }

        // Update the order to accepted
        existingOrder.isAccepted = "accepted";
        await existingOrder.save();

        res.status(200).json({ message: "Order accepted.", updatedOrder: existingOrder });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};


export const accepted_ord_fetch = async (req, res) => {
    try {
        const currentDate = new Date(); // Get the current date

        // Fetch only accepted orders
        const allAcceptedOrders = await Order.find({ isAccepted: "accepted" });

        // Convert "DD/MM/YYYY" to Date object and filter only upcoming orders
        const upcomingAcceptedOrders = allAcceptedOrders.filter(order => {
            if (!order.date) return false; // Skip if date is missing

            const [day, month, year] = order.date.split("/").map(Number);
            const orderDate = new Date(year, month - 1, day); // Convert to JavaScript Date

            // Ensure only future orders are returned (today's orders excluded if needed)
            return orderDate.getTime() >= currentDate.setHours(0, 0, 0, 0);
        });

        if (upcomingAcceptedOrders.length === 0) {
            return res.status(404).json({ message: "No upcoming accepted orders found." });
        }

        res.status(200).json(upcomingAcceptedOrders);

    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};



export const rejectOrderByCatererID = async (req, res) => {
    try {
        const { orderId } = req.params;

        const updatedOrder = await Order.findByIdAndUpdate(
            orderId,
            { isAccepted: "rejected" },  // ✅ Set to "rejected"
            { new: true }
        );

        if (!updatedOrder) {
            return res.status(404).json({ message: "Order not found." });
        }

        res.status(200).json({ message: "Order rejected.", updatedOrder });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};



export const fetchAcceptedOrders = async (req, res) => {
    try {
        const catererId = req.params.catererId;
        const currentDate = new Date(); // Get today's date

        // Fetch all accepted orders
        const allAcceptedOrders = await Order.find({ catererId: catererId, isAccepted: "accepted" });

        // Convert "DD/MM/YYYY" string to actual Date object and filter expired orders
        const upcomingAcceptedOrders = allAcceptedOrders.filter(order => {
            const [day, month, year] = order.date.split("/").map(Number);
            const orderDate = new Date(year, month - 1, day); // Convert to JavaScript Date

            return orderDate >= currentDate; // Only keep today and future orders
        });

        if (upcomingAcceptedOrders.length === 0) {
            return res.status(404).json({ message: "No upcoming accepted orders found for this caterer." });
        }

        res.status(200).json(upcomingAcceptedOrders);
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};





export const paymentOfAcceptedOrderByOrderID = async (req, res) => {
    try {
        const { orderId } = req.params;
        const { paymentID } = req.body; 

        if (!mongoose.Types.ObjectId.isValid(orderId)) {
            return res.status(400).json({ message: "Invalid Order ID." });
        }

        const order = await Order.findById(orderId);
        if (!order) {
            return res.status(404).json({ message: "Order not found." });
        }

        if (order.isAccepted !== "accepted") {
            return res.status(400).json({ message: "Order is not accepted yet." });
        }

        if (!paymentID) {
            return res.status(400).json({ message: "Payment ID is required." });
        }

        order.paymentID = paymentID;
        order.paymentStatus = true; 
        await order.save();

        res.status(200).json({ success: true, message: "Payment updated successfully.", updatedOrder: order });
    } catch (error) {
        console.error("Error processing payment:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};




export const getCompletedOrdersByUserID = async (req, res) => {
    try {
        const { userId } = req.params;

        if (!mongoose.Types.ObjectId.isValid(userId)) {
            return res.status(400).json({ message: "Invalid User ID." });
        }

        const currentDate = moment().startOf("day");

        const orders = await Order.find({
            userId: userId,
            paymentStatus: true
        }).lean(); 

        const expiredOrders = orders.filter(order => {
            const orderDate = moment(order.date, "DD/MM/YYYY").startOf("day");
            return orderDate.isBefore(currentDate);
        });

        if (expiredOrders.length === 0) {
            return res.status(404).json({ message: "No completed orders found." });
        }

        res.status(200).json({ success: true, orders: expiredOrders });
    } catch (error) {
        console.error("Error fetching orders:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};


export const fetchPaidOrders = async (req, res) => {
    try {
        const catererId = req.params.catererId; // Get caterer ID from request params

        // Fetch orders where paymentStatus is "true"
        const paidOrders = await Order.find(
            { catererId: catererId, paymentStatus: "true" },
            "userId catererId catering_name date time_slot number_of_heads contact event_location event_type selected_package extra_menu price isAccepted paymentStatus paymentID"
        ).lean();

        if (paidOrders.length === 0) {
            return res.status(404).json({ message: "No paid orders found for this caterer." });
        }

        res.status(200).json(paidOrders);
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};
