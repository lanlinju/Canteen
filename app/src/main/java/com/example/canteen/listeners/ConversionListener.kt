package com.example.canteen.listeners

import com.example.canteen.models.User

interface ConversionListener {
    fun onConversionClicked(user: User)
}