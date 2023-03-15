package com.enbiz.api.common.app.service.payment;

import java.util.Map;

public interface KcpPayService {

	/**
	 * 결제하기 이전에 거래채결 요청
	 * 
	 * @param ordNo 주문번호
	 * @param goodsName 상품이름
	 * @param totalAmount 결제금액
	 * @param payMethod 결제유형
	 * @param retUrl 응담 받을 URL
	 * @param mallId 사이트 번호
	 * @return 처리결과
	 */
	Map<String, Object> trade(String ordNo, String goodsName, Long totalAmount, String payMethod, String retUrl, String mallId);

	/**
	 * KCP PG 결제승인 요청
	 * 
	 * @param totalAmount 결제금액
	 * @param encData 결제창 인증결과 암호화 정보
	 * @param encInfoKCP 결제창 인증결과 암호화 정보
	 * @param tranCd 결제요청타입
	 * @param mallId 사이트 번호
	 * @return
	 */
	Map<String, Object> payment(Long totalAmount, String encData, String encInfo, String tranCd, String mallId);

	/**
	 * KCP PG 결제취소 요청
	 * 
	 * @param tno KCP 거래 고유번호
	 * @param modType 전체 승인취소 - STSC / 부분취소 - STPC
	 * @param modMny 부분취소일 경우 부분취소금액
	 * @param remMny 부분취소일 경우 남은 원거래 금액
	 * @param modDesc 취소사유
	 * @param mallId 사이트 번호
	 * @return
	 */
	Map<String, Object> cancel(String tno, ModType modType, Long modMny, Long remMny, String modDesc, String mallId);

	/**
	 * 전체체취소: STSC, 부분취소: STPC
	 */
	public static enum ModType {
		STSC, STPC
	}
}
