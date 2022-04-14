package com.example.canteen.fragments.cartfragment


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card


import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.canteen.R
import com.example.canteen.models.Cart
import com.example.canteen.models.Goods
import com.example.canteen.utilities.Constants
import com.example.canteen.viewmodels.CartViewModel
import com.example.composetutorialsample.ui.theme.CanteenTheme
import com.example.canteen.components.showAlert

import com.example.composetutorialsample.ui.theme.LightBlue


@Composable
fun CartDetail(cartViewModel: CartViewModel, fragment: CartFragment) {
    val cartList by cartViewModel.cartListLiveData.observeAsState()
    Scaffold(bottomBar = {
        BottomButton(
            onClicked = {
                fragment.findNavController().navigate(R.id.homeFragment)
            })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
        ) {
            cartList?.let {
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(1f)
                ) {
                    items(items = it) { cart ->
                        ItemContainerCardDetail(cart)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomButton(onClicked: () -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog){
        showAlert(text = "确定结算吗？") { openDialog = false }
    }
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.background))
    ) {
        OutlinedButton(onClick = onClicked) {
            Text(text = "返回首页")
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        OutlinedButton(onClick = { openDialog = true}) {
            Text(text = "结算")
        }
    }

}

@Composable
fun ItemContainerCardDetail(cart: Cart) {
    Card(
        backgroundColor = LightBlue,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        ItemCardContent(cart)
    }
}

@Composable
private fun ItemCardContent(cart: Cart) {
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
            GoodsImage(url = cart.goods.thumbnail)
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
                text = "商品描述：" + cart.goods.content.repeat(4),
                modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
            )
        }
    }

}

@Composable
fun GoodsPrice(cart: Cart) {
    val totalPrice = cart.goods.price.toFloat() * cart.num
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
            providerId = 9
        )
        val cart = Cart("1", "2", 34, goods = goods)
        val cart1 = Cart("1", "2", 100, goods = goods)
        val cart2 = Cart("1", "2", 45, goods = goods)
        val cartList = listOf<Cart>(cart, cart1, cart2, cart)
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = cartList) { cart ->
                ItemContainerCardDetail(cart)
            }
        }
    }
}