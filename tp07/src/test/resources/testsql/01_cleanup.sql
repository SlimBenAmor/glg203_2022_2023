
-- We should probably avoid duplicating all those data.
-- A temporary solution would be to have a script shell for this.
-- but in the next iterations of the software, we will move toward 
-- a systematic use of flyway. 
-- besides, much of the data in the original SQL files don't belong there.
-- In an actual application, the very specific data about the available items
-- won't be hardcoded for instance.

-- Cleanup
DELETE FROM customer;
DELETE FROM item;
DELETE FROM product;
DELETE FROM category;
DELETE FROM sequence_id;

