package com.example.chatgpt

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.*

class Messages(val msgId:String,val msgTxt:String,var user:User,val mDate:Date,val imgUrl:String): IMessage, MessageContentType.Image {
    override fun getId(): String {
        return msgId;
    }

    override fun getText(): String {
            return msgTxt;
    }

    override fun getUser(): IUser {
      return user;
    }

    override fun getCreatedAt(): Date {
          return mDate;
    }

    override fun getImageUrl(): String? {
        if(imgUrl.equals(""))
        {
            return null;
        }
        return imgUrl;
    }

}