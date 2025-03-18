import express from "express";
import multer from "multer";
import path from "path";
import { 
    cat_create, 
    cat_delete, 
    cat_fetch, 
    cat_pass, 
    cat_update, 
    catererLogin, 
    cat_fetch_id ,
    all_cat_fetch
} from "../controller/catererController.js";

// Multer storage configuration
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, "uploads/");
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + file.originalname);
    },
});

// File filter to allow only images
const fileFilter = (req, file, cb) => {
    if (file.mimetype.startsWith('image/')) {
        cb(null, true);
    } else {
        cb(new Error('Unsupported file type. Only image files are allowed'), false);
    }
};

// Configure multer to handle both license and profile image uploads
const upload = multer({ storage: storage, fileFilter });

const route = express.Router();

// Allow license and profile image uploads in create and update routes
route.post("/cat_create", upload.fields([{ name: "license" }, { name: "Profile" }]), cat_create);
route.put("/update/:id", upload.fields([{ name: "license" }, { name: "Profile" }]), cat_update);

route.get("/GetAllCaterers", cat_fetch);
route.delete("/delete/:id", cat_delete);

route.post("/caterers/login", catererLogin);
route.post("/pass/:id", cat_pass);
route.get("/caterers/:id", cat_fetch_id);
route.get("/GetAllCatererss", all_cat_fetch);
export default route;
