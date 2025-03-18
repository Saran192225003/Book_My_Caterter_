import express from "express";
import {approve_caterer,reject_caterer,approved_cat_fetch} from "../controller/adminController.js";

const route = express.Router();

route.put("/caterers/:id/approve", approve_caterer);
route.put("/caterers/:id/reject", reject_caterer);
route.get("/caterers/approved_cat",approved_cat_fetch );
export default route;
