drop table if exists dump;

create table dump(data json);

copy dump(data) 
from '$PWD/export-2004-2016.json' 
csv quote e'\x01' delimiter e'\x02';
