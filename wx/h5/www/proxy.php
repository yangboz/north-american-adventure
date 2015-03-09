<?php
/*--------------------------------------------------------------/
| PROXY.PHP                                                     |
| Created By: Évelyne Lachance                                  |
| Contact: eslachance@gmail.com                                 |
| Description: This proxy does a POST or GET request from any   |
|         page on the authorized domain to the defined URL      |
/--------------------------------------------------------------*/
// Destination URL: Where this proxy leads to.
$destinationURL = 'http://stage.rushucloud.com/stage/bind';
// The only domain from which requests are authorized.
$RequestDomain = 'rushucloud.com';
include('JWT.php');
define("PUBKEY", "1NDgzZGY1OWViOWRmNjI5ZT");
define("USER_EMAIL", '21111111@qq.com');
define("USER_PASSWORD", '111111');
define("SILENCE", 0);
// That's it for configuration!
if(!function_exists('apache_request_headers')) {
// Function is from: http://www.electrictoolbox.com/php-get-headers-sent-from-browser/
    function apache_request_headers() {
        $headers = array();
        foreach($_SERVER as $key => $value) {
            if(substr($key, 0, 5) == 'HTTP_') {
                $headers[str_replace(' ', '-', ucwords(str_replace('_', ' ', strtolower(substr($key, 5)))))] = $value;
            }
        }
        return $headers;
    }
}
// Figure out requester's IP to shipt it to X-Forwarded-For
$ip = '';
if (!empty($_SERVER['HTTP_CLIENT_IP'])) {
    $ip = $_SERVER['HTTP_CLIENT_IP'];
    //echo "HTTP_CLIENT_IP: ".$ip;
} elseif (!empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {
    $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
    //echo "HTTP_X_FORWARDED_FOR: ".$ip;
} else {
    $ip = $_SERVER['REMOTE_ADDR'];
    //echo "REMOTE_ADDR: ".$ip;
}
preg_match('@^(?:http://)?([^/]+)@i', $_SERVER['HTTP_REFERER'], $matches);
$host = $matches[1];
preg_match('/[^.]+\.[^.]+$/', $host, $matches);
$domainName = "{$matches[0]}";
if($domainName == $RequestDomain) {
    $method = $_SERVER['REQUEST_METHOD'];
    $response = proxy_request($destinationURL, ($method == "GET" ? $_GET : $_POST), $method);
    $headerArray = explode("\r\n", $response['header']);
    foreach($headerArray as $headerLine) {
        header($headerLine);
    }
    echo $response['content'];

} else {
    echo "HTTP Referer is not recognized. Cancelling all actions";
}
function proxy_request($url, $data, $method) {
//
    print_r($data);

    $data  = array('email' => USER_EMAIL, 'password' => USER_PASSWORD,'device_token'=>"aaaaabbbbcccc",'device_type'=>'wx');
    print_r($data);
    $jwt = get_header($data);
    $data = array('name' => USER_EMAIL, 'password' => USER_PASSWORD, 'token' => 'this is wx_token');
    $buf = do_Post($url, $data, $jwt);
    print_r($buf);
    $obj = json_decode($buf, true);
    print_r($obj);
    exit;
// Based on post_request from http://www.jonasjohn.de/snippets/php/post-request.htm
    global $ip;
    // Convert the data array into URL Parameters like a=b&foo=bar etc.
    $data = http_build_query($data);
    $datalength = strlen($data);

    // parse the given URL
    $url = parse_url($url);

    if ($url['scheme'] != 'http') {
        die('Error: Only HTTP request are supported !');
    }

    // extract host and path:
    $host = $url['host'];
    $path = $url['path'];

    // open a socket connection on port 80 - timeout: 30 sec
    $fp = fsockopen($host, 80, $errno, $errstr, 30);

    if ($fp){
        // send the request headers:
        if($method == "POST") {
            fputs($fp, "POST $path HTTP/1.1\r\n");
        } else {
            fputs($fp, "GET $path?$data HTTP/1.1\r\n");
        }
        fputs($fp, "Host: $host\r\n");

        fputs($fp, "X-Forwarded-For: $ip\r\n");
        fputs($fp, "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\r\n");

        $requestHeaders = apache_request_headers();
        while ((list($header, $value) = each($requestHeaders))) {
            if($header == "Content-Length") {
                fputs($fp, "Content-Length: $datalength\r\n");
            } else if($header !== "Connection" && $header !== "Host" && $header !== "Content-length") {
                fputs($fp, "$header: $value\r\n");
            }
        }
        fputs($fp, "Connection: close\r\n\r\n");
        fputs($fp, $data);

        $result = '';
        while(!feof($fp)) {
            // receive the results of the request
            $result .= fgets($fp, 128);
        }
    }
    else {
        return array(
            'status' => 'err',
            'error' => "$errstr ($errno)"
        );
    }

    // close the socket connection:
    fclose($fp);

    // split the result header from the content
    $result = explode("\r\n\r\n", $result, 2);

    $header = isset($result[0]) ? $result[0] : '';
    $content = isset($result[1]) ? $result[1] : '';

    // return as structured array:
    return array(
        'status' => 'ok',
        'header' => $header,
        'content' => $content
    );
}

function do_Post($url, $fields, $extraheader = array()){
    $ch  = curl_init() ;
    curl_setopt($ch , CURLOPT_URL, $url ) ;
    curl_setopt($ch , CURLOPT_POST, count ( $fields )) ;
    curl_setopt($ch , CURLOPT_POSTFIELDS, $fields );
    curl_setopt($ch,CURLOPT_HTTPHEADER, $extraheader);
    curl_setopt($ch, CURLOPT_VERBOSE, true) ; // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    ob_start();
    curl_exec($ch );
    $result  = ob_get_contents() ;
    ob_end_clean();
    curl_close($ch ) ;
    return $result;
}

function do_Get($url, $extraheader = array()){

    $ch = curl_init();
    curl_setopt($ch , CURLOPT_URL, $url ) ;
    curl_setopt($ch,CURLOPT_HTTPHEADER, $extraheader);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true) ; // 获取数据返回
    curl_setopt($ch, CURLOPT_BINARYTRANSFER, true) ; // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    curl_setopt($ch, CURLOPT_VERBOSE, true) ; // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    $output = curl_exec($ch) ;
    curl_close($ch);
    return $output;
}

