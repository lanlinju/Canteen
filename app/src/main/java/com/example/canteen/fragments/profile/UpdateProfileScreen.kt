package com.example.canteen.fragments.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.solver.state.State
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.canteen.R
import com.example.canteen.fragments.ProfileProperty
import com.example.canteen.models.User
import com.example.canteen.theme.ui.CanteenM3Theme
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun UpdateProfileScreen(
    user: User,
    scope: CoroutineScope,
    drawerState: BottomDrawerState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            uri?.let {//更新图片
                user.image = context.uriEncodeBitmapString(uri)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LayoutHeader(keyboardController, scope, drawerState)

        ImageProfile(imageUri, user) {//选取图片
            launcher.launch("image/*")
        }

        ProfileProperty(user.name, label = "姓名:", onValueChange = { user.name = it })
        user.password = "123456"//TODO(密码修改待优化)
        ProfileProperty(
            user.password , label = "密码:", onValueChange = { user.password = it },
            visualTransformation = PasswordVisualTransformation(),
            IsCleared = { true }
        )
        ProfileProperty(user.nickname, label = "昵称:", onValueChange = { user.nickname = it })
        ProfileProperty(
            user.email, label = "邮箱:", onValueChange = { user.email = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        ProfileProperty(
            user.phone,
            label = "手机:",
            onValueChange = { user.phone = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        ProfileProperty("当前角色:${user.roleName}", label = "", onValueChange = { }, enabled = false)

        BottomButton {//更新User
            context.getPreferenceManager().putString(Constants.KEY_IMAGE, user.image)//更新缓存在本地的照片
            userViewModel.updateUser(user)
        }
    }
}

@Composable
fun BottomButton(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(10.dp))
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 20.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorPrimary)),
    ) {
        Text(text = "更新")
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun ImageProfile(imageUri: Uri?, user: User, onPickImage: () -> Unit) {
    val modifier = Modifier
        .size(120.dp)
        .clip(CircleShape)
        .clickable {
            onPickImage()
        }
    if (imageUri == null) {
        Image(
            modifier = modifier,
            bitmap = user.image.toBitmap().asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    } else {
        Image(
            modifier = modifier,
            painter = rememberAsyncImagePainter(model = imageUri),
            contentScale = ContentScale.Crop,
            contentDescription = "My Image"
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun ProfileProperty(
    text: String,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    IsCleared: () -> Boolean = { false },
    maxLines: Int = Int.MAX_VALUE,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var value by remember { mutableStateOf(text) }
    OutlinedTextField(
        value = value,
        leadingIcon = { Text(text = label, fontSize = 14.sp) },
        onValueChange = {
            value = it
            onValueChange(it)
        },
        modifier = Modifier.clickable {
            if (IsCleared()) {
                value = ""
            }
        },
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        visualTransformation = visualTransformation
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun LayoutHeader(
    keyboardController: SoftwareKeyboardController?,
    scope: CoroutineScope,
    drawerState: BottomDrawerState
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(modifier = Modifier.align(
            Alignment.CenterEnd
        ), onClick = {
            keyboardController?.hide()
            scope.launch { drawerState.close() }
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close  Drawer"
            )
        }
        Text(text = "编辑个人信息", modifier = Modifier.align(Alignment.Center))
    }
    Spacer(modifier = Modifier.height(10.dp))
}
