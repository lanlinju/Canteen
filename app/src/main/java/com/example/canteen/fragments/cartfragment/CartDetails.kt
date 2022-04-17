@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.canteen.fragments.cartfragment


import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.canteen.R
import com.example.canteen.components.showAlert
import com.example.canteen.models.Cart
import com.example.canteen.models.Goods
import com.example.canteen.models.Order
import com.example.canteen.utilities.Constants
import com.example.canteen.utilities.getPreferenceManager
import com.example.canteen.viewmodels.CartViewModel
import com.example.canteen.viewmodels.OrderViewModel
import com.example.composetutorialsample.ui.theme.CanteenTheme
import com.example.composetutorialsample.ui.theme.LightBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CartDetail(cartViewModel: CartViewModel, fragment: CartFragment) {
    val cartList by cartViewModel.cartListLiveData.observeAsState()
    val scope = rememberCoroutineScope()
    val orderViewModel: OrderViewModel = viewModel()
    val drawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    //键盘控制器，可控制键盘的展示和隐藏
    val keyboardController = LocalSoftwareKeyboardController.current
//    val configuration = LocalConfiguration.current
//    val screenHeight = configuration.screenHeightDp.dp
    BottomDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
            ) {
                cartList?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(vertical = 4.dp)
                    ) {
                        items(items = it, key = { it.goodsId }) { cart ->
                            //滑动删除
                            ItemCart(
                                modifier = Modifier
                                    .padding(vertical = 1.dp)
                                    .animateItemPlacement(),
                                cartViewModel = cartViewModel, cart = cart
                            )
                        }
                    }

                }
                BottomButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(colorResource(id = R.color.background)),
                    onClicked = {
                        fragment.findNavController().navigate(R.id.homeFragment)
                    }, onConfirmClick = {
                        scope.launch { drawerState.expand() }
                    })
            }
        },
        drawerContent = {
            DrawerContent(keyboardController, scope, drawerState) {
                orderViewModel.insertOrder(it)
            }
        }
    )


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemCart(
    cartViewModel: CartViewModel,
    cart: Cart,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        cartViewModel.deleteCart(cart = cart) // 删除Cart
    }
    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { direction ->
            FractionalThreshold(0.5f)
        },
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(8.dp)),
                contentAlignment = alignment
            ) {
                androidx.compose.material.Icon(
                    icon,
                    contentDescription = "Localized description",
                    modifier = Modifier
                        .scale(scale)
                        .size(28.dp)
                )
            }
        },
        dismissContent = {
            Card(
                backgroundColor = LightBlue,
                modifier = Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 8.dp
                ),
                elevation = animateDpAsState(
                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                ).value
            ) {
                ItemCardContent(cart, cartViewModel)
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DrawerContent(
    keyboardController: SoftwareKeyboardController?,
    scope: CoroutineScope,
    drawerState: BottomDrawerState,
    onAddOrder: (order: Order) -> Unit
) {
    var address by remember { mutableStateOf("") }//地址
    var phoneNumber by remember { mutableStateOf("") }//手机号
    val context: Context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colorResource(id = R.color.background),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
    ) {
        IconButton(modifier = Modifier.align(
            Alignment.TopEnd
        ), onClick = {
            keyboardController?.hide()
            scope.launch { drawerState.close() }
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close  Drawer"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            OutlinedTextField(
                value = address,
                label = { Text(text = "地址") },
                onValueChange = {
                    address = it
                })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phoneNumber,
                label = { Text(text = "手机号") },
                onValueChange = {
                    phoneNumber = it
                })
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedButton(onClick = {
                    scope.launch {
                        keyboardController?.hide()
                        drawerState.close()
                    }
                }) {
                    Text(text = "取消")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                OutlinedButton(onClick = {//生成订单
                    val userid =
                        context.getPreferenceManager().getString(Constants.KEY_USER_ID)!!
                    val order =
                        Order(userid = userid, address = address, phone = phoneNumber)
                    onAddOrder(order)
                }) {
                    Text(text = "确定")
                }
            }
        }
    }
}

@Composable
fun BottomButton(modifier: Modifier, onClicked: () -> Unit, onConfirmClick: () -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        showAlert(text = "确定要结算吗？", onConfirmClick = onConfirmClick) { openDialog = false }
    }
    Row(
        horizontalArrangement = Arrangement.Center, modifier = modifier
    ) {
        OutlinedButton(onClick = onClicked) {
            Text(text = "返回首页")
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        OutlinedButton(onClick = { openDialog = true }) {
            Text(text = "结算")
        }
    }

}

@Composable
private fun ItemCardContent(cart: Cart, cartViewModel: CartViewModel) {
    var isRefresh by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {

        Row {
            GoodsImage(url = cart.goods!!.thumbnail)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Text(text = cart.goods.name, maxLines = 1)
                GoodsPrice(cart)
                Row() {
                    IconButton(onClick = {
                        if (cart.num > 1) {
                            cart.num--
                        }
                        isRefresh++
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_minus),
                            contentDescription = "Minus"
                        )
                    }
                    if (isRefresh > -1) {
                        Text(text = "${cart.num}", modifier = Modifier.padding(top = 12.dp))
                    }
                    IconButton(onClick = {
                        cart.num++
                        cartViewModel.updateCart(cart)
                        isRefresh++
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                    }

                }
            }

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    }
                )
            }
        }

        if (expanded) {
            Text(
                text = "商品描述：" + cart.goods!!.content.repeat(4),
                modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
            )
        }
    }

}

@Composable
fun GoodsPrice(cart: Cart) {
    val totalPrice = cart.goods!!.price.toFloat() * cart.num
    Text(
        text = "单价:${cart.goods.price}\n金额:${totalPrice}",
        style = MaterialTheme.typography.body1,
        modifier = Modifier.padding(top = 6.dp)
    )

}

@Composable
fun GoodsImage(url: String) {
    val imageURL = Constants.NETWORK_DOMAIN + url
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageURL)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.cart),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(128.dp)
            .clip(MaterialTheme.shapes.medium)
    )
}

@Composable
fun EmptyScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    CanteenTheme {
        val goods = Goods(
            "1",
            "水果",
            content = "好吃的水果",
            price = "34",
            thumbnail = "ss",
            date = "null",
            categoryId = "1",
            dangerNum = 67,
            number = "22",
            place = "ss",
        )
        val cart = Cart("1", "2", 34, goods = goods)
        val cart1 = Cart("1", "2", 100, goods = goods)
        val cart2 = Cart("1", "2", 45, goods = goods)
        val cartList = listOf<Cart>(cart, cart1, cart2, cart)
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = cartList) { cart ->
                ItemCardContent(
                    cart,
                    cartViewModel = viewModel(modelClass = CartViewModel::class.java)
                )
            }
        }
    }
}