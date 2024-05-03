package com.mertg.baristaautomation.model

import com.google.firebase.Timestamp

data class Order(
    var siparisID : String,
    var masaNumarasi: String,
    var icecekSiparisi: String?,
    var sogukKahveSiparisi: String?,
    var sicakKahveSiparisi: String?,
    var tatliSiparisi: String?,
    var caySiparisi: String?,
    var timestamp: Timestamp,
    var siparisDurumu: String?
)

