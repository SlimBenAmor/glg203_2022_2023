delete from customer;
delete from sequence_id where table_name = 'customer';
insert into sequence_id values ('customer', 0);

