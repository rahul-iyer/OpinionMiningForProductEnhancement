import numpy as np
import sys
from scipy import spatial
data = np.genfromtxt('C:\\xampp\\htdocs\\reco\\suggestion.txt', delimiter = ',',dtype=None)
names=np.array( [x[5] for x in data] )
cpu= np.array( [x[0] for x in data] )
ram= np.array( [x[1] for x in data] )
battery=np.array( [x[2] for x in data] )
camera=np.array( [x[3] for x in data] )
screen=np.array( [x[4] for x in data] )
tree = spatial.KDTree(zip(cpu.ravel(), ram.ravel(),battery.ravel(),camera.ravel(),screen.ravel()))

cpu_1=int(sys.argv[1])
ram_1=int(sys.argv[1])
battery_1=int(sys.argv[1])
camera_1=int(sys.argv[1])
screen_1=int(sys.argv[1])
z=tree.query(np.array([cpu_1,ram_1,battery_1,camera_1,screen_1]),3)
print "Recommended phones are:"
print names[z[1][0]]
print names[z[1][1]]
