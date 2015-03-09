/**
 * Created by yangboz on 14-9-24.
 */
var app = angular.module('rushuApp', [
    'ngResource',
    "ngRoute",
    "ngTouch",
    "mobile-angular-ui",
    "ui.chart"
]);

var API_URL = "http://localhost:8080/activiti-rest/";

app.config(function($routeProvider, $locationProvider) {
    $routeProvider.when('/',          {templateUrl: "views/home.html"});
    $routeProvider.when('/eems',    {templateUrl: "views/eems.html"});
    $routeProvider.when('/reports',      {templateUrl: "views/reports.html"});
    $routeProvider.when('/stats',   {templateUrl: "views/stats.html"});
    $routeProvider.when('/me',     {templateUrl: "views/me.html"});
    //
    $routeProvider.when('/dashboard', {
            templateUrl: 'partials/dashboard.html',
            controller: 'DashboardCtrl'
        });
    $routeProvider.when('/users', {
            templateUrl: 'partials/users.html',
            controller: 'UsersCtrl'
        });
    $routeProvider.when('/groups', {
            templateUrl: 'partials/groups.html',
            controller: 'GroupsCtrl'
        });
    $routeProvider.otherwise({
            redirectTo: '/'
        });
});

app.controller('MainController', function($scope, $http, UserService, Base64, $rootScope, $location){
///Basic
    $rootScope.$on("$routeChangeStart", function(){
        $rootScope.loading = true;
        //Overlay with login status
        $rootScope.toggle('loginOverlay',$rootScope.loggedin?'off':'on');
    });

    $rootScope.$on("$routeChangeSuccess", function(){
        $rootScope.loading = false;
    });

///FixtureData
    var scrollItems = [];

    for (var i=1; i<=100; i++) {
        scrollItems.push("Item " + i);
    }

    $scope.scrollItems = scrollItems;
    $scope.invoice = {payed: true};

    $scope.userAgent =  navigator.userAgent;
    $scope.chatUsers = [
        { name: "Joe Barker", online: false },
        { name: "Lee Norman", online: false },
        { name: "Ebony Rice", online: false }
    ];
///UserLogin
    $rootScope.loggedUser = {

    };
    $scope.username = "";
    $scope.password = "";
    $rootScope.loggedin = false;

    $scope.login = function () {
        console.log("MainController->login() called!");
        $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.username + ":" + $scope.password);

        UserService.get({user: $scope.username}, function (data) {
            $rootScope.loggedin = true;
            $rootScope.loggedUser = data;
            $rootScope.username = $scope.username;
            $rootScope.password = $scope.password;
//            $location.path('/dashboard');
            //Overlay with login status
            $rootScope.toggle('loginOverlay',$rootScope.loggedin?'off':'on');
        });
    };
});

///Controllers
app.value('charting', {
    pieChartOptions: {
        seriesDefaults: {
            // Make this a pie chart.
            renderer: jQuery.jqplot.PieRenderer,
            rendererOptions: {
                // Put data labels on the pie slices.
                // By default, labels show the percentage of the slice.
                showDataLabels: true
            }
        },
        legend: { show:true, location: 'e' }
    }
});

app.controller('ReportsCtrl', function ($scope, charting) {
    $scope.someData = [[
        ['Heavy Industry', 12],['Retail', 9], ['Light Industry', 14],
        ['Out of home', 16],['Commuting', 7], ['Orientation', 9]
    ]];

    $scope.myChartOpts = charting.pieChartOptions;
});

