CREATE TABLE Configurations (
	Id serial primary key,
	Name varchar(255),
	Params json
);

INSERT INTO Configurations(name, params) 
VALUES ('redis',
		'{
  "monitoringScenario": "prepublic",
  "endpoint": "88.197.53.114",
  "collection": "STATS_NUMBERS",
  "labels": {"collectionType":"redis"},
  "querySet": {
    "Results": "res",
    "Projects": "proj",
    "Publications": "pubs",
    "Open Access Publications": "oapubs",
    "Closed Access Publications": "noapubs",
    "FP7 Publications": "fp7pubstotal",
    "FP7 Open Access Publications": "fp7oapubs",
    "FP7 Restricted Access Publications": "fp7respubs",
    "FP7 Embargo Access Publications": "fp7embpubs",
    "FP7 Projects with Publications": "fp7projpubs",
    "FP7 Projects": "fp7projtotal",
    "FP7 Projects with SC39": "sc39fp7projtotal",
    "FP7 Projects with SC39 that have publications": "sc39fp7projpubs",
    "FP7 OA SC39 Publications": "sc39fp7oapubs",
    "WT Projects": "wtprojtotal",
    "WT Projects with Publications": "wtprojpubs",
    "WT Publications": "wtpubs",
    "WT Open Access Publications": "wtoapubs",
    "WT Restricted Access Publications": "wtrespubs",
    "WT Embargo Publications": "wtembpubs",
    "ERC Projects": "ercprojtotal",
    "ERC Projects with Publications": "ercprojpubs",
    "ERC Open Access Publications": "ercoapubs",
    "ERC Restricted Publications": "ercrespubs",
    "ERC Embargo Publications": "ercembpubs",
    "ERC Publications": "ercpubs",
    "Total Datasources": "datasrc",
    "Valid Datasources": "dtsrcpubs",
    "Datasources with Publications": "datasrc_withpubs",
    "Journal Datasources": "datasrc_journals",
    "Publication Repository Datasources": "datasrc_pubrepo",
    "Data Repository Datasources": "datasrc_datarepo",
    "Aggregator Datasources": "datasrc_aggr",
    "Total number of datasets": "data_total",
    "Funders": "funders",
    "Organizations with Publications": "org_withpubs"
  }
}'
);
		
INSERT INTO Configurations(name, params) 
VALUES ('solr',
		'{"monitoringScenario": "prepublic",
  "endpoint": "index1.t.hadoop.research-infrastructures.eu:9983,index2.t.hadoop.research-infrastructures.eu:9983,index3.t.hadoop.research-infrastructures.eu:9983",
  "collection": "DMF-index-openaire",
  "labels": {"collectionType": "solr"},
  "querySet": {
    "Results": "oaftype:result AND deletedbyinference:false",
    "Projects": "oaftype:project",
    "Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication",
    "Open Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND resultbestlicenseid:\"OPEN\"",
    "Closed Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND resultbestlicenseid:\"CLOSED\"",
    "FP7 Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"FP7\"",
    "FP7 Closed Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"FP7\" AND resultbestlicenseid : \"CLOSED\"",
    "FP7 Open Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"FP7\" AND resultbestlicenseid : \"OPEN\"",
    "FP7 Restricted Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"FP7\" AND resultbestlicenseid : \"RESTRICTED\"",
    "FP7 Embargo Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"FP7\" AND resultbestlicenseid : \"EMBARGO\"",
    "FP7 Projects": "oaftype:project AND fundinglevel0_id:\"corda_______::FP7\"",
    "FP7 Projects with SC39": "oaftype:project AND fundinglevel0_id:\"corda_______::FP7\" AND projectecsc39:true",
    "WT Projects": "oaftype:project AND fundinglevel0_id:\"wt::WT\"",
    "WT Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"WT\"",
    "WT Open Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"WT\" AND resultbestlicenseid : \"OPEN\"",
    "WT Restricted Access Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"WT\" AND resultbestlicenseid : \"RESTRICTED\"",
    "WT Embargo Publications": "oaftype:result AND deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"WT\" AND resultbestlicenseid : \"EMBARGO\"",
    "ERC Projects": "oaftype:project AND fundinglevel2_name:ERC",
    "ERC Open Access Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC AND resultbestlicenseid:OPEN",
    "ERC Restricted Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC AND resultbestlicenseid:RESTRICTED",
    "ERC Embargo Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC AND resultbestlicenseid:EMBARGO",
    "ERC Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC",
    "Total Datasources": "oaftype:datasource",
    "Valid Datasources": "oaftype:datasource AND -datasourcecompatibilityid:UNKNOWN AND -datasourcecompatibilityid:notCompatible",
    "Journal Datasources": "oaftype:datasource AND datasourcetypeid:\"pubsrepository::journal\"",
    "Publication Repository Datasources": "oaftype:datasource AND datasourcetypeid:\"pubsrepository::unknown\"",
    "Data Repository Datasources": "oaftype:datasource AND datasourcetypeid:\"datarepository::unknown\"",
    "Aggregator Datasources": "oaftype:datasource AND datasourcetypeid:\"aggregator::pubsrepository::unknown\" OR datasourcetypeid:\"aggregator::pubsrepository::journals\" OR datasourcetypeid:\"aggregator::datarepository\"",
    "Total number of datasets": "resulttypeid:dataset",
    "Harvested publications": "(resulttypeid:\"publication\"  -(+(resultdupid:* ) +(deletedbyinference:false )))"
  }
}'
);
