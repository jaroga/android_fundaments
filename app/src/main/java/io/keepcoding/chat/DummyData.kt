package io.keepcoding.chat

import java.util.UUID
import kotlin.random.Random

object DummyData {

	val channels: List<Channel> by lazy { CHANNELS }

	fun createConversations(channels: List<Channel>): Map<String, MutableList<Message>> =
		channels.associate { it.id to generateMessages(it) }

	private fun generateMessages(channel: Channel): MutableList<Message> =
		MutableList(random.nextInt(10)) {
			Message(
				id = randomId(),
				timestamp = channel.lastMessageTimestamp.takeIf { it == 0L }
					?: randomTimeBefore(channel.lastMessageTimestamp),
				text = randomMessageText(),
				sender = randomUser(),
			)
		}

	private fun randomMessageText() = MESSAGES.random(random)

	fun randomUser() = USERS.random(random)

	private fun randomTimeBefore(time: Long): Long = time - random.nextInt(36000)
	private fun randomId() = UUID.randomUUID().toString()
	private val random = Random(System.currentTimeMillis())
	private val CHANNELS = listOf(
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_android,
			name = "Android",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_ios,
			name = "iOS",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_flutter,
			name = "Flutter",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_firebase,
			name = "Firebase",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_git,
			name = "Git",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_react_native,
			name = "React Native",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_kotlin,
			name = "Kotlin",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_swift,
			name = "Swift",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_javascript,
			name = "Javascript",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_goland,
			name = "Go",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_mobile,
			name = "Mobile",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_development,
			name = "Development",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_random,
			name = "Random",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
		Channel(
			id = randomId(),
			channelImageRes = R.drawable.ic_product,
			name = "Product",
			lastMessageTimestamp = randomTimeBefore(System.currentTimeMillis()),
		),
	)

	private val USERS = listOf(
		User(randomId(), R.drawable.dani_avatar, "Dani"),
		User(randomId(), R.drawable.juan_avatar, "Juan"),
		User(randomId(), R.drawable.jose_avatar, "Jose"),
		User(randomId(), R.drawable.maria_avatar, "María"),
		User(randomId(), R.drawable.isa_avatar, "Isa"),
		User(randomId(), R.drawable.carmen_avatar, "Carmen"),
		User(randomId(), R.drawable.tomas_avatar, "Tomás"),
		User(randomId(), R.drawable.alicia_avatar, "Alicia"),
		User(randomId(), R.drawable.marina_avatar, "Marina"),
		User(randomId(), R.drawable.esther_avatar, "Esther"),
	)

	private val MESSAGES = listOf(
		"Am if number no up period regard sudden better",
		"Decisively surrounded all admiration and not you",
		"Out particular sympathize not favourable introduced insipidity but ham",
		"Rather number can and set prais",
		"Distrusts an it contented perceived attending oh",
		"Thoroughly estimating introduced stimulated why but motionless",
		"Surrounded to me occasional pianoforte alteration unaffected impossible ye",
		"For saw half than cold",
		"Pretty merits waited six talked pulled you",
		"Conduct replied off led whether any shortly why arrived adapted",
		"Numerous ladyship so raillery humoured goodness received an",
		"So narrow formal length my highly longer afford oh",
		"Tall neat he make or at dull ye",
		"Son agreed others exeter period myself few yet nature",
		"Mention mr manners opinion if garrets enabled",
		"To an occasional dissimilar impossible sentiments",
		"Do fortune account written prepare invited no passage",
		"Garrets use ten you the weather ferrars venture friends",
		"I love this movie mate",
		"For who thoroughly her boy estimating conviction",
		"Removed demands expense account in outward tedious do",
		"Particular way thoroughly unaffected projection favourable mrs can projecting own",
		"Thirty it matter enable become admire in giving",
		"See resolved goodness felicity shy civility domestic had but",
		"Drawings offended yet answered jennings perceive laughing six did far",
		"Admiration stimulated cultivated reasonable be projection possession of",
		"Real no near room ye bred sake if some",
		"Is arranging furnished knowledge agreeable so",
		"Fanny as smile up small",
		"It vulgar chatty simple months turned oh at change of",
		"Astonished set expression solicitude way admiration",
		"Now principles discovered off increasing how reasonably middletons men",
		"Add seems out man met plate court sense",
		"His joy she worth truth given",
		"Jin Yang is super funny. Look ",
		"You agreeable breakfast his set perceived immediate",
		"Stimulated man are projecting favourable middletons can cultivated",
		"Certainty determine at of arranging perceived situation or",
		"Or wholly pretty county in oppose",
		"Favour met itself wanted settle put garret twenty",
		"In astonished apartments resolution so an it",
		"Unsatiable on by contrasted to reasonable companions an",
		"On otherwise no admitting to suspicion furniture it",
		"Considered discovered ye sentiments projecting entreaties of melancholy is",
		"In expression an solicitude principles in do",
		"Hard do me sigh with west same lady",
		"Their saved linen downs tears son add music",
		"Expression alteration entreaties mrs can terminated estimating",
		"Her too add narrow having wished",
		"To things so denied admire",
		"Am wound worth water he linen at vexed",
		"By impossible of in difficulty discovered celebrated ye",
		"Justice joy manners boy met resolve produce",
		"Bed head loud next plan rent had easy add him",
		"As earnestly shameless elsewhere defective estimable fulfilled of",
		"Esteem my advice it an excuse enable",
		"Few household abilities believing determine zealously his repulsive",
		"To open draw dear be by side like",
		"Or neglected agreeable of discovery concluded oh it sportsman",
		"Week to time in john",
		"Son elegance use weddings separate",
		"Ask too matter formed county wicket oppose talent",
		"He immediate sometimes or to dependent in",
		"Everything few frequently discretion surrounded did simplicity decisively",
		"Less he year do with no sure loud",
	)
}

