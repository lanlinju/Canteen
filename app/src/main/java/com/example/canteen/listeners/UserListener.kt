package com.example.canteen.listeners

import com.example.canteen.models.User

interface UserListener {
    fun onUserClicked(user:User)
}