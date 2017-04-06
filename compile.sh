#!/bin/sh
if ! [ -d classes ];
then
   mkdir classes
fi
javac -d classes mySRE/*.java
