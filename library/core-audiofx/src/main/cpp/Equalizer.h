#ifndef MAD_FX_EQUALIZER_H
#define MAD_FX_EQUALIZER_H

#include <JuceHeader.h>
#include "BiQuadraticFilter.h"

/**
 * Based on:
 *  - https://webaudio.github.io/Audio-EQ-Cookbook/audio-eq-cookbook.html
 *  - https://www.diva-portal.org/smash/get/diva2:1031081/FULLTEXT01.pdf
 *  - https://arachnoid.com/BiQuadDesigner/index.html
 *  - https://groups.google.com/g/comp.dsp/c/jA-o05autEQ
 */
class Equalizer
{
public:
	Equalizer();
	~Equalizer();
	
	bool add(const BiQuadraticFilter& filter);
	
	bool remove(const BiQuadraticFilter& filter);
	
	BiQuadraticFilter* get(const int& index);
	
	void update();
	
	int size();
	
private:
	void connect();
	std::vector<juce::AudioProcessorGraph::Node::Ptr> getEnabledFilters();
	
	juce::AudioProcessorPlayer* _player;
	juce::AudioProcessorGraph* _graph;
	juce::AudioDeviceManager* _manager;
	juce::AudioIODevice* _device;
	int _inputChannels;
	int _outputChannels;
	juce::AudioProcessorGraph::Node::Ptr _inputNode;
	juce::AudioProcessorGraph::Node::Ptr _outputNode;
	std::vector<juce::AudioProcessorGraph::Node::Ptr> _nodes;
};

#endif //MAD_FX_EQUALIZER_H
