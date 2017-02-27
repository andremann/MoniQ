var module = angular.module('metricsGraphsExpose', ['ngRoute']);

module.controller('MetricsExposeController', function ($scope, $http, $routeParams) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.metricsExplorer = {};
	$scope.metricsExplorer.currentMetric = '';
	$scope.metricsExplorer.metrics = [];
	$scope.metricsExplorer.report = [];
	
	$http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics')
	.success(
		function(data) {
			$scope.metricsExplorer.metrics = data;
		})
	.error(
		function(data) {
			
		}		
	);
});

module.controller('GraphWidgetController', function ($scope, $http, $routeParams) {
	$scope.canvasId = genRandomId();
	$scope.selectMetric = function(m) {
		if ($scope.selectedMetric != m){
			$scope.selectedMetric = m;
			
			/* Redraw the widget*/
			$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics/'+ m + '/labels')
			.success(
				function(data) {
					$scope.labels = data;
					$scope.selectLabel(data[0]);
				})
			.error(
				function(data) {
					
				}		
			);
		}
	}
	
	$scope.selectLabel = function(l) {
		$scope.selectedLabel = l;
		
		$http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + $scope.selectedMetric + "/observations?label=" + $scope.selectedLabel)
		.success(
			function(data) {
				$("#"+$scope.canvasId).empty();
				var c3types = {};
				data.labelValues.forEach(
						function(entry) {
							c3types[entry] = 'step';
						}
				);
				c3.generate({
					bindto : '#'+$scope.canvasId,
					line : {
						step : {
							type : 'step-after'
						}
					},
					data : {
						json : data.observations,
						keys : {
							x : 'timestamp',
							value : data.labelValues
						},
						types : c3types,
					},
					point : {
						r: 4
					},
					axis : {
						x : {
							type : 'timeseries',
							tick : {
								format : '%Y-%m-%d',
								fit: false
							}
						}
					},
					tooltip: {
						format: {
							title: function (x) { return x; }
						}
					},
					zoom: {enabled: true}
				});
			}
		)
		.error(
			function(data) {
				
			}
		);
	}
	
	function genRandomId() {
	    var text = "";
	    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    for( var i=0; i < 5; i++ )
	        text += possible.charAt(Math.floor(Math.random() * possible.length));
	    return text;
	}
});