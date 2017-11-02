package servies;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class KivenFileMethod {
	  // get IdNumber birthday,age, 
	  public static String getUserInfo(String inNumber){
		  //cut the number get oldYear
		  String oldYear = inNumber.substring(6,10);
		  //cut idnumber get birthday
		  String birthDay = inNumber.substring(10,14);
		  //create time object
		  Date date = new Date();
		  //create simpleTimeString format
		  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  //change dateTime to timeString 
		  String format = simpleDateFormat.format(date);
		  //get new time and cut year
		  String newYear = format.substring(0, 4);
		  int oldY = Integer.parseInt(oldYear);
		  int newY = Integer.parseInt(newYear);
		  // java GC wait...
		  date=null;
		  simpleDateFormat=null;
		  //return oldyear ,birthday moth,birthday,and userAge
		  return oldYear+"-"+birthDay.substring(0, 2)+"-"+birthDay.substring(2)+"-"+(newY-oldY);
	  }

   //phone number matcher
   public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
       //phone model  
	   String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
       //begin matchers your phone number
	   Pattern p = Pattern.compile(regExp);
       //return boolean result 'true' or 'false'
	   Matcher m = p.matcher(str);
	   //last return the result
        return m.matches();  
    }  
  
   //honkong phone 
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();  
    } 

	//mkdirs file catalog
	public static String generateRandomDir(String initialPath, String fileName) {
		//get hoshcode
		int hashCode = fileName.hashCode();
		//get hoshcode number
		int dir1 = hashCode&0xf;   
		//get hoshcode number
		int dir2 = (hashCode>>4)&0xf;   
		//install file path
		initialPath = initialPath+"/"+dir1+"/"+dir2;
		//create file object  and input path
		File file = new File(initialPath);
		//if file is don't exist,can create it is
		if(!file.exists()){
			file.mkdirs();
		}
		hashCode=0;
		dir1=0;
		dir2=0;
		//return file path
		return initialPath;
	}
	
	//build random string and return 
	public static String generateUUIDName() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	
	//change string code 'ISO8859-1' to 'utf-8'
	public static String getUtf8(String codeName) throws Exception {
		return new String(codeName.getBytes("ISO8859-1"),"UTF-8");
	}
	
	 //matchers args and change
	  public static boolean returnEmpty(Object... vaule){
		  boolean b=true;
		  //loop args,if args is empty return false 
		  for (Object object : vaule) {
			if(object==null|| "".equals(object)){
				b= false;
				break;
			}else{
				continue;
			}
		}
		  return b;
	  }

		//get now time
		public static String getNowTime(){
			//create time object
			Date date = new Date();
			//create simpleTimeString format
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//change the Date
			String format = simpleDateFormat.format(date);
			date=null;
			simpleDateFormat=null;
			return format;
		}
	//kiven encryption
	public static String kivenEncryption(String password, String condition){
		char[] charArray = password.toCharArray();
		StringBuffer buffer=new StringBuffer();
		for (char c : charArray) {
			int temp=((int)c)+condition.hashCode();
			buffer.append((temp<<1)+"-");
			temp=0;
			
		}
		charArray=null;
		return buffer.toString().substring(0,buffer.length()-1);
	}
	
	//kiven deciphering
	public static String kivenDeciphering(String password, String condition){
		String[] split = password.split("-");
		StringBuffer buffer=new StringBuffer();
		for (String cut : split) {
			int parseInt = Integer.parseInt(cut);
			int hashCode = condition.hashCode();
			char codeOne=(char)((parseInt>>1)-hashCode);
			buffer.append(codeOne);
			parseInt=0;
			hashCode=0;
			codeOne=0;
		}
		split=null;
		 return buffer.toString();
	}
	
	public static int matcersPassword(String password1, String passwaord2){
		if(!passwaord2.equals(password1)){
			return 0;
		}else{
			return 1;
		}
	} 
			
		public static String myRandom(String RandomType, int length){
			//myRandoms you can use selfe thinks get Random 
			String random="";
			if("number".equals(RandomType)){
				String[]num={"0","1","2","3","4","5","6","7","8","9"};
				for (int i = 0; i < length; i++) {
					random+=num[(int)(Math.random()*num.length)];
				}
				
			}else if("both".equals(RandomType)){
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < length; i++) {
					char a=(char)(Math.random()*25+65);
					char b=(char)(Math.random()*25+97);
					int c=(int)(Math.random()*9);
					stringBuffer.append(a);
					stringBuffer.append(b);
					stringBuffer.append(c);
				}
				 random = stringBuffer.substring(0, length);
			}else if("char".equals(RandomType)){
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < length; i++) {
					char a=(char)(Math.random()*25+65);
					char b=(char)(Math.random()*25+97);
					stringBuffer.append(a);
					stringBuffer.append(b);
				}
				random=stringBuffer.substring(0, length);
			}
			return random;
		}
			
		public static String getBlueTooth(String strName){
			//SH09U-E4:75:A4:03:07:14
			String buffer="";
			String[] split = strName.split(":");
			for(int i=split.length-1;i>=2;i--){
				buffer =buffer+split[i];
			}
			return split[0]+":"+buffer;
		}
}
