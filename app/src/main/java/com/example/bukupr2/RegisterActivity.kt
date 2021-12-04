package com.example.bukupr2

import androidx.appcompat.app.AppCompatActivity
import com.example.bukupr2.databinding.ActivityRegisterBinding
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Profile")

        register()

    }

    private fun register() {
        val email = binding.etEmail
        val password = binding.etPassword
        val confirm = binding.etConfirmPassword

        binding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                email.setError("Please enter your email!")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password.text.toString())) {
                password.setError("Please enter your password!")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(confirm.text.toString())) {
                confirm.setError("Please confirm your password!")
                return@setOnClickListener
            } else if (password.text.toString() != confirm.text.toString()) {
                confirm.setError("Password do not match!")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUserDb = databaseReference?.child(currentUser?.uid!!)
                        currentUserDb?.child("email")?.setValue(email.text.toString())
                        currentUserDb?.child("password")?.setValue(password.text.toString())

                        Toast.makeText(this, "Registration Success", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed Please Try Again", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

}