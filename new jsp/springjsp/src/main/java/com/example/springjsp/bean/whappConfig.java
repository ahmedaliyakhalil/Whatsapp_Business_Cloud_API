package com.example.springjsp.bean;

public class whappConfig {
	private String AuthUrlToSendTemplateMessage="https://graph.facebook.com/v13.0/100949046057426/messages";
	private String strToken="EAAGCcmZCCXDgBAEvcT0ZCu3TYIsCx7s9Hx6sWxyDzYQCSgHMQZACIuc0kRjXNPhaLUKH1EeUXsyiUONJJITV1llzUuZBEZCBjdc4ZCxQVfZCCDOPFtfO1h8PsTpXWqhP43Un1mSBXb3N4gZABMbPAfxY9BOcNJ5WqamKcnh7E8TUaTR3LOtKEbdoU5SJcE7pviOux8DmJ3Ba0QZDZDs";
	
	private String AuthUrlUploadMedia="https://graph.facebook.com/v13.0/100949046057426/media";
	
	public String getAuthUrlToSendTemplateMessage() {
		return AuthUrlToSendTemplateMessage;
	}
	public void setAuthUrlToSendTemplateMessage(String authUrlToSendTemplateMessage) {
		AuthUrlToSendTemplateMessage = authUrlToSendTemplateMessage;
	}
	public String getStrToken() {
		return strToken;
	}
	public void setStrToken(String strToken) {
		this.strToken = strToken;
	}
	public String getAuthUrlUploadMedia() {
		return AuthUrlUploadMedia;
	}
	public void setAuthUrlUploadMedia(String authUrlUploadMedia) {
		AuthUrlUploadMedia = authUrlUploadMedia;
	}
	
	
}
