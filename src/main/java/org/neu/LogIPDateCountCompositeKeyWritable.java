package org.neu;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hadoop.io.WritableComparable;

public class LogIPDateCountCompositeKeyWritable implements WritableComparable {
    // Some data

    private String IP;
    private Date date;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");

    public static String formatDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date parseDate(String date) {
        Date parsedDate = null;

        try {
            parsedDate = simpleDateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                parsedDate = new SimpleDateFormat("dd/MMM/yyyy").parse("01/Jan/1970");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return parsedDate;

    }

    public LogIPDateCountCompositeKeyWritable() {

    }

    public LogIPDateCountCompositeKeyWritable(String IP, Date date) {
        super();
        this.IP = IP;
        this.date = date;
    }

    public String getIP() {
        return IP;
    }

    public String setIP(String IP) {
        return this.IP = IP;
    }

    public Date getDate() {
        return date;
    }

    public Date setDate(Date date) {
        return this.date = date;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(IP);
        out.writeUTF(formatDate(date));
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        IP = in.readUTF();

        date = parseDate(in.readUTF());
    }

    @Override
    public int compareTo(Object o) {
        LogIPDateCountCompositeKeyWritable target = (LogIPDateCountCompositeKeyWritable) o;

        int comparedInt = this.date.compareTo(target.date);
        if (comparedInt == 0) {
            comparedInt = this.IP.compareTo(target.IP);
        }

        return comparedInt;
    }

    @Override
    public String toString() {

        List<List<String>> arr = new ArrayList<>();
        
        arr.add(
            Arrays.asList("Date", formatDate(this.date))
        );
        arr.add(
            Arrays.asList("IP", this.IP)
        );

        String finalString = arr.stream().map((entry) -> {
            return entry.get(0) + ":" + entry.get(1);
        }).collect(Collectors.joining(Constants.DELIMITER));

        return finalString;
    }

}