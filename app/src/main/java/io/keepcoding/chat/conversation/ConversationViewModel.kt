package io.keepcoding.chat.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.keepcoding.chat.Message
import io.keepcoding.chat.Repository

class ConversationViewModel(private val repository: Repository) : ViewModel() {

	private val _state: MutableLiveData<State> = MutableLiveData<State>().apply {
		postValue(State.LoadingMessages.Loading)
	}
	val state: LiveData<State> = _state
	private val _message: MutableLiveData<String> = MutableLiveData("")
	val message: LiveData<String> = _message
	val sendButtonEnabled: LiveData<Boolean> = Transformations.map(_message) { it.isNotBlank() }

	fun loadConversation(channelId: String) {
		_state.postValue(
			_state.value?.let {
				when (it) {
					is State.MessagesReceived -> State.LoadingMessages.LoadingWithMessages(it.messages)
					is State.Error.ErrorWithMessages -> State.LoadingMessages.LoadingWithMessages(it.messages)
					is State.LoadingMessages -> it
					else -> State.LoadingMessages.Loading
				}
			}
		)

		val result = repository.getMessages(channelId = channelId)
		_state.postValue(result.fold(::onMessagesReceived, ::onError))
	}

	fun onInputMessageUpdated(newInputMessage: String) {
		newInputMessage.takeUnless { it == _message.value }?.let(_message::postValue)
	}

	private fun onMessagesReceived(messages: List<Message>): State.MessagesReceived =
		State.MessagesReceived(messages.sortedBy { it.timestamp })

	private fun onError(throwable: Throwable): State =
		when (val currentState = _state.value) {
			is State.LoadingMessages.LoadingWithMessages -> State.Error.ErrorWithMessages(
				currentState.messages,
				throwable
			)
			is State.MessagesReceived -> State.Error.ErrorWithMessages(
				currentState.messages,
				throwable
			)
			is State.Error.ErrorWithMessages -> currentState.copy(throwable = throwable)
			else -> State.Error.ErrorLoading(throwable)
		}

	fun sendMessage(channelId: String) {
		_message.value?.let { processSentMessage(repository.sendMessage(channelId, it)) }
	}

	private fun processSentMessage(sentMessage: Result<Message>) {
		sentMessage.onSuccess {
			onNewMessage(it)
			_message.postValue("")
		}
		sentMessage.onFailure {
			// TODO: Check if message was sent properly
		}
	}

	private fun onNewMessage(message: Message) {
		_state.postValue(
			onMessagesReceived(
				when (val currentState = _state.value) {
					is State.Error.ErrorWithMessages -> currentState.messages
					is State.LoadingMessages.LoadingWithMessages -> currentState.messages
					is State.MessagesReceived -> currentState.messages
					else -> listOf()
				} + message
			)
		)
	}

	sealed class State {
		sealed class LoadingMessages : State() {
			object Loading : LoadingMessages()
			data class LoadingWithMessages(val messages: List<Message>) : LoadingMessages()
		}

		data class MessagesReceived(val messages: List<Message>) : State()
		sealed class Error : State() {
			abstract val throwable: Throwable

			data class ErrorLoading(override val throwable: Throwable) : Error()
			data class ErrorWithMessages(
				val messages: List<Message>,
				override val throwable: Throwable
			) : Error()
		}
	}

	class ConversationViewModelProviderFactory(
		private val repository: Repository,
	) : ViewModelProvider.Factory {
		override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
			ConversationViewModel::class.java -> ConversationViewModel(repository) as T
			else -> throw IllegalArgumentException("ConversationViewModelProviderFactory can only create instances of the ConversationViewModel")
		}
	}
}