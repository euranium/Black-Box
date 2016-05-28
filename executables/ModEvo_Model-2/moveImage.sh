#!/bin/bash

fld=${PWD##*/}

mkdir ../../../public/img/gnu/${fld}

mv *.png ../../../public/img/gnu/${fld}
