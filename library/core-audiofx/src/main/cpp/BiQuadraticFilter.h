#ifndef MAD_FX_BIQUADRATICFILTER_H
#define MAD_FX_BIQUADRATICFILTER_H

#include <JuceHeader.h>

class BiQuadraticFilter : public juce::AudioProcessor
{
public:
	enum Type
	{
		LowPass,
		HighPass,
		BandPass,
		Bell,
		Notch,
		LowShelf,
		HighShelf
	};
	
	BiQuadraticFilter(
		const Type& type,
		const double& frequency,
		const double& gain,
		const double& width,
		const double& sampleRate
	);
	
	BiQuadraticFilter(
		const BiQuadraticFilter& other
	);
	
	~BiQuadraticFilter();
	
	const Type& getType();
	
	void setType(const Type& value);
	
	const double& getFrequency();
	
	void setFrequency(const double& value);
	
	const double& getGain();
	
	void setGain(const double& value);
	
	const double& getWidth();
	
	void setWidth(const double& value);
	
	const bool& getEnabled();
	
	void setEnabled(const bool& enabled);
	
	double amplitude(const double& frequency);
	
	const juce::String getName() const override;
	
	void prepareToPlay(double sampleRate, int maximumExpectedSamplesPerBlock) override;
	
	void processBlock(juce::AudioBuffer<double> &buffer, juce::MidiBuffer &midiMessages) override;
	
	void processBlock(juce::AudioBuffer<float> &buffer, juce::MidiBuffer &midiMessages) override;
	
	void releaseResources() override;
	
	bool acceptsMidi() const override;
	
	bool producesMidi() const override;
	
	bool supportsMPE() const override;
	
	bool isMidiEffect() const override;
	
	double getTailLengthSeconds() const override;
	
	juce::AudioProcessorEditor *createEditor() override;
	
	bool hasEditor() const override;
	
	int getNumPrograms() override;
	
	int getCurrentProgram() override;
	
	void setCurrentProgram(int index) override;
	
	const juce::String getProgramName(int index) override;
	
	void changeProgramName(int index, const juce::String &newName) override;
	
	void getStateInformation(juce::MemoryBlock &destData) override;
	
	void setStateInformation(const void *data, int sizeInBytes) override;

private:
	double filter(double sample);
	
	void configure(
		const Type& type,
		const double& frequency,
		const double& gain,
		const double& width,
		const double& rate
	);
	
	void reset() override;
	
	void computeConstants();
	
	constexpr static const float _q_minimum = 0.025f;
	constexpr static const float _q_maximum = 40.0f;
	constexpr static const float _gain_minimum = -30.0f;
	constexpr static const float _gain_maximum = 30.0f;
	constexpr static const float _frequency_minimum = 10.0f;
	constexpr static const float _frequency_maximum = 30000.0f;
	
	bool _enabled;
	
	double _x1 = 0.0;
	double _x2 = 0.0;
	double _y = 0.0;
	double _y1 = 0.0;
	double _y2 = 0.0;
	
	double _a0 = 0.0;
	double _a1 = 0.0;
	double _a2 = 0.0;
	double _b0 = 0.0;
	double _b1 = 0.0;
	double _b2 = 0.0;
	
	double _fs = 0.0;
	double _f = 0.0;
	double _g = 0.0;
	double _q = 0.0;
	Type _type = Type::Bell;
};

#endif //MAD_FX_BIQUADRATICFILTER_H