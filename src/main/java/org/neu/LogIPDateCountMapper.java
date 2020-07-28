package org.neu;

import java.io.IOException;
import java.util.regex.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class LogIPDateCountMapper<K> extends Mapper<K, Text, LogIPDateCountCompositeKeyWritable, LongWritable> {

    private LogIPDateCountCompositeKeyWritable mappedKey = new LogIPDateCountCompositeKeyWritable();
        
    @Override
    public void map(K key, Text value, Context context) throws IOException, InterruptedException {

        if (value == null) {
            return;
        }

        String text = value.toString();

        if (text.length() == 0) {
            return;
        }

        // sample data
        // 129.10.208.141 - - [23/Jul/2013:14:33:53 -0400] "GET /favicon.ico HTTP/1.1" 404 209
        // 129.10.208.141 - - [23/Jul/2013:14:33:53 -0400] "GET /favicon.ico HTTP/1.1" 404 209
        // 129.10.208.141 - - [23/Jul/2013:14:33:56 -0400] "GET /bigdata/movielens_dataloader.rb HTTP/1.1" 200 2173
        // 10.15.8.20 - - [23/Jul/2013:14:34:07 -0400] "GET /bigdata HTTP/1.1" 301 238
        // 10.15.8.20 - - [23/Jul/2013:14:34:07 -0400] "GET /bigdata/ HTTP/1.1" 200 290
        
        Pattern pattern = Pattern.compile("(.+?) - - \\[(.+?):.*");
        Matcher matcher = pattern.matcher(text);

        String IP = "";
        String dateStr = "";

        // while (matcher.find()) {
        if (matcher.find()) {
            IP = matcher.group(1);
            dateStr = matcher.group(2);
        }

        LongWritable mappedValue = new LongWritable(1);
        
        mappedKey.setIP(IP);
        mappedKey.setDate(LogIPDateCountCompositeKeyWritable.parseDate(dateStr));

        context.write(mappedKey, mappedValue);
    }
}