#!/bin/bash

if (( $# != 2 )); then
  echo "Please enter all needed parameters"
  echo "1. Version"
  echo "2. Named Path"
  exit 1
fi

version="$1"
path="$2"

status=$(git status)
if [[ $status == *"$path/$version"* ]]; then
  echo "No new Version detected!"
  exit 0
fi

echo "Adding new files to git"
git add "$path/$version/"
git add gradle.properties
git add "$path/maven-metadata.xml"
git add "$path/maven-metadata.xml.md5"
git add "$path/maven-metadata.xml.sha1"
git add "$path/maven-metadata.xml.sha256"
git add "$path/maven-metadata.xml.sha512"

echo "Creating new commit with name 'Maven Release $version'"
git commit -m "Maven Release $version"
echo "Creating new tag with name 'V$version'"
git tag "V$version"

echo "Should this commit be pushed? [Y/n]"
read answer
if [ "$answer" == "${answer#[Yy]}" ] ;then
  echo "Use 'git push' and 'git push --tags' to manually push this to origin."
  exit
fi

git push
git push --tags