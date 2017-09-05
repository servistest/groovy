ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;

--create temp tables
CREATE global temporary table tmp_identifiers(identifierentityid number(19), identifiervalue varchar(128)) on commit preserve rows;
create index tmp_identifiers_idx on tmp_identifiers(identifierentityid);
CREATE global temporary table tmp_relationship(relatedobjectid number(19), relationobjectid number(19), relatedobjecttypeid number(19), relationobjecttypeid number(19), relationshiprelationshiptypeid number(19)) on commit preserve rows;
create index tmp_relationship_idx1 on tmp_relationship(relatedobjectid);
CREATE global temporary table tmp_english(key number(19),name varchar2(4000)) on commit preserve rows;
create index tmp_english_idx on tmp_english(key);
CREATE global temporary table tmp_japanese(key number(19),name varchar2(4000)) on commit preserve rows;
create index tmp_japanese_idx on tmp_japanese(key);
CREATE global temporary table tmp_chinese(key number(19),name varchar2(4000)) on commit preserve rows;
create index tmp_chinese_idx on tmp_chinese(key);
CREATE global temporary table tmp_rcslookupstaging(rcspermid number(19) not null, rcscode varchar2(128), rcsleaf varchar2(4000),rcsleafja varchar2(4000),rcsleafzh varchar2(4000), rcsgenealogy varchar2(4000),rcsname varchar2(4000),rcsnameja varchar2(4000),rcsnamezh varchar2(4000)) on commit preserve rows;
create index tmp_rcslookupstaging_idx on tmp_rcslookupstaging(rcspermid);

-- I add
--CREATE global temporary table trcsfanoutlookup_staging
--(classification number(19), rcspermid number(19) not null, rcscode varchar2(128),rcsleaf varchar2(4000), rcsleafml varchar2(4000), rcsgenealogy varchar2(4000),rcsname varchar2(4000),rcsnameml varchar2(4000)) on commit preserve rows;
--create index trcsfanoutlookup_staging_idx on trcsfanoutlookup_staging(rcspermid);


CREATE  table trcsfanoutlookup_staging
(classification varchar2(4000), rcspermid number(19) not null, rcscode varchar2(128),rcsleaf varchar2(4000), rcsleafml varchar2(4000), rcsgenealogy varchar2(4000),rcsname varchar2(4000),rcsnameml varchar2(4000)) ;
create index trcsfanoutlookup_staging_idx on trcsfanoutlookup_staging(rcspermid);

--drop table trcsfanoutlookup_staging
--commit

--trcsfanoutlookup_staging(classification,rcspermid,rcscode,rcsleaf,rcsleafml,rcsgenealogy,rcsname,rcsnameml)


--ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;
--load data into temp tables
insert into tmp_identifiers(identifierentityid, identifiervalue)
  select ENTITYID,VALUE
  from IDENTIFIER i
    join METADATARELATIONSHIP r on r.RELATEDOBJECTID= i.ENTITYID and r.RELATIONOBJECTID = 404303 and r.RELATIONSHIPTYPEID=310129
                                   and TO_CHAR(r.EFFECTIVETO, 'DD-MM-RRRR') = '31-12-9999'
  where VALUETYPEID = 320008 --RCSPermId
        and i.ENTITYID not in (select distinct(RELATIONOBJECTID) from METADATARELATIONSHIP where RELATIONOBJECTID = 1001046648) -- Exclude all direct children of M:1
        and i.VALUE not like 'menu%'
        and i.VALUE not in ('E:1','E:10','E:11','E:12','E:13','E:15','E:18','E:19','E:1A','E:1B','E:1C','E:1D','E:1E','E:1F','E:1G','E:1H','E:1I','E:1J','E:1K','E:1L','E:1M','E:1N','E:1P','E:1Q','E:1R','E:1S','E:1T','E:1U','E:1V','E:1W','E:1X','E:1Y','E:1Z','E:2','E:20','E:21','E:22','E:23','E:24','E:25','E:26','E:27','E:28','E:29','E:2A','E:2B','E:2C','E:2D','E:2E','E:2F','E:2G','E:2H','E:2I','E:2J','E:2K','E:2L','E:2M','E:2N','E:2P','E:2Q','E:2R','E:2S','E:2T','E:2U','E:2V','E:2W','E:2X','E:2Y','E:2Z','E:3','E:30','E:31','E:32','E:33','E:34','E:35','E:36','E:37','E:38','E:39','E:3A','E:3B','E:3C','E:3D','E:3E','E:3F','E:3G','E:3H','E:3I','E:3J','E:3K','E:3L','E:3M','E:3N','E:3P','E:3Q','E:3R','E:3S','E:3Y','E:4','E:40','E:42','E:44','E:49','E:4A','E:4B','E:4C','E:4D','E:4E','E:4G','E:4H','E:4I','E:4J','E:4K','E:4L','E:4M','E:4N','E:4P','E:4Q','E:4R','E:4S','E:4T','E:4U','E:4V','E:4W','E:5','E:50','E:51','E:52','E:53','E:54','E:55','E:56','E:57','E:58','E:59','E:5A','E:5B','E:5C','E:5D','E:5E','E:5F','E:5G','E:5J','E:5K','E:6','E:7','E:8','E:9','E:A','E:B','E:C','E:D','E:E','E:F','E:G','E:H','E:I','E:J','E:K','E:L','E:M','E:N','E:P','E:Q','E:R','M:1EN','M:1EQ','M:1ER','M:1ES','M:AA','M:AB','M:AC','M:CN','M:D8','M:MB','cptRel:dependencyOf','cptRel:equivalentTo','cptRel:hasContentLanguage','cptRel:hasCurrency','cptRel:hasGenre','cptRel:hasMinorCurrency','cptRel:hasPrimaryCurrency','cptRel:hasSource','cptRel:hasSubject','cptRel:isInNewsService','cptRel:replacedBy','cptRel:requires','cptRel:useForSearch','cptType:1','cptType:10','cptType:11','cptType:12','cptType:13','cptType:14','cptType:15','cptType:16','cptType:17','cptType:18','cptType:19','cptType:2','cptType:20','cptType:21','cptType:22','cptType:23','cptType:24','cptType:25','cptType:26','cptType:27','cptType:28','cptType:29','cptType:3','cptType:30','cptType:31','cptType:32','cptType:33','cptType:34','cptType:35','cptType:36','cptType:4','cptType:5','cptType:6','cptType:7','cptType:8','cptType:9','dom:data','dom:edit','dom:news','geoProp:3','geoProp:5','geoProp:6','geoProp:7','geoProp:8','iptc:broader','iptc:related','iptc:sameAs','menu:1','menu:10','menu:11','menu:6','menu:7','menu:8','menu:9','prof:1','prof:10','prof:11','prof:12','prof:13','prof:14','prof:15','prof:16','prof:2','prof:3','prof:4','prof:5','prof:6','prof:7','prof:8','prof:9','scheme:A','scheme:B','scheme:BJ','scheme:BL','scheme:C','scheme:CO','scheme:CW','scheme:E','scheme:EIAF','scheme:ES','scheme:G','scheme:I','scheme:J','scheme:L','scheme:LCR','scheme:M','scheme:NP','scheme:NS','scheme:O','scheme:OT','scheme:P','scheme:R','scheme:RR','scheme:SCR','scheme:TR','scheme:U','scheme:cptRel','scheme:cptType','scheme:dom','scheme:geoProp','scheme:iptc','scheme:menu','scheme:prof','scheme:quoteType','scheme:rateType','scheme:scheme','scheme:screenGroup','scheme:screenType','scheme:sourceType','scheme:stg','stg:cancelled','stg:live','stg:notApplicable','stg:retired');



insert into tmp_relationship(relatedobjectid, relationobjectid, relatedobjecttypeid, relationobjecttypeid, relationshiprelationshiptypeid)
  SELECT
    RELATEDOBJECTID,
    RELATIONOBJECTID,
    RELATEDOBJECTTYPEID,
    RELATIONOBJECTTYPEID,
    RELATIONSHIPTYPEID
  FROM METADATARELATIONSHIP
  WHERE RELATEDOBJECTTYPEID=404014
        AND TO_CHAR(EFFECTIVETO, 'DD-MM-RRRR') = '31-12-9999';


insert into tmp_english(key,name)
  SELECT c.CURRENCYID, c.CURRENCYNAME from (
                                             select max(EFFECTIVEFROM) as maxtimestamp, CURRENCYID, LANGUAGEID, NAMETYPEID from CURRENCYNAME
                                             where LANGUAGEID = 505074 and NAMETYPEID = 404500
                                             group by CURRENCYID, LANGUAGEID, NAMETYPEID) a
    join CURRENCYNAME c on c.CURRENCYID=a.CURRENCYID and c.LANGUAGEID=a.LANGUAGEID
                           and c.NAMETYPEID=a.NAMETYPEID and c.EFFECTIVEFROM = a.maxtimestamp;




