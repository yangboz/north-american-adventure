//@examle:http://www.kdmooreconsulting.com/blogs/authentication-with-ionic-and-angular-js-in-a-cordovaphonegap-mobile-web-application/
//
//var API_URL = "http://www.rushucloud.com:90/activiti-rest/";///usr/share/tomcat6/webapps/h5
//var API_URL = "http://localhost:8080/activiti-rest/";
var API_URL = "/activiti-rest/";
//
angular.module('starter.services', [])

//TaskService where asignee==userId
.factory('ReportService', function($resource) {
    var data = $resource(API_URL+'service/runtime/tasks/:taskId', {taskId: "@taskId"});
    return data;
})
//
.factory('StatsService', function() {

})

//Service REST API
//@see:http://www.activiti.org/userguide/#N1301E
///UserService
.factory('UserService', function ($resource) {
    var data = $resource(API_URL+'service/identity/users/:user', {user: "@user"});
    return data;
})
///GroupService
.factory('GroupService', function ($resource) {
    var data = $resource(API_URL+'service/identity/groups/:group', {group: "@group"});
    return data;
})
///TaskService
.factory('TaskService', function ($resource) {
    var data = $resource(API_URL+'service/runtime/tasks/:taskId', {taskId: "@taskId"});
    return data;
})
///HistoryService
.factory('HistoryService', function ($resource) {
    var data = $resource(API_URL+'service/history/historic-process-instances/:processInstanceId', {processInstanceId: "@processInstanceId"});
    return data;
})
///ProcessInstances
.factory('ProcessService', function ($resource) {
    var data = $resource(API_URL+'service/process-instances/:processInstanceId', {processInstanceId: "@processInstanceId"});
    return data;
})
///Jobs
.factory('JobService', function ($resource) {
    var data = $resource(API_URL+'service/management/jobs/:jobId', {jobId: "@jobId"});
    return data;
})
///Executions
.factory('ExecutionService', function ($resource) {
    var data = $resource(API_URL+'service/runtime/executions/:executionId', {executionId: "@executionId"});
    return data;
})
///FormData
.factory('FormDataService', function ($resource) {
    var data = $resource(API_URL+'service/form/form-data?taskId=:taskId', {taskId: "@taskId"});
    return data;
})
///HTTP Header communication.
.factory('Base64', function () {
    var keyStr = 'ABCDEFGHIJKLMNOP' +
        'QRSTUVWXYZabcdef' +
        'ghijklmnopqrstuv' +
        'wxyz0123456789+/' +
        '=';
    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };
})
//WebSocket
.factory('WebsocketService', ['$rootScope', '$timeout', function($rootScope, $timeout) {

    var _ws;
    var _username = '';
    var messages = [];
    var users = [];

    function onMessage(e) {
        var data = JSON.parse(decodeURIComponent(e.data));
        $rootScope.$apply(function() {

            if (data.type === 'users') {
                users = data.message;
                $rootScope.$broadcast('websocket', 'users', users);
                return;
            }

            messages.splice(0, 0, { user: data.user, message: data.message, date: data.date });
            $rootScope.$broadcast('websocket', 'message', messages);
        });
    }

    return {

        login: function(url, username) {

            _username = username;

            _ws = new WebSocket(url);
            _ws.onmessage = onMessage;
            _ws.onopen = function () {
                _ws.send(encodeURIComponent(JSON.stringify({ type: 'change', message: _username })));
            };

        },

        logoff: function() {
            _ws.close();
            _ws = null;
            _username = '';
            users = [];
            $rootScope.$broadcast('websocket', 'users', users);
        },

        send: function(message) {
            _ws.send(encodeURIComponent(JSON.stringify({ type: 'message', message: message })));
            //
            _ws.onmessage = function(message) { console.log("_ws.onmessage:",message); }
        }
    };

}]);