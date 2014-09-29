<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-25
 * Time: 下午2:10
 */
//define your token
//define("TOKEN", "magenta0cat0purple0spider");
//@see http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
//define("appID", "wx34ff2a71ac3c7510");
//define("appSecret", "cfc67c1d52476b2dd8a93b32ef8c4617");
include("../Constants.php");
//@example : https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx34ff2a71ac3c7510&secret=cfc67c1d52476b2dd8a93b32ef8c4617
//@return access_token: 0xQCS_FDjYfjCo-h2wtwnMQeU0aMsQGJZOxdQvcwm8WocHRvGXpNMBk5SiyI3pTZ6fOY0XINHwSzgD_EucMyGw
//
class IndexHandler
{
    public $access_token;
    //
    public function get()
    {
        $echoStr = $_GET["echostr"];

        //valid signature , option
        if($this->checkSignature()){
            echo $echoStr;
            exit;
        }
        //
        file_put_contents('../log/log_wechat.txt','$echoStr:'.$echoStr."\n",FILE_APPEND);
    }

    public function responseMsg()
    {
        //get post data, May be due to the different environments
        $postStr = $GLOBALS["HTTP_RAW_POST_DATA"];

        //extract post data
        if (!empty($postStr)){
            /* libxml_disable_entity_loader is to prevent XML eXternal Entity Injection,
               the best way is to check the validity of xml by yourself */
            libxml_disable_entity_loader(true);
            $postObj = simplexml_load_string($postStr, 'SimpleXMLElement', LIBXML_NOCDATA);
            $fromUsername = $postObj->FromUserName;
            $toUsername = $postObj->ToUserName;
            $keyword = trim($postObj->Content);
            $time = time();
            $textTpl = "<xml>
							<ToUserName><![CDATA[%s]]></ToUserName>
							<FromUserName><![CDATA[%s]]></FromUserName>
							<CreateTime>%s</CreateTime>
							<MsgType><![CDATA[%s]]></MsgType>
							<Content><![CDATA[%s]]></Content>
							<FuncFlag>0</FuncFlag>
							</xml>";
            if(!empty( $keyword ))
            {
                $msgType = "text";
                $contentStr = "Welcome to wechat world!";
                $resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $msgType, $contentStr);
                echo $resultStr;
            }else{
                echo "Input something...";
            }

        }else {
            echo "";
            exit;
        }
    }

    private function checkSignature()
    {
        // you must define TOKEN by yourself
        if (!defined("APP_TOKEN")) {
            throw new Exception('APP_TOKEN is not defined!');
        }

        $signature = $_GET["signature"];
        $timestamp = $_GET["timestamp"];
        $nonce = $_GET["nonce"];

        $token = APP_TOKEN;
        $tmpArr = array($token, $timestamp, $nonce);
        // use SORT_STRING rule
        sort($tmpArr, SORT_STRING);
        $tmpStr = implode( $tmpArr );
        $tmpStr = sha1( $tmpStr );

        if( $tmpStr == $signature ){
            return true;
        }else{
            return false;
        }
    }
}