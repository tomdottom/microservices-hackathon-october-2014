#
# planet-force.py, 25 Oct 14

import math

G=6.67384e-11

def gravity_force(m1, m2, dist):
   return G*m1*m2/math.pow(dist, 2)


def planet_vector(me_p, other_p):
   p_dist=math.sqrt(pow(me_p[0]-other_p[0], 2)+pow(me_p[1]-other_p[1], 2))
   g_force=gravity_force(me_p[2], other_p[2], p_dist)
   x_vect=(other_p[0]-me_p[0])*g_force
   y_vect=(other_p[1]-me_p[1])*g_force
   return [x_vect, y_vect]


def all_forces(the_universe, planet_back, universe_back):
   for my_planet in the_universe:
      my_vector=[0, 0]
      for other_planet in the_universe:
         if (my_planet != other_planet):
            a_vector=planet_vector(my_planet, other_planet)
            my_vector[0]+=a_vector[0]
            my_vector[1]+=a_vector[1]
      planet_back(my_vector)
   universe_back()
   return

def print_universe():
   print "Done universe"

def print_planet(f_vect):
   print f_vect

def test_data():
   return [[1, 1, 10], [2, 3, 20]]

if __name__ == "__main__":
   uni=test_data()
   all_forces(uni, print_planet, print_universe)
