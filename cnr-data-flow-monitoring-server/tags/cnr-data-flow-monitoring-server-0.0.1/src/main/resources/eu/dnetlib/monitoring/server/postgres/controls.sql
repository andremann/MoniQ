CREATE TABLE Controls (
	Id serial primary key,
	Name varchar(255) not null,
	MonitoringScenario varchar(255) not null references Scenarios(name),
	Selectors json,
	AnalyzerClass varchar(255) not null,
	Status varchar(255) not null,
	LastModified timestamp default now()
);

INSERT INTO Controls(name, monitoringScenario, Selectors, analyzerclass, status, LastModified) 
VALUES ('Last values are equal',
		'prepublic',
		'[
		  {
		    "metric": "fp7_projects",
		    "labelName": "collectionType",
		    "labelValue": "solr",
			"samples": 1
		  },
		  {
		    "metric": "fp7_projects",
		    "labelName": "collectionType",
		    "labelValue": "redis",
			"samples": 1
		  }
		]',
		'eu.dnetlib.monitoring.controls.analyzers.impl.AreEqual',
		'active',
		now());
		
INSERT INTO Controls(name, monitoringScenario, Selectors, analyzerclass, status, LastModified) 
VALUES ('Last values are equal',
		'prepublic',
		'[
		  {
		    "metric": "publications",
		    "labelName": "collectionType",
		    "labelValue": "solr",
			"samples": 1
		  },
		  {
		    "metric": "publications",
		    "labelName": "collectionType",
		    "labelValue": "redis",
			"samples": 1
		  }
		]',
		'eu.dnetlib.monitoring.controls.analyzers.impl.AreEqual',
		'active',
		now());
		
INSERT INTO Controls(name, monitoringScenario, Selectors, analyzerclass, status, LastModified) 
VALUES ('Monotonic increasing',
		'prepublic',
		'[
		  {
		    "metric": "fp7_projects",
		    "labelName": "collectionType",
		    "labelValue": "solr",
			"samples": 3
		  }]',
		'eu.dnetlib.monitoring.controls.analyzers.impl.IsMonotonicIncreasing',
		'active',
		now());
		
INSERT INTO Controls(name, monitoringScenario, Selectors, analyzerclass, status, LastModified) 
VALUES ('Monotonic increasing',
		'prepublic',
		'[
		  {
		    "metric": "publications",
		    "labelName": "collectionType",
		    "labelValue": "solr",
			"samples": 5
		  }]',
		'eu.dnetlib.monitoring.controls.analyzers.impl.IsMonotonicIncreasing',
		'active',
		now());