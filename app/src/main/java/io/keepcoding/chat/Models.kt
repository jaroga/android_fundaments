package io.keepcoding.chat

import androidx.annotation.DrawableRes

data class User(
	val id: String,
	@DrawableRes val profileImageRes: Int,
	val name: String,
)

data class Message(
	val id: String,
	val timestamp: Long,
	val text: String,
	val sender: User,
)

data class Channel(
	val id: String,
	@DrawableRes val channelImageRes: Int,
	val name: String,
	val lastMessageTimestamp: Long,
)