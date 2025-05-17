#!/bin/bash

SRC_DIR=src/com/syafiqriza/rushhoursolver
BIN_DIR=bin
JAR_NAME=RushHour.jar
MAIN_CLASS=com.syafiqriza.rushhoursolver.Main

mkdir -p $BIN_DIR

/usr/bin/find ./$SRC_DIR -name "*.java" > sources.txt
javac -d $BIN_DIR -sourcepath /$SRC_DIR @sources.txt
rm sources.txt

jar cfe $BIN_DIR/$JAR_NAME $MAIN_CLASS -C $BIN_DIR .

echo "Compilation and packaging complete. Run with:"
echo "java -jar $BIN_DIR/$JAR_NAME"