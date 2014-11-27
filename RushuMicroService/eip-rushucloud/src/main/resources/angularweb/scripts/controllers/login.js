angular.module('activitiApp').controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location, $log) {
    $rootScope.loggedUser = {

    };
    $scope.username = "employee1";
    $scope.password = "passwordpassword";
    $rootScope.loggedin = false;
    //
    $scope.login = function () {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.username + ":" + $scope.password);
//        alert("$scope.login() call!"+UserService);
        UserService.get({user: $scope.username}, function (data) {
//            data = JSON.parse(data);
            alert(data);
            $log.info("UserLogin result:"+data);
            $rootScope.loggedin = true;
            $rootScope.loggedUser = data;
            $rootScope.username = $scope.username;
            $rootScope.password = $scope.password;
            $location.path('/dashboard');
        });
    };
});

