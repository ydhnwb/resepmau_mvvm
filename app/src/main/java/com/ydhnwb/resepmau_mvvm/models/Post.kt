package com.ydhnwb.resepmau_mvvm.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("content") var content : String? = null
)