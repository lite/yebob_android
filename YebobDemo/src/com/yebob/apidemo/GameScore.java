package com.yebob.apidemo;

public class GameScore {
	
	private int score;
	
	public GameScore(){
		score = 0;
	}
	
	public void reset(){
		score = 0;
	}

	public int getScore() {
		return score;
	}

	public void addPoints() {
		this.score +=50 ;
	}
}
