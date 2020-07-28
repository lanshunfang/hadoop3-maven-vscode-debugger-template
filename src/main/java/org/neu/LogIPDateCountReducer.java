package org.neu;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LogIPDateCountReducer extends Reducer<LogIPDateCountCompositeKeyWritable, LongWritable, Text, LongWritable> {

    private Text finalKey = new Text();
    private LongWritable finalValue = new LongWritable();
    @Override
    public void reduce(LogIPDateCountCompositeKeyWritable key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {
             
        finalKey.set(key.toString());
        finalValue.set(getStat(values));
        context.write(finalKey, finalValue);
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