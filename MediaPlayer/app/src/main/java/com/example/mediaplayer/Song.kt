package com.example.mediaplayer

class Song {
    var title:String?=null
    var author:String?=null
    var LyricUrl:String?=null
    constructor(title:String,author:String,LyricUrl:String){
        this.title=title
        this.author=author
        this.LyricUrl=LyricUrl
    }
}