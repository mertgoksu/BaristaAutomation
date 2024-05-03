package com.mertg.baristaautomation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mertg.baristaautomation.model.Order
import kotlinx.coroutines.tasks.await

class ShowOrdersViewModel : ViewModel(){


    // Firestore referansını al
    private val db = FirebaseFirestore.getInstance()

    // Sipariş durumu renklerini tutacak değişken
    private val siparisDurumuRenkleri = mutableMapOf<String, MutableState<Color>>()

    fun filterEmptyOrders(ordersList: List<Order>) {
        for (order in ordersList) {
            if (order.icecekSiparisi == "---, 0 adet") {
                order.icecekSiparisi = "" // Sipariş edilmemişse, siparişi temizle
            }
            if (order.sogukKahveSiparisi == "---, 0 adet") {
                order.sogukKahveSiparisi = ""
            }
            if(order.sicakKahveSiparisi == "---, 0 adet"){
                order.sicakKahveSiparisi = ""
            }
            if(order.caySiparisi == "---, 0 adet"){
                order.caySiparisi = ""
            }
            if(order.tatliSiparisi == "---, 0 adet"){
                order.tatliSiparisi = ""
            }
        }
    }

    // Sipariş durumu renklerini alma işlemi
    fun getSiparisDurumuRenk(siparisDurumu: String): MutableState<Color> {
        return siparisDurumuRenkleri.getOrPut(siparisDurumu) { mutableStateOf(Color.Red) }
    }

    // Sipariş durumu güncelleme işlemi
    suspend fun updateOrderStatus(order: String, newStatus: String) {
        val orderId = order // Siparişin kimlik belirtecini buradan alın, gerçek isim neyse onu kullanın

        // Belirli bir koleksiyondaki belgeyi al
        val orderRef = db.collection("orders").document(orderId)

        // Sipariş durumu alanını güncelle
        orderRef.update("Sipariş Durumu", newStatus)
            .await()

        // Sipariş durumu rengini güncelle
        getSiparisDurumuRenk(newStatus).value = if (newStatus == "Sipariş Tamamlanmadı") {
            Color.Red
        } else {
            Color.Green
        }
    }


    suspend fun getAllOrders(): List<Order> {
        val ordersList = mutableListOf<Order>()

        // Firestore referansını al
        val db = FirebaseFirestore.getInstance()

        // Belirli bir koleksiyondan tüm belgeleri al
        val querySnapshot = db.collection("orders").get().await()

        // Her belgeyi dönerek içeriğini Order nesnesine dönüştür ve listeye ekle
        for (document in querySnapshot.documents) {
            val siparisID = document.getString("Sipariş ID") ?: ""
            val masaNumarasi = document.getString("Masa Numarası") ?: ""
            val icecekSiparisi = document.getString("Meşrubat Siparişi") ?: ""
            val sogukKahveSiparisi = document.getString("Soğuk Kahve Siparişi") ?: ""
            val sicakKahveSiparisi = document.getString("Sıcak Kahve Siparişi") ?: ""
            val tatliSiparisi = document.getString("Tatlı Siparişi") ?: ""
            val caySiparisi = document.getString("Çay Siparişi") ?: ""
            val siparisTarihi = document.getTimestamp("Sipariş Tarihi")
            val siparisDurumu = document.getString("Sipariş Durumu")

            val order = siparisTarihi?.let {
                Order(
                    siparisID,
                    masaNumarasi,
                    icecekSiparisi,
                    sogukKahveSiparisi,
                    sicakKahveSiparisi,
                    tatliSiparisi,
                    caySiparisi,
                    it,
                    siparisDurumu
                )
            }

            if (order != null) {
                ordersList.add(order)
            }

        }

        return ordersList
    }




}