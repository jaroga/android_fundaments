package io.keepcoding.chat.conversation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.chat.Message
import io.keepcoding.chat.databinding.ViewMessageBinding
import io.keepcoding.chat.extensions.inflater

class MessagesAdapter(
	diffUtilCallback: DiffUtil.ItemCallback<Message> = DIFF
) : ListAdapter<Message, MessagesAdapter.MessageViewHolder>(diffUtilCallback) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
		MessageViewHolder(parent)

	override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	companion object {
		val DIFF = object : DiffUtil.ItemCallback<Message>() {
			override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
				oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
				oldItem == newItem
		}
	}

	class MessageViewHolder(
		parent: ViewGroup,
		private val binding: ViewMessageBinding = ViewMessageBinding.inflate(
			parent.inflater,
			parent,
			false
		)
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(message: Message) {
			binding.channelName.text = "${message.sender.name}: ${message.text}"
		}
	}
}