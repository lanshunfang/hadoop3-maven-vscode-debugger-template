#export INPUT_PATH=hdfs://localhost:9000/NYSE
. ~/.bash_profile
export INPUT_PATH=/logs/access.log
#export OUTPUT_PATH=hdfs://localhost:9000/NYSE-stock-stat-output
export OUTPUT_PATH=/logs-output

export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home

export JAR_NAME=big_data-1.0-SNAPSHOT.jar
export JAR_PATH=target/${JAR_NAME}
export JAVA_CLASS=org.neu.DriverClass

export HOST_HADOOP_SHARED_PATH=~/Downloads/hadoop-shared/

export HADOOP_VERSION=3.3.0
export HADOOP_HOME=/usr/local/Cellar/hadoop/${HADOOP_VERSION}/libexec
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop/
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
#export HADOOP_OPTS="-Djava.library.path=$HADOOP_COMMON_LIB_NATIVE_DIR"

export HADOOP_OPTS="$HADOOP_OPTS -Djava.library.path=$HADOOP_HOME/lib/native"

export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH
export HADOOP_DATA=/Users/lanshunfang/dev/hadoop
export HADOOP_LOG_DIR=${HADOOP_DATA}/logs
 
hadoop_start_local(){
    httpUrl=http://localhost:9870
    echo "[INFO] Starting Hadoop with data stored in ${HADOOP_DATA}"
    echo "[INFO] hdfs://localhost:9000"
    echo "[INFO] ${httpUrl}"
    export JAVA_HOME=$JAVA_HOME_8
    export PATH=${JAVA_HOME}/bin:$PATH
    start-all.sh
    mr-jobhistory-daemon.sh start historyserver
    #start-dfs.sh
    #start-yarn.sh
    jps
    open ${httpUrl}
    echo ""
    echo ""
    echo ""
}
export -f hadoop_start_local

hadoop_stop_local(){
    #cd /Users/lanshunfang/Documents/NEU-Classes-Courses/BD7205/docker-hadoop-master
    #docker-compose down
    export JAVA_HOME=$JAVA_HOME_8
    export PATH=${JAVA_HOME}/bin:$PATH
    stop-all.sh
    #stop-yarn.sh
    #stop-dfs.sh
    jps
    pkill hadoop_start
}
export -f hadoop_stop_local

export HBASE_VERSION=2.2.3
export HBASE_HOME=/usr/local/Cellar/hbase/${HBASE_VERSION}/libexec
export HBASE_CONF_DIR=$HBASE_HOME/conf
export HBASE_MY_DATA_DIR=/Users/lanshunfang/dev/hadoop/hbase
export PATH=$HBASE_HOME/bin:$PATH
hbase_start(){
    #export MY_HBASE_HOME=/usr/local/Cellar/hbase/2.2.3/libexec/

    if [[ ! -d $HADOOP_HOME  ]]; then
        echo "$HADOOP_HOME is not a directory. Maybe it's updated? See brew list hadoop; and brew pin hadoop"
        return
    fi
    #hadoop_start_local
    start-all.sh
    brew pin hbase
    cd $HBASE_HOME
    ./bin/start-hbase.sh
}
export -f hbase_start

hbase_stop(){
    #export MY_HBASE_HOME=/usr/local/Cellar/hbase/2.2.3/libexec/

    if [[ ! -d $HADOOP_HOME  ]]; then
        echo "$HADOOP_HOME is not a directory. Maybe it's updated? See brew list hadoop; and brew pin hadoop"
        return
    fi
    #stop-all.sh
    hadoop_stop_local
    brew pin hbase
    cd $HBASE_HOME
    ./bin/stop-hbase.sh
}
export -f hbase_stop
hbase_config(){
    if [[ ! -d $HADOOP_HOME  ]]; then
        echo "$HADOOP_HOME is not a directory. Maybe it's updated? See brew list hadoop; and brew pin hadoop"
        return
    fi
    # cp $HBASE_CONF_DIR/hbase-env.sh ~/tmp/hbase/
    # cp $HBASE_CONF_DIR/hbase-site.xml ~/tmp/hbase/
    # run  tail -f /usr/local/var/log/hbase/* to see errors
    # hadoop fs -mkdir /hbase first
    brew pin hadoop
    cp ~/tmp/hbase/* $HBASE_CONF_DIR
}
export -f hbase_config


export HIVE_VERSION=3.1.2_1
export HIVE_HOME=/usr/local/Cellar/hive/${HIVE_VERSION}/libexec
export PATH="$HIVE_HOME/bin:$PATH"
hive_start(){
    hadoop_start_local
    brew services restart mysql
    hive_hdfs_init
    hive
    # mkdir -p ~/hive/warehouse
}
export -f hive_start

hive_stop(){
    hadoop_stop_local
    brew services stop mysql
    pkill hive
    # mkdir -p ~/hive/warehouse
}
export -f hive_stop
hive_hdfs_init(){
    hadoop fs -mkdir /tmp
    hadoop fs -mkdir -p /user/hive/warehouse
    hadoop fs -chmod g+w /tmp
    hadoop fs -chmod g+w /user/hive/warehouse

}

hive_config(){
    #cp $HIVE_HOME/conf/hive-default.xml.template ~/dev/hadoop/hive/conf/
    #ln -s ~/dev/hadoop/hive/conf/hive-default.xml.template $HIVE_HOME/conf/hive-site.xml

    hive_hdfs_init

    cp ~/dev/hadoop/hive/mysql-connector-java-8.0.21.jar $HIVE_HOME/lib/

    # mysql -uroot
        #mysql> CREATE DATABASE metastore;
        #mysql> USE metastore;
        #mysql> CREATE USER 'hiveuser'@'localhost' IDENTIFIED BY 'hivePWD';
        #mysql> GRANT SELECT,INSERT,UPDATE,DELETE,ALTER,CREATE ON metastore.* TO 'hiveuser'@'localhost';
        #mysql> GRANT ALL PRIVILEGES ON metastore.* TO 'hiveuser'@'localhost';
    schematool -initSchema -dbType mysql -userName hiveuser -passWord hivePWD -verbose


}
export -f hive_config

export PIG_VERSION=0.17.0_1
export PIG_HOME=/usr/local/Cellar/pig/${PIG_VERSION}/libexec
export PIG_HADOOP_VERSION=${HADOOP_VERSION}
export PIG_CLASSPATH=$HADOOP_HOME/conf
export PIG_MY_DATA_HOME=~/dev/hadoop/pig/
pig_start(){
    brew pin pig
    export JAVA_HOME=${JAVA_HOME_8}
    hadoop_stop_local
    hadoop_start_local
    export PATH=$PIG_HOME/bin:$PATH
    # pig -x local -l $PIG_HOME/logs -4 $PIG_HOME/conf/nolog.conf
    #pig -l ${PIG_MY_DATA_HOME}/logs
    echo "[INFO] open http://localhost:8088/cluster/apps to check the MapReduce logs"
    open http://localhost:8088/cluster/apps
    pig -x mapreduce -l ${PIG_MY_DATA_HOME}/logs

}
export -f pig_start

export HOMEBREW_FORCE_BREWED_CURL=1