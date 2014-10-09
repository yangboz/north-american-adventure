<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-28
 * Time: 下午7:07
 */
include(getcwd()."/Constants.php");
//
class AccessTokenHandler {
    //
    public function get()
    {
        $AT_URL = ACCESS_TOKEN_URI."&appid=".APP_ID."&secret=".APP_SECERT;
        $response = file_get_contents($AT_URL);
        $response = json_decode($response);
        $array = get_object_vars($response);
        var_dump($array["access_token"]);
        file_put_contents("/home/content/g/o/d/godpaper/html/lab/Weixin/RushuWeb/log/access_token.txt",$array["access_token"]);
        return $array["access_token"];
    }
} 