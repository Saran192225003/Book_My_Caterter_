import mongoose from "mongoose";


const reviewSchema= new mongoose.Schema({
    catererId: { type: mongoose.Schema.Types.ObjectId, ref: 'caterers' },
    orderId: { type: mongoose.Schema.Types.ObjectId, ref: "order" },
    
    write:{
        type:String,
        required:true
    },
    rate:{
        type:String,
        required:true
    }
   
});

export default mongoose.model("reviews", reviewSchema)