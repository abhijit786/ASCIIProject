package com;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

public static void main(String[] args) {

logging("Opening File....","info");
logging("Processing File....","info");
logging("Closing File....","info");



}


public static void logging(String message,String severity)
{
Logger logger = Logger.getLogger("MyLog");
FileHandler fh;

try {

// This block configure the logger with handler and formatter
fh = new FileHandler("W:/Srikar T/Legacy Reports/Liveops Report Automation/logger.txt",true);
logger.addHandler(fh);
//logger.setLevel(Level.ALL);
SimpleFormatter formatter = new SimpleFormatter();
fh.setFormatter(formatter);

// the following statement is used to log any messages
if(severity.equals("info"))
{
logger.log(Level.INFO, message); 
}
else if(severity.equals("warning"))
{
logger.log(Level.WARNING, message); 
}
else if(severity.equals("severe"))
{
logger.log(Level.SEVERE, message); 
}


} catch (SecurityException e) {
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}
}
}