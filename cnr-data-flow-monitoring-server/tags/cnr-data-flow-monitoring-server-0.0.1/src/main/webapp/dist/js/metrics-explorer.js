var module = angular.module('metricsExplorer', ['ngRoute']);

module.controller('MetricsExplorerController', function ($scope, $http, $location, $routeParams) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.metricsExplorer = {};
	$scope.metricsExplorer.currentMetric = '';
	$scope.metricsExplorer.metrics = [];
	$scope.metricsExplorer.report = [];
	
	if ($scope.metricsExplorer.metrics.length == 0) {
		$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics')
		.success(
			function(data) {
				$scope.metricsExplorer.metrics = data;
			})
		.error(
			function(data) {
				
			}		
		);
	}
	$scope.metricsExplorer.currentMetric = $routeParams.metric;
	if ($routeParams.metric != undefined) {
		$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics/'+ $routeParams.metric + '/labels')
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
	
	$scope.selectLabel = function(l) {
		if ($scope.metricsExplorer.currentLabel != l) {
			$scope.metricsExplorer.currentLabel = l;
			
			$http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + $routeParams.metric + "/observations?label=" + $scope.metricsExplorer.currentLabel)
			.success(
				function(data) {
					$scope.observations = data.observations;
					$scope.labelValues = data.labelValues;
					var c3types = {};
					data.labelValues.forEach(
							function(entry) {
								c3types[entry] = 'step';
							}
					);
					c3.generate({
						bindto : '#lineGraph',
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
			
			$http.get('ajax/scenarios/'+ $routeParams.scenario +'/reports/' + $routeParams.metric)
			.success(
				function(data) {
					$scope.metricsExplorer.report = data;
				}
			)
			.error(
				function(data) {
					
				}
			);
		}
	}
});