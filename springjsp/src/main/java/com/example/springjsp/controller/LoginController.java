package com.example.springjsp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.springjsp.bean.User;
import com.example.springjsp.bean.whappConfig;
import com.fasterxml.jackson.databind.util.JSONPObject;
//import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
//import org.apache.commons.codec.binary.Base64;


@Controller
public class LoginController {
	whappConfig objConfig=new whappConfig();
	class DocMessage {
        String number = null;
        String document = null;
        String filename = null;
        String messaging_product = null;
    }
	//---------------------- Final
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String gome() {
      return "/home";
	}
	
	/////////////////////////////////
	//SENDING WHATSAPP PREDEFINED MESSAGE TEMPLATE
	
	@RequestMapping(value = "/sendWhatsappMsg", method = RequestMethod.GET)
	public String getSendWhatsappMsgPage(Model model) {
       model.addAttribute("user", new User());
		return "/sendWhatsappMsg";
	}
	
	@RequestMapping(value = "/sendWhatsappMsgProcess", method = RequestMethod.POST)
	public String sendWhatsappMsgProcess(@ModelAttribute("user") User user, HttpServletRequest request) {
		ModelAndView mv=new ModelAndView();
		System.out.println(user.getMessage());
		System.out.println(user.getNumber());
		request.setAttribute("responseData", sendWhatsAppMsgViaFacebook(user.getNumber(),user.getMessage(),user.getCountryCode(),"TEMPLATE"));
       return "/sendWhatsappMsg";
	}
	
	//UPLOADING AND SENDING ATTACHEMNT
	
	@RequestMapping(value = "/upSendAttach", method = RequestMethod.GET)
	public String upSendAttach(Model model) {
       model.addAttribute("user", new User());
		return "/upSendAttach";
	}
	
	/**
	 * select file in html form, upload it to whatsapp server and get uploaded document id.
	 * This id can be send to recipient in a template. Recipient will get the document as attachment referring to this id on whatsapp server
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/upSendAttach", method = RequestMethod.POST)
	public @ResponseBody String processMsg(@RequestParam("file") MultipartFile file,@ModelAttribute("user") User user) throws IOException, Exception {
		System.out.println("Inside processMsg");
		try {
			String AuthUrlUploadMedia= objConfig.getAuthUrlUploadMedia();     //"https://graph.facebook.com/v13.0/105495115543525/media";
			MultipartUtility multipart = new MultipartUtility(AuthUrlUploadMedia, "UTF-8", objConfig.getStrToken());
			multipart.addFormField("messaging_product", "whatsapp");
			
			//Convert spring multipart to java io file
			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
		    file.transferTo(convFile);
			//////////////			
			multipart.addFilePart("file", convFile);
			
			JSONObject obj=null;
			List<String> response = multipart.finish();
             for (String line : response) {
                System.out.println("Response after upload "+line);
                obj=new JSONObject(line);
                
            }
             System.out.println(obj.get("id"));
           //Get docuemnt id via above code
 			sendAttachByIdViaMetaWhatsapp(obj.get("id").toString(),user.getCountryCode()+user.getNumber());
		}catch(Exception e) {e.printStackTrace();}	
		return "/upSendAttach";
	}
	///////////////////////////////////////////////
	
	//SENDING CUSTOMIZED WHATSAPP MESSAGE
	
	
	@RequestMapping(value = "/sendWhatsappMsgCust", method = RequestMethod.GET)
	public String getSendWhatsappMsgPageCust(Model model) {
       model.addAttribute("user", new User());
		return "/sendWhatsappMsgCust";
	}
	
	@RequestMapping(value = "/sendWhatsappMsgCustProcess", method = RequestMethod.POST)
	public String sendWhatsappMsgCustProcess(@ModelAttribute("user") User user, HttpServletRequest request) {
		ModelAndView mv=new ModelAndView();
		System.out.println(user.getMessage());
		System.out.println(user.getNumber());
		request.setAttribute("responseData", sendWhatsAppMsgViaFacebook(user.getNumber(),user.getMessage(),user.getCountryCode(),"CUSTOM"));
       return "/sendWhatsappMsgCust";
	}
	
	//////////////////////////////
	
	//SENDING INTERACTIVE WHATSAPP MESSAGE 
	
	
	@RequestMapping(value = "/sendInteractiveWhatsappMsg", method = RequestMethod.GET)
	public String sendInteractiveWhatsappMsg(Model model) {
       model.addAttribute("user", new User());
		return "/sendInteractiveWhatsappMsg";
	}
	
	@RequestMapping(value = "/sendInteractiveWhatsappMsgProcess", method = RequestMethod.POST)
	public String sendInteractiveWhatsappMsg(@ModelAttribute("user") User user, HttpServletRequest request) {
		ModelAndView mv=new ModelAndView();
		System.out.println(user.getMessage());
		System.out.println(user.getNumber());
		request.setAttribute("responseData", sendInteractiveWhatsAppMsgViaFacebook(user.getNumber(),user.getMessage(),user.getCountryCode()));
       return "/sendInteractiveWhatsappMsg";
	}
	
	////////////////////////////////////////////
	/*//SENDING LOCATION MESSAGE

