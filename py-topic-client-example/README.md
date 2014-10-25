#Setup / dependencies

pip install pika

#Multiprocess subscribe & publish example

	python main.py
	
#Basic subscroption example

	from client import client
	
	# Create topic client
	tc = client.TopicClient()
	
	# Create callback function
	def callback(ch, method, properties, body):
		print " [x] %r:%r" % (method.routing_key, body,)
    
	# Bind callback to incoming messages
	tc.subscribe_to_topic(callback, 'topic_name')

#Basic publish example

	from client import client
	
	# Create topic client
	tc = client.TopicClient()
	
	# Create callback function
	def callback(ch, method, properties, body):
		print " [x] %r:%r" % (method.routing_key, body,)
    
	# Bind callback to incoming messages
	publish_to_topic('test message', 'topic_name')

	
	
	

