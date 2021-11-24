show databases;
create database wallet;
 
use wallet;
show tables;

create table account_users(accno Integer primary key auto_increment ,
aname varchar(20), userid varchar(25) unique,
pass varchar(15), balance Integer, check(balance>=0));
ALTER TABLE account_users AUTO_INCREMENT=1000000000;


create table transactions( tid int(10) primary key auto_increment, type_of_transaction varchar(10), 
c_userid varchar(20), d_userid varchar(20), amount int(10), time_of_transaction datetime, 
foreign key(d_userid) references account_users(userid), 
foreign key(c_userid) references account_users(userid));


select * from account_users; 
select * from transactions;
