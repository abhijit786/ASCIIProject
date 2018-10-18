package com;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CsvReaderWriter {

public static HashMap<Character,String> characterMappings = new HashMap<Character,String>();

public static String logfilepath="D:/test/log.log";
public static String inputfilePath = "D:/test/input.csv";
public static String inputsetfilePath = "D:/test/inputset.csv";
public static String outputfilepath="D:/test/output.csv";

public static int k=1;



public static void main(String[] args) {


	
// this is the hashmap that stores the mappings of the characters to their ascii equivalent

// characterMappings.put( ((char) 9),'\u0000');

	
System.out.println("Starting Program Execution.");
logging("Program Execution Started","info");

System.out.println("starting read input Set file");
logging("starting read input Set file","info");
readCsvSet(inputsetfilePath);


}




public static void writeCsv(String filePath,List<User> users ) throws IOException {
//create demo Users
FileWriter fileWriter = new FileWriter(filePath);

try {

fileWriter.append("Id|First Name|Last Name\n");

for(User u : users)
{

fileWriter.append(replace(String.valueOf(u.getRegion()),1));
fileWriter.append("|");
fileWriter.append(replace(u.getCountry(),2));
fileWriter.append("|");
fileWriter.append(replace(u.getItemtype(),3));
fileWriter.append("\n");
k++;
}

} catch (Exception ex) {
ex.printStackTrace();
logging(ex.getMessage(),"severe");
} finally {
try {
fileWriter.flush();
fileWriter.close();
} catch (Exception e) {
e.printStackTrace();
logging(e.toString(),"severe");
}
}
}








public static boolean checkCsv(String filePath,List<User> users ) {


//create demo Users
try
{
boolean result;
for(User u : users)
{

	result=check(u.getRegion(),1);


if(result==false)
{

	
	return false;
	
}
else
{
	result=check(u.getCountry(),2);
}
	

if(result==false)
{
	return false;
}
else
{
	result=check(u.getItemtype(),3);
}

if(result==false)
{
	return false;
}
	
}
return true;
}catch(Exception e)
{
	e.printStackTrace();
	logging(e.getMessage(),"severe");
}
return false;
}



public static void readCsv(String filePath) {
BufferedReader reader = null;

try {
List<User> users = new ArrayList();
String line = "";
reader = new BufferedReader(new FileReader(filePath));
String[] header = reader.readLine().split("\\|");
reader.readLine();


if(header[0].equals("region") && header[1].equals("country") && header[2].equals("itemtype"))
{
while((line = reader.readLine()) != null) {
String[] fields = line.split("\\|");

if(fields.length > 0) {
User user = new User();
user.setRegion(fields[0]);
user.setCountry(fields[1]);
user.setItemtype(fields[2]);
users.add(user);
}
}

for(User u: users) {
System.out.printf("[userId=%s, firstName=%s, lastName=%s]\n", u.getRegion(), u.getCountry(), u.getItemtype());

}
System.out.println("Total Records in input file "+users.size());
logging("Total Records to be processed:"+users.size(),"info");

if(checkCsv(outputfilepath, users))
{
	System.out.println("Performing the Replacement of characters....");
	logging("Performing the Replacement of characters....","info");
	logging("File Cant be processed as it has extra character","alert");
	writeCsv(outputfilepath, users);
}
else{
	
	System.out.println("File Cant be processed as it has new extra character Please check Character Set!");
	logging("File Cant be processed as it has new extra character Please check Character Set!","severe");
	logging("Program Execution Ended","info");
	
}
}
else
{
	System.out.println("Input CSV Header is invalid !!!");
	logging("Input CSV Header is invalid !!!","info");
	logging("Program Execution Ended","info");
}

} catch (Exception ex) {
ex.printStackTrace();
logging(ex.getMessage(),"severe");
} finally {
try {
reader.close();
} catch (Exception e) {
e.printStackTrace();
logging(e.toString(),"severe");
}
}

}



public static void readCsvSet(String filePath) {
BufferedReader reader = null;

try {
List<InputSet> setlist = new ArrayList();
String line = "";
reader = new BufferedReader(new FileReader(filePath));
//Reading the Header for validating the file

String[] header = reader.readLine().split("\\|");

if(header[0].equals("Input") && header[1].equals("Replacement"))
{
while((line = reader.readLine()) != null) {
String[] fields = line.split("\\|");





if(fields.length > 0) {
	
if (fields[1].equals("remove"))
{
InputSet set = new InputSet();
set.setInputchar(fields[0].charAt(0));
set.setRepchar("");
setlist.add(set);
}
else
if (fields[1].equals("space"))
{
InputSet set = new InputSet();
set.setInputchar(fields[0].charAt(0));
set.setRepchar(" ");
setlist.add(set);
}
/*
else if(fields[1].matches("[0-9.]*"))
{
	InputSet numset = new InputSet();
	set.setInputchar(fields[0].charAt(0));
	set.setRepchar('\u0020');
	setlist.add(set);
}
*/
else
{
InputSet set = new InputSet();
set.setInputchar(fields[0].charAt(0));
set.setRepchar(fields[1]);
setlist.add(set);
}

}
}

for(InputSet s: setlist) {
//System.out.printf(s.getInputchar()+" "+s.getInputcharesc()+" "+s.getRepchar()+" "+s.getRepcharesc());
characterMappings.put(s.getInputchar(),s.getRepchar());
}

System.out.println("Total Mappings:"+setlist.size());

if(setlist.size()>0)
{
	File file = new File(inputfilePath);
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	Date date = new Date();
	String filedate=sdf.format(file.lastModified());
	String todaysdate=(String)(sdf.format(date));
	if(filedate.equals(todaysdate))
	{
		System.out.println("starting read input CSV file");
		logging("starting read input CSV file","info");
		readCsv(inputfilePath);
	}
	else
	{
		System.out.println("Input CSV file is not updated!!!");
		logging("Input CSV file is not updated!!!","severe");
	}
	

	

}
else
{
	System.out.println("CSV Set is Empty!!");
	logging("CSV Set is Empty!!","info");
	logging("Program Execution Ended","info");
}

}
else
{
	
	System.out.println("CSV Set Header is invalid !!!");
	logging("CSV Set Header is invalid !!!","info");
	logging("Program Execution Ended","info");
}

} catch (Exception ex) {
ex.printStackTrace();
logging(ex.getMessage(),"severe");
} finally {
try {
reader.close();
} catch (Exception e) {
e.printStackTrace();
logging(e.toString(),"severe");
}
}

}











public static String replace(String token,int col)
{

String newString = "";


for(int i = 0 ; i < token.length() ; ++i){
	System.out.println("ascii valoe"+token.charAt(i)+" "+" ====="+(int)token.charAt(i) );
	
if( characterMappings.containsKey(token.charAt(i)) )
{
	
newString += characterMappings.get(token.charAt(i));
System.out.println(newString);
System.out.println("For Line :"+k+" "+"Position:"+(i+1)+" "+"Column:"+(col)+token.charAt(i)+"->"+characterMappings.get(token.charAt(i)));

}

else if((int)(token.charAt(i)) >255 || (int)(token.charAt(i)) ==215 || (int)(token.charAt(i)) ==247  || (token.charAt(i) >=127 && token.charAt(i) <=191)) 
{
	
System.out.println("New Character is introduced "+token.charAt(i)+" at Line "+k+"positon :"+(i+1)+"Please add it into the Set");
}

else if((token.charAt(i) >=32 && token.charAt(i) <=126)||(token.charAt(i) >=192 && token.charAt(i) <=255) )
{
	newString += token.charAt(i);
}


}

return newString;

}




public static boolean check(String token,int col)
{
	

String newString = "";


for(int i = 0 ; i < token.length() ; ++i){
	
	
if( characterMappings.containsKey(token.charAt(i)) )
{
	
	
}

else if((int)(token.charAt(i)) >255 || (int)(token.charAt(i)) ==215 || (int)(token.charAt(i)) ==247  || (token.charAt(i) >=127 && token.charAt(i) <=191)) 
{
	
System.out.println("New Character is introduced "+token.charAt(i)+" at Line "+k+"positon :"+(i+1)+" Column no:"+col+"Please add it into the Set");

return false;

}

else if((token.charAt(i) >=32 && token.charAt(i) <=126)||(token.charAt(i) >=192 && token.charAt(i) <=255) )
{
	
}


}

return true;

}




public static void logging(String message,String severity)
{
Logger logger = Logger.getLogger("MyLog");
FileHandler fh;

try {

// This block configure the logger with handler and formatter
fh = new FileHandler(logfilepath,true);
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
fh.close();

} catch (SecurityException e) {
e.printStackTrace();

logging(e.getMessage(),"severe");
} catch (IOException e) {
e.printStackTrace();
logging(e.getMessage(),"severe");
}
}

}






