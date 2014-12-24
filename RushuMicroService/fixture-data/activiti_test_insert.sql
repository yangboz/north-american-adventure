INSERT INTO `companies` (`id`, `created`, `date`, `updated`, `business_key`, `domain`, `email`, `name`, `version`) VALUES
(1, '2014-12-18 00:00:00', '2014-12-18 00:00:00', '2014-12-18 00:00:00', 'reimbursementRequest', 'rushucloud.com', 'contact@rushucloud.com', 'RUSHUCLOUD.COM', 0),
(2, '2014-12-18 00:00:00', '2014-12-18 00:00:00', '2014-12-18 00:00:00', 'reimbursementRequest', 'localhost.com', 'local@host.com', 'LOCALHOST.COM', 0);

INSERT INTO `items` (`id`, `created`, `date`, `updated`, `owner`, `amount`, `category`, `invoices`, `name`, `notes`, `participant_ids`, `type`, `vendors`, `used`) VALUES
(1, '2014-12-19 16:24:28', '2014-12-19 08:00:00', '2014-12-19 16:24:28', 'employee1', 99, '1', '1', '测试条目000', '说明。。。', NULL, 'CostComsumed', '1', b'0');