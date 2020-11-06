package midoriiro.io.core.audiofx.receivers

import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import midoriiro.io.core.receivers.BroadcastReceiver

class AudioSessionIdReceiver : BroadcastReceiver()
{
	var onAudioSessionOpened: ((Int) -> Unit)? = null
	var onAudioSessionClosed: ((Int) -> Unit)? = null

	init
	{
		this._filter.addAction(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION)
		this._filter.addAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
	}

	override fun onReceive(context: Context, intent: Intent)
	{
		val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioEffect.ERROR)
		when (intent.action)
		{
			AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION ->
			{
				this.onAudioSessionOpened?.invoke(sessionId)
			}
			AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION ->
			{
				this.onAudioSessionClosed?.invoke(sessionId)
			}
		}
	}
}