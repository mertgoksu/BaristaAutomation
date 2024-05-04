package com.mertg.baristaautomation.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.mertg.baristaautomation.navigation.AllAppNavigation
import com.mertg.baristaautomation.navigation.Screen
import com.mertg.baristaautomation.util.generateOrderId
import com.mertg.baristaautomation.viewmodel.MainPageViewModel




val mainPageViewModel = MainPageViewModel()
@Composable
fun MainScaffold() {
    var selectedTab by remember { mutableStateOf(0) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
            containerColor = Color.DarkGray
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate(Screen.MainPage.route)
                    },
                    icon = {
                        Icon(Icons.Filled.Home, tint = if (selectedTab == 0) MaterialTheme.colorScheme.onBackground else Color.White, contentDescription = "")
                    },
                    label = {
                        Text(text = "Sipariş Ekle", color = Color.White)
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate(Screen.ShowOrdersPage.route)
                    },
                    icon = {
                        Icon(Icons.Filled.AllInbox,tint = if (selectedTab == 1) MaterialTheme.colorScheme.onBackground else Color.White, contentDescription =  "")
                    },
                    label = {
                        Text(text = "Tüm Siparişler", color = Color.White)
                    }
                )
            }
        }
    ) {
        Column(Modifier.padding(it)) {
            AllAppNavigation(navController = navController)
        }
    }
}

