-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.19 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 goc 的数据库结构
CREATE DATABASE IF NOT EXISTS `goc` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `goc`;

-- 导出  表 goc.announcement 结构
CREATE TABLE IF NOT EXISTS `announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `start_timestamp` int(11) NOT NULL,
  `end_timestamp` int(11) NOT NULL,
  `lastmod_timestamp` int(11) NOT NULL,
  `deleted` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index1` (`start_timestamp`,`end_timestamp`,`deleted`),
  KEY `index2` (`deleted`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.announcement 的数据：1 rows
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
INSERT INTO `announcement` (`id`, `title`, `content`, `start_timestamp`, `end_timestamp`, `lastmod_timestamp`, `deleted`) VALUES
	(1, '公告1', '莫扎特說過一句富有哲理的話，誰和我一樣用功，誰就會和我一樣成功。這似乎解答了我的疑惑。科學和人文誰更有意義，發生了會如何，不發生又會如何。一般來講，我們都必須務必慎重的考慮考慮。馬克思曾經提到過，一切節省，歸根到底都歸結為時間的節省。這似乎解答了我的疑惑。', 0, 2099999999, 1608113061, 0);
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;

-- 导出  表 goc.game 结构
CREATE TABLE IF NOT EXISTS `game` (
  `id` int(11) NOT NULL,
  `name_zh` varchar(50) NOT NULL DEFAULT '',
  `name_en` varchar(50) NOT NULL DEFAULT '',
  `desc_zh` text NOT NULL,
  `desc_en` text NOT NULL,
  `lang` enum('Zh','En') NOT NULL,
  `level_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.game 的数据：1 rows
/*!40000 ALTER TABLE `game` DISABLE KEYS */;
INSERT INTO `game` (`id`, `name_zh`, `name_en`, `desc_zh`, `desc_en`, `lang`, `level_id`) VALUES
	(1, '遊戲 1', 'Game 1', '遊戲 1', 'Game 1', 'Zh', 1);
/*!40000 ALTER TABLE `game` ENABLE KEYS */;

-- 导出  表 goc.game_log 结构
CREATE TABLE IF NOT EXISTS `game_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `status` enum('PLAYING','SURRENDERED','FINISHED') NOT NULL,
  `score` float NOT NULL,
  `timestamp` int(11) NOT NULL,
  `extra` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index` (`user_id`,`game_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.game_log 的数据：1 rows
/*!40000 ALTER TABLE `game_log` DISABLE KEYS */;
INSERT INTO `game_log` (`id`, `user_id`, `game_id`, `status`, `score`, `timestamp`, `extra`) VALUES
	(1, 1, 1, 'FINISHED', 100, 1608609011, ' '),
	(2, 1, 1, 'FINISHED', 25, 1608613344, ' ');
/*!40000 ALTER TABLE `game_log` ENABLE KEYS */;

-- 导出  表 goc.leaderboard 结构
CREATE TABLE IF NOT EXISTS `leaderboard` (
  `id` int(11) NOT NULL,
  `type` enum('PERSONAL SCORE','SCHOOL SCORE') NOT NULL,
  `score` double NOT NULL DEFAULT '0',
  `timestamp` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `school_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index1` (`game_id`,`type`),
  KEY `index2` (`game_id`,`school_id`,`type`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.leaderboard 的数据：2 rows
/*!40000 ALTER TABLE `leaderboard` DISABLE KEYS */;
INSERT INTO `leaderboard` (`id`, `type`, `score`, `timestamp`, `game_id`, `school_id`, `user_id`) VALUES
	(1, 'PERSONAL SCORE', 125, 1608617560, 1, 1, 1),
	(2, 'SCHOOL SCORE', 125, 1608617560, 1, 1, NULL);
/*!40000 ALTER TABLE `leaderboard` ENABLE KEYS */;

-- 导出  表 goc.level 结构
CREATE TABLE IF NOT EXISTS `level` (
  `id` int(11) NOT NULL,
  `name_zh` varchar(50) NOT NULL,
  `name_en` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.level 的数据：2 rows
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` (`id`, `name_zh`, `name_en`) VALUES
	(1, '普通', 'Normal'),
	(2, '困難', 'Hard');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;

-- 导出  表 goc.news 结构
CREATE TABLE IF NOT EXISTS `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `status` enum('SHOW','HIDDEN','DELETED') NOT NULL,
  `release_timestamp` int(11) NOT NULL,
  `add_timestamp` int(11) NOT NULL,
  `lastmod_timestamp` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `school_id` int(11) NOT NULL,
  `lang` enum('Zh','En') NOT NULL,
  `order` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index1` (`school_id`,`status`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.news 的数据：1 rows
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` (`id`, `title`, `content`, `status`, `release_timestamp`, `add_timestamp`, `lastmod_timestamp`, `user_id`, `school_id`, `lang`, `order`) VALUES
	(1, '佐治', '本人也是經過了深思熟慮，在每個日日夜夜思考這個問題。文森特·皮爾曾經說過，改變你的想法，你就改變了自己的世界。這啟發了我， 這種事實對本人來說意義重大，相信對這個世界也是有一定意義的。', 'SHOW', 1608105572, 1608105572, 1608105572, 1, 1, 'Zh', 1);
/*!40000 ALTER TABLE `news` ENABLE KEYS */;

-- 导出  表 goc.school 结构
CREATE TABLE IF NOT EXISTS `school` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name_zh` varchar(255) DEFAULT NULL,
  `name_en` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.school 的数据：1 rows
/*!40000 ALTER TABLE `school` DISABLE KEYS */;
INSERT INTO `school` (`id`, `name_zh`, `name_en`) VALUES
	(1, '八達大學', 'The University of Octopus');
/*!40000 ALTER TABLE `school` ENABLE KEYS */;

-- 导出  表 goc.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name_zh` varchar(255) NOT NULL,
  `name_en` varchar(255) NOT NULL,
  `avatar_index` int(11) NOT NULL,
  `avatar_image_url` text NOT NULL,
  `register_timestamp` int(11) NOT NULL,
  `lastlogin_timestamp` int(11) NOT NULL,
  `school_id` int(11) NOT NULL,
  `admin_level` int(11) NOT NULL,
  `deleted` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_id` (`login_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 正在导出表  goc.user 的数据：1 rows
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `login_id`, `password`, `name_zh`, `name_en`, `avatar_index`, `avatar_image_url`, `register_timestamp`, `lastlogin_timestamp`, `school_id`, `admin_level`, `deleted`) VALUES
	(1, 'octopus', '$argon2id$v=19$m=16,t=2,p=1$cmI0aU5QbkluOUZaMnc1bw$Q29rLlmb5nJKP71v1B1YLA', '八達', 'octopus', 1, '', 1607583695, 1608615745, 1, 2, 0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
