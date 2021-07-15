package me.fridayio.wasapplication.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostResponse {
    @SerializedName("hr1")
    @Expose
    var hr1: Double? = null

    @SerializedName("hr2")
    @Expose
    var hr2: Double? = null

    @SerializedName("peak_locff")
    @Expose
    var peak_locff: Double? = null

    @SerializedName("peak_locr")
    @Expose
    var peak_locr: Double? = null

    @SerializedName("peak_locim")
    @Expose
    var peak_locim: Double? = null

    @SerializedName("stdre")
    @Expose
    var stdre: Double? = null

    @SerializedName("stdim")
    @Expose
    var stdim: Double? = null

    @SerializedName("result")
    @Expose
    var result: Int? = null
}