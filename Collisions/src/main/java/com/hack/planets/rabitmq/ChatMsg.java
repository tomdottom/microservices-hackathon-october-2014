package com.hack.planets.rabitmq;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class ChatMsg {
	String who;
	public String getWho() {
		return who;
	}
	public String getSays() {
		return says;
	}
	String says;
	public ChatMsg(String who, String says) {
		super();
		this.who = who;
		this.says = says;
	}
	public String toString(){
		return "{who: "+who+" says: "+says+"}";
	}
}
