#!/bin/bash

fld=${PWD##*/}

mkdir ../../public/img/${fld}

cp *.png ../../public/img/${fld}
