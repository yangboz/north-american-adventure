angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {
})

///App Controllers
.value('charting', {
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
})

.controller('StatsCtrl', function ($scope, charting) {
    $scope.someData = [[
        ['Heavy Industry', 12],['Retail', 9], ['Light Industry', 14],
        ['Out of home', 16],['Commuting', 7], ['Orientation', 9]
    ]];
    $scope.myChartOpts = charting.pieChartOptions;
})
.controller('ReportsCtrl', function($scope, Reports) {
  $scope.reports = Reports.all();
})

.controller('ReportDetailCtrl', function($scope, $stateParams, Reports) {
  $scope.report = Reports.get($stateParams.reportId);
})

.controller('AccountCtrl', function($scope) {
});
