//@examle:http://www.kdmooreconsulting.com/blogs/authentication-with-ionic-and-angular-js-in-a-cordovaphonegap-mobile-web-application/
//
//var API_URL = "http://www.rushucloud.com:90/activiti-rest/";///usr/share/tomcat6/webapps/h5
//var API_URL = "http://localhost:8080/activiti-rest/";
var API_URL = "/activiti-rest/";
//
angular.module('starter.services', [])

/**
 * Report service that returns some data.
 */
    .factory('ReportService', function() {
        // Might use a resource here that returns a JSON array

        // Some fake testing data
        var reports = [
            { id: 0, name: 'Scruff McGruff' },
            { id: 1, name: 'G.I. Joe' },
            { id: 2, name: 'Miss Frizzle' },
            { id: 3, name: 'Ash Ketchum' }
        ];

        return {
            all: function() {
                return reports;
            },
            get: function(reportId) {
                // Simple index lookup
                return reports[reportId];
            }
        }
    })
/**
 * Stats service that returns some data.
 */
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
    var data = $resource(API_URL+'service/runtime/tasks/:task', {task: "@task"});
    return data;
})
///HistoryService
.factory('HistoryService', function ($resource) {
    var data = $resource(API_URL+'service/history/historic-process-instances/:history', {history: "@history"});
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
});