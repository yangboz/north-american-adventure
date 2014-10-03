angular.module('starter.controllers', [])

.controller('DashCtrl', function($scope) {
})

.controller('StatsCtrl', function ($scope) {
//        $http.get('performance.json').success(function(data) {
//            $scope.performance  = data;
//        }).error(function(error) {
//            console.log(error);
//        })
        /* Chart options */
        $scope.options = {
            chart: {
                type: 'discreteBarChart',
                // height:450,
                margin : {
                    top: 20,
                    right: 20,
                    bottom: 50,
                    left: 55
                },
                x: function(d){ return d.label; },
                y: function(d){ return d.value; },
                showValues: true,
                valueFormat: function(d){
                    return d3.format(',.4f')(d);
                },
                transitionDuration: 500,
                xAxis: {
                    axisLabel: 'X Axis'
                },
                yAxis: {
                    axisLabel: 'Y Axis',
                    axisLabelDistance: 30
                }
            }
        };

        /* Chart data */
        $scope.data = [{
            key: "Cumulative Return",
            values: [
                { "label" : "A" , "value" : 29.765957771107 },
                { "label" : "B" , "value" : 0 },
                { "label" : "C" , "value" : 32.807804682612 },
                { "label" : "D" , "value" : 196.45946739256 },
                { "label" : "E" , "value" : 0.19434030906893 },
                { "label" : "F" , "value" : 98.079782601442 },
                { "label" : "G" , "value" : 13.925743130903 },
                { "label" : "H" , "value" : 5.1387322875705 }
            ]
        }];
        //
        $scope.chart = {
            labels : ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"],
            datasets : [
                {
                    fillColor : "rgba(151,187,205,0)",
                    strokeColor : "#e67e22",
                    pointColor : "rgba(151,187,205,0)",
                    pointStrokeColor : "#e67e22",
                    data : [4, 3, 5, 4, 6]
                },
                {
                    fillColor : "rgba(151,187,205,0)",
                    strokeColor : "#f1c40f",
                    pointColor : "rgba(151,187,205,0)",
                    pointStrokeColor : "#f1c40f",
                    data : [8, 3, 2, 5, 4]
                }
            ]
        };
})
.controller('ReportsCtrl', function($scope, Reports) {
  $scope.reports = Reports.all();
})

.controller('ReportDetailCtrl', function($scope, $stateParams, Reports) {
  $scope.report = Reports.get($stateParams.reportId);
})

.controller('AccountCtrl', function($scope) {
});
