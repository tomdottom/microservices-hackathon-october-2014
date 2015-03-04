from client.client import TopicClient
from time import sleep
from multiprocessing import Process
import json
import itertools
from functools import partial

from planet_force import all_forces
id_gen = itertools.count()


def callback(ch, method, properties, body):
    print " [x] %r:%r" % (method.routing_key, body,)


def example_send(send_func=None, topic='test'):

    example_universe_data = [[0, 0, 10, 1], [10, 10, 30, 2]]

    def planet_callback(eek):
        print "planet_callback"
        example_planet_data = {
            'id': planet_id,
            'name': 'planet_P{0}'.format(planet_id),
            'radius': 10,
            'mass': 10,
            'location': [0, 0],
            'vector': [0, 0],
            'force': [0, 0]
        }
        send_func(json.dumps(example_planet_data), "force.planet")

    def universe_callback():
        print "universe_callback"

    planet_id = id_gen.next()

    try:
        while True:
            print "Calculating universe"
            all_forces(example_universe_data, planet_callback, universe_callback)
            sleep(5)
    except KeyboardInterrupt:
        pass


def format_universe_data(json_universe):
    # print "format_universe_data {0}".format(json_universe['bodies'])
    bodies = json_universe['bodies']
    our_universe_data = [
        [
            planet['location']['x'],
            planet['location']['y'],
            planet['mass'],
            planet['id']
        ]
        for planet in bodies
    ]
    return our_universe_data

if __name__ == '__main__':

    print "Starting"

    processes = []

    tc = TopicClient()

    def process_universe(ch, method, properties, body):
        # print "Processing universe"
        incoming_universe_obj = json.loads(body)
        # print "Found universe {0}".format(incoming_universe_obj)

        our_universe_data = format_universe_data(incoming_universe_obj)
        all_forces(
            our_universe_data,
            partial(planet_callback, incoming_universe_obj),
            partial(universe_callback, incoming_universe_obj)
        )

    def planet_callback(universe, data):
        print "planet callback {0}".format(data)
        print "universe {0}".format(universe)
        # planet_id = data[2]
        # force_data = data[0:2]
        new_data = {
            'id': data[2],
            'force': {
                'x': data[0],
                'y': data[1]
            }
        }
        # try:
        #     planet = filter(lambda x: x['id']==planet_id, universe['bodies'])[0]
        # except IndexError:
        #     return
        # new_data = planet.copy()
        # new_data['force'] = force_data
        # print 'new data {0}'.format(new_data)

        tc.publish_to_topic(json.dumps(new_data), 'body.force')

    def universe_callback(data):
        pass

    tc.subscribe_to_topic(process_universe, 'universe')

    p1 = Process(target=tc.run, args=())
    p1.start()
    processes.append(p1)

    # p2 = Process(
    #     target=example_send,
    #     kwargs=dict(
    #         send_func=tc.publish_to_topic,
    #         topic='force'
    #     )
    # )
    # p2.start()
    # processes.append(p2)

    try:
        for process in processes:
            process.join()
    except KeyboardInterrupt:
        print "Keyboard interrupt in main"
    finally:
        print "Cleaning up Main"
