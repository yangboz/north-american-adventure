<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-11-10
 * Time: 下午7:17
 */
define("APP_ID","wxd66cc874c28c3b3c");
define("APP_SECERT","a89b5a7d646aabbe63161e319ac4ad7f");
define("APP_TOKEN","magenta0cat0purple0spider");
define("ACCESS_TOKEN_URI","https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx34ff2a71ac3c7510&secret=cfc67c1d52476b2dd8a93b32ef8c4617

$AT_URL = ACCESS_TOKEN_URI."&appid=".APP_ID."&secret=".APP_SECERT;
$response = file_get_contents($AT_URL);
$response = json_decode($response);
$array = get_object_vars($response);
var_dump($array["access_token"]);
?>