import express from "express";
import mongoose from "mongoose";
import bodyParser from "body-parser";
import dotenv from "dotenv";
import u from "./routes/userRoutes.js";
import m from "./routes/menuRoutes.js";
import p from "./routes/packageRoutes.js";
import o from "./routes/orderRoutes.js";
import r from "./routes/reviewRoutes.js";
import c from "./routes/catererRoutes.js";
import a from "./routes/availabilityRoutes.js";
import pa from "./routes/catererRoutes.js";
import admin from "./routes/adminRoutes.js";
import lo from "./routes/catererRoutes.js";
import ulo from "./routes/userRoutes.js";

dotenv.config();

const app = express();
app.use(bodyParser.json());

const PORT = process.env.PORT || 5000;
const MONGOURL = process.env.MONGO_URL;

mongoose
  .connect(MONGOURL)
  .then(() => {
    console.log("Database connected successfully.");
    app.listen(PORT, () => {
      console.log(`Server is running on port ${PORT}`);
    });
  })
  .catch((error) => {
    console.error("MongoDB connection error:", error.message);
  });

  app.use("/api/user",u);
  app.use("/api/caterer",c);
  app.use("/api/menu",m);
  app.use("/api/packages",p);
  app.use("/api/order",o);
  app.use("/api/reviews",r);
  app.use("/api/avail",a);
  app.use("/api/passwords",pa);
  app.use("/api/admin",admin);
  app.use("/api/login",lo);
  app.use("/api/ulogin",ulo);
  