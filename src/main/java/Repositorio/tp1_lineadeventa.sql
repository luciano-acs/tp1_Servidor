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
-- Table structure for table `lineadeventa`
--

DROP TABLE IF EXISTS `lineadeventa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lineadeventa` (
  `idLinea` int NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `Venta_idVenta` int DEFAULT NULL,
  `Producto_codProducto` int NOT NULL,
  `subtotal` double DEFAULT NULL,
  `precioU` double DEFAULT NULL,
  `Talle_idTalle` int DEFAULT NULL,
  `Color_idColor` int DEFAULT NULL,
  PRIMARY KEY (`idLinea`),
  KEY `fk_Linea de Venta_Producto1_idx` (`Producto_codProducto`),
  KEY `fk_LineaDeVenta_Talle1_idx` (`Talle_idTalle`),
  KEY `fk_LineaDeVenta_Color1_idx` (`Color_idColor`),
  CONSTRAINT `fk_Linea de Venta_Producto1` FOREIGN KEY (`Producto_codProducto`) REFERENCES `producto` (`codProducto`),
  CONSTRAINT `fk_LineaDeVenta_Color1` FOREIGN KEY (`Color_idColor`) REFERENCES `color` (`idColor`),
  CONSTRAINT `fk_LineaDeVenta_Talle1` FOREIGN KEY (`Talle_idTalle`) REFERENCES `talle` (`idTalle`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineadeventa`
--

LOCK TABLES `lineadeventa` WRITE;
/*!40000 ALTER TABLE `lineadeventa` DISABLE KEYS */;
INSERT INTO `lineadeventa` VALUES (1,2,1,1,28586.25,5717.25,10,1),(2,2,2,1,11434.5,5717.25,10,1),(3,4,3,1,22869,5717.25,10,2),(4,0,4,1,11434.5,5717.25,10,2),(5,1,5,1,5717.25,5717.25,10,1),(6,1,5,1,5717.25,5717.25,10,2);
/*!40000 ALTER TABLE `lineadeventa` ENABLE KEYS */;
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
