
--     CHANGE

-- OLD                       NEW
-- currencyid                  CURRENCYID
-- currencyname                CURRENCYNAME
-- currencynameeffectivefrom   EFFECTIVEFROM
-- currencylanguageid          LANGUAGEID
-- currencynametype            NAMETYPEID

-- insert into tmp_english(key,name)

ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;

-- select c.currencyid,c.currencyname from (
 SELECT c.CURRENCYID, c.CURRENCYNAME from (

-- select max(currencynameeffectivefrom) as maxtimestamp, currencyid, currencylanguageid, currencynametype from currencyname
select max(EFFECTIVEFROM) as maxtimestamp, CURRENCYID, LANGUAGEID, NAMETYPEID from CURRENCYNAME

-- where currencylanguageid = 505126 and currencynametype = 404500
where LANGUAGEID = 505126 and NAMETYPEID = 404500

-- group by currencyid, currencylanguageid, currencynametype) a
group by CURRENCYID, LANGUAGEID, NAMETYPEID) a

-- join currencyname c on c.currencyid=a.currencyid and c.currencylanguageid=a.currencylanguageid
join CURRENCYNAME c on c.CURRENCYID=a.CURRENCYID and c.LANGUAGEID=a.LANGUAGEID

-- and c.currencynametype=a.currencynametype and c.currencynameeffectivefrom = a.maxtimestamp;
and c.NAMETYPEID=a.NAMETYPEID and c.EFFECTIVEFROM = a.maxtimestamp;