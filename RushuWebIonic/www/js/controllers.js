angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {
})

.controller('ReportsCtrl', function($scope, Friends) {
  $scope.reports = Friends.all();
})

.controller('ReportDetailCtrl', function($scope, $stateParams, Friends) {
  $scope.report = Friends.get($stateParams.friendId);
})

.controller('StatsCtrl', function($scope) {
})

.controller('AccountCtrl', function($scope) {
});
