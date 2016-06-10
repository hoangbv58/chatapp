package com.baidar.androidChatter.toolBox;

import com.baidar.androidChatter.typo.InfoOfMessage;

/*
 * This class can store friendInfo and check userkey and username combination 
 * according to its stored data
 */
public class MessageController 
{
	
	private static InfoOfMessage[] messagesInfo = null;
	
	public static void setMessagesInfo(InfoOfMessage[] messageInfo)
	{
		MessageController.messagesInfo = messageInfo;
	}
	
	
	
	public static InfoOfMessage checkMessage(String username)
	{
		InfoOfMessage result = null;
		if (messagesInfo != null) 
		{
			for (int i = 0; i < messagesInfo.length;) 
			{
				
					result = messagesInfo[i];
					break;
								
			}			
		}		
		return result;
	}
	
	



	public static InfoOfMessage getMessageInfo(String username) 
	{
		InfoOfMessage result = null;
		if (messagesInfo != null) 
		{
			for (int i = 0; i < messagesInfo.length;) 
			{
					result = messagesInfo[i];
					break;
							
			}			
		}		
		return result;
	}






	public static InfoOfMessage[] getMessagesInfo() {
		return messagesInfo;
	}



	
	
	

}
