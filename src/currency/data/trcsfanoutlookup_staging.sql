
ALTER SESSION SET CURRENT_SCHEMA = CCMETADATA2SDI;

-- insert into trcsfanoutlookup_staging(classification,rcspermid,rcscode,rcsleaf,rcsleafml,rcsgenealogy,rcsname,rcsnameml)




--
-- select 'currency', rcspermid,
-- max(rcscode) as rcscode,
-- max(cast(rcsleaf as varchar(500))) as rcsleaf,
-- max(cast('ja '+rcsleafja+'|zh-Hans '+rcsleafzh as varchar(500))) as rcsleafml,
-- cast(listagg(rcsgenealogy, '|') within group (order by rcsgenealogy asc) as varchar(500)) as rcsgenealogy,
-- cast(listagg(rcsname, '|') within group (order by rcsgenealogy asc) as varchar(1000)) as rcsname,
-- cast(listagg('ja '||rcsnameja||'|zh-Hans '||rcsnamezh, '|') within group (order by rcsgenealogy asc) as varchar(1000)) as rcsnameml
-- from tmp_rcslookupstaging g
-- group by rcspermid;