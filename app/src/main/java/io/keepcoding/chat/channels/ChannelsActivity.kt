package io.keepcoding.chat.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import io.keepcoding.chat.Channel
import io.keepcoding.chat.Repository
import io.keepcoding.chat.conversation.ConversationActivity
import io.keepcoding.chat.databinding.ActivityChannelsBinding

class ChannelsActivity : AppCompatActivity() {

	val binding: ActivityChannelsBinding by lazy { ActivityChannelsBinding.inflate(layoutInflater) }
	val channelsAdapter: ChannelsAdapter by lazy { ChannelsAdapter(::openChannel) }
	val vm: ChannelsViewModel by viewModels {
		ChannelsViewModel.ChannelsViewModelProviderFactory(Repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		binding.topics.apply {
			adapter = channelsAdapter
			addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
		}
		vm.state.observe(this) {
			when (it) {
				is ChannelsViewModel.State.ChannelsReceived -> {
					channelsAdapter.submitList(it.channels)
					hideLoading()
				}
				is ChannelsViewModel.State.Error.ErrorLoading -> {
					hideLoading()
				}
				is ChannelsViewModel.State.Error.ErrorWithChannels -> {
					channelsAdapter.submitList(it.channels)
					hideLoading()
				}
				is ChannelsViewModel.State.LoadingChannels.Loading -> {
					showLoading()
				}
				is ChannelsViewModel.State.LoadingChannels.LoadingWithChannels -> {
					channelsAdapter.submitList(it.channels)
				}
			}
		}
	}

	private fun showLoading() {
		// TODO: Show loading
	}

	private fun hideLoading() {
		// TODO: Hide loading
	}

	override fun onResume() {
		super.onResume()
		vm.loadChannels()
	}

	private fun openChannel(channel: Channel) {
		startActivity(ConversationActivity.createIntent(this, channel))
	}
}