<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-28
 * Time: 下午8:46
 */
define("APP_ID","wx34ff2a71ac3c7510");
define("APP_SECERT","cfc67c1d52476b2dd8a93b32ef8c4617");
define("APP_TOKEN","magenta0cat0purple0spider");
define("ACCESS_TOKEN_URI","https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx34ff2a71ac3c7510&secret=cfc67c1d52476b2dd8a93b32ef8c4617
define("APP_LOGIN_URL","http://www.lookbacon.com/lab/Weixin/test/fn_wx_login.php");
define("APP_REDIRECT_URL","");
define("APP_OAUTH2_URI","https://open.weixin.qq.com/connect/oauth2/authorize");
define("APP_API_SCOPE","snsapi_userinfo");
//Logs,e.g:/home/content/g/o/d/godpaper/html/lab/Weixin/RushuWeb/log/log_wechat.txt
define("FILE_LOG_TOKEN",getcwd()."log/access_token.txt");
define("FILE_LOG_WECHAT",getcwd()."log/log_wechat.txt");
define("FILE_LOG_DEBUG",getcwd()."log/log_debug.txt");
//SQLite
//TimeZone setting.
date_default_timezone_set('UTC');
// Connect to an ODBC database using driver invocation
define('DSN', 'sqlite:data/reim_dev.db');
define('USER_NAME', NULL);
define('PASS_WORD', NULL);
?>