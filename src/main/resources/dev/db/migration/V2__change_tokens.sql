-- add is_mobile column if not exists
SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'token'
    AND TABLE_SCHEMA = 'Gizmos'
    AND COLUMN_NAME = 'is_mobile';

-- If the column does not exist, add it
SET @alterStatement = IF(@columnCount = 0,
    'ALTER TABLE token ADD COLUMN is_mobile TINYINT(1) DEFAULT 0;',
    '');

-- Execute the ALTER TABLE statement if necessary
PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;