@Composable
fun MainPage() {

    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) } // Set isLoading to true initially

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

    var masaNo by remember { mutableStateOf("") }

    var siparisDurumu by remember { mutableStateOf("Tamamlanmadı") }


    if (isLoading) {
        Toast.makeText(context, "Ürünler Yükleniyor ...", Toast.LENGTH_SHORT).show()
        mainPageViewModel.LoadingIndicator(isLoading = isLoading)
    }

    var hotCoffeeNames: List<String>? by remember { mutableStateOf(null) }
    mainPageViewModel.getOrder(context, "hotCoffeeNames", "7IRSnNF1uWrLCuJyWppO") { names ->
        hotCoffeeNames = names
        isLoading = false
    }

    var coldCoffeeNames: List<String>? by remember { mutableStateOf(null) }
    mainPageViewModel.getOrder(context, "coldCoffeeNames", "v9wXhrEwt6TlCDLUoqH4") { names ->
        coldCoffeeNames = names
        isLoading = false
    }

    var cayNames: List<String>? by remember { mutableStateOf(null) }
    mainPageViewModel.getOrder(context, "cayNames", "v2xXIWHwKX6BHbxQWRUX") { names ->
        cayNames = names
        isLoading = false
    }

    var dessertNames: List<String>? by remember { mutableStateOf(null) }
    mainPageViewModel.getOrder(context, "dessertNames", "9WgaXjYX6H0KxNR94kgu") { names ->
        dessertNames = names
        isLoading = false
    }

    var softDrinkNames: List<String>? by remember { mutableStateOf(null) }
    mainPageViewModel.getOrder(context, "softDrinkNames", "zF7uVzyWRm7gLZrlGws0") { names ->
        softDrinkNames = names
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(25.dp))

        Text("Sipariş Ekle", fontSize = 35.sp)

        Spacer(modifier = Modifier.height(30.dp))

        // Masa Numarası giriş için textfield
        OutlinedTextField(
            value = masaNo,
            onValueChange = {
                // Sadece sayı girişine izin vermek
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    // Eğer boşsa veya sadece sayı varsa değeri güncelle
                    // Eğer girilen değer 0'dan büyükse güncelle
                    if (it.isNotEmpty() && it != "0") {
                        // Eğer girilen değer 0 ile başlıyorsa sadece 0'ı al
                        val finalValue = if (it.startsWith("0")) {
                            it.take(1)
                        } else {
                            it
                        }
                        masaNo = finalValue
                    } else {
                        // Eğer boşsa veya 0 ise, hiçbir şey yazma
                        // Kullanıcı silme durumunda yapılacak işlemler
                        // Bu işlemi değiştirmek için burayı özelleştirebilirsiniz
                        masaNo = ""
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Masa Numarası") },
            placeholder = { Text("Masa Numarası") },
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary) // İstediğiniz renk
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Sicak kahve seçimi dropdown
        hotCoffeeNames?.let {
            mainPageViewModel.CoffeeSelectionDropdown(
                title = "Sıcak Kahve",
                options = it,
                selectedOption = selectedHotCoffee,
                onOptionSelected = { coffee ->
                    if(coffee == "---"){
                        selectedHotCoffee = coffee
                        hotCoffeeQuantity = 0
                    }else{
                        selectedHotCoffee = coffee
                        hotCoffeeQuantity = 1
                    }
                },
                quantity = hotCoffeeQuantity,
                onQuantityChanged = { newQuantity ->
                    hotCoffeeQuantity = newQuantity
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Soguk kahve seçimi dropdown
        coldCoffeeNames?.let {
            mainPageViewModel.CoffeeSelectionDropdown(
                title = "Soğuk Kahve",
                options = it,
                selectedOption = selectedColdCoffee,
                onOptionSelected = { coffee ->
                    if(coffee == "---"){
                        selectedColdCoffee = coffee
                        coldCoffeeQuantity = 0
                    }else {
                        selectedColdCoffee = coffee
                        coldCoffeeQuantity = 1
                    }
                },
                quantity = coldCoffeeQuantity,
                onQuantityChanged = { newQuantity ->
                    coldCoffeeQuantity = newQuantity
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Cay seçimi dropdown
        cayNames?.let {
            mainPageViewModel.CoffeeSelectionDropdown(
                title = "Çay",
                options = it,
                selectedOption = selectedCay,
                onOptionSelected = { coffee ->
                    if(coffee == "---"){
                        selectedCay = coffee
                        cayQuantity = 0
                    }else {
                        selectedCay = coffee
                        cayQuantity = 1
                    }
                },
                quantity = cayQuantity,
                onQuantityChanged = { newQuantity ->
                    cayQuantity = newQuantity
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // Tatlı seçimi dropdown
        dessertNames?.let {
            mainPageViewModel.CoffeeSelectionDropdown(
                title = "Tatlı",
                options = it,
                selectedOption = selectedDessert,
                onOptionSelected = { dessert ->
                    selectedDessert = dessert
                    if(dessert == "---"){
                        selectedDessert = dessert
                        dessertQuantity = 0
                    }else {
                        selectedDessert = dessert
                        dessertQuantity = 1
                    }
                },
                quantity = dessertQuantity,
                onQuantityChanged = { newQuantity ->
                    dessertQuantity = newQuantity
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        // İçecek seçimi dropdown
        softDrinkNames?.let {
            mainPageViewModel.CoffeeSelectionDropdown(
                title = "İçecek",
                options = it,
                selectedOption = selectedDrink,
                onOptionSelected = { drink ->
                    if(drink == "---"){
                        selectedDrink = drink
                        drinkQuantity = 0
                    }else{
                        selectedDrink = drink
                        drinkQuantity = 1
                    }
                },
                quantity = drinkQuantity,
                onQuantityChanged = { newQuantity ->
                    drinkQuantity = newQuantity
                }
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        if (!isLoading) {
            AddOrderButton(
                selectedHotCoffee, hotCoffeeQuantity,
                selectedColdCoffee, coldCoffeeQuantity,
                selectedCay, cayQuantity,
                selectedDessert, dessertQuantity,
                selectedDrink, drinkQuantity,
                masaNo, siparisDurumu
            )
        }
    }
}

@Composable
fun AddOrderButton(
    selectedHotCoffee: String, hotCoffeeQuantity: Int,
    selectedColdCoffee: String, coldCoffeeQuantity: Int,
    selectedCay: String, cayQuantity: Int,
    selectedDessert: String, dessertQuantity: Int,
    selectedDrink: String, drinkQuantity: Int,
    masaNo: String, siparisDurumu: String
) {
    val context = LocalContext.current

    Button(
        onClick = {
            if (masaNo.isNotEmpty()) {
                var isError = false // Hata durumunu takip etmek için bir değişken ekledik

                if (selectedHotCoffee != "---" && hotCoffeeQuantity == 0) {
                    Toast.makeText(context, "Sıcak Kahve seçimi doğru yapılmalıdır!", Toast.LENGTH_SHORT).show()
                    isError = true // Hata durumu, sıcak kahve seçimi doğru yapılmamışsa true olur
                }
                if (selectedColdCoffee != "---" && coldCoffeeQuantity == 0) {
                    Toast.makeText(context, "Soğuk Kahve seçimi doğru yapılmalıdır!", Toast.LENGTH_SHORT).show()
                    isError = true // Hata durumu, soğuk kahve seçimi doğru yapılmamışsa true olur
                }
                if (selectedCay != "---" && cayQuantity == 0) {
                    Toast.makeText(context, "Çay seçimi doğru yapılmalıdır!", Toast.LENGTH_SHORT).show()
                    isError = true // Hata durumu, çay seçimi doğru yapılmamışsa true olur
                }
                if (selectedDessert != "---" && dessertQuantity == 0) {
                    Toast.makeText(context, "Tatlı seçimi doğru yapılmalıdır!", Toast.LENGTH_SHORT).show()
                    isError = true // Hata durumu, tatlı seçimi doğru yapılmamışsa true olur
                }
                if (selectedDrink != "---" && drinkQuantity == 0) {
                    Toast.makeText(context, "İçecek seçimi doğru yapılmalıdır!", Toast.LENGTH_SHORT).show()
                    isError = true // Hata durumu, içecek seçimi doğru yapılmamışsa true olur
                }

                val siparisID = generateOrderId()

                // Eğer hiçbir hata yoksa ve en az bir seçim yapılmışsa
                if (!isError && (hotCoffeeQuantity > 0 || coldCoffeeQuantity > 0 || cayQuantity > 0 || dessertQuantity > 0 || drinkQuantity > 0)) {
                    mainPageViewModel.addOrder(
                        context, siparisID,
                        selectedHotCoffee, hotCoffeeQuantity,
                        selectedColdCoffee, coldCoffeeQuantity,
                        selectedCay, cayQuantity,
                        selectedDessert, dessertQuantity,
                        selectedDrink, drinkQuantity,
                        masaNo, siparisDurumu
                    )
                } else if (!isError) {
                    // Eğer hiçbir hata yoksa ve hiçbir seçim yapılmamışsa
                    Toast.makeText(context, "En azından 1 seçim yapın!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Masa Numarası Doldurulmalıdır!", Toast.LENGTH_SHORT).show()
            }

        },
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text("Sipariş Ver", fontSize = 18.sp)
    }
}