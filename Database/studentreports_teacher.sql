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
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `teacherid` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(15) DEFAULT NULL,
  `firstname` varchar(25) DEFAULT NULL,
  `surname` varchar(25) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `department` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`teacherid`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'Mr','D. Z.','Matewe','M',NULL,'Arts'),(2,'Mr','A.','Mutsando','M',NULL,'Practicals'),(3,'Mrs','S.','Chamunorwa','F',NULL,'Practicals'),(4,'Mrs','C.','Chidamwoyo','F',NULL,'Practicals'),(5,'Mrs','D.','Mbizvo','F',NULL,'Arts'),(6,'Mr','V.','Moyo','M',NULL,'Commercials'),(7,'Mr','M.','Mlotshwa','M',NULL,'Science'),(8,'Mrs','S.','Gonah','F',NULL,'Commercials'),(9,'Mrs','T.','Zenda','F',NULL,'Arts'),(10,'Mr','C.','Ncube','M',NULL,NULL),(11,'Mrs','T.','Matewe','F',NULL,'Science'),(12,'Mr','T. J.','Cambo','M',NULL,NULL),(13,'Mr','M. S.','Kache','M',NULL,NULL),(14,'Miss','R. F.','Jonato','F',NULL,'Arts'),(15,'Mr','E.','Dube','M',NULL,NULL),(16,'Mr','I.','Mhembere','M',NULL,NULL),(17,'Mr','O.','Gaolaolwe','M',NULL,'Science'),(18,'Mr','W.','Mwashita','M',NULL,NULL),(19,'Mrs','L.','Dube','F',NULL,NULL),(20,'Mrs','Z.','Moyo','F',NULL,NULL),(21,'Mr','C','Mhazo','M',NULL,NULL),(22,'Miss','G.','Tshegisi','F',NULL,'Arts'),(23,'Mr','D.','Matimbira','M',NULL,NULL),(24,'Miss','M.','Modimoeng','F',NULL,NULL),(25,'Miss','A','Makama','F',NULL,NULL),(26,'Miss','T.','Rabana','F',NULL,NULL),(27,'Miss','T.','Moiteelasilo','F',NULL,NULL),(28,'Miss','S.','Ndlovu','F',NULL,NULL),(29,'Miss','G.','Masusu','F','','Science'),(30,'Mr','O.','Gaolaolwe','M','','Science'),(31,'Miss','G.','Tshegisi','F','','Arts');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
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
