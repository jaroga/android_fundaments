package io.keepcoding.chat

import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.UUID
import kotlin.random.Random

object Repository {

	val currentSender: User = DummyData.randomUser()

	private val random: Random = Random(System.currentTimeMillis())
	private var channels: List<Channel> = DummyData.channels
	private val conversations: Map<String, MutableList<Message>> =
		DummyData.createConversations(channels)

	fun getChannels(): Result<List<Channel>> {
		return Result.success(channels).takeIfRandom()
	}

	fun getMessages(channelId: String): Result<List<Message>> {
		return conversations[channelId]
			?.let { Result.success(it).takeIfRandom() }
			?: Result.failure(IllegalArgumentException("ChannelId '$channelId' doesn't exists"))
	}

	fun sendMessage(channelId: String, text: String): Result<Message> {
		val newMessage = Message(
			id = UUID.randomUUID().toString(),
			timestamp = System.currentTimeMillis(),
			text = text,
			sender = currentSender
		)

		return Result.success(newMessage).takeIfRandom()
			.fold(
				onSuccess = {
					if (conversations[channelId]?.add(newMessage) == true) {
						channels = channels.map { channel ->
							channel
								.takeIf { it.id == channelId }
								?.copy(lastMessageTimestamp = newMessage.timestamp)
								?: channel
						}
						Result.success(it)
					} else {
						Result.failure(IllegalArgumentException("ChannelId '$channelId' doesn't exists"))
					}
				},
				onFailure = { Result.failure(it) }
			)
	}

	private fun <T> Result<T>.takeIfRandom(): Result<T> =
		this.takeUnless { (random.nextInt() % 20) == 0 }
			?: Result.failure(IOException("There was an error, try again later"))
}