import express from "express";
import { create, fetch, update } from "../controller/menuController.js";
 const route =express.Router();


 
  route.post("/create/:id",create);
 route.get("/getAllMenu/:id",fetch);
  route.put("/update/:id",update);
  export default route;