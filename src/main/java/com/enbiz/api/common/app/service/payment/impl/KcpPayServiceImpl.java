package com.enbiz.api.common.app.service.payment.impl;

import static com.github.underscore.U.objectBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.enbiz.api.common.app.service.payment.KcpPayService;
import com.google.common.base.Joiner;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
@Lazy
@Slf4j
public class KcpPayServiceImpl implements KcpPayService {
	
	private static HttpClient httpClient = HttpClient.create()
			.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
			.responseTimeout(Duration.ofMillis(50000))
			.doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
					.addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));
	
	@Value("${payment.kcp.siteCd}")
	private String kcpSiteCd;
	@Value("${payment.kcp.siteName}")
	private String kcpSiteName;
	@Value("${payment.kcp.certInfo}")
	private String kcpCertInfo;
	@Value("${payment.kcp.kcpPaymentUrl}")
	private String kcpPaymentUrl;
	@Value("${payment.kcp.kcpPaymentCancelUrl}")
	private String kcpPaymentCancelUrl;
	@Value("${payment.kcp.kcpTradeRegUrl}")
	private String kcpTradeRegUrl;
	@Value("${payment.kcp.pkcs8PrivateKey}")
	private String pkcs8PrivateKey;
	@Value("${payment.kcp.pkcs8Password}")
	private String pkcs8Password;
	
	@Override
	public Map<String, Object> trade(String ordNo, String goodsName, Long totalAmount, String payMethod, String retUrl, String mallId) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();

		var response = webClient.post()
				.uri(kcpTradeRegUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(objectBuilder()
						.add("kcp_cert_info", kcpCertInfo)
						.add("site_cd", kcpSiteCd)
						.add("site_name", kcpSiteName)
						.add("ordr_idxx", ordNo)
						.add("good_mny", totalAmount)
						.add("pay_method", payMethod)
						.add("good_name", goodsName)
						.add("Ret_URL", retUrl)
						.add("escw_used", "N")
						.add("user_agent", "")
						.build()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;
		
		return response.block();
	}
	
	@Override
	public Map<String, Object> payment(Long totalAmount, String encData, String encInfo, String tranCd, String mallId) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
		
		var response = webClient.post()
				.uri(kcpPaymentUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(objectBuilder()
						.add("kcp_cert_info", kcpCertInfo)
						.add("site_cd", kcpSiteCd)
						.add("site_name", kcpSiteName)
						.add("enc_data", encData)
						.add("enc_info", encInfo)
						.add("tran_cd", tranCd)
						.add("ordr_mony", totalAmount)
						.build()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;
		
		return response.block();
	}
	
	@Override
	public Map<String, Object> cancel(String tno, ModType modType, Long modMny, Long remMny, String modDesc, String mallId) {
		var webClient = WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.codecs(this::textPlanCodecConfigurer)
				.build();
		
		var response = webClient.post()
				.uri(kcpPaymentCancelUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(objectBuilder()
						.add("kcp_cert_info", kcpCertInfo)
						.add("kcp_sign_data", makeSignatureData(Joiner.on("^").join(kcpSiteCd, tno, modType), pkcs8PrivateKey, pkcs8Password))
						.add("site_cd", kcpSiteCd)
						.add("site_name", kcpSiteName)
						.add("tno", tno)
						.add("mod_type", modType)
						.add("mod_mny", modMny)
						.add("rem_mny", remMny)
						.add("mod_desc", modDesc)
						.build()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
				;
		
		return response.block();
	}

	private void textPlanCodecConfigurer(ClientCodecConfigurer configurer) {
		var objectMapper = configurer.getReaders().stream()
				.filter(reader -> reader instanceof Jackson2JsonDecoder)
				.map(reader -> (Jackson2JsonDecoder) reader)
				.map(Jackson2JsonDecoder::getObjectMapper)
				.findFirst()
				.orElseGet(Jackson2ObjectMapperBuilder.json()::build);

		configurer.customCodecs().registerWithDefaultConfig(new Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN));
	}
	
	
	private static PrivateKey loadSplMctPrivateKeyPKCS8(String privateKey, String password) {
		try {
			var derSeq = ASN1Sequence.getInstance(Base64.decodeBase64(privateKey));
			var encPkcs8PriKeyInfo = new PKCS8EncryptedPrivateKeyInfo(EncryptedPrivateKeyInfo.getInstance(derSeq));
			var decProvider = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(password.toCharArray());
			var priKeyInfo = encPkcs8PriKeyInfo.decryptPrivateKeyInfo(decProvider);
			
			return new JcaPEMKeyConverter().getPrivateKey(priKeyInfo);
		} catch (IOException | OperatorCreationException | PKCSException e) {
			log.warn("", e);
			return null;
		}
	}
	
	private static String makeSignatureData(String targetData, String privateKey, String password) {
		try {
			Signature sign = Signature.getInstance("SHA256WithRSA");
			sign.initSign(loadSplMctPrivateKeyPKCS8(privateKey, password));
			sign.update(targetData.getBytes(Charset.defaultCharset()));
	
			return Base64.encodeBase64String(sign.sign());
		}
		catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			log.warn(e.getMessage(), e);
			return null;
		}
	}
	
	

	public static void main(String[] args) throws Exception {
		System.out.println(makeSignatureData("ABC",
				"MIIE9jAoBgoqhkiG9w0BDAEDMBoEFFvH6G3SXuphZHSeeCUXxFRrK+EYAgIIAASCBMghqwqvkan1kJMkUHnr4qhOKVSHuKgYDYh1rOQDwXS0Ln78pEug3l+2ox5TgAHpRscUP84VVYRt5RnLP7VzsopYEHUsbD5onrC2DEVelV2imToKy91DntXzmJ54xGULq8vYACGStPsvZGvYlmfkCpmz0fYW8pZfPddCS6QRNBlalWHomN/E3dT7XUmudTGaGJTQRtCDbnSdXkvQv2BLx7Y+lwnDo591MoqnbZ+gwj+a8aGKQF58y4HwjNzR7DTvfe4pCrMNUo3i5SvAajlALuhKBxL/wRvStpe8xF8B+YZai0RU5wSgEd/+OS5uz/wU4UTPHE2RrP6yDFacKRKHx76JTcxHHfGbjB8Wd73yL20wW11/Sh7To/HkiDJhQD1MQP2d4cmFKQoKL9XiHYH9igBYqmtTkupFmaine+gRqKQy65RhHAVZYoX074VNL/eIeMH+QDv/7Yd1SwXytqM12OSdCQCfETbDDNIMkLkny+ku9SY/1cEWB6Is0IfMz/b2FKZt1zTWIwrqbRP91CCiEBekWs2JScnQ1scwIMg6stCF8c4ORz14Apc/wLdGN3MYnx0BuJpL5ACSlMl6KueaE26MHHn6KaSV2SHCO6xH/YXLT+Mc5gWi5ax4+GrD9k/3/bP/ie8EZFpO7PNyvYvJVhT2CZPWZiyBX43mxqqHp9j7EyXEdlI2qHxWHfCTSrix2jVb2HkjCsIdOTAd7BT6bCrCVww07V6Q5QYwi4+wLd8KK92850sKxKZi39zrQdvrHktG8qC2sFMNCI8AHcEQ34XkC+GfgFJdJVlmddVAkrW61X/pnkk3IOmOvIW7y30M2lFpNMzDLmwEFfxMQC1HBev1FLRvs0LHEatv+v8gvRw7GdK0C5GsemStQJCuxqTTRjMdyCXOiS9q1hF8AfTuWWFiSVi8jLe4HBAjAr8uGBcTlb7dZEd3GaugFSKjOFuRS8LGAOf6ZzCjm4K7zJ2k56zolPc7azMHLVWEnYMuDHEJH7HY+zyumOCROil2nab8ngQmeWwviRM283CwjmikZUdUlWYg9eOVZHYPThDSEoCiOn7XwgT1LZzTo3o9eiBJ8/VPTJ4xnfaL0/9/GUDLyL6NM6Z98SwySutqWWT4QXpTEVacu736m7+OwtOTs/gZdo1uUXj1HnEdgBRhoW+Y/7n2aNkUm9d5tGKLMzfvmttNiLgDbN4y7fL8wtHLwOJynz8cQWBOOSBp1Kem+7Y679FsNis7AabD28VajhrndnWZd0KSPF2NPOFoBGxGo0lmJJlTev5FBblIWCC9sbIkNQSeGhJoeS0pWbuOKFqXLC80nXBV3047b526ih/0ZsfPZtn6g62d2aOUkC3UFgpLqRgzOmjUfGjmXuCe9WoZvKIrmu2ZNxgYZc6sgyCt0HWZufzz+ZUhE27fBd3fEElc2nutaZ3cNHWFldeVeFO2UzGFrUbqN9F9/IoJFRNMVbGwPlBnHdlZ5C1ct17WW+SeWKwuDJmUPOEcPos+1V5g3HdrvkiTI46cggg6rbhmiFjgX//1O+m2cpj7v4IZN1YNyle+zw3eSThES84olm1pdZWEl60Ls8suzWi0IldMEv88mJEySEAzL/hYgScJTHV/d6JDR02ERNGaaig=",
				"changeit"));
	}
}