	@RequestMapping(value = "/sendLocationWhatsappMsg", method = RequestMethod.GET)
	public String getsendLocationWhatsappMsg(Model model) {
       model.addAttribute("user", new User());
		return "/sendLocationWhatsappMsg";
	}
	
	@RequestMapping(value = "/sendLocationWhatsappMsgProcess", method = RequestMethod.POST)
	public String sendLocationWhatsappMsgProcess(@ModelAttribute("user") User user, HttpServletRequest request) {
		ModelAndView mv=new ModelAndView();
		System.out.println(user.getMessage());
		System.out.println(user.getNumber());
		request.setAttribute("responseData", sendWhatsAppMsgViaFacebook(user.getNumber(),user.getMessage(),user.getCountryCode(),"CUSTOM"));
       return "/sendLocationWhatsappMsgProcess";
	}*/
	////////////////////////////////////////////////////
	
	
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String loginPage(User user) {
		//  sendWhatsAppMsgViaFacebook();		
		return "/addUser";
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String loginPagePost(User user) {
		//sendWhatsAppMsgViaFacebook();		
		return "/addUser";
	}
	
	
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public String uploadFile(User user) {
		return "/uploadFile";
	}

	/**
	 * select file in html form, upload it to whatsapp server and get uploaded document id.
	 * This id can be send to recipient in a template. Recipient will get the document as attachment referring to this id on whatsapp server
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String processMsg(@RequestParam("file") MultipartFile file) throws IOException, Exception {
		System.out.println("Inside processMsg");
		try {
			String AuthUrl="https://graph.facebook.com/v13.0/105495115543525/media";
			MultipartUtility multipart = new MultipartUtility(AuthUrl, "UTF-8");
			multipart.addFormField("messaging_product", "whatsapp");
			
			//Convert spring multipart to java io file
			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
		    file.transferTo(convFile);
			//////////////			
			multipart.addFilePart("file", convFile);
			
			JSONObject obj=null;
			List<String> response = multipart.finish();
             for (String line : response) {
                System.out.println("Response after upload "+line);
                obj=new JSONObject(line);
                
            }
             System.out.println(obj.get("id"));
           //Get docuemnt id via above code
 			sendAttachByIdViaMetaWhatsapp(obj.get("id").toString());
		}catch(Exception e) {e.printStackTrace();}
	
		
		
		
		return "/uploadFile";
	}*/
	
		
	/*public void sendMessageHttp(MultipartFile file) throws IOException {
		URL url = new URL("https://graph.facebook.com/v13.0/105495115543525/media");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String strToken="EAAF20R6hNfYBACq20Hgi4ItA91E1mdRvnRylEDxwusZAOUgEwOvec9FD0CVwxn4ZALaZBUtC5o5kMZCcQixkR7UoK0WWCL4HgnDNpE0pSKmYdVz9ZBSIyfk7gtJxvs8ZC5fYFVHAE4sk27hqwZCogs50PS31dKDcjZCBZCOG2Yqk2w8319Phlhnk2cqclUukUWNmpx972vRvkHgZDZD";

		String auth = "Bearer " + strToken;
		connection.setRequestProperty("Authorization", strToken);

		String boundary = UUID.randomUUID().toString();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

		DataOutputStream request = new DataOutputStream(connection.getOutputStream());

		request.writeBytes("--" + boundary + "\r\n");
		request.writeBytes("Content-Disposition: form-data; name=\"messaging_product\"\r\n\r\n");
		request.writeBytes("whatsapp" + "\r\n");

		request.writeBytes("--" + boundary + "\r\n");
		request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n\r\n");
		request.write(file.getBytes());
		request.writeBytes("\r\n");

		request.writeBytes("--" + boundary + "--\r\n");
		request.flush();
		int respCode = connection.getResponseCode();
		System.out.println("respCode "+respCode);
	}
	*/
	
	////////////////////////////////////////////////
	//SENDING WHATSAPP MESSAGE USING VONAGE API

	
	public void sendWhatsAppMsgViaVonage() {
		System.out.println("Inside sendWhatsAppMsg");
		try {
			String AuthUrl="https://messages-sandbox.nexmo.com/v1/messages";
			System.out.println("AuthUrl    "+AuthUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(AuthUrl).openConnection();
		    String strToken="MGYwMDU1MWU6eHhTZDFVME94WGFjenB0ZQ==";
		    System.out.println("strToken"+strToken);
		     con.setRequestMethod("POST"); //can set GET, POST, HEAD
		     con.setRequestProperty("Authorization", "Basic "+strToken);
		     con.setRequestProperty("Content-Type", "application/json; utf-8");
		     con.setRequestProperty("Accept", "application/json");
		     con.setRequestProperty("Content-Language", "en-US");
		     con.setUseCaches(false);
		     con.setDoOutput(true);
		     
		     JSONObject obj=new  JSONObject();
		     obj.put("from", "14157386102");
		     obj.put("to", "919987870583");
		     obj.put("message_type", "text");
		     obj.put("text", "This is a WhatsApp Message sent from the Messages");
		     obj.put("channel", "whatsapp");
		    
		   //Calling API
		      String finalJSONData=obj.toString();
		      try(OutputStream os = con.getOutputStream()) {
		             byte[] input = finalJSONData.getBytes("utf-8");
		             os.write(input, 0, input.length);
		         }catch(Exception e) { e.printStackTrace();}
		         StringBuffer responseData = new StringBuffer();
		         try(BufferedReader br = new BufferedReader(
		           new InputStreamReader(con.getInputStream(), "utf-8"))) {
		             String responseLine = null;
		             while ((responseLine = br.readLine()) != null) {
		              responseData.append(responseLine.trim());
		             }
		         }
		         System.out.println(responseData.toString());
		         
		}catch(Exception e) {e.printStackTrace();}
	}
	/**
	 * Send attachment to recipient after uploading the document on whatsapp, then get id and then send that id as an attachment
	 */
	public void sendAttachByIdViaMetaWhatsapp(String docId,String contact) {
		System.out.println("Inside sendWhatsAppMsg");
		try {
			String AuthUrl= objConfig.getAuthUrlToSendTemplateMessage();     //"https://graph.facebook.com/v13.0/105495115543525/messages";
			System.out.println("AuthUrl    "+AuthUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(AuthUrl).openConnection();
		    
		    String strToken=objConfig.getStrToken();      //"EAAF20R6hNfYBACq20Hgi4ItA91E1mdRvnRylEDxwusZAOUgEwOvec9FD0CVwxn4ZALaZBUtC5o5kMZCcQixkR7UoK0WWCL4HgnDNpE0pSKmYdVz9ZBSIyfk7gtJxvs8ZC5fYFVHAE4sk27hqwZCogs50PS31dKDcjZCBZCOG2Yqk2w8319Phlhnk2cqclUukUWNmpx972vRvkHgZDZD";
		    System.out.println("strToken"+strToken);
		     con.setRequestMethod("POST"); //can set GET, POST, HEAD
		     con.setRequestProperty("Authorization",  "Bearer "+strToken);
		     con.setRequestProperty("Content-Type", "application/json; utf-8");
		     con.setRequestProperty("Accept", "application/json");
		     con.setRequestProperty("Content-Language", "en-US");
		     con.setUseCaches(false);
		     con.setDoOutput(true);
		    
			
		     JSONObject obj=new  JSONObject();
		     obj.put("messaging_product", "whatsapp");
		     obj.put("to", contact);
		     obj.put("type", "document");
		     JSONObject obj2=new  JSONObject();
		     //obj2.put("id", "3412539642347204");
		     obj2.put("id",docId);
		     //JSONObject obj3=new  JSONObject();
		     //obj3.put("code", "en_US");
		     obj2.put("caption","Hi Aliya .This is a test pdf file send from JAVA code");
		     obj2.put("filename","sample.pdf");
		     obj.put("document",obj2);
		    
		     
		   //Calling API
		      String finalJSONData=obj.toString();
		      System.out.println(finalJSONData);
		      
		      try(OutputStream os = con.getOutputStream()) {
		             byte[] input = finalJSONData.getBytes("utf-8");
		             os.write(input, 0, input.length);
		         }catch(Exception e) { e.printStackTrace();}
		         StringBuffer responseData = new StringBuffer();
		         try(BufferedReader br = new BufferedReader(
		           new InputStreamReader(con.getInputStream(), "utf-8"))) {
		             String responseLine = null;
		             while ((responseLine = br.readLine()) != null) {
		              responseData.append(responseLine.trim());
		             }
		         }
		         System.out.println(responseData.toString());
		     
		    
		    
		  // System.out.println(obj.toString());
		         
		}catch(Exception e) {e.printStackTrace();}
	}
	/**
	 * Sending msg for a pre approved templates
	 */
	public String sendWhatsAppMsgViaFacebook(String contact,String message,String countryCode,String msgType) {
		System.out.println("Inside sendWhatsAppMsg");
		try {
			String AuthUrl=objConfig.getAuthUrlToSendTemplateMessage();       //"https://graph.facebook.com/v13.0/105495115543525/messages";
			System.out.println("AuthUrl    "+AuthUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(AuthUrl).openConnection();
		    String strToken=objConfig.getStrToken();
		    		//"EAAF20R6hNfYBACoZBdLMl3dLZC09GNQubgUEUkR08ZCkUGCXVvbJI3KBjr9XK8lO05UOQtARpu9LKUSbkpKZAjtUvZAw8jsSLwzEniNkdZAFlOL8eNumZCBN1mvX0WubH04XnbyMCmMIO2PLXrJZAhJZA428OI4EoZBxSDqxvkOrBx8XockzrcrdZC7ZBJwoYngbsQQMay70ZBmwOYgZDZD";
		    System.out.println("strToken"+strToken);
		     con.setRequestMethod("POST"); //can set GET, POST, HEAD
		     con.setRequestProperty("Authorization",  "Bearer "+strToken);
		     con.setRequestProperty("Content-Type", "application/json; utf-8");
		     con.setRequestProperty("Accept", "application/json");
		     con.setRequestProperty("Content-Language", "en-US");
		     con.setUseCaches(false);
		     con.setDoOutput(true);
		    
			
		     JSONObject obj=new  JSONObject();
		     if(msgType.equals("TEMPLATE")) {
		    	 obj.put("messaging_product", "whatsapp");
				    // obj.put("to", "919987870583");
				     obj.put("to", countryCode+contact);
				     obj.put("type", "template");
				     JSONObject obj2=new  JSONObject();
				     obj2.put("name", message);
				     JSONObject obj3=new  JSONObject();
				     obj3.put("code", "en_US");
				     obj2.put("language",obj3);
				     obj.put("template",obj2);				    	 
		     }else if(msgType.equals("CUSTOM")){
		    	     obj.put("messaging_product", "whatsapp");
				     obj.put("to", countryCode+contact);
				     //obj.put("type", "template");
				     JSONObject obj2=new  JSONObject();
				     obj2.put("body", message);
				     //JSONObject obj3=new  JSONObject();
				     //obj3.put("code", "en_US");
				     //obj2.put("language",obj3);
				     obj.put("text",obj2);	
		     }
		     
		     
		     System.out.println(obj);
		   //Calling API
		      String finalJSONData=obj.toString();
		      try(OutputStream os = con.getOutputStream()) {
		             byte[] input = finalJSONData.getBytes("utf-8");
		             os.write(input, 0, input.length);
		         }catch(Exception e) { e.printStackTrace();}
		         StringBuffer responseData = new StringBuffer();
		         try(BufferedReader br = new BufferedReader(
		           new InputStreamReader(con.getInputStream(), "utf-8"))) {
		             String responseLine = null;
		             while ((responseLine = br.readLine()) != null) {
		              responseData.append(responseLine.trim());
		             }
		         }
		         System.out.println(responseData.toString());
		         return responseData.toString();	
		    
		    
		  // System.out.println(obj.toString());
		         
		}catch(Exception e) {e.printStackTrace();}
		return "";
		
	}
	
	
	public String sendInteractiveWhatsAppMsgViaFacebook(String contact,String message,String countryCode) {
		System.out.println("Inside sendWhatsAppMsg");
		try {
			String AuthUrl=objConfig.getAuthUrlToSendTemplateMessage();       //"https://graph.facebook.com/v13.0/105495115543525/messages";
			System.out.println("AuthUrl    "+AuthUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(AuthUrl).openConnection();
		    String strToken=objConfig.getStrToken();
		    		//"EAAF20R6hNfYBACoZBdLMl3dLZC09GNQubgUEUkR08ZCkUGCXVvbJI3KBjr9XK8lO05UOQtARpu9LKUSbkpKZAjtUvZAw8jsSLwzEniNkdZAFlOL8eNumZCBN1mvX0WubH04XnbyMCmMIO2PLXrJZAhJZA428OI4EoZBxSDqxvkOrBx8XockzrcrdZC7ZBJwoYngbsQQMay70ZBmwOYgZDZD";
		    System.out.println("strToken"+strToken);
		     con.setRequestMethod("POST"); //can set GET, POST, HEAD
		     con.setRequestProperty("Authorization",  "Bearer "+strToken);
		     con.setRequestProperty("Content-Type", "application/json; utf-8");
		     con.setRequestProperty("Accept", "application/json");
		     con.setRequestProperty("Content-Language", "en-US");
		     con.setUseCaches(false);
		     con.setDoOutput(true);
		    
		     String str="{\r\n"
			     		+ "  \"recipient_type\": \"individual\",\r\n"
			     		+ "  \"messaging_product\":\"whatsapp\",\r\n"
			     		+ "  \"to\" :\" "+countryCode+contact+" \",\r\n"
			     		+ "  \"type\": \"interactive\" ,\r\n"
			     		+ "\"interactive\":{\r\n"
			     		+ "    \"type\": \"list\",\r\n"
			     		+ "    \"header\": {\r\n"
			     		+ "      \"type\": \"text\",\r\n"
			     		+ "      \"text\": \"AIR India\"\r\n"
			     		+ "    },\r\n"
			     		+ "    \"body\": {\r\n"
			     		+ "      \"text\": \"select your nearest airport\"\r\n"
			     		+ "    },\r\n"
			     		+ "    \"footer\": {\r\n"
			     		+ "      \"text\": \"Delhi Headquarter\"\r\n"
			     		+ "    },\r\n"
			     		+ "    \"action\": {\r\n"
			     		+ "      \"button\": \"Click to select\",\r\n"
			     		+ "      \"sections\":[\r\n"
			     		+ "        {\r\n"
			     		+ "          \"title\":\"Delhi\",\r\n"
			     		+ "          \"rows\": [\r\n"
			     		+ "            {\r\n"
			     		+ "              \"id\":\"IGIT\",\r\n"
			     		+ "              \"title\": \"Terminal 1\",\r\n"
			     		+ "              \"description\": \"Indira Gandhi International Airport\"           \r\n"
			     		+ "            }\r\n"
			     		+ "          ]\r\n"
			     		+ "        },\r\n"
			     		+ "        {\r\n"
			     		+ "          \"title\":\"PATNA\",\r\n"
			     		+ "          \"rows\": [\r\n"
			     		+ "            {\r\n"
			     		+ "              \"id\":\"Terminal 3\",\r\n"
			     		+ "              \"title\": \"LJPN\",\r\n"
			     		+ "              \"description\": \"Lok Nayak Jai Prakash AIR Port\"           \r\n"
			     		+ "            }\r\n"
			     		+ "          ]\r\n"
			     		+ "        }\r\n"
			     		+ "      ]\r\n"
			     		+ "    }\r\n"
			     		+ "  }\r\n"
			     		+ "}";
		     JSONObject obj=new  JSONObject(str);
		     /*obj.put("messaging_product", "whatsapp");
				     obj.put("to", countryCode+contact);
				     obj.put("type", "interactive");
				     
				     JSONObject obj2=new  JSONObject();
				     obj2.put("type", "list");
				     
				     JSONObject obj3=new  JSONObject();
				     obj3.put("type", "text");
				     obj3.put("text", "AIR India");
				     obj2.put("header",obj3);
				     
				     JSONObject obj4=new  JSONObject();
				     obj4.put("text", "select your nearest airport");
				     obj2.put("body",obj4);
				     
				     JSONObject obj5=new  JSONObject();
				     obj5.put("text", "Delhi Headquarter");
				     obj2.put("footer",obj5);
				     
				     
				     
				     obj.put("template",obj2);*/
		     
				     
				     
				    
		     
		     obj.put("", str);
		     System.out.println(obj);
		   //Calling API
		      String finalJSONData=obj.toString();
		      try(OutputStream os = con.getOutputStream()) {
		             byte[] input = finalJSONData.getBytes("utf-8");
		             os.write(input, 0, input.length);
		         }catch(Exception e) { e.printStackTrace();}
		         StringBuffer responseData = new StringBuffer();
		         try(BufferedReader br = new BufferedReader(
		           new InputStreamReader(con.getInputStream(), "utf-8"))) {
		             String responseLine = null;
		             while ((responseLine = br.readLine()) != null) {
		              responseData.append(responseLine.trim());
		             }
		         }
		         System.out.println(responseData.toString());
		         return responseData.toString();	
		    
		    
		  // System.out.println(obj.toString());
		         
		}catch(Exception e) {e.printStackTrace();}
		return "";
		
	}
	/**
	 * This method  is being used to upload the document 
	 */
	public void uploadDcumentAngGetDocIdOnWhatsAppMsgViaFacebook() {
		System.out.println("Inside sendWhatsAppMsg");
		try {
			String AuthUrl="https://graph.facebook.com/v13.0/105495115543525/media";
			System.out.println("AuthUrl    "+AuthUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(AuthUrl).openConnection();
		    
		    String strToken="EAAF20R6hNfYBAF88M6I3ZB9xFHzJwO8gDd296VCvn4kr2IKOniFhRHJU2daGJGuqmJ5ZCUFMFhxCPgYyo7ySAtNvqNYvAZB6idxJm2gwNw2cZCRyHAivbUq9kpdwxXgZCpwlZBdh4kVsOFpFTeoTorKLTZC0kNE0spqIPVvRE0Q35KfZCdEcdjZBG2TCnZAagTiehJR9Pq9RxfTwZDZD";
		    System.out.println("strToken"+strToken);
		     con.setRequestMethod("POST"); //can set GET, POST, HEAD
		     con.setRequestProperty("Authorization",  "Bearer "+strToken);
		     con.setRequestProperty("Content-Type", "application/json; utf-8");
		     con.setRequestProperty("Accept", "application/json");
		     con.setRequestProperty("Content-Language", "en-US");
		     con.setUseCaches(false);
		     con.setDoOutput(true);
		    
			
		     JSONObject obj=new  JSONObject();
		     obj.put("messaging_product", "whatsapp");
		     obj.put("file", "");
		     obj.put("type", "template");
		     
		   //Calling API
		      String finalJSONData=obj.toString();
		      try(OutputStream os = con.getOutputStream()) {
		             byte[] input = finalJSONData.getBytes("utf-8");
		             os.write(input, 0, input.length);
		         }catch(Exception e) { e.printStackTrace();}
		         StringBuffer responseData = new StringBuffer();
		         try(BufferedReader br = new BufferedReader(
		           new InputStreamReader(con.getInputStream(), "utf-8"))) {
		             String responseLine = null;
		             while ((responseLine = br.readLine()) != null) {
		              responseData.append(responseLine.trim());
		             }
		         }
		         System.out.println(responseData.toString());
		     
		    
		    
		  // System.out.println(obj.toString());
		         
		}catch(Exception e) {e.printStackTrace();}
	}
}

class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset,String strToken)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        //String strToken="EAAF20R6hNfYBACq20Hgi4ItA91E1mdRvnRylEDxwusZAOUgEwOvec9FD0CVwxn4ZALaZBUtC5o5kMZCcQixkR7UoK0WWCL4HgnDNpE0pSKmYdVz9ZBSIyfk7gtJxvs8ZC5fYFVHAE4sk27hqwZCogs50PS31dKDcjZCBZCOG2Yqk2w8319Phlhnk2cqclUukUWNmpx972vRvkHgZDZD";
	    System.out.println("strToken"+strToken);
	    httpConn.setRequestMethod("POST"); //can set GET, POST, HEAD
	    httpConn.setRequestProperty("Authorization", "Bearer "+strToken);
	    
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);    // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }
}
