import packages from "../models/packageModels.js"
import caterers from "../models/catererModels.js"
import mongoose from "mongoose";
/*
export const create = async (req, res) => {
    try {
        const id = req.params.id; // Extract the caterer ID from the URL
        const { package_name, items, price } = req.body;

        // Check if the caterer exists
        const catererExist = await caterers.findOne({ _id: id });
        if (!catererExist) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Check if the package already exists for this caterer
        const packageExist = await packages.findOne({ package_name, catererId: id });
        if (packageExist) {
            return res.status(400).json({ message: "Package already exists for this caterer." });
        }

        // Create a new package associated with the caterer
        const new_package = new packages({
            catererId: id, // Associate the package with the caterer's ID
            package_name,
            items,
            price
        });

        await new_package.save();
        res.status(201).json(new_package);
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};

*/
export const fetch = async (req, res) => {
    try {
        const id = req.params.id; // Get the caterer ID from the URL
        console.log("Received caterer ID:", id);

        const ObjectId = mongoose.Types.ObjectId;
        const objectId = new ObjectId(id);
        console.log("Converted ObjectId:", objectId);

        const packagesForCaterer = await packages.find({ catererId: objectId });

        console.log("Found packages:", packagesForCaterer); // Log the packages

        if (packagesForCaterer.length === 0) {
            return res.status(404).json({ message: "No packages found for this caterer." });
        }

        res.status(200).json(packagesForCaterer);
    } catch (error) {
        console.error("Error:", error);
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};





export const update = async(req, res)=>{
    try{
        const id = req.params.id;
        const packageExist=await packages.findOne({_id:id})
        if(!packageExist){
            return res.status(404).json({message:"package not found."})
        }
        const updatepackage=await packages.findByIdAndUpdate(id, req.body,{new:true})
        res.status(201).json(updatepackage);
    }catch(error){
        res.status(500).json({error:"Internal Server error."}); 
    }
};





export const deletepack = async(req, res)=>{
    try{
        const id = req.params.id;
        const userExist=await packages.findOne({_id:id})
        if(!userExist){
            return res.status(404).json({message:"not found."})
        }
        await packages.findByIdAndDelete(id);
        res.status(201).json({message:"deleted."});
    }catch(error){
        res.status(500).json({error:"Internal Server error."}); 
    }
};





export const createPackage = async (req, res) => {
    try {
        const catererId = req.params.id; // Get catererId from the URL
        const { package_name, items, price } = req.body; // Extract package data from the body

        // Check if the caterer exists
        const catererExists = await caterers.findById(catererId);
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Check if a package with the same name already exists for this caterer
        const existingPackage = await packages.findOne({ catererId, package_name });
        if (existingPackage) {
            return res.status(400).json({ message: "Package name already exists for this caterer." });
        }

        // Create a new package associated with the caterer
        const newPackage = new packages({
            catererId,  // Associate with the caterer
            package_name,
            items,
            price
        });

        // Save the new package to the database
        await newPackage.save();
        res.status(201).json({ message: "Package created successfully.", package: newPackage });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};
export const fetch_a_pack = async (req, res) => {
    try {
        const packageId = req.params.id; // Get the package ID from the URL
        console.log("Received package ID:", packageId);

        // Validate ObjectId
        if (!mongoose.Types.ObjectId.isValid(packageId)) {
            return res.status(400).json({ message: "Invalid package ID." });
        }

        // Fetch the package by its ID
        const packageData = await packages.findById(packageId);

        console.log("Found package:", packageData); // Log the package

        // If no package is found, return 404
        if (!packageData) {
            return res.status(404).json({ message: "Package not found." });
        }

        res.status(200).json(packageData);
    } catch (error) {
        console.error("Error:", error);
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};
