/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.canteen.fragments

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomDrawer
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteen.R
import com.example.canteen.components.FunctionalityNotAvailablePopup
import com.example.canteen.components.baselineHeight
import com.example.canteen.fragments.profile.CanteenAppBar
import com.example.canteen.fragments.profile.UpdateProfileScreen
import com.example.canteen.models.User
import com.example.canteen.utilities.toBitmap
import com.example.canteen.viewmodels.CartViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(userData: User) {
    var expanded by remember { mutableStateOf(false) }
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    BottomDrawer(
        drawerContent = {
            UpdateProfileScreen(
                userData,
                scope = scope,
                drawerState = drawerState)
        },
        drawerState = drawerState,
        gesturesEnabled = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            CanteenAppBar(
                scrollBehavior = scrollBehavior,
                onNavIconPressed = {},
                title = { Text(text = "个人信息", fontSize = 16.sp) },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable(onClick = { expanded = true })
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .height(24.dp),
                        contentDescription = "更多选项"
                    )
                    DropdownMenu(
                        expanded = expanded,
//                        modifier = Modifier.background(Color.White),
                        onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = "编辑") },
                            onClick = {
                                expanded = false
                                scope.launch { drawerState.expand() }
                            }

                        )
                    }
                }
            )
            Surface() {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    ProfileHeader(
                        userData,
                    )
                    UserInfoFields(userData)
                }
            }

        }
    }

}

@Composable
private fun UserInfoFields(userData: User) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndPosition(userData)

        ProfileProperty(stringResource(R.string.email), userData.email)

        ProfileProperty(stringResource(R.string.phone), userData.phone)

        ProfileProperty(stringResource(R.string.role), userData.roleName, isLink = true)

//        userData.timeZone?.let {
//            ProfileProperty(stringResource(R.string.timezone), userData.timeZone)
//        }

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        //Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition(
    userData: User
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(
            userData,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Role(
            userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(userData: User, modifier: Modifier = Modifier) {
    Text(
        text = userData.name,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun Role(userData: User, modifier: Modifier = Modifier) {
    Text(
        text = userData.roleName,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        // TODO (M3): No Divider, replace when available
        androidx.compose.material.Divider()
        Text(
            text = label,
            modifier = Modifier.baselineHeight(24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val style = if (isLink) {
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
private fun ProfileHeader(
    data: User,
) {
    Image(
        bitmap = data.image.toBitmap().asImageBitmap(),
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape), contentDescription = null
    )
}

@Composable
fun ProfileError() {
    Text(stringResource(R.string.profile_error))
}

@Composable
fun ProfileDetailDescription(cartViewModel: CartViewModel) {
    val cartList by cartViewModel.cartListLiveData.observeAsState()
    cartList?.let {

    }

}
