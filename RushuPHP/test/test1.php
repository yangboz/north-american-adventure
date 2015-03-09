<?php
/**
 * 微信公共接口测试
 * 
 */
	include("../wechat.class.php");
    include("../Constants.php");
    require("../handlers/AccessTokenHandler.php");

	function logdebug($text){
		file_put_contents(FILE_LOG_DEBUG,$text."\n",FILE_APPEND);
	};

    $handler =new AccessTokenHandler();
    $token = $handler->get();

	$options = array(
		'token'=> $token, //填写你设定的key
			'debug'=>true,
			'logcallback'=>'logdebug',
        'appid'=>APP_ID,
        "appsecret"=>APP_SECERT,
        "access_token"=>$token
	);
	$weObj = new Wechat($options);
var_dump($weObj);
	$weObj->valid();
	$type = $weObj->getRev()->getRevType();
	switch($type) {
		case Wechat::MSGTYPE_TEXT:
				$weObj->text("hello, I'm wechat")->reply();
				exit;
				break;
		case Wechat::MSGTYPE_EVENT:
				break;
		case Wechat::MSGTYPE_IMAGE:
				break;
		default:
				$weObj->text("help info")->reply();
	}
