#!/bin/bash

#
# Copyright 2019,2020,2021 yoyosource
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Config
buildDir="./build"

# Code
crc32 -file $(find ./src/main/java/yapion/serializing/serializer/ -type f) > $buildDir/tmp/serializerCache/serializer.crc32
hash=$(crc32 $buildDir/tmp/serializerCache/serializer.crc32)
echo "crc32Value: $hash"

# Not used since a bit slower than the code above
# hash=$(crc32 -file `find ./src/main/java/yapion/serializing/serializer/ -type f` | md5)
# echo "md5Hash: hash"

if [[ -d "$buildDir/tmp/serializerCache/$hash" ]]
then
  if [[ -f "$buildDir/tmp/serializerCache/$hash/serializer.pack" ]]
  then
    echo "Using cached serializer."
    cp -r $buildDir/tmp/serializerCache/$hash/* ./build/classes/java/main/yapion/serializing/
    exit 0
  fi
fi

echo "Generating serializer."
java -cp $buildDir/classes/java/main/ yapion/serializing/serializer/SerializerPacker

echo "Creating cache for serializer."
mkdir -p $buildDir/tmp/serializerCache/$hash
cp $buildDir/classes/java/main/yapion/serializing/serializer.pack $buildDir/tmp/serializerCache/$hash/
cp $buildDir/classes/java/main/yapion/serializing/serializer.pack.meta $buildDir/tmp/serializerCache/$hash/