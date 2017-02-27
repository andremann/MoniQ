var module = angular.module('metricsGraphsExpose', ['ngRoute', 'jlareau.pnotify']);

module.controller('MetricsExposeController', function ($scope, $http, $route, $routeParams, $location, notificationService) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.expose = {};
	$scope.expose.widgets = [];
	$scope.expose.widgets = parseRouteParam($routeParams.widgetConf);
	
	$http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics')
	.success(
		function(data) {
			$scope.expose.metrics = data;
		})
	.error(
		function(data) {
			notificationService.error('Error..');
		}		
	);
	
	$scope.expose.fetchMetrics = function() {
		if ($scope.expose.metrics.length == 0) {
			$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics')
			.success(
				function(data) {
					$scope.expose.metrics = data;
				})
			.error(
				function(data) {
					notificationService.error('Error..');
				}		
			);
		}
	};
	
	$scope.$watch('expose.widgets', function () {
		var newParams = {};
		newParams['widgetConf'] = encode($scope.expose.widgets);
		$route.updateParams(newParams);
	}, true);
	
	function encode(widgetConf) {
		var url = '';
		for (key in widgetConf) {
			url += widgetConf[key].metric;
			url += ':';
			url += widgetConf[key].label;
			if (key != widgetConf.length - 1) {
				url += '!';
			}
		}
		return url;
	};
	
	$scope.expose.fetchLabelNames = function(m) {
		$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics/'+ m + '/labels')
		.success(
			function(data) {
				$scope.expose.labelNames = data;
			})
		.error(
			function(data) {
				notificationService.error('Error..');
			}		
		);
	};
	
	$scope.expose.addWidget = function() {
		var widgetSetup = {};
		widgetSetup["metric"] = $scope.expose.selectedMetric;
		widgetSetup["label"] = $scope.expose.selectedLabel;
		$scope.expose.widgets.push(widgetSetup);
		$scope.expose.selectedMetric = undefined;
		$scope.expose.selectedLabel = undefined;
	};
	
	$scope.expose.reset = function() {
		$scope.expose.widgets = {};
	};
	
	function parseRouteParam(widgetSetupString) {
		if (widgetSetupString != '' && widgetSetupString != undefined) {
			var widgetSetup = widgetSetupString.split("!");
			var widgets = [];
			for (var key in widgetSetup) {
				var widgetParams = widgetSetup[key].split(":");
				var widget = {};
				widget["metric"] = widgetParams[0];
				widget["label"] = widgetParams[1];
				widgets.push(widget);
			}
			return widgets;
		} else {
			return [];
		}
	};
});

module.controller('GraphWidgetController', function ($scope, $http, $routeParams) {
	$scope.canvasId = genRandomId();
	
	$http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + $scope.widget.metric + "/observations?label=" + $scope.widget.label)
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
							format : '%d-%m-%Y@%H:%M:%S',
							fit: true,
							rotate: -20,
			                multiline: true
						},
						height: 50
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
			notificationService.error('Error..');
		}
	);
	
	function genRandomId() {
	    var text = "";
	    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    for( var i=0; i < 5; i++ )
	        text += possible.charAt(Math.floor(Math.random() * possible.length));
	    return text;
	};
	
});