from client.client import TopicClient
from time import sleep
from multiprocessing import Process
import json
import itertools

id_gen = itertools.count()


def callback(ch, method, properties, body):
    print " [x] %r:%r" % (method.routing_key, body,)


def example_send(send_func=None, topic='test'):
    planet_id = id_gen.next()
    example_planet_data = {
        'id': planet_id,
        'name': 'planet_P{0}'.format(planet_id),
        'radius': 10,
        'mass': 10,
        'position': [0, 0],
        'velocity': [0, 0],
        'force': [0, 0]
    }

    try:
        while True:
            print "Sending Message"
            send_func(json.dumps(example_planet_data), topic)
            sleep(5)
    except KeyboardInterrupt:
        pass


if __name__ == '__main__':

    print "Starting"

    processes = []

    tc = TopicClient()

    tc.subscribe_to_topic(callback, 'bodies:creation')

    p1 = Process(target=tc.run, args=())
    p1.start()
    processes.append(p1)

    p2 = Process(
        target=example_send,
        kwargs=dict(send_func=tc.publish_to_topic, topic='force:dummy_planet_data'))
    p2.start()
    processes.append(p2)

    try:
        for process in processes:
            process.join()
    except KeyboardInterrupt:
        print "Keyboard interrupt in main"
    finally:
        print "Cleaning up Main"
