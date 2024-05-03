package com.mertg.baristaautomation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainPageViewModel : ViewModel(){


    // Firestore referansını al
    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    // Firestore'a sipariş eklemek için fonksiyon
    fun addOrder(
        context : Context, siparisID : String,
        selectedHotCoffee: String, hotCoffeeQuantity: Int,
        selectedColdCoffee: String, coldCoffeeQuantity: Int,
        selectedCay: String, cayQuantity: Int,
        selectedDessert: String, dessertQuantity: Int,
        selectedDrink: String, drinkQuantity: Int,
        masaNo : String, siparisDurumu: String
    ) {
        // Firestore koleksiyonunu ve belirli bir dokümanı belirle
        val ordersCollectionRef = db.collection("orders")

        // Şu anki zamanı al ve Firestore'a eklemek için uygun formata dönüştür
        val currentTimeStamp = Timestamp.now()

        // Kullanıcı seçimlerini ve zaman bilgisini bir veri map'ine dönüştür
        val orderData = hashMapOf(
            "Sipariş ID" to siparisID,
            "Sıcak Kahve Siparişi" to "$selectedHotCoffee, $hotCoffeeQuantity adet",
            "Soğuk Kahve Siparişi" to "$selectedColdCoffee, $coldCoffeeQuantity adet",
            "Çay Siparişi" to "$selectedCay, $cayQuantity adet",
            "Tatlı Siparişi" to "$selectedDessert, $dessertQuantity adet",
            "Meşrubat Siparişi" to "$selectedDrink, $drinkQuantity adet",
            "Masa Numarası" to "$masaNo Numaralı Masa",
            "Sipariş Tarihi" to currentTimeStamp,
            "Sipariş Durumu" to "Sipariş $siparisDurumu"
        )

        // Firestore'a veriyi ekle
        ordersCollectionRef.add(orderData)
            .addOnSuccessListener { documentReference ->
                // Başarıyla eklendiğinde yapılacak işlemler
                Toast.makeText(context, "Siparişiniz alındı!", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                // Ekleme başarısız olduğunda yapılacak işlemler
                Toast.makeText(context, "Sipariş eklenirken hata oluştu: $e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getOrder(context: Context, collectionName: String, collectionPath: String, callback: (List<String>?) -> Unit) {
        // İstenen koleksiyonundaki belgeyi al
        val docRef = db.collection(collectionName).document(collectionPath)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Belge varsa ürün isimlerini al ve callback'i çağır
                    val itemNames = document.data?.values?.map { it.toString() }
                    callback(itemNames)
                } else {
                    Toast.makeText(context, "Belge bulunamadı", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Veri alınamadı", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }

    @Composable
    fun CoffeeSelectionDropdown(
        title: String,
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit,
        quantity: Int,
        onQuantityChanged: (Int) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        // Eğer seçenek "−−−" ise, miktarı 0 olarak ayarla
        if (selectedOption == "---") {
            onQuantityChanged(0)
        }

        Row(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(8f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "$title: $selectedOption", fontSize = 16.sp)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text =  {
                                    Text(text = option)
                                },
                                onClick = {
                                    onOptionSelected(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                OutlinedTextField(
                    value = if (quantity == 0) "" else quantity.toString(),
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            // Kullanıcı boş bıraktıysa 0'a dönüştür
                            onQuantityChanged(0)
                        } else {
                            // Girilen değeri tamsayıya dönüştürmeye çalış
                            val intValue = newValue.toIntOrNull()
                            if (intValue != null) {
                                // Girilen değer 0'dan büyükse güncelle
                                if (intValue > 0) {
                                    // Eğer girilen değer 0 ile başlıyorsa sadece 0'ı al
                                    val finalValue = if (newValue.startsWith("0")) {
                                        newValue.take(1)
                                    } else {
                                        newValue
                                    }
                                    onQuantityChanged(finalValue.toInt())
                                }
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }

    @Composable
    fun LoadingIndicator(isLoading: Boolean) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }



}