function do_Put($url, $fields, $extraheader = array()){
    $ch  = curl_init() ;
    curl_setopt($ch , CURLOPT_URL, $url ) ;
    curl_setopt($ch , CURLOPT_POST, count ( $fields )) ;
    curl_setopt ($ch, CURLOPT_CUSTOMREQUEST, 'PUT');
    curl_setopt($ch , CURLOPT_POSTFIELDS, $fields );
    curl_setopt($ch,CURLOPT_HTTPHEADER, $extraheader);
    curl_setopt($ch, CURLOPT_VERBOSE, true) ; // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    ob_start();
    curl_exec($ch );
    $result  = ob_get_contents() ;
    ob_end_clean();
    curl_close($ch ) ;
    return $result;
}

function do_Delete($url, $fields, $extraheader = array()){
    $ch  = curl_init() ;
    curl_setopt($ch , CURLOPT_URL, $url ) ;
    curl_setopt($ch , CURLOPT_POST, count ($fields)) ;
    curl_setopt ($ch, CURLOPT_CUSTOMREQUEST, 'DELETE');
    curl_setopt($ch , CURLOPT_POSTFIELDS, $fields);
    curl_setopt($ch,CURLOPT_HTTPHEADER, $extraheader);
    curl_setopt($ch, CURLOPT_VERBOSE, true) ; // 在启用 CURLOPT_RETURNTRANSFER 时候将获取数据返回
    ob_start();
    curl_exec($ch );
    $result  = ob_get_contents() ;
    ob_end_clean();
    curl_close($ch ) ;
    return $result;
}

function get_jwt(){
    $users  = array(
        'email' => USER_EMAIL
    , 'password' => USER_PASSWORD
    ,'device_type' => 'wx'
    ,'device_token' => ''
    );
    return get_header($users);
}

function get_header($config){
    return array('X-REIM-JWT: ' . JWT::encode($config, PUBKEY),"X-wx-api: 1");
}
?>