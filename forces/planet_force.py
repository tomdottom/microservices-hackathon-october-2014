#
# planet-force.py, 25 Oct 14

import math

# Newtons' universal gravitational constant
G=6.67384e-11

# Law of gravity
def gravity_force(m1, m2, dist):
   if dist < 1.0:
      dist = 1.0
   print "%s %s %s" %(m1, m2, dist)
   return G*m1*m2/math.pow(dist, 2)


# Calculate force vector between two planets
def planet_vector(me_p, other_p):
   p_dist=math.sqrt(pow(me_p[0]-other_p[0], 2)+pow(me_p[1]-other_p[1], 2))
   # print p_dist
   g_force=gravity_force(me_p[2], other_p[2], p_dist)
   if not g_force:
      g_force = 0.00000001
   xy=float(abs(other_p[0]-me_p[0])+abs(other_p[1]-me_p[1]))
   x_vect=((other_p[0]-me_p[0])/xy)*g_force
   y_vect=((other_p[1]-me_p[1])/xy)*g_force
   # print p_dist, " ", xy, " ", g_force, [x_vect, y_vect]
   return [x_vect, y_vect]


# Loop over all planets
# Calculate total force experienced by every planet
def all_forces(the_universe, planet_back, universe_back):
   for my_planet in the_universe:
      my_vector=[0, 0]
      for other_planet in the_universe:
         if (my_planet != other_planet):
            a_vector=planet_vector(my_planet, other_planet)
            my_vector[0]+=a_vector[0]
            my_vector[1]+=a_vector[1]
# Pass information to planet callback
      planet_back([my_vector[0], my_vector[1], my_planet[3]])
   universe_back()
   return

def print_universe():
   print "Done universe"

def print_planet(f_vect):
   print f_vect

def test_data():
   return [[1, 1, 10, "a"], [2, 3, 20, "b"], [3, 1, 4, "c"]]

if __name__ == "__main__":
   uni=test_data()
   all_forces(uni, print_planet, print_universe)
