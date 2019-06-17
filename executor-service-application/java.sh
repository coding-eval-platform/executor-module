# Copyright 2018-2019 Bellini & Lobo
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#    http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


#!/bin/bash

# This is a template used to create new executors
# Note that the script will be called setting the CODE and TIMEOUT global variables,
# and the inputs as arguments.

# ========================================================
# WARNING: DO NOT PRINT IN EITHER STDOUT OR STDERR!!
# ========================================================

set -e # Stop script execution if something goes wrong

# Initialization
echo "$CODE" > ./Main.java

# Compilation
javac ./Main.java > /dev/null 2> ./compile-errors

# TODO: check error code and compile errors

# Execution
timeout "$TIMEOUT" java Main "$@"
