import mongoose from "mongoose";
const menuSchema= new mongoose.Schema({
    catererId: { type: mongoose.Schema.Types.ObjectId, ref: 'caterers' },
   item_name:{
        type:String,
        required:true
    },
    price:{
        type:String,
        required:true
    },
  
});

export default mongoose.model("menu", menuSchema)