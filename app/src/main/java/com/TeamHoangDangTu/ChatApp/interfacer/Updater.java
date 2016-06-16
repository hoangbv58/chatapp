package com.TeamHoangDangTu.ChatApp.interfacer;
import com.TeamHoangDangTu.ChatApp.typo.InfoOfFriend;
import com.TeamHoangDangTu.ChatApp.typo.InfoOfMessage;


public interface Updater {
	public void updateData(InfoOfMessage[] messages, InfoOfFriend[] friends, InfoOfFriend[] unApprovedFriends, String userKey);

}
