create table article (
     id           serial primary key,
     title        text not null unique check (trim(title)  ''),
     description  text not null default '',
     body         text not null default '',
     created      timestamp not null default now(),
     updated      timestamp not null default now(),
     published    timestamp null default null
);
insert into article (title, description, body) values
     ('Article 1', 'My first article',
      '<p>Paragraph 1 in article 1</p><p>Another paragraph</p>'),
     ('Article 2', 'My second article',
      '<p>This article also has a paragraph</p><p>And another</p>');