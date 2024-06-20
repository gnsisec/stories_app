package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()

        viewModel.signup.observe(this) {
            when (it.message) {
                "User created" -> {
                    alertDialog("Success!", "Anda berhasil membuat akun.\nSilahkan login!")
                }

                "Email already exists" -> {
                    alertDialog("Gagal!", "Email sudah terdaftar!")
                }

                else -> {
                    alertDialog("Gagal!", it.message)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.signup(name = name, email = email, password = password)
        }
    }

    private fun alertDialog(title: String, description: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(description)
            when (title) {
                "Success!" -> {
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }

                "Gagal!" -> {
                    setNegativeButton("Ulangi") { _, _ -> }
                }

                else -> {
                    setNegativeButton("Ulangi") { _, _ -> }
                }
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.edRegisterName.isEnabled = !isLoading
        binding.edRegisterPassword.isEnabled = !isLoading
        binding.edRegisterEmail.isEnabled = !isLoading
        binding.signupButton.isEnabled = !isLoading
    }
}