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
    echo "Code:".$_GET['code']."\r\n";
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
    $userinfo = json_decode($user, true);
    if (empty($userinfo)) {
        setLog("file_get_contents(user,info) results: NULL!");
    }
    //dump user info
//    var_dump($userinfo);
    setDb($userinfo);
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
//SQLite
//TimeZone setting.
date_default_timezone_set('UTC');
// Connect to an ODBC database using driver invocation
define('DB_DSN', 'sqlite:reim_dev.db');
define('DB_USER_NAME', NULL);
define('DB_PASS_WORD', NULL);
//
function setDb($userinfo)
{   //subscribe,openid,nickname,sex,city,country,province,language,headimgurl,unionid
    $subscribe = $userinfo["openid"];
    $openid = $userinfo["openid"];
    $nickname = $userinfo["nickname"];
    $sex = $userinfo["sex"];
    $city = $userinfo["city"];
    $country = $userinfo["country"];
    $province = $userinfo["province"];
    $language = $userinfo["language"];
    $headimgurl = $userinfo["headimgurl"];
    $unionid = $userinfo["unionid"];
//
    try {
//        echo "DB_DSN:". DB_DSN ."\r\n";
//        $dbh = new PDO(DB_DSN, DB_USER_NAME, DB_PASS_WORD);
        $dbh = new PDO("sqlite:reim_dev.db", NULL, NULL);
        $dbh ->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        //echo 'PDO Connection  OK!','';
        $sth = $dbh -> prepare("SELECT openid FROM user WHERE openid='$openid' LIMIT 1");
        $sth -> execute();
        $result = $sth -> fetchAll();
        if($result)
        {
            echo 'OpenId: ',$openid,' has exist!<a href="javascript:history.back(-1);">back</a> Please use another OpenId';
            exit;
            $dsn = null;
        }else{
            $insert = "INSERT INTO user(subscribe,openid,nickname,sex,city,country,province,language,headimgurl,unionid)VALUES('$subscribe','$openid','$nickname','$sex','$city','$country','$province','$language','$headimgurl','$unionid')";
            $dbh->exec($insert);
            $dbh->lastInsertId();
            if($dbh->lastInsertId()){
                exit('UserInfo Insert Success!');
                $dsn = null;
            } else {
                echo 'UserInfo Insert Error!',mysql_error(),'<br />';
                echo 'Click <a href="javascript:history.back(-1);"> to return</a> and try again!';
                $dsn = null;
            }
        }
    } catch (PDOException $e) {

        echo 'Connection failed: ' . $e -> getMessage();

        $dsn = null;

    }
}
?>