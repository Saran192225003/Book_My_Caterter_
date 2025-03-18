import caterers from "../models/catererModels.js";

export const cat_create = async (req, res) => {
    try {
        const { name, Catering_name, phone, Business_Size, City } = req.body;

        // Check if the caterer already exists
        const catExist = await caterers.findOne({ phone });
        if (catExist) {
            return res.status(400).json({ message: "Caterer already exists." });
        }

        // Handle file uploads (license and Profile)
        const license = req.files?.license ? `uploads/${req.files.license[0].filename}` : "";
        const Profile = req.files?.Profile ? `uploads/${req.files.Profile[0].filename}` : "";

        // Create a new caterer object
        const newcat = new caterers({
            name,
            Catering_name,
            phone,
            Business_Size,
            City,
            license,
            Profile
        });

        // Save to the database
        await newcat.save();
        res.status(200).json({ success: true, message: "Request sent successfully", data: newcat });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};




export const cat_fetch = async (req, res) => {
    try {
        const cats = await caterers.find({
            isApproved: { $ne: true }, // Exclude approved caterers
            isReject: { $ne: true }   // Exclude rejected caterers
        });

        if (cats.length === 0) {
            return res.status(404).json({ message: "No caterers found" });
        }

        res.status(200).json(cats);
    } catch (error) {
        res.status(500).json({ error: "Internal Server error." });
    }
};





export const cat_update = async (req, res) => {
    try {
        const id = req.params.id;
        const catExist = await caterers.findOne({ _id: id });
        if (!catExist) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Prepare update data
        const updateData = { ...req.body };
        if (req.files?.license) {
            updateData.license = req.files.license[0].path;
        }
        if (req.files?.Profile) {
            updateData.Profile = req.files.Profile[0].path;
        }

        // Update the caterer
        const updatedCaterer = await caterers.findByIdAndUpdate(id, updateData, { new: true });
        res.status(201).json(updatedCaterer);
    } catch (error) {
        res.status(500).json({ error: "Internal Server error." });
    }
};


export const cat_delete = async(req, res)=>{
    try{
        const id = req.params.id;
        const userExist=await caterers.findOne({_id:id})
        if(!userExist){
            return res.status(404).json({message:"caterer not found."})
        }
        await caterers.findByIdAndDelete(id);
        res.status(201).json({message:"caterer removed."});
    }catch(error){
        res.status(500).json({error:"Internal Server error."}); 
    }
};





export const cat_pass = async(req, res)=>{
   try {
           const { id } = req.params;
           const { password } = req.body;
   
           const updatedCaterer = await caterers.findByIdAndUpdate(
               id,
               { password },
               { new: true } 
           );
   
           if (!updatedCaterer) {
               return res.status(404).json({ message: "Caterer not found." });
           }
   
           res.status(200).json({ message: "Caterer password set successfully.", updatedCaterer });
       } catch (error) {
           res.status(500).json({ error: "Internal Server Error: " + error.message });
       }
};


// export const cat_log = async (req, res) => {
//     try {
//         const { id } = req.params; 
//         const { passwor } = req.body;

//         const caterer = await caterers.findById(id);
//         if (!caterer) {
//             return res.status(404).json({ message: "Caterer not found." });
//         }

//         const isPasswordValid =  caterers.({password:passwor});
//         if (!isPasswordValid) {
//             return res.status(401).json({ message: "Invalid password." });
//         }

//         res.status(200).json({ message: "Login successful.", caterer });
//     } catch (error) {
//         res.status(500).json({ error: "Internal Server Error: " + error.message });
//     }
// };


export const catererLogin = async (req, res) => {
    try {
        const { phone, password } = req.body;

        // Check if phone and password are provided
        if (!phone || !password) {
            return res.status(400).json({ message: "Phone number and password are required." });
        }

        // Find the caterer by phone number
        const caterer = await caterers.findOne({ phone });
        if (!caterer) {
            return res.status(404).json({ message: "Caterer not found." });
        }

        // Validate password
        if (caterer.password !== password) {
            return res.status(401).json({ message: "Invalid password." });
        }

        // Respond with caterer details
        res.status(200).json({
            success:true, 
            message: "Login successful.",
            caterer: {
                id: caterer._id,
                name: caterer.name,
                Catering_name: caterer.Catering_name,
                phone: caterer.phone,
                Business_Size: caterer.Business_Size,
                City: caterer.City
            }
        });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};


export const cat_fetch_id=async(req, res)=>{
    try{
        const id = req.params.id;
      const cats = await caterers.findOne({_id:id});
      if(cats.length==0){
        return res.status(404).json({message:"caterer not found"});
      }
      res.status(200).json(cats);

  }catch(error){
        res.status(500).json({error:"Internal Server error."});
    }
};


export const all_cat_fetch=async(req, res)=>{
    try{
      const cats = await caterers.find();
      if(cats.length==0){
        return res.status(404).json({message:"caterer not found"});
      }
      res.status(200).json(cats);

  }catch(error){
        res.status(500).json({error:"Internal Server error."});
    }
};