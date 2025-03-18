 import express from "express";
import { create, fetch, update,deleteUser,userLogin } from "../controller/userController.js";
 const route =express.Router();

 route.post("/create_user",create);
 route.get("/getAllUsers",fetch);
 route.put("/update/:id",update);
 route.delete("/delete/:id",deleteUser);
 route.post("/create_user_login",userLogin);
 export default route;