CREATE TABLE Configurations (
	Id serial primary key,
	Name varchar(255),
	Params json,
	LastModified timestamp default now(),
	Status varchar(25) not null
);

INSERT INTO Configurations(name, params) 
VALUES ('redis',
		'{
  "monitoringScenario": "prepublic",
  "endpoint": "88.197.53.114",
  "collection": "SHADOW_STATS_NUMBERS",
  "labels": {"collectionType":"redis"},
  "querySet": {
    "Projects": "proj",
  	"Results": "res",
    "Datasets": "data_total",
    "Publications": "pubs",
    "Open Access Publications": "oapubs",
    "Closed Access Publications": "noapubs",
    "FP7 Publications": "fp7pubstotal",
    "FP7 Open Access Publications": "fp7oapubs",
    "FP7 Projects with Publications": "fp7projpubs",
    "FP7 Projects": "fp7projtotal",
    "FP7 Projects with SC39": "sc39fp7projtotal",
    "FP7 Projects with SC39 that have publications": "sc39fp7projpubs",
    "WT Projects": "wtprojtotal",
    "WT Projects with Publications": "wtprojpubs",
    "WT Publications": "wtpubstotal",
    "WT Open Access Publications": "wtoapubs",
    "ERC Projects": "ercprojtotal",
    "ERC Projects with Publications": "ercprojpubs",
    "ERC Open Access Publications": "ercoapubs",
    "ERC Publications": "ercpubstotal",
    "Datasources with Publications": "datasrc_withpubs",
    "EGI Projects": "egiproj",
    "EGI Projects with Publications": "egiprojpubs",
    "EGI Publications": "egipubs",
    "EGI Open Access Publications": "egioa",
    "FCT Publications": "fctpubstotal",
    "FCT Projects": "fctprojtotal",
    "FCT Projects with Publications": "fctprojpubs",
    "FCT Open Access Publications": "fctoapubs",
    "FET Publications": "fetpubs",
    "FET Open Access Publications": "fetoapubs"
  }
}'
);
		
INSERT INTO Configurations(name, params) 
VALUES ('solr',
		'{"monitoringScenario": "prepublic",
  "endpoint": "index1.t.hadoop.research-infrastructures.eu:9983,index2.t.hadoop.research-infrastructures.eu:9983,index3.t.hadoop.research-infrastructures.eu:9983",
  "collection": "",
  "labels": {"collectionType": "solr"},
  "querySet": {
    "Projects": "oaftype:project",
    "Results": "oaftype:result AND deletedbyinference:false",
    "Datasets": "resulttypeid:dataset",
    "Publications": "deletedbyinference:false AND resulttypeid:publication",
    "Harvested publications": "(resulttypeid:\"publication\"  -(+(resultdupid:* ) +(deletedbyinference:false )))",
    "Open Access Publications": "deletedbyinference:false AND resulttypeid:publication AND resultbestlicenseid:\"OPEN\"",
    "Closed Access Publications": "deletedbyinference:false AND resulttypeid:publication AND resultbestlicenseid:\"CLOSED\"",
    "FP7 Publications": "deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"ec__________::EC::FP7\"",
    "FP7 Open Access Publications": "deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"ec__________::EC::FP7\" AND resultbestlicenseid : \"OPEN\"",
    "FP7 Projects": "oaftype:project AND fundinglevel0_id:\"ec__________::EC::FP7\"",
    "FP7 Projects with SC39": "oaftype:project AND fundinglevel0_id:\"ec__________::EC::FP7\" AND projectecsc39:true",
    "H2020 Publications": "deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"ec__________::EC::H2020\"",
    "H2020 Open Access Publications": "deletedbyinference:false AND resulttypeid:publication AND relfundinglevel0_id : \"ec__________::EC::H2020\" AND resultbestlicenseid : \"OPEN\"",
    "H2020 Projects": "oaftype:project AND fundinglevel0_id:\"ec__________::EC::H2020\"",
    "WT Projects": "oaftype:project AND funderid:\"wt__________::WT\"",
    "WT Publications": "deletedbyinference:false AND resulttypeid:publication AND contextid : \"WT\"",
    "WT Open Access Publications": "deletedbyinference:false AND resulttypeid:publication AND contextid : \"WT\" AND resultbestlicenseid : \"OPEN\"",
    "ERC Projects": "oaftype:project AND fundinglevel2_name:ERC",
    "ERC Open Access Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC AND resultbestlicenseid:OPEN",
    "ERC Publications": "resulttypeid:publication AND deletedbyinference:false AND conceptname:ERC",
    "EGI Open Access Publications": "resulttypeid:publication AND deletedbyinference:false AND contextname:EGI AND resultbestlicenseid:OPEN",
    "EGI Publications": "resulttypeid:publication AND deletedbyinference:false AND contextname:EGI",
    "FET Open Access Publications": "resulttypeid:publication AND deletedbyinference:false AND contextname:FET AND resultbestlicenseid:OPEN",
    "FET Publications": "resulttypeid:publication AND deletedbyinference:false AND contextname:FET",
    "FCT Projects": "oaftype:project AND funderid:\"fct_________::FCT\"",
    "FCT Open Access Publications": "resulttypeid:publication AND deletedbyinference:false AND relfunderid:\"fct_________::FCT\" AND resultbestlicenseid:OPEN",
    "FCT Publications": "resulttypeid:publication AND deletedbyinference:false AND relfunderid:\"fct_________::FCT\""
  }
}'
);

INSERT INTO Configurations(name, params) 
VALUES ('xml',
		'{"monitoringScenario": "native",
  		"labels": {},
  		"metric": "XML coverage",
  		"xpaths": [
		    "//*[local-name()=\"description\"]",
		    "//*[local-name()=\"material\"]",
		    "//*[local-name()=\"decoration\"]"
  			]
		}'
);
