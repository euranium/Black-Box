"""
program to cd to a directory and executes a command
@params: path command [args]
"""

from sys import argv
from os import chdir
from subprocess import call
# verify input
if len(argv) < 2:
    print("params: path command [args]")
    exit()
# first arg is current path, so splice it out along with path
args = argv[2:-1]
if len(args) == 0:
    args = argv[2]
path = argv[1]
chdir(path)
call(args)