insert into tmp_japanese(key,name)
  SELECT c.CURRENCYID, c.CURRENCYNAME from (
                                             select max(EFFECTIVEFROM) as maxtimestamp, CURRENCYID, LANGUAGEID, NAMETYPEID from CURRENCYNAME
                                             where LANGUAGEID = 505126 and NAMETYPEID = 404500
                                             group by CURRENCYID, LANGUAGEID, NAMETYPEID) a
    join CURRENCYNAME c on c.CURRENCYID=a.CURRENCYID and c.LANGUAGEID=a.LANGUAGEID
                           and c.NAMETYPEID=a.NAMETYPEID and c.EFFECTIVEFROM = a.maxtimestamp;



insert into tmp_chinese(key,name)
  SELECT c.CURRENCYID, c.CURRENCYNAME from (
                                             select max(EFFECTIVEFROM) as maxtimestamp, CURRENCYID, LANGUAGEID, NAMETYPEID from CURRENCYNAME
                                             where LANGUAGEID = 505049 and NAMETYPEID = 404500
                                             group by CURRENCYID, LANGUAGEID, NAMETYPEID) a
    join CURRENCYNAME c on c.CURRENCYID=a.CURRENCYID and c.LANGUAGEID=a.LANGUAGEID
                           and c.NAMETYPEID=a.NAMETYPEID and c.EFFECTIVEFROM = a.maxtimestamp;



--level 1
insert into tmp_rcslookupstaging(rcspermid,rcscode,rcsleaf,rcsleafja,rcsleafzh,rcsgenealogy,rcsname,rcsnameja,rcsnamezh)
  select
    e.childid as rcspermid,
    case e.hierarchylevel when 1 then e.i1 when 2 then e.i2 when 3 then e.i3 when 4 then e.i4 when 5 then e.i5 end as rcscode,
    case e.hierarchylevel when 1 then e.g1 when 2 then e.g2 when 3 then e.g3 when 4 then e.g4 when 5 then e.g5 end as rcsleaf,
    case e.hierarchylevel when 1 then e.gja1 when 2 then e.gja2 when 3 then e.gja3 when 4 then e.gja4 when 5 then e.gja5 end as rcsleafja,
    case e.hierarchylevel when 1 then e.gzh1 when 2 then e.gzh2 when 3 then e.gzh3 when 4 then e.gzh4 when 5 then e.gzh5 end as rcsleafzh,
    case e.hierarchylevel when 1 then e.i1 when 2 then e.i1 || '\' || e.i2 when 3 then e.i1 || '\' || e.i2  || '\' || e.i3 when 4 then e.i1||'\'||e.i2||'\'||e.i3||'\'||e.i4 when 5 then e.i1||'\'||e.i2||'\'||e.i3||'\'||e.i4||'\'||e.i5 end as rcsgenealogy,
    case e.hierarchylevel when 1 then e.g1 when 2 then e.g1||'\'||e.g2 when 3 then e.g1||'\'||e.g2||'\'||e.g3 when 4 then e.g1||'\'||e.g2||'\'||e.g3||'\'||e.g4 when 5 then e.g1||'\'||e.g2||'\'||e.g3||'\'||e.g4||'\'||e.g5 end as rcsname,
    case e.hierarchylevel when 1 then coalesce(e.gja1,'') when 2 then coalesce(e.gja1,'')||'\'||coalesce(e.gja2,'') when 3 then coalesce(e.gja1,'')||'\'||coalesce(e.gja2,'')||'\'||coalesce(e.gja3,'') when 4 then coalesce(e.gja1,'')||'\'||coalesce(e.gja2,'')||'\'||coalesce(e.gja3,'')||'\'||coalesce(e.gja4,'') when 5 then coalesce(e.gja1,'')||'\'||coalesce(e.gja2,'')||'\'||coalesce(e.gja3,'')||'\'||coalesce(e.gja4,'')||'\'||coalesce(e.gja5,'') end as rcsnameja,
    case e.hierarchylevel when 1 then coalesce(e.gzh1,'') when 2 then coalesce(e.gzh1,'')||'\'||coalesce(e.gzh2,'') when 3 then coalesce(e.gzh1,'')||'\'||coalesce(e.gzh2,'')||'\'||coalesce(e.gzh3,'') when 4 then coalesce(e.gzh1,'')||'\'||coalesce(e.gzh2,'')||'\'||coalesce(e.gzh3,'')||'\'||coalesce(e.gzh4,'') when 5 then coalesce(e.gzh1,'')||'\'||coalesce(e.gzh2,'')||'\'||coalesce(e.gzh3,'')||'\'||coalesce(e.gzh4,'')||'\'||coalesce(e.gzh5,'') end as rcsnamezh

  from (
         select
           distinct 1 as hierarchylevel,
                    r1.relationobjectid as parentid,
                    r1.relatedobjectid as childid,
                    g1.name as g1,
                    '' as g2,
                    '' as g3,
                    '' as g4,
                    '' as g5,
                    '' as g6,
                    gja1.name as gja1,
                    '' as gja2,
                    '' as gja3,
                    '' as gja4,
                    '' as gja5,
                    '' as gja6,
                    gzh1.name as gzh1,
                    '' as gzh2,
                    '' as gzh3,
                    '' as gzh4,
                    '' as gzh5,
                    '' as gzh6,
                    i1.identifiervalue as i1,
                    '' as i2,
                    '' as i3,
                    '' as i4,
                    '' as i5,
                    '' as i6
         from tmp_relationship r1
           join tmp_english g1 on r1.relatedobjectid=g1.key
           left join tmp_japanese gja1 on r1.relatedobjectid = gja1.key
           left join tmp_chinese gzh1 on r1.relatedobjectid = gzh1.key
           join tmp_identifiers i1 ON r1.relatedobjectid = i1.identifierentityid
         where r1.relatedobjecttypeid=404014 -- Currency
               and r1.relationobjecttypeid=1000228224 -- Heading
               and r1.relationshiprelationshiptypeid=1000228235 -- IsActiveForView relationship
       )e;


