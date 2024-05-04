package com.mertg.baristaautomation.view

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mertg.baristaautomation.model.Order
import com.mertg.baristaautomation.util.formatDate
import com.mertg.baristaautomation.viewmodel.ShowOrdersViewModel


val showOrdersViewModel = ShowOrdersViewModel()

val orderList = mutableStateListOf<Order>()
val isButtonClicked : MutableState<Boolean> = mutableStateOf(false)

@Composable
fun ShowOrdersPage() {
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        orderList.clear()
        orderList.addAll(showOrdersViewModel.getAllOrders().sortedByDescending { it.timestamp.seconds })
        showOrdersViewModel.filterEmptyOrders(orderList)
        isLoading.value = false
    }

    if (isLoading.value) {
        Toast.makeText(context, "Siparişler Yükleniyor ...", Toast.LENGTH_SHORT).show()
        CircularProgressIndicator()
    } else {
        LazyColumn {
            items(orderList) { order ->
                if (order.icecekSiparisi != null || order.sogukKahveSiparisi != null || order.tatliSiparisi != null || order.caySiparisi != null) {
                    SiparisRow(order = order)
                }
            }
        }
    }
}


@Composable
fun siparisDurumuRengi(siparisDurumu: String): MutableState<Color> {
    return remember {
        when (siparisDurumu) {
            "Sipariş Tamamlandı" -> mutableStateOf(Color.Green)
            "Sipariş Tamamlanmadı" -> mutableStateOf(Color.Red)
            else -> mutableStateOf(Color.Red)
        }
    }
}
@Composable
fun SiparisRow(order: Order) {
    val context = LocalContext.current

    val siparisDurumuRengi = siparisDurumuRengi(siparisDurumu = order.siparisDurumu!!)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            Modifier.padding(12.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(9f)
            ) {
                Text(
                    text = order.masaNumarasi,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sipariş Tarihi: ${formatDate(order.timestamp)}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sipariş Durumu: ${order.siparisDurumu}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if(order.sicakKahveSiparisi?.isNotEmpty() == true){
                    Text(
                        text = order.sicakKahveSiparisi!!,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if(order.sogukKahveSiparisi?.isNotEmpty() == true){
                    Text(
                        text = order.sogukKahveSiparisi!!,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if(order.caySiparisi?.isNotEmpty() == true){
                    Text(
                        text = order.caySiparisi!!,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if(order.tatliSiparisi?.isNotEmpty() == true){
                    Text(
                        text = order.tatliSiparisi!!,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if(order.icecekSiparisi?.isNotEmpty() == true){
                    Text(
                        text = order.icecekSiparisi!!,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(1f))
            {
                Button(
                    onClick = {
                        // Butona tıklama olayını tetikle
                        isButtonClicked.value = true
                        showOrdersViewModel.updateOrdersByTableNumber(order.masaNumarasi)
                        Toast.makeText(context, "${order.masaNumarasi} sipariş durumu değiştiriliyor, sayfayı yenileyin", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = siparisDurumuRengi.value)
                ) {
                    // Buton içeriği
                }
            }
        }

    }

}
