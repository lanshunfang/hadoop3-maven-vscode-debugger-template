package org.neu;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class LogIPDateCountPartioning extends Partitioner<LogIPDateCountCompositeKeyWritable, IntWritable> {

    @Override
    public int getPartition(LogIPDateCountCompositeKeyWritable key, IntWritable value, int countOfPartitions) {
        return Math.abs(key.getIP().hashCode() & Integer.MAX_VALUE) % countOfPartitions;
    }

}