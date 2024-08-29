-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: studentreports
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

USE highschool;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `schedules`
--

DROP TABLE IF EXISTS `schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedules` (
  `subjectid` int(11) NOT NULL,
  `optionlevel` int(11) NOT NULL,
  `optiontype` varchar(25) NOT NULL,
  `scheduleid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`scheduleid`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedules`
--

LOCK TABLES `schedules` WRITE;
/*!40000 ALTER TABLE `schedules` DISABLE KEYS */;
INSERT INTO `schedules` VALUES (417,1,'General',1),(417,2,'General',2),(417,3,'General',3),(445,1,'Practical',4),(445,2,'Practicals',5),(445,3,'Practicals',6),(450,1,'General',7),(450,2,'Commercials',8),(450,3,'Commercials',9),(452,1,'General',10),(452,2,'Commercials',11),(460,1,'General',12),(460,2,'Arts',13),(460,3,'Arts',14),(470,1,'General',15),(470,2,'Arts',16),(470,3,'Arts',17),(486,2,'Arts',18),(486,3,'Arts',19),(500,1,'General',20),(500,2,'General',21),(500,3,'General',22),(520,1,'Language',23),(520,2,'Language',24),(520,3,'Language',25),(600,1,'Practical',26),(600,2,'Practicals',27),(600,3,'Practicals',28),(606,2,'Optional',29),(606,3,'Optional',30),(648,1,'Practical',31),(648,2,'Practicals',32),(653,1,'General',33),(680,3,'Optional',34),(1123,3,'Optional',35),(3158,1,'Language',36),(3158,2,'Language',37),(3158,3,'Language',38),(5801,3,'General',39),(5802,1,'General',40),(5802,2,'General',41),(5802,3,'General',42),(6101,3,'Sciences',43),(6102,2,'Sciences',44),(6102,3,'Sciences',45),(6201,3,'Sciences',46),(6202,2,'Sciences',47),(6202,3,'Sciences',48),(6251,3,'Sciences',49),(6252,2,'Sciences',50),(6252,3,'Sciences',51),(7100,5,'Alevel',52),(9093,5,'ALevel',53),(9389,5,'ALevel',54),(9609,5,'ALevel',55),(9618,5,'ALevel',56),(9626,5,'ALevel',57),(9695,5,'ALevel',58),(9696,5,'ALevel',59),(9700,5,'ALevel',60),(9701,5,'ALevel',61),(9702,5,'ALevel',62),(9706,5,'ALevel',63),(9708,5,'ALevel',64),(9709,5,'ALevel',65),(648,3,'Practicals',66);
/*!40000 ALTER TABLE `schedules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26 12:02:01
