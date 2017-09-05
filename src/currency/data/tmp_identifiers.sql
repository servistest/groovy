
--                    Change

-- old                 new
-- IDENTIFIER
--           identifierentityid          ENTITYID
--           identifiervalue             VALUE
--           identifieridentifiertypeid  VALUETYPEID

--     RelationShip
--     old                                         new
-- relatedobjectid                             RELATIONOBJECTID
-- relationshiprelationshiptypeid              RELATIONSHIPTYPEID

-- work
-- select ENTITYID
-- from IDENTIFIER
-- where VALUETYPEID = 320008 --RCSPermId





--work
-- SELECT RELATIONSHIPID
-- FROM METADATARELATIONSHIP
-- WHERE RELATIONOBJECTID = 404303

-- work
-- SELECT RELATIONSHIPTYPEID
-- FROM METADATARELATIONSHIP
-- WHERE RELATIONSHIPTYPEID = 310129

-- work
-- SELECT RELATIONSHIPID
-- FROM METADATARELATIONSHIP
-- WHERE RELATIONOBJECTID = 1001046648



-- SELECT EFFECTIVETO
-- FROM METADATARELATIONSHIP
-- WHERE TO_CHAR(EFFECTIVETO, 'DD-MM-RRRR') = '31-12-9999'

-- 9999-12-31 00:00:00.000000

-- select identifierentityid,identifiervalue

ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;

select ENTITYID,VALUE
from IDENTIFIER i
  --join relationship r on r.relatedobjectid = i.identifierentityid and r.relationobjectid = 404303 and r.relationshiprelationshiptypeid=310129
  join METADATARELATIONSHIP r on r.RELATEDOBJECTID= i.ENTITYID and r.RELATIONOBJECTID = 404303 and r.RELATIONSHIPTYPEID=310129
                                 --                                  and  r.EFFECTIVETO IS NULL
                                 and TO_CHAR(r.EFFECTIVETO, 'DD-MM-RRRR') = '31-12-9999'
