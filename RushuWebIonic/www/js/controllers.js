angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {
})

.controller('ReportsCtrl', function($scope, Reports) {
  $scope.reports = Reports.all();
})

.controller('ReportDetailCtrl', function($scope, $stateParams, Reports) {
  $scope.report = Reports.get($stateParams.reportId);
})

.controller('StatsCtrl', function($scope) {
})

.controller('AccountCtrl', function($scope) {
});