app.controller('UsersCtrl', function ($scope, $http, UserService, $rootScope, $location, $modal) {
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
    $scope.users = UserService.get();


    /**
     * New user Initial data
     */
    $scope.newUser =  {
        "id": "",
        "firstName": "",
        "lastName": "",
        "email": "",
        "password": ""
    }

    /**
     * Create user function
     * @param newUser
     */
    $scope.createUser = function (newUser) {
        var user = new UserService(newUser);
        user.id= newUser.id;
        user.firstName= newUser.firstName;
        user.lastName= newUser.lastName;
        user.email= newUser.email;
        user.password= newUser.password;

        user.$save(function (u, putResponseHeaders) {
            $scope.users.data.push(u);
        });
    };

    /**
     * Controler for handling modal
     * @param $scope
     * @param $modalInstance
     * @param newUser
     * @constructor
     */
    var ModalInstanceCtrl = function ($scope, $modalInstance, newUser) {
        $scope.newUser = newUser;
        $scope.ok = function () {
            $modalInstance.close(newUser);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };

    /**
     * Show modal dialog
     */
    $scope.open = function () {
        var modalInstance = $modal.open({
            templateUrl: 'partials/modals/createUser.html',
            controller: ModalInstanceCtrl,
            resolve: {
                newUser: function () {
                    return $scope.newUser;
                }
            }
        });
        modalInstance.result.then(function (newUser) {
            $scope.createUser(newUser);
        }, function () {
        });
    };


    $scope.removeUser = function(user)
    {
        UserService.delete({"user": user.id}, function (data) {
            $scope.users = UserService.get();
        });
    }

    $scope.query = "";
});


app.controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location) {
    $rootScope.loggedUser = {

    };
    $scope.username = "";
    $scope.password = "";
    $rootScope.loggedin = false;

    $scope.login = function () {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.username + ":" + $scope.password);

        UserService.get({user: $scope.username}, function (data) {
            $rootScope.loggedin = true;
            $rootScope.loggedUser = data;
            $rootScope.username = $scope.username;
            $rootScope.password = $scope.password;
            $location.path('/dashboard');
        });
    };
});

app.controller('DashboardCtrl', function ($scope, $rootScope, $location) {
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
});

app.controller('GroupsCtrl', function ($scope, $rootScope, $location, GroupService,$modal) {
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
    $scope.groups = GroupService.get();

    /**
     * Initial data of new group
     * @type {{id: string, name: string, type: string}}
     */
    $scope.newGroup = {"id": "", "name": "", "type": "security-role"};

    /**
     * Create group function
     * @param newGroup
     */
    $scope.createGroup = function (newGroup) {
        var group = new GroupService(newGroup);
        group.name = newGroup.name;
        group.id = newGroup.id;
        group.$save(function (u, putResponseHeaders) {
            $scope.groups.data.push(u);
            $scope.isCollapsed = true;
            $scope.newGroup.id = "";
            $scope.newGroup.name = "";
        });
    };

    /**
     * Controler for handling modal actions
     * @param $scope
     * @param $modalInstance
     * @param newGroup
     * @constructor
     */
    var ModalInstanceCtrl = function ($scope, $modalInstance, newGroup) {
        $scope.newGroup = newGroup;
        $scope.ok = function () {
            $modalInstance.close($scope.newGroup);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };

    /**
     * Open Modal
     */
    $scope.open = function () {
        var modalInstance = $modal.open({
            templateUrl: 'partials/modals/createGroup.html',
            controller: ModalInstanceCtrl,
            resolve: {
                newGroup: function () {
                    return $scope.newGroup;
                }
            }
        });
        modalInstance.result.then(function (newGroup) {
            $scope.createGroup(newGroup);
        }, function () {
        });
    };

    /**
     * Remove Group
     * @param group
     */
    $scope.removeGroup = function (group) {
        GroupService.delete({"group": group.id}, function (data) {
            $scope.groups = GroupService.get();
        });
    };

    $scope.cancel = function () {
        $scope.newGroup.id = "";
        $scope.newGroup.name = "";
    };

    $scope.query = "";
});

//Service REST API
//@see:http://www.activiti.org/userguide/#N1301E
///UserService
app.factory('UserService', function ($resource) {
    var data = $resource(API_URL+'service/identity/users/:user', {user: "@user"});
    return data;
});
///GroupService
app.factory('GroupService', function ($resource) {
    var data = $resource(API_URL+'service/identity/groups/:group', {group: "@group"});
    return data;
});
///TaskService
app.factory('TaskService', function ($resource) {
    var data = $resource(API_URL+'service/runtime/tasks/:task', {task: "@task"});
    return data;
});
///HistoryService
app.factory('HistoryService', function ($resource) {
    var data = $resource(API_URL+'service/history/historic-process-instances/:history', {history: "@history"});
    return data;
});

///HTTP Header communication.
app.factory('Base64', function () {
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
