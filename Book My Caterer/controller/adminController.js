import Caterer from "../models/catererModels.js"; 

// Adjust the path to match your project structure

export const approve_caterer = async (req, res) => {
    try {
        const { id } = req.params; // Get the caterer ID from the route parameters

        // Find the caterer by ID and update the isApproved field to true
        const updatedCaterer = await Caterer.findByIdAndUpdate(
            id,
            { isApproved: true },
            { new: true } // Return the updated document
        );

        // Check if the caterer exists
        if (!updatedCaterer) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        res.status(200).json({ message: "Caterer approved successfully.", updatedCaterer });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};



export const reject_caterer = async (req, res) => {
    try {
        const { id } = req.params; // Get the caterer ID from the route parameters

        // Find the caterer by ID and update the isApproved field to true
        const updatedCaterer = await Caterer.findByIdAndUpdate(
            id,
            { isReject: true },
            { new: true } // Return the updated document
        );

        // Check if the caterer exists
        if (!updatedCaterer) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        res.status(200).json({ message: "Caterer rejected.", updatedCaterer });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};





export const approved_cat_fetch=async(req, res)=>{
    try{
      const cats = await Caterer.find({isApproved:true});
      if(cats.length==0){
        return res.status(404).json({message:"caterer not found"});
      }
      res.status(200).json(cats);

  }catch(error){
        res.status(500).json({error:"Internal Server error."+error});
    }
};