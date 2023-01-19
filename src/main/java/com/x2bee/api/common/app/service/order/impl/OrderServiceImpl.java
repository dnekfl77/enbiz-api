package com.x2bee.api.common.app.service.order.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.underscore.U;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.inicis.std.util.SignatureUtil;
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
import com.x2bee.api.common.app.service.payment.InicisPayService;
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

	@Value("${payment.inicis.mid}")
	private String inicisMid;
	@Value("${payment.inicis.signKey}")
	private String inicisSignKey;
	@Value("${payment.inicis.jsUrl}")
	private String inicisJsUrl;
	
//    mid: INIpayTest
//    password: inicis~1111
//    signKey: SU5JTElURV9UUklQTEVERVNfS0VZU1RS
//    jsUrl: https://stgstdpay.inicis.com/stdjs/INIStdPay.js
	
//	private final PayMapper payMapper;
	private final OrdMapper ordMapper;
	private final PayTrxMapper payTrxMapper;
	private final OrdTrxMapper ordTrxMapper;
	
	private final KcpPayService kcpPayService;
	private final InicisPayService inicisPayService;

	@Override
	public Map<String, Object> kcpPayInfo() {
		log.debug("kcpPayInfo");

		return U.objectBuilder()
				.add("kcpSiteCd", kcpSiteCd)
				.add("kcpSiteName", kcpSiteName)
				.add("kcpJsUrl", kcpJsUrl)
				.add("orderXxx", generateOrderNo())
				.add("goodName", "test_good_name")
				.add("goodMny", 1004)
				.build();
	}

	@Override
	public Map<String, Object> inicisPayInfo() {
		log.debug("inicisPayInfo");

		return injectSignature(U.objectBuilder()
				.add("inicisMid", inicisMid)
				.add("inicisSignKey", inicisSignKey)
				.add("inicisJsUrl", inicisJsUrl)
				.add("mKey", generateMKey())
				.add("timestamp", SignatureUtil.getTimestamp())
				.add("orderXxx", generateOrderNo())
				.add("goodName", "test_good_name")
				.add("goodMny", 1004)
				.build());
	}

	@Override
	public Map<String, Object> saveOrder(Map<String, Object> request) {
		if (StringUtils.equals(MapUtils.getString(request, "target_pg"), "KCP")) {
			return kcpSaveOrder(request);
		} else {
			return inicisSaveOrder(request);
		}
	}

	private Map<String, Object> kcpSaveOrder(Map<String, Object> request) {
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

	private Map<String, Object> inicisSaveOrder(Map<String, Object> request) {
		var timestamp = SignatureUtil.getTimestamp();
		var approveResult = inicisPayService.payment(MapUtils.getString(request, "authUrl"),
				MapUtils.getString(request, "netCancelUrl"),
				MapUtils.getString(request, "mid"),
				MapUtils.getString(request, "authToken"),
				timestamp);

		log.debug("result: {}", approveResult);

		ordTrxMapper.save(Ord.builder()
				.orderNo(MapUtils.getString(approveResult, "MOID"))
				.goodsName(MapUtils.getString(approveResult, "goodsName"))
				.goodsPrice(MapUtils.getLong(approveResult, "TotPrice", 0L))
				.buyerName(MapUtils.getString(approveResult, "buyerName"))
				.buyerPhone(MapUtils.getString(approveResult, "buyerTel"))
				.buyerEmail(MapUtils.getString(approveResult, "buyerEmail"))
				.targetPg(TargetPg.INISIS)
				.build());

		payTrxMapper.save(Pay.builder()
				.orderNo(MapUtils.getString(approveResult, "MOID"))
				.resMsg(MapUtils.getString(approveResult, "resultMsg"))
				.resCd(MapUtils.getString(approveResult, "resultCode"))
				.pgTxid(MapUtils.getString(approveResult, "authSignature"))
				.amount(MapUtils.getLong(approveResult, "TotPrice", 0L))
				.traceNo(timestamp)
				.tno(MapUtils.getString(approveResult, "tid"))
				.payMethod(MapUtils.getString(approveResult, "payMethod"))
				.payState(PayState.COMPLETE)
				.build());

		return Maps.newHashMap();
	}
	
	@Override
	public List<OrdResponse> listOrder(OrdRequest request) {
		return ordMapper.listByRequest(request);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void cancelOrder(Map<String, Object> params) {
		payTrxMapper.cancel(Pay.builder()
				.id(MapUtils.getLong(params, "id"))
				.payState(PayState.CANCEL)
				.build());
		
		if (StringUtils.equals(MapUtils.getString(params, "target_pg"), "KCP")) {
			kcpPayService.cancel(MapUtils.getString(params, "tno"), ModType.STSC, 
					MapUtils.getLong(params, "amount"), MapUtils.getLong(params, "amount"), "cancel", null);
		} else {
			inicisPayService.cancel(MapUtils.getString(params, "payMethod"), "Refund", 
					MapUtils.getString(params, "traceNo"), "127.0.0.1", "INIpayTest", MapUtils.getString(params, "tno"), "취소요청");
		}
	}
	
	@Override
	public void cancelOrder(OrdRequest request) {
		var pay = request.getPayRequest(); 

		payTrxMapper.cancel(Pay.builder()
				.id(pay.getId())
				.payState(PayState.CANCEL)
				.build());
		
		if (request.getTargetPg() == TargetPg.KCP) {
			kcpPayService.cancel(pay.getTno(), ModType.STSC, pay.getAmount(), pay.getAmount(), "cancel", null);
		} else {
			inicisPayService.cancel(pay.getPayMethod(), "Refund", DateFormatUtils.format(Date.from(Instant.now()), "yyyyMMddhhmmss"),
					"127.0.0.1", "INIpayTest",  pay.getTno(), "취소요청");
		}
	}
	
	private String generateOrderNo() {
		return Joiner.on("-").join(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()), RandomStringUtils.randomAlphanumeric(5));
	}
	
	private String generateMKey() {
		try {
			return SignatureUtil.hash(inicisSignKey, "SHA-256");
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}
	
	private Map<String, Object> injectSignature(Map<String, Object> params) {
		try {
			params.put("signature", SignatureUtil.makeSignature(ImmutableMap.<String, String>builder()
					.put("oid", MapUtils.getString(params, "orderXxx"))
					.put("price", MapUtils.getString(params, "goodMny"))
					.put("timestamp", MapUtils.getString(params, "timestamp"))
					.build()));
		} catch (Exception e) {
		}
		return params;
	}
	
	private TargetPg convertTargetPg(String targetPgCd) {
		return StringUtils.isBlank(targetPgCd) ? null : TargetPg.valueOf(targetPgCd);
	}
}
