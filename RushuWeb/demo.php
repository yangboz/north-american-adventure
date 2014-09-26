<?php
include "wechat.class.php";
$options = array(
			'token'=>'WzSKU6Z9rPDAGl6SWu4CXJG9iXqvxC2XoubOO1LEsVOVYc0DYmwT3_JsbxLcfxwJi57XCylBP0Y9mn_6jJODlg', //填写你设定的key
 			'appid'=>'wx34ff2a71ac3c7510', //填写高级调用功能的app id
 			'appsecret'=>'cfc67c1d52476b2dd8a93b32ef8c4617' //填写高级调用功能的密钥
 			//'partnerid'=>'88888888', //财付通商户身份标识
 			//'partnerkey'=>'', //财付通商户权限密钥Key
 			//'paysignkey'=>'' //商户签名密钥Key
	);
$weObj = new Wechat($options);
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