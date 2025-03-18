import mongoose from "mongoose";
const availSchema= new mongoose.Schema({
    catererId: { type: mongoose.Schema.Types.ObjectId, ref: 'caterers' },
   date:{
        type:String,
        required:true
    },
    time_slot:{
        type:String,
        required:true
    },
  
});

export default mongoose.model("avail", availSchema)