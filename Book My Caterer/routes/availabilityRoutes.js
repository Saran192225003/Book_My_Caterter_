import express from "express";
import { create, fetch,  } from "../controller/availabiltyController.js";
 const route =express.Router();


 
  route.post("/create/:id",create);
 route.get("/getAllAvail/:id",fetch);
 
  export default route;