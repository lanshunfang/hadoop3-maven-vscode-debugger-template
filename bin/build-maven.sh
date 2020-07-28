#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && cd .. && pwd )"

cd "${DIR}"

source ./bin/bash_rc.sh

mvn clean package

hadoop fs -rm -r ${OUTPUT_PATH}
# hadoop jar ${JAR_PATH} ${JAVA_CLASS} ${INPUT_PATH} ${OUTPUT_PATH}
time hadoop jar ${JAR_PATH} ${JAVA_CLASS} ${INPUT_PATH} ${OUTPUT_PATH} 

hadoop fs -ls ${OUTPUT_PATH}
hadoop fs -head ${OUTPUT_PATH}/part-r-00000

# Run in Docker
# cp target/${JAR_NAME}  ${HOST_HADOOP_SHARED_PATH}

# link mvn project to hadoop-shared
#cp -r "$(pwd)/src" ~/Downloads/hadoop-shared/latest/
#cp -r "$(pwd)/pom.xml" ~/Downloads/hadoop-shared/latest/

# hadoop_shell_with_mount

# # All the rest commands are in docker shell
# #root@337ba1d73c9d:/

# # Or build project in docker install Maven
# # Only for the first time
# apt update
# apt install maven -y
# mvn -version
# # Build JAR
# PATH_TO_THE_MAVEN_PROJECT=/hadoop-shared/latest
# cd ${PATH_TO_THE_MAVEN_PROJECT}

# mvn clean package

# Run hadoop jar ${JAR_PATH} ${JAVA_CLASS} ${INPUT_PATH} ${OUTPUT_PATH} in the shared
#JAR_NAME=access_log_ip_visit-1.0-SNAPSHOT.jar
# JAR_NAME=big_data-1.0-SNAPSHOT.jar
# JAR_PATH=/hadoop-shared/${JAR_NAME}
# JAVA_CLASS=org.neu.DriverClass
# #INPUT_PATH=/logs/access.log 
# INPUT_PATH=/NYSE/
# OUTPUT_PATH=/NYSE-output

# Run Hadoop job in the JAR

# Option 1: Run via mvn
#mvn exec:java -Dexec.mainClass="${JAVA_CLASS}" -Dexec.args="${INPUT_PATH} ${OUTPUT_PATH}"  

# # Option 2: Run via hadoop jar
# hadoop fs -rm -r ${OUTPUT_PATH}
# # hadoop jar ${JAR_PATH} ${JAVA_CLASS} ${INPUT_PATH} ${OUTPUT_PATH}
# hadoop jar ${JAR_PATH} ${JAVA_CLASS} ${INPUT_PATH} ${OUTPUT_PATH} 

# # Observe job status
# # Open a new tab 
# hadoop_shell_with_mount
# hadoop job -list
# hadoop job  -status {job_id}
# # kill mapred job -kill JOB_ID

# hadoop fs -ls ${OUTPUT_PATH}

# hadoop fs -cat ${OUTPUT_PATH}/*

