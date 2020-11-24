#include "BiQuadraticFilter.h"
#include <jni.h>
#include <cmath>


BiQuadraticFilter::BiQuadraticFilter(
	const BiQuadraticFilter::Type& type,
	const double& frequency,
	const double& gain,
	const double& width,
	const double& sampleRate
) : AudioProcessor()
{
	this->_enabled = false;
	this->configure(
		type,
		frequency,
		gain,
		width,
		sampleRate
	);
}

BiQuadraticFilter::BiQuadraticFilter(const BiQuadraticFilter& other)
{
	this->_enabled = other._enabled;
	this->_type = other._type;
	this->_f = other._f;
	this->_g = other._g;
	this->_q = other._q;
	this->_fs = other._fs;
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

BiQuadraticFilter::~BiQuadraticFilter()
{
}

const BiQuadraticFilter::Type& BiQuadraticFilter::getType()
{
	return this->_type;
}

void BiQuadraticFilter::setType(const BiQuadraticFilter::Type& value)
{
	this->_type = value;
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

const double& BiQuadraticFilter::getFrequency()
{
	return this->_f;
}

void BiQuadraticFilter::setFrequency(const double& value)
{
	this->_f = value;
	if(this->_f < _frequency_minimum)
	{
		this->_f = _frequency_minimum;
	}
	else if(this->_f > _frequency_maximum)
	{
		this->_f = _frequency_maximum;
	}
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

const double& BiQuadraticFilter::getGain()
{
	return this->_g;
}

void BiQuadraticFilter::setGain(const double& value)
{
	this->_g = value;
	if(this->_g < _gain_minimum)
	{
		this->_g = _gain_minimum;
	}
	else if(this->_g > _gain_maximum)
	{
		this->_g = _gain_maximum;
	}
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

const double& BiQuadraticFilter::getWidth()
{
	return this->_q;
}

void BiQuadraticFilter::setWidth(const double& value)
{
	this->_q = value;
	if(this->_q < _q_minimum)
	{
		this->_q = _q_minimum;
	}
	else if(this->_q > _q_maximum)
	{
		this->_q = _q_maximum;
	}
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

const bool& BiQuadraticFilter::getEnabled()
{
	return this->_enabled;
}

void BiQuadraticFilter::setEnabled(const bool& enabled)
{
	this->_enabled = enabled;
}

double BiQuadraticFilter::amplitude(const double& frequency)
{
	double phi = std::pow(2.0, std::sin(2.0 * M_PI * frequency / (2.0 * this->_fs)));
	double a2 = std::pow(2.0, this->_a0 + this->_a1 + this->_a2);
	double b2 = std::pow(2.0, this->_b0 + this->_b1 + this->_b2);
	double a4 = this->_a0*this->_a1 + 4.0*this->_a0*this->_a2 + this->_a1*this->_a2;
	double b4 = this->_b0*this->_b1 + 4.0*this->_b0*this->_b2 + this->_b1*this->_b2;
	double numerator = b2 * 4.0*(b4)*phi + 16.0*this->_b0*this->_b2*phi*phi;
	double denominator = a2 * 4.0*(a4)*phi + 16.0*this->_a0*this->_a2*phi*phi;
	return 20.0 * std::log10(std::sqrt(numerator / denominator));
}

void BiQuadraticFilter::configure(
	const BiQuadraticFilter::Type& type,
	const double& frequency,
	const double& gain,
	const double& width,
	const double& rate
)
{
	this->reset();
	this->_type = type;
	this->_fs = rate;
	this->_f = frequency;
	this->_g = gain;
	this->_q = width;
	this->computeConstants();
}

void BiQuadraticFilter::reset()
{
	this->_x1 = 0.0;
	this->_x2 = 0.0;
	this->_y1 = 0.0;
	this->_y2 = 0.0;
}

void BiQuadraticFilter::computeConstants()
{
	auto A = std::pow(10.0, this->_g / 40.0);
	auto omega = 2.0 * M_PI * this->_f / this->_fs;
	auto sin = std::sin(omega);
	auto cos = std::cos(omega);
	auto alpha = sin / (2.0 * this->_q);
	auto beta = std::sqrt(A + A);
	double m1;
	double m2;
	double m3;
	switch(this->_type)
	{
		case Type::BandPass:
			this->_b0 = alpha;
			this->_b1 = +0.0;
			this->_b2 = -alpha;
			this->_a0 = +1.0 + alpha;
			this->_a1 = -2.0 * cos;
			this->_a2 = +1.0 - alpha;
			break;
		case Type::LowPass:
			m1 = 1.0 - cos;
			m2 = m1 / 2.0;
			this->_b0 = m2;
			this->_b1 = m1;
			this->_b2 = m2;
			this->_a0 = +1.0 + alpha;
			this->_a1 = -2.0 * cos;
			this->_a2 = +1.0 - alpha;
			break;
		case Type::HighPass:
			m1 = 1.0 + cos;
			m2 = m1 / 2.0;
			this->_b0 = m2;
			this->_b1 = -m1;
			this->_b2 = m2;
			this->_a0 = +1.0 + alpha;
			this->_a1 = -2.0 * cos;
			this->_a2 = +1.0 - alpha;
			break;
		case Type::Notch:
			m1 = -2.0 * cos;
			this->_b0 = +1.0;
			this->_b1 = m1;
			this->_b2 = +1.0;
			this->_a0 = +1.0 + alpha;
			this->_a1 = m1;
			this->_a2 = +1.0 - alpha;
			break;
		case Type::Bell:
			m1 = alpha * A;
			m2 = alpha / A;
			m3 = -2.0 * cos;
			this->_b0 = +1.0 + m1;
			this->_b1 = m3;
			this->_b2 = +1.0 - m1;
			this->_a0 = +1.0 + m2;
			this->_a1 = m3;
			this->_a2 = +1.0 - m2;
			break;
		case Type::LowShelf:
			m1 = A + 1.0;
			m2 = A - 1.0;
			this->_b0 = A * (m1 - m2 * cos + beta * sin);
			this->_b1 = +2.0 * A * (m2 - m1 * cos);
			this->_b2 = A * (m1 - m2 * cos - beta * sin);
			this->_a0 = m1 + m2 * cos + beta * sin;
			this->_a1 = -2.0 * (m2 + m1 * cos);
			this->_a2 = m1 + m2 * cos - beta * sin;
			break;
		case Type::HighShelf:
			m1 = A + 1.0;
			m2 = A - 1.0;
			this->_b0 = A * (m1 + m2 * cos + beta * sin);
			this->_b1 = -2.0 * A * (m2 + m1 * cos);
			this->_b2 = A * (m1 + m2 * cos - beta * sin);
			this->_a0 = m1 - m2 * cos + beta * sin;
			this->_a1 = 2.0 * (m2 - m1 * cos);
			this->_a2 = m1 - m2 * cos - beta * sin;
			break;
	}
	this->_b0 /= this->_a0;
	this->_b1 /= this->_a0;
	this->_b2 /= this->_a0;
	this->_a1 /= this->_a0;
	this->_a2 /= this->_a0;
	this->_a0 = 1.0;
}

const juce::String BiQuadraticFilter::getName() const
{
	return "BiQuadraticFilter";
}

void BiQuadraticFilter::prepareToPlay(double sampleRate, int maximumExpectedSamplesPerBlock)
{
	this->_fs = sampleRate;
	this->configure(
		this->_type,
		this->_f,
		this->_g,
		this->_q,
		this->_fs
	);
}

void BiQuadraticFilter::processBlock(
	juce::AudioBuffer<double> &buffer,
	juce::MidiBuffer __unused &midiMessages
)
{
	int numberOfChannels = buffer.getNumChannels();
	int numberOfSamples = buffer.getNumSamples();
	for(int channel = 0; channel < numberOfChannels; channel++)
	{
		for(int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++)
		{
			double sample = buffer.getSample(channel, sampleIndex);
			buffer.setSample(channel, sampleIndex, this->filter(sample));
		}
	}
}

void BiQuadraticFilter::processBlock(
	juce::AudioBuffer<float> &buffer,
	juce::MidiBuffer __unused &midiMessages
)
{
	int numberOfChannels = buffer.getNumChannels();
	int numberOfSamples = buffer.getNumSamples();
	for(int channel = 0; channel < numberOfChannels; channel++)
	{
		for(int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++)
		{
			double sample = buffer.getSample(channel, sampleIndex);
			buffer.setSample(channel, sampleIndex, this->filter(sample));
		}
	}
}

void BiQuadraticFilter::releaseResources(){}

bool BiQuadraticFilter::acceptsMidi() const
{
	return false;
}

bool BiQuadraticFilter::producesMidi() const
{
	return false;
}

bool BiQuadraticFilter::supportsMPE() const
{
	return false;
}

bool BiQuadraticFilter::isMidiEffect() const
{
	return false;
}

double BiQuadraticFilter::getTailLengthSeconds() const
{
	return 0;
}

juce::AudioProcessorEditor *BiQuadraticFilter::createEditor()
{
	return nullptr;
}

bool BiQuadraticFilter::hasEditor() const
{
	return false;
}

int BiQuadraticFilter::getNumPrograms()
{
	return 0;
}

int BiQuadraticFilter::getCurrentProgram()
{
	return 0;
}

void BiQuadraticFilter::setCurrentProgram(int index){}

const juce::String BiQuadraticFilter::getProgramName(int index)
{
	return juce::String();
}

void BiQuadraticFilter::changeProgramName(int index, const juce::String &newName){}

void BiQuadraticFilter::getStateInformation(juce::MemoryBlock &destData){}

void BiQuadraticFilter::setStateInformation(const void *data, int sizeInBytes){}

double BiQuadraticFilter::filter(double sample)
{
	this->_y = (this->_b0 * sample) +
	           (this->_b1 * this->_x1) +
	           (this->_b2 * this->_x2) -
	           (this->_a1 * this->_y1) -
	           (this->_a2 * this->_y2);
	this->_x2 = this->_x1;
	this->_x1 = sample;
	this->_y2 = this->_y1;
	this->_y1 = this->_y;
	return (float)this->_y;
}

extern "C"
{
	int getEnumValue(JNIEnv * env, jobject enumObj)
	{
		jclass klass = env->FindClass(
			"midoriiro/io/core/audiofx/effects/BiQuadraticFilter$Type"
		);
		jmethodID method = env->GetMethodID(klass, "getValue", "()I");
		return env->CallIntMethod(enumObj, method);
	}
	
	BiQuadraticFilter* getFilterPointerFromLong(jlong pointer)
	{
		return (BiQuadraticFilter*)pointer;
	}

	JNIEXPORT jlong JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_constructor(
		JNIEnv * env,
		jobject __unused obj,
		jobject type,
		jdouble frequency,
		jdouble gain,
		jdouble width,
		jdouble sampleRate
	)
	{
		return (jlong)new BiQuadraticFilter(
			(BiQuadraticFilter::Type)getEnumValue(env, type),
			frequency,
			gain,
			width,
			sampleRate
		);
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_setType(
		JNIEnv * env,
		jobject __unused obj,
		jlong pointer,
		jobject type
	)
	{
		getFilterPointerFromLong(pointer)->setType((BiQuadraticFilter::Type)getEnumValue(env, type));
	}

	JNIEXPORT jobject JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_getType(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return (jobject) getFilterPointerFromLong(pointer)->getType();
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_setFrequency(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jdouble frequency)
	{
		getFilterPointerFromLong(pointer)->setFrequency(frequency);
	}

	JNIEXPORT jdouble JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_getFrequency(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return getFilterPointerFromLong(pointer)->getFrequency();
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_setGain(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jdouble gain
	)
	{
		getFilterPointerFromLong(pointer)->setGain(gain);
	}
	
	JNIEXPORT jdouble JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_getGain(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return getFilterPointerFromLong(pointer)->getGain();
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_setWidth(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jdouble width
	)
	{
		getFilterPointerFromLong(pointer)->setWidth(width);
	}

	JNIEXPORT jdouble JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_getWidth(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return getFilterPointerFromLong(pointer)->getWidth();
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_setEnabled(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jboolean state
	)
	{
		getFilterPointerFromLong(pointer)->setEnabled(state);
	}

	JNIEXPORT jboolean JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_getEnabled(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return getFilterPointerFromLong(pointer)->getEnabled();
	}

	JNIEXPORT jdouble JNICALL
	Java_midoriiro_io_core_audiofx_effects_BiQuadraticFilterNative_amplitude(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jdouble frequency
	)
	{
		return getFilterPointerFromLong(pointer)->amplitude(frequency);
	}
}