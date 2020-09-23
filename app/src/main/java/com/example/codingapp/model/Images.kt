package com.example.codingapp.model

data class ImageResponse (
    val data : List<Data>,
    val success : Boolean,
    val status : Int
)

data class Data (

    val id : String,
    val title : String,
    val description : String,
    val datetime : Int,
    val cover : String,
    val cover_width : Int,
    val cover_height : Int,
    val account_url : String,
    val account_id : Int,
    val privacy : String,
    val layout : String,
    val views : Int,
    val link : String,
    val ups : Int,
    val downs : Int,
    val points : Int,
    val score : Int,
    val is_album : Boolean,
    val vote : String,
    val favorite : Boolean,
    val nsfw : Boolean,
    val section : String,
    val comment_count : Int,
    val favorite_count : Int,
    val topic : String,
    val topic_id : Int,
    val images_count : Int,
    val in_gallery : Boolean,
    val is_ad : Boolean,
    val tags : List<Tags>,
    val ad_type : Int,
    val ad_url : String,
    val in_most_viral : Boolean,
    val include_album_ads : Boolean,
    val images : List<Images>,
    val ad_config : Ad_config
)

data class Tags (

    val name : String,
    val display_name : String,
    val followers : Int,
    val total_items : Int,
    val following : Boolean,
    val is_whitelisted : Boolean,
    val background_hash : String,
    val thumbnail_hash : String,
    val accent : String,
    val background_is_animated : Boolean,
    val thumbnail_is_animated : Boolean,
    val is_promoted : Boolean,
    val description : String,
    val logo_hash : String,
    val logo_destination_url : String,
)

data class Images (
    val id : String,
    var title : String,
    val description : String,
    val datetime : Double,
    val type : String,
    val animated : Boolean,
    val width : Int,
    val height : Int,
    val size : Int,
    val views : Int,
    val bandwidth : Double,
    val vote : String,
    val favorite : Boolean,
    val nsfw : String,
    val section : String,
    val account_url : String,
    val account_id : String,
    val is_ad : Boolean,
    val in_most_viral : Boolean,
    val has_sound : Boolean,
    val tags : List<String>,
    val ad_type : Int,
    val ad_url : String,
    val edited : Int,
    val in_gallery : Boolean,
    val link : String,
    val mp4_size : Int,
    val mp4 : String,
    val gifv : String,
    val hls : String,
    val processing : Processing,
    val comment_count : String,
    val favorite_count : String,
    val ups : String,
    val downs : String,
    val points : String,
    val score : String
)

data class Processing (
    val status : String
)


data class Ad_config (
    val safeFlags : List<String>,
    val highRiskFlags : List<String>,
    val unsafeFlags : List<String>,
    val wallUnsafeFlags : List<String>,
    val showsAds : Boolean
)
