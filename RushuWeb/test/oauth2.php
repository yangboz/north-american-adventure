<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-29
 * Time: 上午10:09
 */
//@see: http://www.cnblogs.com/txw1958/p/weixin71-oauth20.html
//@example: https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx34ff2a71ac3c7510&redirect_uri=http://www.lookbackon.com/lab/Weixin/RushuWeb/test/oauth2.php&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
define("APPID", "wx34ff2a71ac3c7510");
define("SECRET", "cfc67c1d52476b2dd8a93b32ef8c4617");
if (isset($_GET['code']))
{
    echo $_GET['code'];
    //get token,openid
    $code = $_GET['code'];
    $url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" . APPID . "&secret=" . SECRET . "&code=$code&grant_type=authorization_code";
    $con = file_get_contents($url);
    //
    if (empty($con)) {
        setLog('file_get_contents(token,openid) results: NULL!');
    }
    //
    $rs = json_decode($con, true);
    //
    $userURL = "https://api.weixin.qq.com/sns/userinfo?access_token=$rs[access_token]&openid=$rs[openid]";
    //
    $user = file_get_contents($userURL);
    //
    $u = json_decode($user, true);
    if (empty($u)) {
        setLog("file_get_contents(user,info) results: NULL!");
    }
    //dump user info
    var_dump($u);
}else{
    echo "NO WeChat OAuth2 CODE!";
}
//
function setLog($con)
{
    $time = date("m/d H:i:s", time());
    $con = "# [" . $time . "]" . $con . "\r\n";
    $file = getcwd() . "log/log_wechat.txt";
    $handle = fopen($file, "a");
    fwrite($handle, $con);
    fclose($handle);
}
?>