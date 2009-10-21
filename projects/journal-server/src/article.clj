create table article (
     id           serial primary key,
     title        text not null unique check (trim(title)  ''),
     description  text not null default '',
     body         text not null default '',
     created      timestamp not null default now(),
     updated      timestamp not null default now(),
     published    timestamp null default null
);