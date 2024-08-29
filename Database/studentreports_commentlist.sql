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
-- Table structure for table `commentlist`
--

DROP TABLE IF EXISTS `commentlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commentlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) NOT NULL,
  `type` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commentlist`
--

LOCK TABLES `commentlist` WRITE;
/*!40000 ALTER TABLE `commentlist` DISABLE KEYS */;
INSERT INTO `commentlist` VALUES (2,'Excellent performance','Teacher'),(3,'Brilliant work','Teacher'),(4,'A sterling effort for the term','Teacher'),(5,'Very good performance','Teacher'),(6,'Very good work','Teacher'),(7,'Should keep aiming high','Teacher'),(8,'A very pleasing achievement','Teacher'),(9,'Proficient work','Teacher'),(10,'A pleasing performance','Teacher'),(11,'Pleasing work','Teacher'),(12,'Should even aim higher','Teacher'),(13,'Good work','Teacher'),(14,'Satisfactory work','Teacher'),(15,'Average performance','Teacher'),(16,'Should put more effort','Teacher'),(17,'Competent performance','Teacher'),(18,'There is room for improvement','Teacher'),(19,'Has the potential to do better','Teacher'),(20,'Below average performance','Teacher'),(21,'A lot of progress has been noted this term. Student must keep working hard.','Principal'),(22,'Student worked very well this term. Student must maintain the momentum.','Principal'),(23,'A brilliant performance for the term. Student must maintain the high standard.','Principal'),(24,'Student has performed satisfactorily this term. He/she must keep working hard.','Principal'),(25,'Student’s performance will improve with better focus on his/her schoolwork.','Principal'),(26,'A pleasing performance for the term. Student is encouraged to keep working hard.','Principal'),(27,'A very fair attempt in some subjects this term. Student is advised to revise work in all subject areas regularly.','Principal'),(28,'Should aim higher','Teacher'),(29,'Should prepare well for examinations','Teacher'),(30,'Can do better with more effort','Teacher'),(31,'Should work hard','Teacher'),(32,'More effort is needed','Teacher'),(33,'More effort is needed in studying','Teacher'),(34,'More effort is needed in polishing up diagrams','Teacher'),(35,'More effort is needed in interpreting diagrams','Teacher'),(36,'More effort is needed in reading diagrams','Teacher'),(37,'Below average performance. Should improve spelling','Teacher'),(38,'Encouraged to participate in class discussions','Teacher'),(39,'Needs to work harder','Teacher'),(40,'Needs help to improve','Teacher'),(41,'Far below average','Teacher'),(42,'A worrying performance','Teacher'),(43,'Should revise school work regularly','Teacher'),(44,'Needs a lot of help','Teacher'),(45,'Should master basic concepts','Teacher');
/*!40000 ALTER TABLE `commentlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26 12:02:03
