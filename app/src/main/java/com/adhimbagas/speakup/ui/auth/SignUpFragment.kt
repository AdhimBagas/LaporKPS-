package com.adhimbagas.speakup.ui.auth
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.adhimbagas.speakup.R
import com.adhimbagas.speakup.databinding.FragmentSignUpBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var selectedPhotoUri: Uri? = null



    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {


        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(
                inflater, R.layout.fragment_sign_up,container,false
        )


        auth = FirebaseAuth.getInstance()




        binding.btnSelectedPhoto.setOnClickListener{
            Log.d("RegisterActivity", "Memilih Photo")

            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i, 0)


        }

        binding.btnRegister.setOnClickListener {

            val email = binding.tvEmail.text.toString()
            val username = binding.tvUsername.text.toString().trim()
            val password =  binding.tvPassword.toString().trim()

            if (email.isEmpty()){
                binding.tfEmail.error = "Email Required"
                Log.d("RegisterActivity", "Email kosong. . ")
                return@setOnClickListener
            } else{
                Log.d("RegisterActivity", "email selected ")
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.tfEmail.error = "Valid Email Required"
                Log.d("RegisterActivity", "Email tidak valid. . ")
                return@setOnClickListener
            } else
                Log.d("RegisterActivity", "email valid ")

            if (username.isEmpty()){
                binding.tfUsername.setErrorIconOnClickListener {
                    Toast.makeText(context, "username is empty, please input username", Toast.LENGTH_SHORT).show()
                }
                Log.d("Register Activity", "Username is empty ")
                binding.tfUsername.requestFocus()
                return@setOnClickListener
            }  else
                Log.d("RegisterActivity", "email selected ")

            if (password.isEmpty() || password.length< 8){
                binding.tfPassword.error = "8 Char password required"
                binding.tfPassword.requestFocus()
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->

                        if (task.isSuccessful){
                            Log.d("RegisterActivity", "Register Success")
                            Toast.makeText(context, "Register Succes, please re-login. .", Toast.LENGTH_SHORT).show()
                            uploadImageToFirebaseStorage()


                        } else
                            task.isCanceled



                    }
                    .addOnFailureListener {
                        Log.d("RegisterActivity","Failed to create user")
                        Toast.makeText(context, "Failed to create user, please Try Again", Toast.LENGTH_SHORT).show()
                    }


    }



        return  binding.root   }


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

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Succesfull uploaded Image")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "Succesfull download url here : $it")
                        saveUserToFirebase(it.toString())
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(context, "Image can't upload, please try again", Toast.LENGTH_LONG).show()
                }






    }

    private fun saveUserToFirebase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User (uid, tvUsername.text.toString(), profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Succesfull save username")
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "cannot save user")
                }
    }




}


class User(val uid: String, val username: String, val profileImageUrl: String)

