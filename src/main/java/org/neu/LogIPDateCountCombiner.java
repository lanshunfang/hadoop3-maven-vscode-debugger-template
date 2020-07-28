package org.neu;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class LogIPDateCountCombiner
        extends Reducer<LogIPDateCountCompositeKeyWritable, LongWritable, LogIPDateCountCompositeKeyWritable, LongWritable> {

    //private LogIPDateCountCompositeKeyWritable intermediateKey = new LogIPDgitateCountCompositeKeyWritable();
    private LongWritable intermediateValue = new LongWritable();
    @Override
    public void reduce(LogIPDateCountCompositeKeyWritable key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {
             
        intermediateValue.set(getStat(values));
        context.write(key, intermediateValue);
    }

    private long getStat(Iterable<LongWritable> values) {

        long count = 0;

        Iterator<LongWritable> iterator = values.iterator();

        while (iterator.hasNext()) {
            LongWritable current = iterator.next();
            long countCurrent = current.get();
            count += countCurrent;
        }

        return count;
    }

}