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
-- Temporary view structure for view `subjectschedules`
--

DROP TABLE IF EXISTS `subjectschedules`;
/*!50001 DROP VIEW IF EXISTS `subjectschedules`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `subjectschedules` AS SELECT 
 1 AS `ID`,
 1 AS `subjectid`,
 1 AS `gnum`,
 1 AS `grade`,
 1 AS `optionlevel`,
 1 AS `optiontype`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `studentsummaries`
--

DROP TABLE IF EXISTS `studentsummaries`;
/*!50001 DROP VIEW IF EXISTS `studentsummaries`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `studentsummaries` AS SELECT 
 1 AS `ID`,
 1 AS `studentid`,
 1 AS `tyear`,
 1 AS `term`,
 1 AS `average`,
 1 AS `grade`,
 1 AS `ranked`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `subjectsummaries`
--

DROP TABLE IF EXISTS `subjectsummaries`;
/*!50001 DROP VIEW IF EXISTS `subjectsummaries`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `subjectsummaries` AS SELECT 
 1 AS `ID`,
 1 AS `subjectid`,
 1 AS `tyear`,
 1 AS `term`,
 1 AS `average`,
 1 AS `grade`,
 1 AS `ranked`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `registrationdetails`
--

DROP TABLE IF EXISTS `registrationdetails`;
/*!50001 DROP VIEW IF EXISTS `registrationdetails`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `registrationdetails` AS SELECT 
 1 AS `studentid`,
 1 AS `fullname`,
 1 AS `subjects`,
 1 AS `exammark`,
 1 AS `tavemark`,
 1 AS `comments`,
 1 AS `teacherid`,
 1 AS `teachername`,
 1 AS `grade`,
 1 AS `tyear`,
 1 AS `term`,
 1 AS `registrationid`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `gradessummaries`
--

DROP TABLE IF EXISTS `gradessummaries`;
/*!50001 DROP VIEW IF EXISTS `gradessummaries`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `gradessummaries` AS SELECT 
 1 AS `ID`,
 1 AS `grade`,
 1 AS `tyear`,
 1 AS `term`,
 1 AS `average`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `subjectschedules`
--

/*!50001 DROP VIEW IF EXISTS `subjectschedules`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `subjectschedules` AS select row_number() over ( order by `s`.`optionlevel`) AS `ID`,`s`.`subjectid` AS `subjectid`,left(`g`.`grade`,1) AS `gnum`,`g`.`grade` AS `grade`,`s`.`optionlevel` AS `optionlevel`,`s`.`optiontype` AS `optiontype` from (`schedules` `s` join `gradename` `g`) where left(`g`.`grade`,1) = `s`.`optionlevel` or `s`.`optionlevel` = 3 and left(`g`.`grade`,1) = 4 or `s`.`optionlevel` = 5 and left(`g`.`grade`,1) = 'A' order by `s`.`optionlevel` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `studentsummaries`
--

/*!50001 DROP VIEW IF EXISTS `studentsummaries`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `studentsummaries` AS select `ranking`.`ID` AS `ID`,`ranking`.`studentid` AS `studentid`,`ranking`.`tyear` AS `tyear`,`ranking`.`term` AS `term`,`ranking`.`average` AS `average`,`ranking`.`grade` AS `grade`,`ranking`.`ranked` AS `ranked` from (select `registration`.`studentid` AS `studentid`,`registration`.`tyear` AS `tyear`,`registration`.`term` AS `term`,avg(`registration`.`exammark`) AS `average`,`registration`.`grade` AS `grade`,row_number() over ( order by `registration`.`tyear`,`registration`.`term`,`registration`.`grade`,avg(`registration`.`exammark`) desc) AS `ID`,row_number() over ( partition by `registration`.`grade`,`registration`.`tyear`,`registration`.`term` order by avg(`registration`.`exammark`) desc) AS `ranked` from `registration` group by `registration`.`studentid`,`registration`.`grade`,`registration`.`tyear`,`registration`.`term` order by `registration`.`tyear`,`registration`.`term`,`registration`.`grade`,avg(`registration`.`exammark`) desc) `ranking` where `ranking`.`ranked` <= 5 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `subjectsummaries`
--

/*!50001 DROP VIEW IF EXISTS `subjectsummaries`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `subjectsummaries` AS select `ranking`.`ID` AS `ID`,`ranking`.`subjectid` AS `subjectid`,`ranking`.`tyear` AS `tyear`,`ranking`.`term` AS `term`,`ranking`.`average` AS `average`,`ranking`.`grade` AS `grade`,`ranking`.`ranked` AS `ranked` from (select `registration`.`subjectid` AS `subjectid`,`registration`.`tyear` AS `tyear`,`registration`.`term` AS `term`,avg(`registration`.`exammark`) AS `average`,`registration`.`grade` AS `grade`,row_number() over ( order by `registration`.`tyear`,`registration`.`term`,`registration`.`grade`,avg(`registration`.`exammark`) desc) AS `ID`,row_number() over ( partition by `registration`.`grade`,`registration`.`tyear`,`registration`.`term` order by avg(`registration`.`exammark`) desc) AS `ranked` from `registration` group by `registration`.`subjectid`,`registration`.`grade`,`registration`.`tyear`,`registration`.`term` order by `registration`.`tyear`,`registration`.`term`,`registration`.`grade`,avg(`registration`.`exammark`) desc) `ranking` where `ranking`.`ranked` <= 5 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `registrationdetails`
--

/*!50001 DROP VIEW IF EXISTS `registrationdetails`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `registrationdetails` AS select `r`.`studentid` AS `studentid`,concat(`s`.`surname`,', ',`s`.`firstname`) AS `fullname`,concat('[',lpad(`r`.`subjectid`,4,0),']',' ',`sb`.`title`) AS `subjects`,`r`.`exammark` AS `exammark`,`r`.`tavemark` AS `tavemark`,`r`.`comments` AS `comments`,`t`.`teacherid` AS `teacherid`,concat(`t`.`surname`,', ',`t`.`firstname`) AS `teachername`,`r`.`grade` AS `grade`,`r`.`tyear` AS `tyear`,`r`.`term` AS `term`,`r`.`registrationid` AS `registrationid` from ((((`registration` `r` join `student` `s` on(`r`.`studentid` = `s`.`studentid`)) join `subject` `sb` on(`r`.`subjectid` = `sb`.`subjectid`)) join `subjectteacher` `st` on(`r`.`subjectid` = `st`.`subjectid` and `r`.`grade` = `st`.`grade` and `r`.`tyear` = `st`.`tyear` and `r`.`term` = `st`.`term`)) join `teacher` `t` on(`st`.`teacherid` = `t`.`teacherid`)) order by `r`.`grade`,concat(`s`.`surname`,', ',`s`.`firstname`),concat('[',lpad(`r`.`subjectid`,4,0),']',' ',`sb`.`title`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `gradessummaries`
--

/*!50001 DROP VIEW IF EXISTS `gradessummaries`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `gradessummaries` AS select row_number() over ( order by `registration`.`grade`) AS `ID`,`registration`.`grade` AS `grade`,`registration`.`tyear` AS `tyear`,`registration`.`term` AS `term`,avg(`registration`.`exammark`) AS `average` from `registration` group by `registration`.`grade`,`registration`.`tyear`,`registration`.`term` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26 12:02:03
