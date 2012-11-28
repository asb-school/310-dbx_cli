#!/bin/sh

cat svg-head.txt

cat input.txt | java -jar dist/dbx_cli.jar | head -n -1

cat svg-tail.txt
