package com.tropicalias

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    val firebaseUser = FirebaseAuth.getInstance().currentUser!!
}