import mongoose from "mongoose";
const passSchema= new mongoose.Schema({
   
   password:{
        type:String,
        required:true
    }
    
});

export default mongoose.model("passwords", passSchema)