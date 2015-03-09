<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-29
 * Time: 上午9:49
 */
//@see:http://www.myexception.cn/php/1512557.html
include("../Constants.php");
//$url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid='.$appid.'&redirect_uri=http%3a%2f%2fwww.aaa.com%2fuc%2ffn_callback.php&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect';
$url = APP_OAUTH2_URI."?appid=".APP_ID.'&redirect_uri='.
    APP_REDIRECT_URL."&response_type=code&scope=".
    APP_API_SCOPE.'&state=STATE#wechat_redirect';
//
header("Location:".$url);
?>