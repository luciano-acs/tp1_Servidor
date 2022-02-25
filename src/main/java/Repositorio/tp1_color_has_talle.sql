-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tp1
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `color_has_talle`
--

DROP TABLE IF EXISTS `color_has_talle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `color_has_talle` (
  `Color_idColor` int NOT NULL,
  `Talle_idTalle` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  `Stock_idStock` int DEFAULT NULL,
  KEY `fk_Color_has_Talle_Talle1_idx` (`Talle_idTalle`),
  KEY `fk_Color_has_Talle_Color1_idx` (`Color_idColor`),
  KEY `fk_Color_has_Talle_Stock1_idx` (`Stock_idStock`),
  CONSTRAINT `fk_Color_has_Talle_Color1` FOREIGN KEY (`Color_idColor`) REFERENCES `color` (`idColor`),
  CONSTRAINT `fk_Color_has_Talle_Stock1` FOREIGN KEY (`Stock_idStock`) REFERENCES `stock` (`idStock`),
  CONSTRAINT `fk_Color_has_Talle_Talle1` FOREIGN KEY (`Talle_idTalle`) REFERENCES `talle` (`idTalle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `color_has_talle`
--

LOCK TABLES `color_has_talle` WRITE;
/*!40000 ALTER TABLE `color_has_talle` DISABLE KEYS */;
INSERT INTO `color_has_talle` VALUES (1,10,0,1),(2,10,2,1);
/*!40000 ALTER TABLE `color_has_talle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-06 22:28:35
