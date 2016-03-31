import sys

if (len(sys.argv) != 3):
    print("error, needs two arguments")
    sys.exit(-1)

print(int(sys.argv[1]) + int(sys.argv[2]))
