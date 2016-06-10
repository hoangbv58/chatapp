package com.baidar.androidChatter.interfacer;
import com.baidar.androidChatter.typo.InfoOfFriend;
import com.baidar.androidChatter.typo.InfoOfMessage;


public interface Updater {
	public void updateData(InfoOfMessage[] messages, InfoOfFriend[] friends, InfoOfFriend[] unApprovedFriends, String userKey);

}
