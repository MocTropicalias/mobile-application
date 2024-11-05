package com.tropicalias.ui.posts.newpost

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentNewPostBinding
import com.tropicalias.utils.ApiHelper
import java.util.UUID

class NewPostViewModel : ViewModel() {

    var imageUrl: Uri? = null

}