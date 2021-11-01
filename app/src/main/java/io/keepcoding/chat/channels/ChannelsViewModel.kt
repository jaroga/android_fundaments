package io.keepcoding.chat.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.keepcoding.chat.Channel
import io.keepcoding.chat.Repository

class ChannelsViewModel(private val repository: Repository) : ViewModel() {

	private val _state: MutableLiveData<State> = MutableLiveData<State>().apply {
		postValue(State.LoadingChannels.Loading)
	}

	val state: LiveData<State> = _state

	fun loadChannels() {
		_state.postValue(
			_state.value?.let {
				when (it) {
					is State.ChannelsReceived -> State.LoadingChannels.LoadingWithChannels(it.channels)
					is State.Error.ErrorWithChannels -> State.LoadingChannels.LoadingWithChannels(it.channels)
					is State.LoadingChannels -> it
					else -> State.LoadingChannels.Loading
				}
			}
		)
		val result = repository.getChannels()
		_state.postValue(result.fold(::onChannelsReceived, ::onError))
	}

	private fun onChannelsReceived(channels: List<Channel>): State.ChannelsReceived =
		State.ChannelsReceived(channels.sortedByDescending { it.lastMessageTimestamp })

	private fun onError(throwable: Throwable): State =
		when (val currentState = _state.value) {
			is State.LoadingChannels.LoadingWithChannels -> State.Error.ErrorWithChannels(
				currentState.channels,
				throwable
			)
			is State.ChannelsReceived -> State.Error.ErrorWithChannels(
				currentState.channels,
				throwable
			)
			is State.Error.ErrorWithChannels -> currentState.copy(throwable = throwable)
			else -> State.Error.ErrorLoading(throwable)
		}

	sealed class State {
		sealed class LoadingChannels : State() {
			object Loading : LoadingChannels()
			data class LoadingWithChannels(val channels: List<Channel>) : LoadingChannels()
		}

		data class ChannelsReceived(val channels: List<Channel>) : State()
		sealed class Error : State() {
			abstract val throwable: Throwable

			data class ErrorLoading(override val throwable: Throwable) : Error()
			data class ErrorWithChannels(
				val channels: List<Channel>,
				override val throwable: Throwable
			) : Error()
		}
	}

	class ChannelsViewModelProviderFactory(
		private val repository: Repository,
	) : ViewModelProvider.Factory {
		override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
			ChannelsViewModel::class.java -> ChannelsViewModel(repository) as T
			else -> throw IllegalArgumentException("ChannelsViewModelProviderFactory can only create instances of the ChannelsViewModel")
		}
	}
}