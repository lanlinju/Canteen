package com.example.canteen.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.canteen.R
import com.example.canteen.databinding.ActivitySignUpBinding
import com.example.canteen.models.User
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.SignUpViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.net.URI

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var preferenceManager: PreferenceManager
    private var encodedImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        preferenceManager = PreferenceManager(applicationContext)
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        setListeners()
    }

    private fun setListeners() {
        binding.textSignIn.setOnClickListener { onBackPressed() }
        binding.buttonSignUp.setOnClickListener {
            if (isValidSignUpDetails()) {
                signUp()
            }
        }
        //选择照片
        binding.layoutImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private fun signUp() {
        loading(true)
        val user = User(
            name = binding.inputName.text.toString().trim(),
            email = binding.inputEmail.text.toString().trim(),
            password = binding.inputPassword.text.toString().trim(),
            image = encodedImage
        )
        signUpViewModel.signUp(user).observe(this) {
            loading(false)
            if (it != null && -1 != it.code) {
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID, user.id)
                preferenceManager.putString(Constants.KEY_NAME, user.name)
                preferenceManager.putString(Constants.KEY_EMAIL, user.email)
                preferenceManager.putString(Constants.KEY_IMAGE, user.image)
                preferenceManager.putString(Constants.KEY_ROSE_NAME, user.roleName)
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                displayToast("注册成功")
            } else {
                displayToast("注册失败")
            }
        }
    }

    private val pickImage =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->  //选择照片
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val uri = result.data!!.data
                    try {
                        val inputStream = contentResolver.openInputStream(uri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageProfile.setImageBitmap(bitmap)
                        binding.textAddImage.visibility = View.INVISIBLE
                        encodedImage = encodeImage(bitmap)

                        println(encodedImage)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }

//    private val getDocumentLauncher =
//        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
//
//        }
//    getDocumentLauncher.launch(arrayOf("image/*","text/plain"))//选择照片

    private fun encodeImage(bitmap: Bitmap): String { //对位图进行修改大小 压缩和编码
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun isValidSignUpDetails(): Boolean { //验证输入的合法性
        return if (encodedImage.isEmpty()) {
            displayToast("Select profile image")
            false
        } else if (binding.inputName.text.toString().trim().isEmpty()) {
            displayToast("Enter name")
            false
        } else if (binding.inputEmail.text.toString().trim().isEmpty()) {
            displayToast("Enter email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString())
                .matches()
        ) {
            displayToast("Enter valid email")
            false
        } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
            displayToast("Enter your password")
            false
        } else if (binding.inputConfirmPassword.text.toString().trim().isEmpty()) {
            displayToast("Confirm your password")
            false
        } else if (!binding.inputPassword.text.toString().trim()
                .equals(binding.inputConfirmPassword.text.toString().trim())
        ) {
            displayToast("Password & confirm password must be same")
            false
        } else true
    }

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.buttonSignUp.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.buttonSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}

