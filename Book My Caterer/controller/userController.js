import User from "../models/userModels.js"



//user




export const create = async(req, res)=>{
    try{
        const {name,email,phone,City,password}=req.body;
        const userExist=await User.findOne({phone})
        if(userExist){
            return res.status(400).json({message:"user already exist."});
        }

       
        const newUser=new User({
            name,email,phone,City,password
        });

        await newUser.save();
        res.status(200).json({success:true, message:"User created successfully",data:newUser});
    }catch(error){
        res.status(500).json({error:"Internal Server error." + error});
    }
} 


export const fetch=async(req, res)=>{
    try{
      const users = await User.find();
      if(users.length==0){
        return res.status(404).json({message:"user not found"});
      }
      res.status(200).json(users);

  }catch(error){
        res.status(500).json({error:"Internal Server error."});
    }
};



export const update = async(req, res)=>{
    try{
        const id = req.params.id;
        const userExist=await User.findOne({_id:id})
        if(!userExist){
            return res.status(404).json({message:"user not found."})
        }
        const updateUser=await User.findByIdAndUpdate(id, req.body,{new:true})
        res.status(201).json(updateUser);
    }catch(error){
        res.status(500).json({error:"Internal Server error."}); 
    }
};

export const deleteUser = async(req, res)=>{
    try{
        const id = req.params.id;
        const userExist=await User.findOne({_id:id})
        if(!userExist){
            return res.status(404).json({message:"user not found."})
        }
        await User.findByIdAndDelete(id);
        res.status(201).json({message:"user deleted."});
    }catch(error){
        res.status(500).json({error:"Internal Server error."}); 
    }
};





export const userLogin = async (req, res) => {
    try {
        const { phone, password } = req.body;

        // Check if phone and password are provided
        if (!phone || !password) {
            return res.status(400).json({ message: "Phone number and password are required." });
        }

        // Find the caterer by phone number
        const caterer = await User.findOne({ phone });
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
            User: caterer
        });
    } catch (error) {
        res.status(500).json({ error: "Internal Server Error: " + error.message });
    }
};
