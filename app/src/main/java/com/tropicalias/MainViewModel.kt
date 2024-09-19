package com.tropicalias

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tropicalias.api.model.User

class MainViewModel : ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser!!
    var userProfile: User? = null

}