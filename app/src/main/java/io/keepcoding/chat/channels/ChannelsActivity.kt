package io.keepcoding.chat.channels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
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

		val swipeContainer = binding.swipeContainer
		swipeContainer.setOnRefreshListener {
			vm.loadChannels()
			println("Actualizando")
			swipeContainer.setRefreshing(false)
		}

		val progressBarContainer = binding.progressBarChannelsLayout

		binding.topics.apply {
			adapter = channelsAdapter
			addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
		}
		vm.state.observe(this) {
			when (it) {
				is ChannelsViewModel.State.ChannelsReceived -> {
					showLoading(progressBarContainer)
					if (it.channels.size > 0) {
						channelsAdapter.submitList(it.channels)
						binding.noChannelListLayout.visibility = View.GONE
					} else {
						binding.noChannelListLayout.visibility = View.VISIBLE
					}

					binding.root.postDelayed({
						hideLoading(progressBarContainer)
					},1000)
				}
				is ChannelsViewModel.State.Error.ErrorLoading -> {
					Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
					hideLoading(progressBarContainer)
				}
				is ChannelsViewModel.State.Error.ErrorWithChannels -> {
					Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
					channelsAdapter.submitList(it.channels)
					hideLoading(progressBarContainer)
				}
				is ChannelsViewModel.State.LoadingChannels.Loading -> {
					showLoading(progressBarContainer)
				}
				is ChannelsViewModel.State.LoadingChannels.LoadingWithChannels -> {
					if (it.channels.size > 0) {
						channelsAdapter.submitList(it.channels)
						binding.noChannelListLayout.visibility = View.GONE
					} else {
						binding.noChannelListLayout.visibility = View.VISIBLE
					}
				}
			}
		}
	}

	private fun showLoading(progressBarContainer: RelativeLayout) {
		println("Mostrando ProgressBar")
		progressBarContainer.visibility = View.VISIBLE
	}

	private fun hideLoading(progressBarContainer: RelativeLayout) {
		println("Ocultando ProgressBar")
		progressBarContainer.visibility = View.GONE
	}

	override fun onResume() {
		super.onResume()
		vm.loadChannels()
	}

	private fun openChannel(channel: Channel) {
		startActivity(ConversationActivity.createIntent(this, channel))
	}
}