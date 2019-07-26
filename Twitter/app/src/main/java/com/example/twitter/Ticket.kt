package com.example.twitter

class Ticket {
    var twitterID:String?=null
    var twitterText:String?=null
    var twitterImageURL:String?=null
    var twitterUserID:String?=null
    constructor(twitterID:String,twitterText:String,twitterImageURL:String,twitterUserID:String){
        this.twitterID=twitterID
        this.twitterImageURL=twitterImageURL
        this.twitterText=twitterText
        this.twitterUserID=twitterUserID
    }

}