
ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;


-- work
-- SELECT RELATIONSHIPID
-- FROM METADATARELATIONSHIP
-- WHERE RELATEDOBJECTID = 404014

-- work
-- SELECT RELATIONSHIPID
-- FROM METADATARELATIONSHIP
-- WHERE RELATEDOBJECTTYPEID = 404014



-- insert into tmp_relationship(relatedobjectid, relationobjectid, relatedobjecttypeid, relationobjecttypeid, relationshiprelationshiptypeid)


-- relatedobjectid                             RELATEDOBJECTID
-- relationshiprelationshiptypeid              RELATIONSHIPTYPEID
-- relatedobjecttypeid                         RELATEDOBJECTTYPEID
-- relationshiprelationshiptypeid              RELATIONSHIPTYPEID
-- relationshipeffectiveto                     EFFECTIVETO

--   select
     SELECT

--     relatedobjectid,
       RELATEDOBJECTID,

--     relationobjectid,
       RELATIONOBJECTID,

--     relatedobjecttypeid,
       RELATEDOBJECTTYPEID,

--     relationobjecttypeid,
       RELATIONOBJECTTYPEID,


--     relationshiprelationshiptypeid
         RELATIONSHIPTYPEID

--   from relationship
     FROM METADATARELATIONSHIP

--   where relatedobjecttypeid=404014
     WHERE RELATEDOBJECTTYPEID=404014

--         and relationshipeffectiveto is null;
     AND TO_CHAR(EFFECTIVETO, 'DD-MM-RRRR') = '31-12-9999'
