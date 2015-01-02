INSERT INTO `companies` (`id`, `created`, `date`, `updated`, `business_key`, `domain`, `email`, `name`, `version`) VALUES
(1, '2014-12-18 00:00:00', '2014-12-18 00:00:00', '2014-12-18 00:00:00', 'reimbursementRequest', 'www1.rushucloud.com', 'dev@dev.com', 'DEV.COM', 0),
(2, '2015-01-01 00:00:00', '2015-01-01 00:00:00', '2015-01-01 00:00:00', 'reimbursementRequest', 'www2.rushucloud.com', 'test@test.com', 'TEST.COM', 0);

INSERT INTO `items` (`id`, `created`, `date`, `updated`, `owner`, `amount`, `category`, `invoices`, `name`, `notes`, `participant_ids`, `type`, `vendors`, `used`) VALUES
(1, '2014-12-19 16:24:28', '2014-12-19 08:00:00', '2014-12-19 16:24:28', 'employee1', 99, '1', '1', '测试条目000', '说明。。。', NULL, 'CostComsumed', '1', b'0');

INSERT INTO `categories` (`id`, `created`, `date`, `updated`, `icon`, `name`, `parent_id`) VALUES
(1, '2014-12-26 12:23:32', NULL, '2014-12-26 12:23:32', '00', '总分类', NULL),
(2, '2014-12-26 12:25:36', NULL, '2014-12-26 12:25:36', '01', '机票', 1),
(3, '2014-12-26 13:55:39', NULL, '2014-12-26 13:55:39', '02', '话费', 1);