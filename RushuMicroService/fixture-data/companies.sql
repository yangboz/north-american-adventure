-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: 2014-12-19 02:46:16
-- 服务器版本： 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `activiti_test`
--

-- --------------------------------------------------------

--
-- 表的结构 `companies`
--

CREATE TABLE IF NOT EXISTS `companies` (
`id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `date` datetime DEFAULT NULL,
  `updated` datetime NOT NULL,
  `business_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `domain` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `companies`
--

INSERT INTO `companies` (`id`, `created`, `date`, `updated`, `business_key`, `domain`, `email`, `name`, `version`) VALUES
(1, '2014-12-18 00:00:00', '2014-12-18 00:00:00', '2014-12-18 00:00:00', 'reimbursementRequest', 'rushucloud.com', 'contact@rushucloud.com', 'RUSHUCLOUD.COM', 0),
(2, '2014-12-18 00:00:00', '2014-12-18 00:00:00', '2014-12-18 00:00:00', 'reimbursementRequest', 'localhost.com', 'local@host.com', 'LOCALHOST.COM', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `companies`
--
ALTER TABLE `companies`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `companies`
--
ALTER TABLE `companies`
MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
