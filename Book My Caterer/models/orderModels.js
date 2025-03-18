import mongoose from "mongoose";
const orderSchema= new mongoose.Schema({
    userId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
    catererId: { type: mongoose.Schema.Types.ObjectId, ref: 'caterers' },
    
    catering_name:{
        type:String,
        required:true
    },
    date:{
        type:String,
        required:true
    },
    time_slot:{
        type:String,
        required:true
    },
    number_of_heads:{
        type:String,
        required:true
    },
    contact:{
        type:String,
        required:true
    },
    event_location:{
        type:String,
        required:true
    },
    event_type:{
        type:String,
        required:true
    },
    selected_package:{
        type:String,
        required:true
    },
    extra_menu:{
        type:String,
        required:true
    },
    price:{
        type:String,
        required:false
    },
    isAccepted:{
        type:String,
        required:false
    },
    paymentID:{
        type:String,
        required:false
    },
    paymentStatus:{
        type:String,
        required:false
    }
});

export default mongoose.model("order", orderSchema)