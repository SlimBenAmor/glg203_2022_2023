
delete from customer;
delete from sequence_id where table_name = 'customer';

insert into sequence_id values ('customer', 2);

insert into customer 
    (id, firstname, lastname, telephone, email,
     street1, street2, city, state, zipcode, country) 
    values ('1', 'firstname1', 'lastname1', 'telephone1', 'email1', 
        'street1_1', 'street2_1', 'city1', 'state1', 'zipcode1', 'country1');

insert into customer (id, firstname, lastname, telephone, email, 
    street1, street2, city, state, zipcode, country) 
    values ('2', 'firstname2', 'lastname2', 'telephone2', 'email2',
     'street1_2', 'street2_2', 'city2', 'state2', 'zipcode2', 'country2');


