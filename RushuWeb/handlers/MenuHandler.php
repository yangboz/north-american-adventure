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
    private $post_data_menu;
    private $post_data_menu_resp;
    //
    public function get()
    {
        $this->token_handler = new AccessTokenHandler();
        $this->access_token = $this->token_handler->get();
        echo "access_token:".$this->access_token;
        //
        $this->http_client = new HttpClient("https://api.weixin.qq.com/");
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
        $path = "cgi-bin/menu/create?access_token=".$this->access_token;
        $this->post_data_menu_resp = $this->http_client->post($path,$this->post_data_menu);
        var_dump($this->post_data_menu_resp);
    }
} 