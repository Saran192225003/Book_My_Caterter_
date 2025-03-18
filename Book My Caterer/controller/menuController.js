import menu from "../models/menuModels.js"

import caterers from '../models/catererModels.js';  // Import the caterers model

export const create = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL
        const { item_name, price } = req.body;  // Extract menu item data from the body

        // Check if the caterer exists
        const catererExists = await caterers.findById(catererId);
        if (!catererExists) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Check if the menu item already exists
        const menuExist = await menu.findOne({ item_name, catererId });
        if (menuExist) {
            return res.status(400).json({ message: "Menu item already exists." });
        }

        // Create a new menu item associated with the caterer
        const newItem = new menu({
            item_name,
            price,
            catererId  // Associate with the caterer
        });

        // Save the new menu item to the database
        await newItem.save();
        res.status(201).json({ message: "Menu item created successfully.", item: newItem });
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};



export const fetch = async (req, res) => {
    try {
        const catererId = req.params.id;  // Get catererId from the URL

        // Fetch menu items associated with the specific caterer
        const menuItemsForCaterer = await menu.find({ catererId: catererId });

        if (menuItemsForCaterer.length === 0) {
            return res.status(404).json({ message: "No menu items found for this caterer." });
        }

        res.status(200).json(menuItemsForCaterer);
    } catch (error) {
        res.status(500).json({ error: "Internal Server error: " + error.message });
    }
};




export const update = async(req, res)=>{
    try{
        const id = req.params.id;
        const menuExist=await menu.findOne({_id:id})
        if(!menuExist){
            return res.status(404).json({message:"item not found."})
        }
        const updateprice=await menu.findByIdAndUpdate(id, req.body,{new:true})
        res.status(201).json(updateprice);
    }catch(error){
        res.status(500).json({error:"Internal Server error."+error}); 
    }
};
