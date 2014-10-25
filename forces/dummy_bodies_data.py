from client.client import TopicClient
from time import sleep
from multiprocessing import Process
import json
import itertools

from planet_force import all_forces
id_gen = itertools.count()


def example_send(send_func):
    test_data = [
        {
            'id': id_gen.next(),
            'name': 'planet',
            'radius': 10,
            'mass': 10,
            'location': [0, 0],
            'vector': [0, 0],
        },
        {
            'id': id_gen.next(),
            'name': 'planet',
            'radius': 20,
            'mass': 20,
            'location': [40, 40],
            'vector': [40, 40],
        }
    ]
    try:
        while True:
            print "Sending Test Data Message"
            send_func(json.dumps(test_data), 'force.ignore')
            sleep(2)
    except KeyboardInterrupt:
        pass


if __name__ == '__main__':

    print "Starting"

    tc = TopicClient()

    processes = []

    p2 = Process(target=example_send, args=(tc.publish_to_topic,))
    p2.start()
    processes.append(p2)

    try:
        for process in processes:
            process.join()
    except KeyboardInterrupt:
        print "Keyboard interrupt in main"
    finally:
        print "Cleaning up Main"
