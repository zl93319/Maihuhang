package util;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ToolMethod {

	 //获取request中的inputstream,转byte[]
	 public static final byte[] input2byte(InputStream inStream)   {   
			byte[] in2b = null;
			
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
			
			try{  
				byte[] buff = new byte[1024];  
				int rc = 0;  
				while ((rc = inStream.read(buff, 0, 100)) > 0) {  
					swapStream.write(buff, 0, rc);   
				}   
			}catch(Exception e) { 
			
			}
			
			in2b = swapStream.toByteArray();  
			return in2b;  
	 }
	 


	  
	  //请求脉象算法结果数据
	  public static void contentHttp(String url, String deviceid, Integer age
			  , int sex, Double sugar, Integer high, Integer low){
		  HttpRequest_v1.sendGet(url, "deviceid="+deviceid+"&age="+age+"&sex="
				  +sex+"&sugar="+sugar+"&high="+high+"&low="+low);
		  System.out.println("[INFO]:脉象数据提交！");	
	  }
	  
	  //返回所占百分比
	  public static int getPercentage(int maiNumber){
		  switch(maiNumber){
		  	  case 0:
		  		maiNumber=5;
			  break;
			  
		  	case 1:
		  		maiNumber=25;
			  break;
			  
		  	case 2:
		  		maiNumber=50;
			  break;
			  
		  	case 3:
		  		maiNumber=75;
			  break;
			  
		  	case 4:
		  		maiNumber=100;
			  break;
			  
			default:
				maiNumber=-1;
				break;
		  }
		  return maiNumber;
	  }
	  
	  //返回脉象综合分数
	  public static int getSum(String maiSum){
		  if(maiSum.contains("4")){
			  return 4;
		  }if(maiSum.contains("3")){
			  return 3;
		  }else if(maiSum.contains("2")){
			  return 2;
		  }else if(maiSum.contains("1")){
			  return 1;
		  }else{
			  return 0;
		  }
	  }
	  
	//返回脉象综合分数
	  public static String getFileType(int temp){
		  String ECg="";
		  if(temp==1){
			  ECg= "血压报告";
		  }if(temp==2){
			  ECg= "血糖报告";
		  }else if(temp==3){
			  ECg= "舌象报告";
		  }else if(temp==4){
			  ECg= "面向报告";
		  }
		  return ECg;
	  }
}
