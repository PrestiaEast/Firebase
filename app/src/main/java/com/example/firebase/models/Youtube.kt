package com.example.firebase.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Youtube(var id:String? = "", var channel:String, var rank:String, var link:String, var comment:String) {
    override fun toString(): String {
        return "Channel: ${channel}, Rank: ${rank}, Link: ${link}, Comment: ${comment}"
    }
}