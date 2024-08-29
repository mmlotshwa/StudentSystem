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
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `subjectid` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `department` varchar(50) NOT NULL,
  PRIMARY KEY (`subjectid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (417,'Information Technology (ICT)','Science'),(445,'Design and Technology','Practical'),(450,'Business Studies','Commercial'),(452,'Accounting','Commercial'),(453,'Development Studies','Commercial'),(460,'Geography','Art'),(470,'History','Art'),(478,'Computer Science','Science'),(480,'Guidance and Counselling','Art'),(486,'Literature in English','Art'),(500,'First Language English','Art'),(520,'French','Art'),(580,'Mathematics','Science'),(581,'Mathematics Core','Science'),(600,'Agriculture','Practical'),(606,'Additional Mathematics','Science'),(610,'Biology','Science'),(620,'Chemistry','Science'),(625,'Physics','Science'),(648,'Food and Nutrition','Practical'),(652,'Physical Science','Science'),(653,'Combined Science','Science'),(654,'Co-ordinated Sciences (Double Award)','Science'),(680,'Environmental Management','Art'),(1000,'I. C. D. L.','Science'),(1123,'English Language','Art'),(2251,'Sociology','Art'),(3158,'Setswana','Art'),(5801,'Mathematics (Core)','Science'),(5802,'Mathematics (Extended)','Science'),(6101,'Biology (Core)','Science'),(6102,'Biology (Extended)','Science'),(6201,'Chemistry (Core)','Science'),(6202,'Chemistry (Extended)','Science'),(6251,'Physics (Core)','Science'),(6252,'Physics (Extended)','Science'),(7100,'Commerce','Alevel'),(9093,'English Language','Alevel'),(9336,'Food Studies','Alevel'),(9389,'History','Alevel'),(9609,'Business','Alevel'),(9618,'Computer Science','Alevel'),(9626,'Information Technology','Alevel'),(9695,'Literature in English','Alevel'),(9696,'Geography','Alevel'),(9700,'Biology','Alevel'),(9701,'Chemistry','Alevel'),(9702,'Physics','Alevel'),(9706,'Accounting','Alevel'),(9708,'Economics','Alevel'),(9709,'Mathematics','Alevel');
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26 12:02:02
