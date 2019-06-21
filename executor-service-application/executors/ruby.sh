#!/bin/bash
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


# This is a template used to create new executors
# Note that the script will be called setting the CODE and TIMEOUT global variables,
# and the inputs as arguments.


# =====================================================================================================================
# WARNING: DO NOT PRINT IN EITHER STDOUT OR STDERR!!
# =====================================================================================================================

set -e # Stop script execution if something goes wrong

# ---------------------------------------------------------------------------------------------------------------------
# First define some functions to help the scripting
# ---------------------------------------------------------------------------------------------------------------------

# --------------------------------
# Output helpers
# --------------------------------

# ---
# Stores the given result in a file with the given name.
#
# @param result     ($1): The result to be stored.
# @param file_name  ($2): The name given to the file where the result will be stored.
# @return 0 if any error occurred while storing results, or any other value otherwise.
# ---
function store_result {
    local RESULT=$1
    local FILE_NAME=$2

    echo ${RESULT} > ${FILE_NAME}
}

# ---
# Reports an initialization error (i.e stores in a the file with the given name the INITIALIZATION_ERROR value).
#
# @param initialization_exit_code   ($1): The exit code the initialization process returned,
#                                         which is then returned by this function.
# @param file_name                  ($2): The name given to the file where the result will be stored.
# @return The value of the first argument.
# ---
function report_initialization_error {
    local INITIALIZATION_RESULT=$1
    local FILE_NAME=$2

    store_result INITIALIZATION_ERROR ${FILE_NAME}
    return ${INITIALIZATION_RESULT};
}

# ---
# Reports a compile error (i.e stores in a the file with the given name the COMPILE_ERROR value).
#
# @param compiler_exit_code ($1): The exit code the compiler returned, which is then returned by this function.
# @param file_name          ($2): The name given to the file where the result will be stored.
# @return The value of the first argument.
# ---
function report_compile_error {
    local COMPILE_RESULT=$1
    local FILE_NAME=$2

    store_result COMPILE_ERROR ${FILE_NAME}
    return ${COMPILE_RESULT};
}

# ---
# Reports that running the code failed
# (i.e stores in a the file with the given name the TIMEOUT or UNKNOWN_ERROR values).
# Failure might occur due to a timeout.
#
# @param exit_code  ($1): The exit code the runner command returned, which is then returned by this function.
# @param file_name  ($2): The name given to the file where the result will be stored.
# @return The value of the first argument.
# ---
function report_failed {
    local EXIT_CODE=$1
    local FILE_NAME=$2

    # First check if the result code is 124 (timeout). Check http://man7.org/linux/man-pages/man1/timeout.1.html
    if [[ ${EXIT_CODE} -eq 124 ]]
    then
        store_result TIMEOUT ${FILE_NAME};
    else
        # In any other case, report an UNKNOWN_ERROR
        store_result UNKNOWN_ERROR ${FILE_NAME};
    fi
    return ${EXIT_CODE}
}

# ---
# Reports that the process is completed (i.e stores in a the file with the given name the COMPLETED value).
#
# @param file_name  ($1): The name given to the file where the result will be stored.
# @return 0 if any error occurred while reporting, or any other value otherwise.
# ---
function report_completed {
    local FILE_NAME=$1

    store_result COMPLETED ${FILE_NAME}
}


# --------------------------------
# Execution phases
# --------------------------------

# ---
# Initializes execution stuff. This phase is mainly intended to create the file/s with the code to be run.
#
# @param code   ($1): The code to be run.
# @return 0 if no error occurred while initializing stuff, or any other value otherwise.
# ---
function initialize_code {
    local CODE=$1

    cat <<< "${CODE}" > ./main.rb # Store whatever the CODE variable has in a Main.java file in the current directory
}

# ---
# Runs the code.
#
# @param timeout    ($1):       The timeout given to run.
# @param args       ($2... $n): Arguments passed to the execution of the program.
# @return 0 if no error occurred while running the code, 124 if there is a timeout,
# or any other value in case of errors.
# ---
function run_code {
    local TIMEOUT=$1

    shift # Shift function arguments as the first one contains the timeout

    timeout ${TIMEOUT} ruby main.rb "$@"
}


# ---------------------------------------------------------------------------------------------------------------------
# Here starts the runner script
# ---------------------------------------------------------------------------------------------------------------------

# --------------------------------
# Declaration of variables
# --------------------------------

# ---
# The code to be run.
# ---
declare CODE;
# ---
# The timeout given to the program resulting from the givne code.
# ---
declare TIMEOUT;
# ---
# The name of the file where the results will be stored.
# ---
declare RESULT_FILE_NAME;


# Initialization
initialize_code "${CODE}" || report_initialization_error $? "${RESULT_FILE_NAME}"

# Execution
run_code "${TIMEOUT}" "$@" && report_completed "${RESULT_FILE_NAME}" || report_failed $? "${RESULT_FILE_NAME}"
