package com.javateam.healthyFoodProject.domain;

import lombok.Data;

@Data
public class PageVO {
	
	// 시작 페이지
	private int startPage;
	
	// 마지막 페이지
	private int endPage;
	
	// 총 페이지 수
	private int maxPage;
	
	// 현재 페이지
	private int currentPage;
	
	// 총 인원, 게시글 수
	private int listCount;	
	
	// 이전 페이지
	private int previousPage;
	
	// 다음 페이지 
	private int nextPage;

	
	// 총 인원, 게시글 수
	public static int getMaxPage(int listCount, int limit) {
		
		return (int)((double)listCount/limit+0.95); //0.95를 더해서 올림 처리
	}
	
	// 현재 페이지
	public static int getStartPage(int currentPage, int limit) {
		
		return  (((int) ((double)currentPage / limit + 0.9)) - 1) * limit + 1;
	}
}
