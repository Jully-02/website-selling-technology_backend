SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'token'
    AND TABLE_SCHEMA = 'Gizmos'
    AND COLUMN_NAME = 'refresh_token';

SET @alterStatement = IF(@columnCount = 0,
    'ALTER TABLE token ADD COLUMN refresh_token VARCHAR(255) DEFAULT '''';',
    '');
PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'token'
    AND TABLE_SCHEMA = 'Gizmos'
    AND COLUMN_NAME = 'refresh_expiration_date';

SET @alterStatement = IF(@columnCount = 0,
    'ALTER TABLE token ADD COLUMN refresh_expiration_date datetime;',
    '');
PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;