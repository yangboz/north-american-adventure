<?php
require("handlers/IndexHandler.php");
require("libs/Toro.php");
require("handlers/GroupHandler.php");
require("handlers/GroupsHandler.php");
require("handlers/UserHandler.php");
require("handlers/UsersHandler.php");

ToroHook::add("404", function() {
    echo "404 Not found!";
});

Toro::serve(array(
    "/" => "IndexHandler",
    "/user/" => "UsersHandler",
    "/user/:alpha" => "UserHandler",
    "/group/" => "GroupsHandler",
    "/group/:alpha" => "GroupHandler"
));