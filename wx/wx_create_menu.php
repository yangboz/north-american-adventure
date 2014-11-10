<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-11-10
 * Time: 下午7:27
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
//var_dump($array["access_token"]);
//
$access_token = $array["access_token"];
$token_handler;
$http_client;
$post_url = "http://api.weixin.qq.com/";
$post_data_menu;
$post_data_menu_resp;
//

    echo "access_token:".$access_token;
    //
//        $this->http_client = new HttpClient($this->post_url);
    $post_data_menu = " {
             'button':[
             {
                  'type':'click',
                  'name':'报销',
                  'key':'wxm_btn_id_eem'
              },
              {
                  'type':'click',
                  'name':'统计',
                  'key':'wxm_btn_id_stats'
              },
              {
                  'type':'click',
                  'name':'我',
                  'key':'wxm_btn_id_me'
              }
              ]
         }";
    //
    $post_url = $post_url."cgi-bin/menu/create?access_token=".$access_token;
//        $this->http_client->setDebug(true);
//        $this->post_data_menu_resp = $this->http_client->post($path,$this->post_data_menu);
    $post_data_menu_resp = httpsPost($post_url,$post_data_menu,NULL,$access_token);
    var_dump($post_data_menu_resp);
//
function httpsPost($url,$jsonData,$cookie,$access_token){ // 模拟提交数据函数
    $curl = curl_init("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=".$access_token) ;
    curl_setopt($curl, CURLOPT_POST, 1);
    curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($curl, CURLOPT_POSTFIELDS,$jsonData);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
    $result = curl_exec($curl) ;
    if (curl_errno($curl)) {
        echo 'Errno'.curl_error($curl);//捕抓异常
    }
    curl_close($curl);
    return $result;
}