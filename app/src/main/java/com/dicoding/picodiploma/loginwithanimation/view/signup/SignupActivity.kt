package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
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
                getString(R.string.user_created) -> {
                    alertDialog(getString(R.string.success_signup), getString(R.string.account_created_message))
                }

                getString(R.string.email_already_taken) -> {
                    alertDialog(getString(R.string.failed_signup), getString(R.string.email_already_taken_message))
                }

                else -> {
                    alertDialog(getString(R.string.failed_signup), it.message)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
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
                getString(R.string.success_login) -> {
                    setPositiveButton(getString(R.string.continue_button)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }

                else -> {
                    setNegativeButton(getString(R.string.retry_button)) { _, _ -> }
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