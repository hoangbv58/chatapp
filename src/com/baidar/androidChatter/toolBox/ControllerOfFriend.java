package com.baidar.androidChatter.toolBox;

import com.baidar.androidChatter.typo.InfoOfFriend;

/*
 * This class can store friendInfo and check userkey and username combination 
 * according to its stored data
 */
public class ControllerOfFriend 
{
	
	private static InfoOfFriend[] friendsInfo = null;
	private static InfoOfFriend[] unapprovedFriendsInfo = null;
	private static String activeFriend;
	
	public static void setFriendsInfo(InfoOfFriend[] friendInfo)
	{
		ControllerOfFriend.friendsInfo = friendInfo;
	}
	
	
	
	public static InfoOfFriend checkFriend(String username, String userKey)
	{
		InfoOfFriend result = null;
		if (friendsInfo != null) 
		{
			for (int i = 0; i < friendsInfo.length; i++) 
			{
				if ( friendsInfo[i].userName.equals(username) && 
					 friendsInfo[i].userKey.equals(userKey)
					)
				{
					result = friendsInfo[i];
					break;
				}				
			}			
		}		
		return result;
	}
	
	public static void setActiveFriend(String friendName){
		activeFriend = friendName;
	}
	
	public static String getActiveFriend()
	{
		return activeFriend;
	}



	public static InfoOfFriend getFriendInfo(String username) 
	{
		InfoOfFriend result = null;
		if (friendsInfo != null) 
		{
			for (int i = 0; i < friendsInfo.length; i++) 
			{
				if ( friendsInfo[i].userName.equals(username) )
				{
					result = friendsInfo[i];
					break;
				}				
			}			
		}		
		return result;
	}



	public static void setUnapprovedFriendsInfo(InfoOfFriend[] unapprovedFriends) {
		unapprovedFriendsInfo = unapprovedFriends;		
	}



	public static InfoOfFriend[] getFriendsInfo() {
		return friendsInfo;
	}



	public static InfoOfFriend[] getUnapprovedFriendsInfo() {
		return unapprovedFriendsInfo;
	}
	
	
	

}
