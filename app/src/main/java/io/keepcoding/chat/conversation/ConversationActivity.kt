package io.keepcoding.chat.conversation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.keepcoding.chat.Channel
import io.keepcoding.chat.Message
import io.keepcoding.chat.Repository
import io.keepcoding.chat.common.TextChangedWatcher
import io.keepcoding.chat.databinding.ActivityConversationBinding
import java.io.IOException

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

		val progressBarContainer = binding.progressBarConversationLayout

		binding.conversation.apply {
			layoutManager = LinearLayoutManager(context).apply {
				stackFromEnd = true
			}
			adapter = messagesAdapter
		}
		vm.state.observe(this) {
			when (it) {
				is ConversationViewModel.State.MessagesReceived -> {
					showLoading(progressBarContainer)
					if (it.messages.size > 0 ) {
						renderMessages(it.messages)
						binding.noMessegeListLayout.visibility = View.GONE
					} else{
						binding.noMessegeListLayout.visibility = View.VISIBLE
					}

					binding.root.postDelayed({
						hideLoading(progressBarContainer)
					},1000)
				}
				is ConversationViewModel.State.Error.ErrorLoading -> {
					Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
					hideLoading(progressBarContainer)
				}
				is ConversationViewModel.State.Error.ErrorWithMessages -> {
					Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
					renderMessages(it.messages)
					hideLoading(progressBarContainer)
				}
				is ConversationViewModel.State.LoadingMessages.Loading -> {
					showLoading(progressBarContainer)
				}
				is ConversationViewModel.State.LoadingMessages.LoadingWithMessages -> {
					if (it.messages.size > 0 ) {
						renderMessages(it.messages)
						binding.noMessegeListLayout.visibility = View.GONE
					} else{
						binding.noMessegeListLayout.visibility = View.VISIBLE
					}
				}
			}
		}
		vm.message.observe(this) {
			binding.tvMessage.apply {
				setText(it)
				setSelection(it.length)
			}
		}

		vm.sendButtonEnabled.observe(this){
			binding.sendButton.isEnabled = it
			if (it) {
				binding.sendButton.alpha = 1.0.toFloat()
			} else {
				binding.sendButton.alpha = 0.1.toFloat()
			}
		}

		binding.tvMessage.addTextChangedListener(
			TextChangedWatcher(vm::onInputMessageUpdated)
		)
		binding.sendButton.setOnClickListener {
			println("Enviando mensaje")
			vm.sendMessage(channelId)
		}
	}

	private fun renderMessages(messages: List<Message>) {
		messagesAdapter.submitList(messages) { binding.conversation.smoothScrollToPosition(messages.size) }
	}

	private fun showLoading(progressBarContainer: RelativeLayout) {
		println("Mostrando ProgressBar")
		progressBarContainer.visibility = View.VISIBLE
	}

	private fun hideLoading(progressBarContainer: RelativeLayout) {
		println("Mostrando ProgressBar")
		progressBarContainer.visibility = View.GONE
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