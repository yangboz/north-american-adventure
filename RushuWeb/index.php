<?php
//Wechat,@see: https://github.com/ligboy/Wechat-php
//Toro,@see: https://github.com/yangboz/ToroPHP
//
require_once("libs/Toro.php");
//
require_once("handlers/IndexHandler.php");
require_once("handlers/GroupHandler.php");
require_once("handlers/GroupsHandler.php");
require_once("handlers/UserHandler.php");
require_once("handlers/UsersHandler.php");
require_once("handlers/AccessTokenHandler.php");
require_once("handlers/MenuHandler.php");
//
/*
ToroHook::add("404", function() {
    echo "404 Not found!";
});
*/
//
Toro::serve(array(
    "/" => "IndexHandler",
    "/user/" => "UsersHandler",
    "/user/:alpha" => "UserHandler",
    "/group/" => "GroupsHandler",
    "/group/:alpha" => "GroupHandler",
    "/token/" => "AccessTokenHandler",
    "/menu/"=> "MenuHandler"
));