package com.notmiyouji.newsapp.kotlin.LoginedModel

import com.google.gson.annotations.SerializedName

class Register {
    @SerializedName("email")
    var email: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("nickname")
    var nickname: String? = null

}