package com.example.foodapp

import android.media.Image

class FoodApp{

    var name:String ?=null
    var des:String?=null
    var image:Int?=null

    constructor(name:String,des:String,image:Int){
        this.name=name
        this.des=des
        this.image= image
    }
}
