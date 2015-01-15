package com.rushucloud.wechat.cp;

import java.util.List;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {
	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws WxErrorException {
		SpringApplication.run(Application.class, args);
		// Wechat-CP testing code here:
		// @see:
		// https://github.com/chanjarster/weixin-java-tools/wiki/CP_Quick-Start
		WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
		config.setCorpId("wxdb1834a05c43e4fa"); // 设置微信企业号的appid
		config.setCorpSecret("Yzo8BH1brF7J-pe1wndApJTWrJe73DaAYTpBpk0-DuTPCSPWShK7WD0l4Nrhr-pu"); // 设置微信企业号的app
																									// corpSecret
		config.setAgentId("2"); // 设置微信企业号应用ID
		config.setToken("TzAPtuMryApadDWWmGHo"); // 设置微信企业号应用的token
		config.setAesKey("eHOhWSIZIkkMjxB440hSTEkyMwH4Amkqwm7Prw8XSCt"); // 设置微信企业号应用的EncodingAESKey

		WxCpServiceImpl wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(config);
		LOG.info("wxDepartGet:"+wxCpService.departGet().toString());
		List<WxCpUser> users = wxCpService.departGetUsers(1, true, 1);
		for(int i=0;i<users.size();i++)
		{
			LOG.info("wxDepartGetUser["+i+":"+users.get(i).toJson().toString());
		}
//		String userId = "yangbozhou";
//		WxCpMessage message = WxCpMessage.TEXT().agentId("2")
//				.toParty("1")
//				.toUser(userId)
//				.content("Foo bar!").build();
//		try {
//			LOG.info("wxMessageSend:"+message.toJson().toString());
//			wxCpService.messageSend(message);
//		} catch (WxErrorException e) {
//			LOG.error(e.toString());
//		}
	}

}
