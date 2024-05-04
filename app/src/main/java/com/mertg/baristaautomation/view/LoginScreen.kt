import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mertg.baristaautomation.R
import com.mertg.baristaautomation.navigation.Screen
import com.mertg.baristaautomation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {

            Text("XXX Cafe", fontSize = 50.sp, color = MaterialTheme.colorScheme.secondary)

        }

        // Arka plan resmi
        Image(
            painter = painterResource(id = R.drawable.background), // Arka plan resmini buraya ekle
            contentDescription = null, // Content description null olarak ayarlanır
            modifier = Modifier.fillMaxSize(1f), // Arka plan resmini ekran boyutunda doldur
            alignment = Alignment.BottomCenter
        )


        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.secondary
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.loginUser(email, password) { success ->
                            if (success) {
                                navController.navigate(Screen.MainScaffold.route)
                            } else {
                                // Hata durumunda gerekirse mesaj gösterilebilir
                                Toast.makeText(context, "Hatalı Giriş", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // E-posta veya şifre boş ise kullanıcıya uyarı ver
                        Toast.makeText(context, "E-posta veya şifre boş olamaz", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Giriş Yap")
            }
        }
    }
}
