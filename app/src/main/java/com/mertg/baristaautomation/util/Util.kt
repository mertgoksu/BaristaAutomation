package com.mertg.baristaautomation.util

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
// Firestore referansını al
@SuppressLint("StaticFieldLeak")
val db = Firebase.firestore

// Firestore'a sipariş eklemek için fonksiyon
fun addOrder(
    selectedHotCoffee: String, hotCoffeeQuantity: Int,
    selectedColdCoffee: String, coldCoffeeQuantity: Int,
    selectedCay: String, cayQuantity: Int,
    selectedDessert: String, dessertQuantity: Int,
    selectedDrink: String, drinkQuantity: Int
) {
    // Firestore koleksiyonunu ve belirli bir dokümanı belirle
    val ordersCollectionRef = db.collection("orders")

    // Şu anki zamanı al ve Firestore'a eklemek için uygun formata dönüştür
    val currentTimeStamp = Timestamp.now()

    // Kullanıcı seçimlerini ve zaman bilgisini bir veri map'ine dönüştür
    val orderData = hashMapOf(
        "Sıcak Kahve Siparişi" to "$selectedHotCoffee, $hotCoffeeQuantity adet",
//        "hotCoffeeQuantity" to hotCoffeeQuantity,
        "Soğuk Kahve Siparişi" to "$selectedColdCoffee, $coldCoffeeQuantity adet",
//        "coldCoffeeQuantity" to coldCoffeeQuantity,
        "Çay Siparişi" to "$selectedCay $cayQuantity, adet",
//        "cayQuantity" to cayQuantity,
        "Tatlı Siparişi" to "$selectedDessert, $dessertQuantity adet",
//        "dessertQuantity" to dessertQuantity,
        "Meşrubat Siparişi" to "$selectedDrink, $drinkQuantity adet",
//        "drinkQuantity" to drinkQuantity,
        "Sipariş Tarihi" to currentTimeStamp
    )

    // Firestore'a veriyi ekle
    ordersCollectionRef.add(orderData)
        .addOnSuccessListener { documentReference ->
            // Başarıyla eklendiğinde yapılacak işlemler
            println("Sipariş başarıyla eklendi. Doküman ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            // Ekleme başarısız olduğunda yapılacak işlemler
            println("Sipariş eklenirken hata oluştu: $e")
        }
}
