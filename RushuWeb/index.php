<?php
//Wechat,@see: https://github.com/ligboy/Wechat-php
//Toro,@see: https://github.com/yangboz/ToroPHP
//
require("libs/Toro.php");
//
require("handlers/IndexHandler.php");
require("handlers/GroupHandler.php");
require("handlers/GroupsHandler.php");
require("handlers/UserHandler.php");
require("handlers/UsersHandler.php");
require("handlers/AccessTokenHandler.php");
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
    "/token/" => "AccessTokenHandler"
));