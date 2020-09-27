package midoriiro.madfx.audio.services

import android.media.audiofx.Visualizer
import midoriiro.madfx.audio.events.FftDataCaptureEvent
import midoriiro.madfx.audio.events.WaveFormDataCaptureEvent
import org.greenrobot.eventbus.EventBus

class VisualizerService
{
	// TODO enable/disable service based on subscriber demand. Avoid sending data into the void
	companion object
	{
		private val _visualizer = Visualizer(0)
		
		val visualizer: Visualizer
			get() = this._visualizer
		
		var isEnabled: Boolean
			set(value)
			{
				this._visualizer.enabled = value
			}
			get() = this._visualizer.enabled
		
		var isNormalized: Boolean
			set(value)
			{
				this._visualizer.enabled = false
				
				if(value)
				{
					this._visualizer.scalingMode = Visualizer.SCALING_MODE_NORMALIZED
				}
				else
				{
					this._visualizer.scalingMode = Visualizer.SCALING_MODE_AS_PLAYED
				}
				
				this._visualizer.enabled = true
			}
			get() = this._visualizer.scalingMode == Visualizer.SCALING_MODE_NORMALIZED
		
		init
		{
			this._visualizer.enabled = false
			this._visualizer.captureSize = Visualizer.getCaptureSizeRange().last()
			this._visualizer.setDataCaptureListener(
				object: Visualizer.OnDataCaptureListener{
					override fun onWaveFormDataCapture(visualizer: Visualizer, waveform: ByteArray, samplingRate: Int)
					{
						EventBus.getDefault().post(WaveFormDataCaptureEvent(waveform))
					}
					override fun onFftDataCapture(visualizer: Visualizer, fft: ByteArray, samplingRate: Int) {
						EventBus.getDefault().post(FftDataCaptureEvent(fft))
					}
				},
				Visualizer.getMaxCaptureRate(),
				true,
				false
			)
			this._visualizer.enabled = true
		}
	}
}