package io.keepcoding.chat.conversation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.keepcoding.chat.Channel
import io.keepcoding.chat.Message
import io.keepcoding.chat.Repository
import io.keepcoding.chat.common.TextChangedWatcher
import io.keepcoding.chat.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {

	private val binding: ActivityConversationBinding by lazy {
		ActivityConversationBinding.inflate(layoutInflater)
	}
	private val vm: ConversationViewModel by viewModels {
		ConversationViewModel.ConversationViewModelProviderFactory(Repository)
	}
	private val messagesAdapter: MessagesAdapter = MessagesAdapter()
	private val channelId: String by lazy { intent.getStringExtra(CHANNEL_ID)!! }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		binding.conversation.apply {
			layoutManager = LinearLayoutManager(context).apply {
				stackFromEnd = true
			}
			adapter = messagesAdapter
		}
		vm.state.observe(this) {
			when (it) {
				is ConversationViewModel.State.MessagesReceived -> {
					renderMessages(it.messages)
					hideLoading()
				}
				is ConversationViewModel.State.Error.ErrorLoading -> {
					hideLoading()
				}
				is ConversationViewModel.State.Error.ErrorWithMessages -> {
					renderMessages(it.messages)
					hideLoading()
				}
				is ConversationViewModel.State.LoadingMessages.Loading -> {
					showLoading()
				}
				is ConversationViewModel.State.LoadingMessages.LoadingWithMessages -> {
					renderMessages(it.messages)
				}
			}
		}
		vm.message.observe(this) {
			binding.tvMessage.apply {
				setText(it)
				setSelection(it.length)
			}
		}
		binding.tvMessage.addTextChangedListener(TextChangedWatcher(vm::onInputMessageUpdated))
		binding.sendButton.setOnClickListener { vm.sendMessage(channelId) }
	}

	private fun renderMessages(messages: List<Message>) {
		messagesAdapter.submitList(messages) { binding.conversation.smoothScrollToPosition(messages.size) }
	}

	private fun showLoading() {
		// TODO: Show loading
	}

	private fun hideLoading() {
		// TODO: Hide loading
	}

	override fun onResume() {
		super.onResume()
		vm.loadConversation(channelId)
	}

	companion object {
		const val CHANNEL_ID = "CHANNEL_ID"

		fun createIntent(context: Context, channel: Channel): Intent =
			Intent(
				context,
				ConversationActivity::class.java
			).apply {
				putExtra(CHANNEL_ID, channel.id)
			}
	}
}