package com.example.chatgpt

import com.stfalcon.chatkit.commons.models.IUser

class User(val uId:String, val uName:String,val uAvatar:String):IUser {
    override fun getId(): String {
        return uId;
    }

    override fun getName(): String {
        return uName;
    }

    override fun getAvatar(): String {
        return uAvatar;
    }
}