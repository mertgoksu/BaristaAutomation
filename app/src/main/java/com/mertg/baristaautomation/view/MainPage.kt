package com.mertg.baristaautomation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mertg.baristaautomation.util.addOrder

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
                .weight(8f)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Seçilen $title: $selectedOption")
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
fun MainPage(navController: NavController) {
    var selectedHotCoffee by remember { mutableStateOf("---") }
    var hotCoffeeQuantity by remember { mutableStateOf(0) }

    var selectedColdCoffee by remember { mutableStateOf("---") }
    var coldCoffeeQuantity by remember { mutableStateOf(0) }

    var selectedCay by remember { mutableStateOf("---") }
    var cayQuantity by remember { mutableStateOf(0) }

    var selectedDessert by remember { mutableStateOf("---") }
    var dessertQuantity by remember { mutableStateOf(0) }

    var selectedDrink by remember { mutableStateOf("---") }
    var drinkQuantity by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Coffee selection dropdown
        CoffeeSelectionDropdown(
            title = "Sıcak Kahve",
            options = listOf("---", "Türk Kahvesi", "Sıcak Latte", "Sıcak Mocha"),
            selectedOption = selectedHotCoffee,
            onOptionSelected = { coffee ->
                selectedHotCoffee = coffee
            },
            quantity = hotCoffeeQuantity,
            onQuantityChanged = { newQuantity ->
                hotCoffeeQuantity = newQuantity
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Coffee selection dropdown
        CoffeeSelectionDropdown(
            title = "Soğuk Kahve",
            options = listOf("---", "Iced Americano", "Iced Latte", "Iced Mocha"),
            selectedOption = selectedColdCoffee,
            onOptionSelected = { coffee ->
                selectedColdCoffee = coffee
            },
            quantity = coldCoffeeQuantity,
            onQuantityChanged = { newQuantity ->
                coldCoffeeQuantity = newQuantity
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Coffee selection dropdown
        CoffeeSelectionDropdown(
            title = "Çay",
            options = listOf("---", "Siyah Çay", "Earl Çay", "Yeşil Çay"),
            selectedOption = selectedCay,
            onOptionSelected = { coffee ->
                selectedCay = coffee
            },
            quantity = cayQuantity,
            onQuantityChanged = { newQuantity ->
                cayQuantity = newQuantity
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dessert selection dropdown
        CoffeeSelectionDropdown(
            title = "Tatlı",
            options = listOf("---", "San Sebastian", "Limonlu Cheesecake", "Puding"),
            selectedOption = selectedDessert,
            onOptionSelected = { dessert ->
                selectedDessert = dessert
            },
            quantity = dessertQuantity,
            onQuantityChanged = { newQuantity ->
                dessertQuantity = newQuantity
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Drink selection dropdown
        CoffeeSelectionDropdown(
            title = "İçecek",
            options = listOf("---", "Kola", "Fanta", "Sade Soda"),
            selectedOption = selectedDrink,
            onOptionSelected = { drink ->
                selectedDrink = drink
            },
            quantity = drinkQuantity,
            onQuantityChanged = { newQuantity ->
                drinkQuantity = newQuantity
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        AddOrderButton(
            selectedHotCoffee, hotCoffeeQuantity,
            selectedColdCoffee, coldCoffeeQuantity,
            selectedCay, cayQuantity,
            selectedDessert, dessertQuantity,
            selectedDrink, drinkQuantity
        )

    }
}

@Composable
fun AddOrderButton(
    selectedHotCoffee: String, hotCoffeeQuantity: Int,
    selectedColdCoffee: String, coldCoffeeQuantity: Int,
    selectedCay: String, cayQuantity: Int,
    selectedDessert: String, dessertQuantity: Int,
    selectedDrink: String, drinkQuantity: Int
) {
    val context = LocalContext.current

    Button(
        onClick = {
            // Add the order to Firestore
            addOrder(
                selectedHotCoffee, hotCoffeeQuantity,
                selectedColdCoffee, coldCoffeeQuantity,
                selectedCay, cayQuantity,
                selectedDessert, dessertQuantity,
                selectedDrink, drinkQuantity
            )
            Toast.makeText(context, "Siparişiniz alındı!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text("Sipariş Ver")
    }
}



/*CoffeeSelectionDropdown(
selectedCoffee = selectedCoffee,
onCoffeeSelected = { coffee ->
    selectedCoffee = coffee
},
quantity = quantity,
onQuantityChanged = { newQuantity ->
    quantity = newQuantity
}
)*/

@Composable
fun WhiteBackground(){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){}
}

@Preview
@Composable
private fun PreviewMain() {
    WhiteBackground()
    MainPage(navController = rememberNavController())
}
