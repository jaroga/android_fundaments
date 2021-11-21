package io.keepcoding.chat.conversation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.keepcoding.chat.Message
import io.keepcoding.chat.Repository
import io.keepcoding.chat.databinding.ViewMessageBinding
import io.keepcoding.chat.databinding.ViewMessageOutgoingBinding
import io.keepcoding.chat.extensions.inflater

class MessagesAdapter(
	diffUtilCallback: DiffUtil.ItemCallback<Message> = DIFF
) : ListAdapter<Message, RecyclerView.ViewHolder >(diffUtilCallback) {
	val OUTPUT_MESSAGE: Int = 1
	val INPUT_MESSAGE: Int = 0

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = run {
		if (viewType == OUTPUT_MESSAGE) {
			MessageViewHolderOutput(parent)
		} else {
			MessageViewHolder(parent)
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder){
			is MessageViewHolderOutput -> holder.bind(getItem(position))
			is MessageViewHolder -> holder.bind(getItem(position))
		}
	}

	override fun getItemViewType(position: Int): Int {
		var message = currentList[position]
		if (message.sender.id == Repository.currentSender.id) {
			return OUTPUT_MESSAGE
		} else {
			return INPUT_MESSAGE
		}
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
			binding.messageUserAvatar.setImageResource(message.sender.profileImageRes)
			binding.messageUserLabel.text = "${message.sender.name}:"
			binding.messageUser.text = message.text
		}
	}
	class MessageViewHolderOutput(
		parent: ViewGroup,
		private val binding: ViewMessageOutgoingBinding = ViewMessageOutgoingBinding.inflate(
			parent.inflater,
			parent,
			false
		)
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(message: Message) {
			binding.messageUserAvatar.setImageResource(message.sender.profileImageRes)
			binding.messageUserLabel.text = "${message.sender.name}:"
			binding.messageUser.text = message.text
		}
	}
}