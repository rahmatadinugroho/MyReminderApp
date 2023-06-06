package com.example.myreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.myreminder.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnsignup.setOnClickListener {
            val email = binding.edtEmailRegis.text.toString()
            val password = binding.edtPwdRegis.text.toString()

            // Validasi Email
            if (email.isEmpty()){
                binding.edtEmailRegis.error = "Email Harus Diisi"
                binding.edtEmailRegis.requestFocus()
                return@setOnClickListener
            }

            // Validasi Email tidak sesuai
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailRegis.error = "Email Tidak Valid"
                binding.edtEmailRegis.requestFocus()
                return@setOnClickListener
            }

            // Validasi password
            if (password.isEmpty()){
                binding.edtPwdRegis.error = "Password Harus Diisi"
                binding.edtPwdRegis.requestFocus()
                return@setOnClickListener
            }

            // Validasi panjang password
            if(password.length < 8){
                binding.edtPwdRegis.error = "Password Minimal 8 Karakter"
                binding.edtPwdRegis.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase(email, password)
        }


    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else{
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}