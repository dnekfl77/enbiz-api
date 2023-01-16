package com.x2bee.api.common.app.service.order.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.github.underscore.U;
import com.google.common.base.Joiner;
import com.x2bee.api.common.app.dto.request.order.OrdRequest;
import com.x2bee.api.common.app.dto.response.order.OrdResponse;
import com.x2bee.api.common.app.entity.Ord;
import com.x2bee.api.common.app.entity.Pay;
import com.x2bee.api.common.app.enums.PayState;
import com.x2bee.api.common.app.enums.TargetPg;
import com.x2bee.api.common.app.repository.displayrodb.order.OrdMapper;
import com.x2bee.api.common.app.repository.displayrwdb.order.OrdTrxMapper;
import com.x2bee.api.common.app.repository.displayrwdb.order.PayTrxMapper;
import com.x2bee.api.common.app.service.order.OrderService;
import com.x2bee.api.common.app.service.payment.KcpPayService;
import com.x2bee.api.common.app.service.payment.KcpPayService.ModType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

@Service
@Lazy
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	@Value("${payment.kcp.siteCd}")
	private String kcpSiteCd;
	@Value("${payment.kcp.siteName}")
	private String kcpSiteName;
	@Value("${payment.kcp.jsUrl}")
	private String kcpJsUrl;

//	private final PayMapper payMapper;
	private final OrdMapper ordMapper;
	private final PayTrxMapper payTrxMapper;
	private final OrdTrxMapper ordTrxMapper;
	
	private final KcpPayService kcpPayService;

	@Override
	public Map<String, Object> payInfo() {
		log.debug("payInfo");

		return U.objectBuilder()
				.add("kcpSiteCd", kcpSiteCd)
				.add("kcpSiteName", kcpSiteName)
				.add("kcpJsUrl", kcpJsUrl)
				.add("orderXxx", Joiner.on("-").join(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()), RandomStringUtils.randomAlphanumeric(5)))
				.add("goodName", "test_good_name")
				.add("goodMny", 1004)
				.build();
	}

	@Override
	public Map<String, Object> saveOrder(Map<String, Object> request) {
		var approveResult = kcpPayService.payment(MapUtils.getLong(request, "good_mny"),
				MapUtils.getString(request, "enc_data"),
				MapUtils.getString(request, "enc_info"),
				MapUtils.getString(request, "tran_cd"),
				StringUtils.EMPTY);

		ordTrxMapper.save(Ord.builder()
				.orderNo(MapUtils.getString(approveResult, "order_no"))
				.goodsName(MapUtils.getString(request, "good_name"))
				.goodsPrice(MapUtils.getLong(request, "good_mny", 0L))
				.buyerName(MapUtils.getString(request, "buyr_name"))
				.buyerPhone(MapUtils.getString(request, "buyr_tel1"))
				.buyerEmail(MapUtils.getString(request, "buyr_mail"))
				.targetPg(convertTargetPg(MapUtils.getString(request, "target_pg")))
				.build());

		payTrxMapper.save(Pay.builder()
				.orderNo(MapUtils.getString(approveResult, "order_no"))
				.resMsg(MapUtils.getString(approveResult, "res_msg"))
				.resCd(MapUtils.getString(approveResult, "res_cd"))
				.pgTxid(MapUtils.getString(approveResult, "pg_txid"))
				.amount(MapUtils.getLong(approveResult, "amount", 0L))
				.traceNo(MapUtils.getString(approveResult, "trace_no"))
				.tno(MapUtils.getString(approveResult, "tno"))
				.payMethod(MapUtils.getString(approveResult, "pay_method"))
				.payState(PayState.COMPLETE)
				.build());

		return approveResult;
	}

	@Override
	public List<OrdResponse> listOrder(OrdRequest request) {
		return ordMapper.listByRequest(request);
	}
	
	@Override
	public void cancelOrder(Map<String, Object> params) {
		payTrxMapper.cancel(Pay.builder()
				.id(MapUtils.getLong(params, "id"))
				.payState(PayState.CANCEL)
				.build());
		
		kcpPayService.cancel(MapUtils.getString(params, "tno"), ModType.STSC, 
				MapUtils.getLong(params, "amount"), MapUtils.getLong(params, "amount"), "cancel", null);
	}
	
	@Override
	public void cancelOrder(OrdRequest request) {
		var pay = request.getPayRequest(); 

		payTrxMapper.cancel(Pay.builder()
				.id(pay.getId())
				.payState(PayState.CANCEL)
				.build());
		
		kcpPayService.cancel(pay.getTno(), ModType.STSC, pay.getAmount(), pay.getAmount(), "cancel", null);
	}
	
	private TargetPg convertTargetPg(String targetPgCd) {
		return StringUtils.isBlank(targetPgCd) ? null : TargetPg.valueOf(targetPgCd);
	}
}
