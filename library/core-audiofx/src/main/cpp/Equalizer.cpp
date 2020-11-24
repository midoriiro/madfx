#include "Equalizer.h"
#include <jni.h>

Equalizer::Equalizer()
{
	this->_player = new juce::AudioProcessorPlayer(true);
	this->_graph = new juce::AudioProcessorGraph();
	this->_manager = new juce::AudioDeviceManager();
	this->_nodes = std::vector<juce::AudioProcessorGraph::Node::Ptr>(16);
	
	this->_player->setProcessor(this->_graph);
	this->_manager->initialiseWithDefaultDevices(
		2,
		2
	);
	this->_manager->addAudioCallback(this->_player);
	this->_device = this->_manager->getCurrentAudioDevice();
	this->_inputChannels = this->_device->getActiveInputChannels().getHighestBit() + 1;
	this->_outputChannels = this->_device->getActiveOutputChannels().getHighestBit() + 1;
	this->_graph->setPlayConfigDetails(
		this->_inputChannels,
		this->_outputChannels,
		this->_device->getCurrentSampleRate(),
		this->_device->getCurrentBufferSizeSamples()
	);
	this->_graph->prepareToPlay(
		this->_device->getCurrentSampleRate(),
		this->_device->getCurrentBufferSizeSamples()
	);
	this->_inputNode = this->_graph->addNode(
		std::make_unique<juce::AudioProcessorGraph::AudioGraphIOProcessor>(
			juce::AudioProcessorGraph::AudioGraphIOProcessor::audioInputNode
		)
	);
	this->_outputNode = this->_graph->addNode(
		std::make_unique<juce::AudioProcessorGraph::AudioGraphIOProcessor>(
			juce::AudioProcessorGraph::AudioGraphIOProcessor::audioOutputNode
		)
	);
	this->connect();
}

Equalizer::~Equalizer()
{
	this->_manager->removeAudioCallback(this->_player);
	this->_player->setProcessor(nullptr);
	this->_graph->releaseResources();
	this->_graph->clear();
	this->_nodes.clear();
	delete this->_player;
	delete this->_graph;
	delete this->_manager;
}

bool Equalizer::add(const BiQuadraticFilter& filter)
{
	for(auto node : this->_graph->getNodes())
	{
		if(node->getProcessor() == &filter)
		{
			return false;
		}
	}
	auto node = this->_graph->addNode(std::make_unique<BiQuadraticFilter>(filter));
	this->_nodes.push_back(node);
	this->connect();
	return true;
}

bool Equalizer::remove(const BiQuadraticFilter& filter)
{
	for(auto node : this->_graph->getNodes())
	{
		if(node->getProcessor() == &filter)
		{
			auto iterator = std::find(this->_nodes.cbegin(), this->_nodes.cend(), node);
			int index = std::distance(this->_nodes.cbegin(), iterator);
			this->_graph->removeNode(node);
			this->_nodes.erase(this->_nodes.cbegin() + index);
			this->connect();
			return true;
		}
	}
	return false;
}

BiQuadraticFilter* Equalizer::get(const int &index)
{
	if(!(0 <= index && index <= this->_nodes.size() - 1))
	{
		return nullptr;
	}
	return (BiQuadraticFilter*)this->_nodes[index]->getProcessor();
}

void Equalizer::update()
{
	this->connect();
}

int Equalizer::size()
{
	return this->_nodes.size();
}

void Equalizer::connect()
{
	auto nodes = this->getEnabledFilters();
	for(auto connection : this->_graph->getConnections())
	{
		this->_graph->removeConnection(connection);
	}
	if(nodes.empty())
	{
		for(int channel = 0 ; channel < this->_inputChannels ; ++channel)
		{
			this->_graph->addConnection({
				{this->_inputNode->nodeID,  channel},
				{this->_outputNode->nodeID, channel}
			});
		}
	}
	else if(nodes.size() == 1)
	{
		for (int channel = 0 ; channel < this->_inputChannels ; ++channel)
		{
			this->_graph->addConnection({
				{this->_inputNode->nodeID, channel},
				{nodes[0]->nodeID,  channel}
			});
			this->_graph->addConnection({
				{nodes[0]->nodeID,   channel},
				{this->_outputNode->nodeID, channel}
			});
		}
	}
	else
	{
		for(int index = 0 ; index < this->_nodes.size() - 1 ; ++index)
		{
			for(int channel = 0 ; channel < this->_inputChannels ; channel++)
			{
				this->_graph->addConnection({
					{nodes[index]->nodeID, channel},
					{nodes[index + 1]->nodeID, channel}
				});
			}
			for (int channel = 0 ; channel < this->_inputChannels ; ++channel)
			{
				this->_graph->addConnection({
					{this->_inputNode->nodeID, channel},
					{nodes[0]->nodeID, channel}
				});
				this->_graph->addConnection({
					{nodes[nodes.size() - 1]->nodeID, channel},
					{this->_outputNode->nodeID, channel}
				});
			}
		}
	}
}

std::vector<juce::AudioProcessorGraph::Node::Ptr> Equalizer::getEnabledFilters()
{
	const auto condition = [&](const juce::AudioProcessorGraph::Node::Ptr & x)
	{
		return ((BiQuadraticFilter*)x->getProcessor())->getEnabled();
	};
	auto iterator = std::find_if(
		this->_nodes.cbegin(),
		this->_nodes.cend(),
		condition
	);
	auto nodes = std::vector<juce::AudioProcessorGraph::Node::Ptr>();
	while(iterator != this->_nodes.cend())
	{
		int index = std::distance(std::cbegin(this->_nodes), iterator);
		nodes.push_back(this->_nodes[index]);
		iterator = std::find_if(std::next(iterator), std::cend(this->_nodes), condition);
	}
	return nodes;
}

extern "C"
{
	Equalizer* getEqualizerPointerFromLong(jlong pointer)
	{
		return (Equalizer*)pointer;
	}

	BiQuadraticFilter* getFilterReferenceFromLong(jlong pointer)
	{
		return (BiQuadraticFilter*)pointer;
	}
	
	JNIEXPORT jlong JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_constructor(
		JNIEnv * __unused env,
		jobject __unused obj
	)
	{
		return (jlong)new Equalizer();
	}

	JNIEXPORT jboolean JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_add(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jlong filter
	)
	{
		return getEqualizerPointerFromLong(pointer)->add(*getFilterReferenceFromLong(filter));
	}

	JNIEXPORT jboolean JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_remove(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jlong filter
	)
	{
		return getEqualizerPointerFromLong(pointer)->remove(*getFilterReferenceFromLong(filter));
	}

	JNIEXPORT jlong JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_get(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer,
		jint index
	)
	{
		return (jlong)getEqualizerPointerFromLong(pointer)->get(index);
	}

	JNIEXPORT void JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_update(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		getEqualizerPointerFromLong(pointer)->update();
	}

	JNIEXPORT jint JNICALL
	Java_midoriiro_io_core_audiofx_effects_Equalizer_size(
		JNIEnv * __unused env,
		jobject __unused obj,
		jlong pointer
	)
	{
		return (jlong)getEqualizerPointerFromLong(pointer)->size();
	}
}