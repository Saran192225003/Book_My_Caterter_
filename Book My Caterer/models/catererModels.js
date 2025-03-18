import mongoose from "mongoose";
const catSchema= new mongoose.Schema({
    name:{
        type:String,
        required:true
    },
    Catering_name:{
        type:String,
        required:true
    },
    phone:{
        type:String,
        required:true
    },
    Business_Size:{
        type:String,
        required:true
    },
    FoodType:{
        type:String,
        required:false
    },
    City:{
        type:String,
        required:true
    },
    license:{
        type:String,
        required:true
    },
    isApproved:{
        type:Boolean,
        required:false
    },
    isReject:{
        type:Boolean,
        required:false
    },
    password:{
        type:String,
        required:false
    },
    Profile:{
        type:String,
        required:false
    }

});

export default mongoose.model("caterers",catSchema)



