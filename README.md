# hadoop3-maven-vscode-debugger-template

Hadoop is an open source framework over Google MapReduce big-data paper. Due to historical issue, I got real tough lesson to set it up. Here I share my work as a template for you to get started.

# Features
- Hadoop 3, avoiding all the hassling of org.apache.hadoop.mapre or org.apache.hadoop.mapreduce
- Vscode IDE tempalte with gitignore
- Vscode in-IDE real-time debugger launch.json
- Maven

# Usage
## Presequisites
- Java 8 installed and set as `JAVA_HOME`. Java version must be Java 8, not higher.
- Maven is installed
## Step
- `mvn install` in VSCode terminal;
- Or just use VSCode auto maven import to let the IDE do the job
- Open `src/main/java/org/neu/DriverClass.java` in VSCode
- Click `Debug` upon the `public static void main(...`
  - You may want to set a break point before debugging by clicking the line number in left side of the IDE
