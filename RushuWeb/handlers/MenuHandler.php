<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-29
 * Time: 下午11:19
 */
require_once("AccessTokenHandler.php");
include(getcwd()."/libs/HttpClient.class.php");
//
class MenuHandler
{
    //
    private $access_token;
    private $token_handler;
    private $http_client;
    private $post_url = "http://api.weixin.qq.com/";
    private $post_data_menu;
    private $post_data_menu_resp;
    //
    public function get()
    {
        $this->token_handler = new AccessTokenHandler();
        $this->access_token = $this->token_handler->get();
        echo "access_token:".$this->access_token;
        //
//        $this->http_client = new HttpClient($this->post_url);
        $this->post_data_menu = " {
             'button':[
             {
                  'type':'click',
                  'name':'报销',
                  'key':'wxm_btn_id_eem'
              },
              {
                  'type':'click',
                  'name':'报告',
                  'key':'wxm_btn_id_report'
              },
              {
                  'type':'click',
                  'name':'统计',
                  'key':'wxm_btn_id_stats'
              },
              {
                  'type':'click',
                  'name':'我!',
                  'key':'wxm_btn_id_me'
              }
              ]
         }";
        //
        $this->post_url = $this->post_url."cgi-bin/menu/create?access_token=".$this->access_token;
//        $this->http_client->setDebug(true);
//        $this->post_data_menu_resp = $this->http_client->post($path,$this->post_data_menu);
        $this->post_data_menu_resp = $this->httpsPost($this->post_url,$this->post_data_menu,NULL);
        var_dump($this->post_data_menu_resp);
    }
    private function httpsPost($url,$jsonData,$cookie){ // 模拟提交数据函数
        $curl = curl_init("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=".$this->access_token) ;
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
} 