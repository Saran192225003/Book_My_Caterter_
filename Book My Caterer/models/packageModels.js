import mongoose from "mongoose";
const packageSchema= new mongoose.Schema({
    catererId: { type: mongoose.Schema.Types.ObjectId, ref: 'caterers' },
   package_name:{
        type:String,
        required:true
    },
    items:{
        type:String,
        required:true
    },
    price:{
        type:String,
        required:true
    },
  
});

export default mongoose.model("packages", packageSchema)