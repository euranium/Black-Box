"""
program to cd to a directory and executes a command
@params: command [args] path
"""

from sys import argv
from os import chdir
from subprocess import call
args = argv[1:-1]
path = argv[-1]
chdir(path)
call(args)
