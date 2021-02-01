package com.adhimbagas.speakup.ui.auth
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.databinding.DataBindingUtil
import com.adhimbagas.speakup.R
import com.adhimbagas.speakup.databinding.FragmentSignUpBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    var selectedPhotoUri: Uri? = null

    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {


        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(
                inflater, R.layout.fragment_sign_up,container,false
        )

        val view = binding.root

        auth = Firebase.auth

        val email = binding.tvEmail.text.toString().trim()
        val username = binding.tvUsername.text.toString().trim()
        val password =  binding.tvPassword.toString().trim()


        binding.btnSelectedPhoto.setOnClickListener{
            Log.d("RegisterActivity", "Memilih Photo")

            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, 0)


        }

        binding.btnRegister.setOnClickListener {


            if (email.isEmpty()){
                binding.tfEmail.error = "Email Required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.tfEmail.error = "Valid Email Required"
                binding.tfEmail.requestFocus()
                return@setOnClickListener
            }

            if (username.isEmpty()){
                binding.tfUsername.error = "Username Required"
                binding.tfUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length< 8){
                binding.tfPassword.error = "8 Char password required"
                binding.tfPassword.requestFocus()
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->

                        if (task.isSuccessful){
                            Log.d("RegisterActivity", "Login Success")
                            Toast.makeText(context, "Login Succes, please re-login. .", Toast.LENGTH_SHORT).show()

                        }
                        

                    }


    }



        return view    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterActivity", "Photo was selected")
            selectedPhotoUri = data.data
            Glide.with(this)
                    .load(selectedPhotoUri)
                    .into(circleImage)
           circleImage.setImageURI(selectedPhotoUri)
            btn_selected_photo.alpha = 0f
        }
    }
    /*
    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Succesfull uploaded Image")
                }
        ref.downloadUrl


    }
     */
        /*
    private fun saveUsernameToDatabase() {
        val Uid = FirebaseAuth.getInstance().uid
        val refUser = FirebaseDatabase.getInstance().getReference("/users/$Uid")
        val user = User(id, )
        refUser.setValue(user)
    }
    */


}

/*
class User(val id: String, val username: String, val profileImage: String) : View() {

}

*/