insert into trcsfanoutlookup_staging(classification,rcspermid,rcscode,rcsleaf,rcsleafml,rcsgenealogy,rcsname,rcsnameml)
  select 'currency', rcspermid,
    max(rcscode) as rcscode,
    max(cast(rcsleaf as varchar(500))) as rcsleaf,
    max(cast('ja '||rcsleafja||'|zh-Hans '||rcsleafzh as varchar(500))) as rcsleafml,
    cast(listagg(rcsgenealogy, '|') within group (order by rcsgenealogy asc) as varchar(500)) as rcsgenealogy,
    cast(listagg(rcsname, '|') within group (order by rcsgenealogy asc) as varchar(1000)) as rcsname,
    cast(listagg('ja '||rcsnameja||'|zh-Hans '||rcsnamezh, '|') within group (order by rcsgenealogy asc) as varchar(1000)) as rcsnameml
  from tmp_rcslookupstaging g
  group by rcspermid;


-- drop temp tables
--truncate table tmp_identifiers;
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_identifiers';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_identifiers';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/


--truncate table tmp_rcslookupstaging
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_rcslookupstaging';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_rcslookupstaging';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/

--truncate table tmp_english;
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_english';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_english';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/

--truncate table tmp_japanese;
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_japanese';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_japanese';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/

--truncate table tmp_chinese;
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_chinese';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_chinese';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/

--truncate table tmp_relationship;
BEGIN
  EXECUTE IMMEDIATE 'truncate table tmp_relationship';
  EXECUTE IMMEDIATE 'DROP TABLE tmp_relationship';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/


-- I add !!!!
--truncate table trcsfanoutlookup_staging;
BEGIN
  EXECUTE IMMEDIATE 'truncate table trcsfanoutlookup_staging';
  EXECUTE IMMEDIATE 'DROP TABLE trcsfanoutlookup_staging';
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -942 THEN
    RAISE;
  END IF;
END;
/

--BEGIN
--EXECUTE IMMEDIATE 'truncate table trcsfanoutlookup_staging';
--EXECUTE IMMEDIATE 'DROP TABLE trcsfanoutlookup_staging';
--EXCEPTION
--WHEN OTHERS THEN
--IF SQLCODE != -942 THEN
--    RAISE;
--  END IF;
--END;
--/





--drop temp tables
--drop table tmp_rcslookupstaging;
-- drop if exists tmp_english;
-- drop if exists tmp_japanese;
--drop if exists tmp_chinese;
-- drop if exists tmp_identifiers;
-- drop if exists tmp_relationship;