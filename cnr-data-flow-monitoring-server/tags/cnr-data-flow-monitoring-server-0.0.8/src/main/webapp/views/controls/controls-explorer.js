var module = angular.module('controlsExplorer', ['ngRoute', 'ui.select', 'ngSanitize', 'akoenig.deckgrid', 'jlareau.pnotify']);

module.controller('ControlsExplorerController', function ($scope, $http, $routeParams, notificationService) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.controlsExplorer = {};
	$scope.controlsExplorer.metrics = [];
	
	$http.get('ajax/scenarios/'+ $routeParams.scenario +'/controls/')
	.success(
		function(data) {
			$scope.controlsExplorer.controls = data;
		}
	)
	.error(
		function(data) {
			notificationService.error('Error..');
		}
	);
	
	$scope.controlsExplorer.open = function(control) {
		if (control) {
			//open
			$scope.controlsExplorer.selectedControlIndex = $scope.controlsExplorer.controls.indexOf(control);
			$scope.controlsExplorer.tmpControl = angular.copy(control);
			delete $scope.controlsExplorer.tmpControl.$index;
		} else {
			//new
			$scope.controlsExplorer.selectedControlIndex = null;
			$scope.controlsExplorer.tmpControl = {monitoringScenario : $scope.dashboard.currentScenario, status: 'inactive'};
		}
		$('#controlModal').modal('show');
	};
	
	$scope.controlsExplorer.save = function() {
		$scope.controlsExplorer.tmpControl.lastModified = new Date().getTime();
		if ($scope.controlsExplorer.selectedControlIndex != null) {
			// update
			$http.put('ajax/scenarios/' + $routeParams.scenario + '/controls', $scope.controlsExplorer.tmpControl)
			.success(
				function(data) {
					$scope.controlsExplorer.controls[$scope.controlsExplorer.selectedControlIndex] = $scope.controlsExplorer.tmpControl;
					notificationService.success('Success!!!');
				}
				
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		} else {
			// new
			$http.post('ajax/scenarios/' + $routeParams.scenario + '/controls', $scope.controlsExplorer.tmpControl)
			.success(
				function(data) {
					$scope.controlsExplorer.tmpControl.id = data;
					$scope.controlsExplorer.controls.push($scope.controlsExplorer.tmpControl);
					notificationService.success('Success!!!');
				}
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		}
		$('#controlModal').modal('hide');
	};
	
	$scope.controlsExplorer.remove = function(control) {
		var result = confirm("Want to delete?");
		if (result) {
		    //Logic to delete the item
			$scope.controlsExplorer.selectedControlIndex = $scope.controlsExplorer.controls.indexOf(control);
			$http['delete']('ajax/scenarios/' + $routeParams.scenario + '/controls/' + control.id)
			.success(
				function(data) {
					$scope.controlsExplorer.controls.splice($scope.controlsExplorer.selectedControlIndex, 1);
					notificationService.success('Success!!!');
				}
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		}
	};
	
	$scope.controlsExplorer.removeSelector = function(selectorIndex) {
		$scope.controlsExplorer.tmpControl.selectors.splice(selectorIndex,1);
	};
	
	$scope.controlsExplorer.addSelector = function() {
		if (!$scope.controlsExplorer.tmpControl.selectors) {
			$scope.controlsExplorer.tmpControl.selectors = [];
		}
		$scope.controlsExplorer.tmpControl.selectors.push({});
	};
	
	$scope.controlsExplorer.setStatus = function(status) {
		$scope.controlsExplorer.tmpControl.status = status;
	};
	
	$scope.controlsExplorer.fetchMetrics = function() {
		if ($scope.controlsExplorer.metrics.length == 0) {
			$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics')
			.success(
				function(data) {
					$scope.controlsExplorer.metrics = data;
				})
			.error(
				function(data) {
					notificationService.error('Error..');
				}		
			);
		}
	};
	
	$scope.controlsExplorer.fetchLabelNames = function(m) {
		$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics/'+ m + '/labels')
		.success(
			function(data) {
				$scope.controlsExplorer.labelNames = data;
			})
		.error(
			function(data) {
				notificationService.error('Error..');
			}		
		);
	};
	
	$scope.controlsExplorer.fetchLabelValues = function(m, l) {
		$http.get('ajax/scenarios/'+ $routeParams.scenario +'/metrics/'+ m + '/labels/' + l)
		.success(
			function(data) {
				$scope.controlsExplorer.labelValues = data;
			})
		.error(
			function(data) {
				notificationService.error('Error..');
			}		
		);
	};
	
	$scope.controlsExplorer.fetchAnalyzers = function() {
		$http.get('ajax/analyzers/')
		.success(
			function(data) {
				$scope.controlsExplorer.analyzers = data;
			})
		.error(
			function(data) {
				notificationService.error('Error..');
			}		
		);
	};
	
	$scope.controlsExplorer.fetchAnalyzers();
});