-- where identifieridentifiertypeid = 320008 --RCSPermId
where VALUETYPEID = 320008 --RCSPermId

      --       and i.identifierentityid not in (select distinct(relatedobjectid) from relationship where relationobjectid = 1001046648) -- Exclude all direct children of M:1
      and i.ENTITYID not in (select distinct(RELATIONOBJECTID) from METADATARELATIONSHIP where RELATIONOBJECTID = 1001046648) -- Exclude all direct children of M:1
      --       and i.identifiervalue not like 'menu%'
      and i.VALUE not like 'menu%'
      --       and identifiervalue not in ('E:1','E:10','E:11','E:12','E:13','E:15','E:18','E:19','E:1A','E:1B','E:1C','E:1D','E:1E','E:1F','E:1G','E:1H','E:1I','E:1J','E:1K','E:1L','E:1M','E:1N','E:1P','E:1Q','E:1R','E:1S','E:1T','E:1U','E:1V','E:1W','E:1X','E:1Y','E:1Z','E:2','E:20','E:21','E:22','E:23','E:24','E:25','E:26','E:27','E:28','E:29','E:2A','E:2B','E:2C','E:2D','E:2E','E:2F','E:2G','E:2H','E:2I','E:2J','E:2K','E:2L','E:2M','E:2N','E:2P','E:2Q','E:2R','E:2S','E:2T','E:2U','E:2V','E:2W','E:2X','E:2Y','E:2Z','E:3','E:30','E:31','E:32','E:33','E:34','E:35','E:36','E:37','E:38','E:39','E:3A','E:3B','E:3C','E:3D','E:3E','E:3F','E:3G','E:3H','E:3I','E:3J','E:3K','E:3L','E:3M','E:3N','E:3P','E:3Q','E:3R','E:3S','E:3Y','E:4','E:40','E:42','E:44','E:49','E:4A','E:4B','E:4C','E:4D','E:4E','E:4G','E:4H','E:4I','E:4J','E:4K','E:4L','E:4M','E:4N','E:4P','E:4Q','E:4R','E:4S','E:4T','E:4U','E:4V','E:4W','E:5','E:50','E:51','E:52','E:53','E:54','E:55','E:56','E:57','E:58','E:59','E:5A','E:5B','E:5C','E:5D','E:5E','E:5F','E:5G','E:5J','E:5K','E:6','E:7','E:8','E:9','E:A','E:B','E:C','E:D','E:E','E:F','E:G','E:H','E:I','E:J','E:K','E:L','E:M','E:N','E:P','E:Q','E:R','M:1EN','M:1EQ','M:1ER','M:1ES','M:AA','M:AB','M:AC','M:CN','M:D8','M:MB','cptRel:dependencyOf','cptRel:equivalentTo','cptRel:hasContentLanguage','cptRel:hasCurrency','cptRel:hasGenre','cptRel:hasMinorCurrency','cptRel:hasPrimaryCurrency','cptRel:hasSource','cptRel:hasSubject','cptRel:isInNewsService','cptRel:replacedBy','cptRel:requires','cptRel:useForSearch','cptType:1','cptType:10','cptType:11','cptType:12','cptType:13','cptType:14','cptType:15','cptType:16','cptType:17','cptType:18','cptType:19','cptType:2','cptType:20','cptType:21','cptType:22','cptType:23','cptType:24','cptType:25','cptType:26','cptType:27','cptType:28','cptType:29','cptType:3','cptType:30','cptType:31','cptType:32','cptType:33','cptType:34','cptType:35','cptType:36','cptType:4','cptType:5','cptType:6','cptType:7','cptType:8','cptType:9','dom:data','dom:edit','dom:news','geoProp:3','geoProp:5','geoProp:6','geoProp:7','geoProp:8','iptc:broader','iptc:related','iptc:sameAs','menu:1','menu:10','menu:11','menu:6','menu:7','menu:8','menu:9','prof:1','prof:10','prof:11','prof:12','prof:13','prof:14','prof:15','prof:16','prof:2','prof:3','prof:4','prof:5','prof:6','prof:7','prof:8','prof:9','scheme:A','scheme:B','scheme:BJ','scheme:BL','scheme:C','scheme:CO','scheme:CW','scheme:E','scheme:EIAF','scheme:ES','scheme:G','scheme:I','scheme:J','scheme:L','scheme:LCR','scheme:M','scheme:NP','scheme:NS','scheme:O','scheme:OT','scheme:P','scheme:R','scheme:RR','scheme:SCR','scheme:TR','scheme:U','scheme:cptRel','scheme:cptType','scheme:dom','scheme:geoProp','scheme:iptc','scheme:menu','scheme:prof','scheme:quoteType','scheme:rateType','scheme:scheme','scheme:screenGroup','scheme:screenType','scheme:sourceType','scheme:stg','stg:cancelled','stg:live','stg:notApplicable','stg:retired');
      and i.VALUE not in ('E:1','E:10','E:11','E:12','E:13','E:15','E:18','E:19','E:1A','E:1B','E:1C','E:1D','E:1E','E:1F','E:1G','E:1H','E:1I','E:1J','E:1K','E:1L','E:1M','E:1N','E:1P','E:1Q','E:1R','E:1S','E:1T','E:1U','E:1V','E:1W','E:1X','E:1Y','E:1Z','E:2','E:20','E:21','E:22','E:23','E:24','E:25','E:26','E:27','E:28','E:29','E:2A','E:2B','E:2C','E:2D','E:2E','E:2F','E:2G','E:2H','E:2I','E:2J','E:2K','E:2L','E:2M','E:2N','E:2P','E:2Q','E:2R','E:2S','E:2T','E:2U','E:2V','E:2W','E:2X','E:2Y','E:2Z','E:3','E:30','E:31','E:32','E:33','E:34','E:35','E:36','E:37','E:38','E:39','E:3A','E:3B','E:3C','E:3D','E:3E','E:3F','E:3G','E:3H','E:3I','E:3J','E:3K','E:3L','E:3M','E:3N','E:3P','E:3Q','E:3R','E:3S','E:3Y','E:4','E:40','E:42','E:44','E:49','E:4A','E:4B','E:4C','E:4D','E:4E','E:4G','E:4H','E:4I','E:4J','E:4K','E:4L','E:4M','E:4N','E:4P','E:4Q','E:4R','E:4S','E:4T','E:4U','E:4V','E:4W','E:5','E:50','E:51','E:52','E:53','E:54','E:55','E:56','E:57','E:58','E:59','E:5A','E:5B','E:5C','E:5D','E:5E','E:5F','E:5G','E:5J','E:5K','E:6','E:7','E:8','E:9','E:A','E:B','E:C','E:D','E:E','E:F','E:G','E:H','E:I','E:J','E:K','E:L','E:M','E:N','E:P','E:Q','E:R','M:1EN','M:1EQ','M:1ER','M:1ES','M:AA','M:AB','M:AC','M:CN','M:D8','M:MB','cptRel:dependencyOf','cptRel:equivalentTo','cptRel:hasContentLanguage','cptRel:hasCurrency','cptRel:hasGenre','cptRel:hasMinorCurrency','cptRel:hasPrimaryCurrency','cptRel:hasSource','cptRel:hasSubject','cptRel:isInNewsService','cptRel:replacedBy','cptRel:requires','cptRel:useForSearch','cptType:1','cptType:10','cptType:11','cptType:12','cptType:13','cptType:14','cptType:15','cptType:16','cptType:17','cptType:18','cptType:19','cptType:2','cptType:20','cptType:21','cptType:22','cptType:23','cptType:24','cptType:25','cptType:26','cptType:27','cptType:28','cptType:29','cptType:3','cptType:30','cptType:31','cptType:32','cptType:33','cptType:34','cptType:35','cptType:36','cptType:4','cptType:5','cptType:6','cptType:7','cptType:8','cptType:9','dom:data','dom:edit','dom:news','geoProp:3','geoProp:5','geoProp:6','geoProp:7','geoProp:8','iptc:broader','iptc:related','iptc:sameAs','menu:1','menu:10','menu:11','menu:6','menu:7','menu:8','menu:9','prof:1','prof:10','prof:11','prof:12','prof:13','prof:14','prof:15','prof:16','prof:2','prof:3','prof:4','prof:5','prof:6','prof:7','prof:8','prof:9','scheme:A','scheme:B','scheme:BJ','scheme:BL','scheme:C','scheme:CO','scheme:CW','scheme:E','scheme:EIAF','scheme:ES','scheme:G','scheme:I','scheme:J','scheme:L','scheme:LCR','scheme:M','scheme:NP','scheme:NS','scheme:O','scheme:OT','scheme:P','scheme:R','scheme:RR','scheme:SCR','scheme:TR','scheme:U','scheme:cptRel','scheme:cptType','scheme:dom','scheme:geoProp','scheme:iptc','scheme:menu','scheme:prof','scheme:quoteType','scheme:rateType','scheme:scheme','scheme:screenGroup','scheme:screenType','scheme:sourceType','scheme:stg','stg:cancelled','stg:live','stg:notApplicable','stg:retired');







