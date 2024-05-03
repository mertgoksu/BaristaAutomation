package com.mertg.baristaautomation.viewmodel


import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import io.grpc.Context

class AuthViewModel : ViewModel() {
    fun loginUser(email: String, password: String, onLoginResult: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoginResult(true)
                } else {
                    onLoginResult(false)
                }
            }
    }

}
