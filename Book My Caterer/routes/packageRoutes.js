import express from "express";
import { fetch, createPackage,update,deletepack,fetch_a_pack } from "../controller/packageController.js";
 const route =express.Router();


 
  route.post("/create/:id",createPackage);
  route.get('/GEtAllPackages/:id', fetch);
  route.put('/update/:id', update);
  route.delete('/delete/:id', deletepack);
  route.get('/GEtAPackage/:id', fetch_a_pack);
  export default route;