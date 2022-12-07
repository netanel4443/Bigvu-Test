package com.e.bigvutest.data.dataobjects

import com.google.gson.annotations.SerializedName

data class WorkshopData(
    @SerializedName("name")
    val name:String,
    @SerializedName("image")
    val image:String,
    @SerializedName("description")
    val description:String,
    @SerializedName("text")
    val text:String,
    @SerializedName("video")
    val video:String
) {
}