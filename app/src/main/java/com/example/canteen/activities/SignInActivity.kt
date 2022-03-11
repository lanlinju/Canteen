package com.example.canteen.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.databinding.ActivitySignInBinding
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.PreferenceManager
import com.example.canteen.utilities.displayToast
import com.example.canteen.viewmodels.SignInViewModel

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        preferenceManager = PreferenceManager(applicationContext)
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        setListeners()
    }

    private fun setListeners() {
        binding.textCreateNewAccount.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
        binding.buttonSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
                // toDo()
                signIn()

            }
        }
    }

    private fun signIn() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            findViewById<Button>(R.id.buttonSignIn).windowToken,
            0
        )
        loading(true)
        signInViewModel.signIn(
            binding.inputEmail.text.toString(),
            binding.inputPassword.text.toString()
        ).observe(this) { user ->
            if (user != null) {
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID, user.id)
                preferenceManager.putString(Constants.KEY_NAME, user.name)
                preferenceManager.putString(Constants.KEY_IMAGE, user.image)
                preferenceManager.putString(Constants.KEY_ROSE_NAME, user.roleName)
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.buttonSignIn.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.buttonSignIn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun isValidSignInDetails(): Boolean { //验证输入的合法性
        return if (binding.inputEmail.text.toString().trim().isEmpty()) {
            displayToast("请输入邮箱")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
            displayToast("邮箱格式不正确")
            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            displayToast("输入你的密码")
            false
        } else true
    }
}