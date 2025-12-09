package com.example.currencyconverter.data.api.models

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("bid") val bid: String,
    @SerializedName("ask") val ask: String,
    @SerializedName("code") val code: String,
    @SerializedName("codein") val codein: String,
    @SerializedName("name") val name: String,
    @SerializedName("high") val high: String,
    @SerializedName("low") val low: String,
    @SerializedName("varBid") val varBid: String,
    @SerializedName("pctChange") val pctChange: String,
    @SerializedName("create_date") val createDate: String
)