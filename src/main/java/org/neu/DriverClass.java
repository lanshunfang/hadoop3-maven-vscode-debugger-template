package org.neu;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class DriverClass {

    public static void main(String[] args) throws Exception {
        Configuration conf = loadConfiguration();
        // $HADOOP_CONF_DIR
        // /usr/local/Cellar/hadoop/3.2.1_1/libexec/etc/hadoop/

        Job job = Job.getInstance(conf, Constants.JOB_NAME);

        job.setJarByClass(DriverClass.class);

        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);

        System.out.println(String.format("Working on input path %s and output path %s", args[0], args[1]));

        deleteOutputPath(outputPath, conf);

        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapperClass(LogIPDateCountMapper.class);

        job.setMapOutputKeyClass(LogIPDateCountCompositeKeyWritable.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setCombinerClass(LogIPDateCountCombiner.class);

        job.setReducerClass(LogIPDateCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setGroupingComparatorClass(LogIPDateCountGrouping.class);
        job.setSortComparatorClass(LogIPDateCountCompositeKeyComparator.class);
        job.setPartitionerClass(LogIPDateCountPartioning.class);

        try {
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void deleteOutputPath(Path filePath, Configuration conf) {
        try {
            FileSystem hdfs = FileSystem.get(conf);
            if (hdfs.exists(filePath)) {
                hdfs.delete(filePath, true);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static Configuration loadConfiguration() {
        Path coreSiteXml = new Path("./config/core-site.xml");
        // Path hdfsSiteXml = new Path("./config/hdfs-site.xml");
        // Path mapredSiteXml = new Path("./config/mapred-site.xml");
        Configuration conf = new Configuration();
        conf.addResource(coreSiteXml);
        // conf.addResource(hdfsSiteXml);
        // conf.addResource(mapredSiteXml);

        return conf;
    }
}
