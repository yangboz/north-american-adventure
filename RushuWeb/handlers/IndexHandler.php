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
include(getcwd()."/Constants.php");
//@example : https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx34ff2a71ac3c7510&secret=cfc67c1d52476b2dd8a93b32ef8c4617
//@return access_token: 0xQCS_FDjYfjCo-h2wtwnMQeU0aMsQGJZOxdQvcwm8WocHRvGXpNMBk5SiyI3pTZ6fOY0XINHwSzgD_EucMyGw
include(getcwd()."/libs/Wechat.class.php");
//
class IndexHandler
{
    public $access_token;
    private $checkSignature;
    private $echoStr;
    //
    public function get()
    {
//        echo "IndexHandler->get()";
        if (isset($_GET['echostr'])) {
            $this->echoStr = $_GET["echostr"];
            //
            file_put_contents(getcwd().'/log/log_wechat.txt','$echoStr:'.$this->echoStr."\n",FILE_APPEND);
            //valid signature , option
            $this->checkSignature = $this->checkSignature();
            file_put_contents(getcwd().'/log/log_wechat.txt','$checkSignature:'.$this->checkSignature."\n",FILE_APPEND);
            if($this->checkSignature){
                echo $this->echoStr;
                exit;
                //Wechat initializtion
//            $this->wechat_initialization();
            }
        }else{
            $this->responseMsg();
        }
    }

    public function responseMsg()
    {
        $postStr = $GLOBALS["HTTP_RAW_POST_DATA"];

        if (!empty($postStr)){
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
            if($keyword == "?" || $keyword == "？")
            {
                $msgType = "text";
                $contentStr = date("Y-m-d H:i:s",time());
                $resultStr = sprintf($textTpl, $fromUsername, $toUsername, $time, $msgType, $contentStr);
                echo $resultStr;
            }else{

            }
        }else{
            echo "Empty responseMsg()";
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
//
    private function wechat_initialization()
    {
        //
        file_put_contents(getcwd().'/log/log_wechat.txt','wechat_initialization:'.''."\n",FILE_APPEND);
        $wechatOptions = array(
            'token'=>APP_TOKEN,
            'account'=>APP_DEV_USERNAME,
            'password'=>APP_DEV_PASSWORD
//            "wechattool"=>$wechatToolObj /*这里是上面的接口类实例对象,也可以通过setWechatToolFun()设置*/
        );
        $wechatObj = new Wechat($wechatOptions);
        $wechatObj->valid();//可以在认证后注释掉(只是这样可能不安全)
        $wechatObj->positiveInit();  //主动响应组件初始化
        $wechatObj->setAutoSendOpenidSwitch(TRUE);  //设置自动附带发送Openid
        $wechatObj->setPassiveAscSwitch(TRUE, TRUE);  //设置打开被动关联组件，并获取用户详细信息
        $wechatObj->getRev();
        //被动响应实例
        $wechatObj->valid(); //验证请求来源是否合法，在通过平台验证后可以去掉，但是不安全啊。
        $msgtype = $wechatObj->getRev()->getRevType();
        switch($msgtype) {
            case Wechat::MSGTYPE_TEXT:

                $wechatObj->text("你好我是微信小机器人")->reply();
                exit;
                break;
            case Wechat::MSGTYPE_EVENT:
                $revEvent = array();
                $revEvent = $this->wechatObj->getRevEvent();
                switch ($revEvent['event']) {
                    //关注订阅事件
                    case "subscribe":
                        $wechatObj->text("你好我是微信小机器人")->reply();
                        break;
                    //取消关注订阅事件
                    case "unsubscribe":
                        //做一些删除用户记录之类的事情
                        break;
                }
                break;
            case Wechat::MSGTYPE_IMAGE:
                break;
            case Wechat::MSGTYPE_VOICE:
                break;
            case Wechat::MSGTYPE_MUSIC:
                break;
            case Wechat::MSGTYPE_LOCATION:
                break;
            case Wechat::MSGTYPE_LINK:
                break;
            default:
                $wechatObj->text($wechatObj->wechatObj)->reply();
                break;
        }
        //主动发送消息示例
        //群发消息
        $fakeids = array("823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881","823058881");
        //接收返回结果数组
        $batresult = $wechatObj->batSend($fakeids,"这是一种问候啊！\n下个10分钟再见。");
        //单条消息发送
        $singleresult = $wechatObj->send("823058881", "这是一种问候啊！");
    }
}