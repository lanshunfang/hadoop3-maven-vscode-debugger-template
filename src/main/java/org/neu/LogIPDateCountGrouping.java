package org.neu;

import org.apache.hadoop.io.*;

public class LogIPDateCountGrouping extends WritableComparator {

    public LogIPDateCountGrouping() {
        super(LogIPDateCountCompositeKeyWritable.class, true);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable o1, WritableComparable o2) {
        LogIPDateCountCompositeKeyWritable source = (LogIPDateCountCompositeKeyWritable) o1;
        LogIPDateCountCompositeKeyWritable target = (LogIPDateCountCompositeKeyWritable) o2;

        // return source.getIP().compareTo(target.getIP());
        return source.compareTo(target);
    }

}