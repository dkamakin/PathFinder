#!/bin/bash

path=$1
cd $path
for subDir in $(awk -F'repository: file://' '{print $2}' Chart.yaml)
do
  echo "Updating dependencies in "$(pwd)"/"$subDir" ..."
  rm -rf "$(pwd)"/"$subDir"/charts
  helm dep up "$(pwd)"/"$subDir"
  echo
done
echo "Updating dependencies in "$(pwd)" ..."
rm -rf "$(pwd)"/charts
helm dep up "$(pwd)"
echo