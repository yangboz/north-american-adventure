<?php
/**
 * Created by PhpStorm.
 * User: yangboz
 * Date: 14-9-29
 * Time: 上午9:47
 */
//@see:http://www.myexception.cn/php/1512557.html
include("../Constants.php");
//
if(empty($_SESSION['user'])){
//    header("Location:http://www.aaa.net/uc/fn_wx_login.php");
    header("Location:".APP_LOGIN_URL);
}else{
    print_r($_SESSION['user']);
}
?>