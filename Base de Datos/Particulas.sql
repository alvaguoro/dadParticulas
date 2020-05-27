-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: particulas
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `actuador`
--

DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuador` (
  `idActuador` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `idDevice` int NOT NULL,
  `ubicacion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idActuador`),
  KEY `actuador_device_idx` (`idDevice`),
  CONSTRAINT `actuador_device` FOREIGN KEY (`idDevice`) REFERENCES `device` (`idDevice`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuador`
--

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` VALUES (1,'Luz','Led',1,'Casa'),(2,'Alarma','Zumbador',2,'Trabajo'),(3,'Luasdz','Ledasd',1,'Caasdsa');
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actuador_alarma_value`
--

DROP TABLE IF EXISTS `actuador_alarma_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuador_alarma_value` (
  `idActuador_alarma_value` int NOT NULL AUTO_INCREMENT,
  `idActuador` int NOT NULL,
  `value` int NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`idActuador_alarma_value`),
  KEY `actuador_value_actuador_idx` (`idActuador`),
  CONSTRAINT `actuador_value_actuador` FOREIGN KEY (`idActuador`) REFERENCES `actuador` (`idActuador`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuador_alarma_value`
--

LOCK TABLES `actuador_alarma_value` WRITE;
/*!40000 ALTER TABLE `actuador_alarma_value` DISABLE KEYS */;
INSERT INTO `actuador_alarma_value` VALUES (1,1,1,120),(2,2,0,65754685),(3,2,32,212234235346);
/*!40000 ALTER TABLE `actuador_alarma_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actuador_lcd_value`
--

DROP TABLE IF EXISTS `actuador_lcd_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actuador_lcd_value` (
  `idActuador_LCD_value` int NOT NULL AUTO_INCREMENT,
  `idActuador` int NOT NULL,
  `mensaje` varchar(45) NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`idActuador_LCD_value`),
  KEY `actuador_LCD_value_sensor_idx` (`idActuador`),
  CONSTRAINT `actuador_LCD_value_sensor` FOREIGN KEY (`idActuador`) REFERENCES `actuador` (`idActuador`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actuador_lcd_value`
--

LOCK TABLES `actuador_lcd_value` WRITE;
/*!40000 ALTER TABLE `actuador_lcd_value` DISABLE KEYS */;
INSERT INTO `actuador_lcd_value` VALUES (1,2,'Este actuador ha sido actuado',302),(2,2,'12312323',123123123123);
/*!40000 ALTER TABLE `actuador_lcd_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device` (
  `idDevice` int NOT NULL AUTO_INCREMENT,
  `ip` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `idUsuario` int NOT NULL,
  `InitialTimestamp` bigint DEFAULT NULL,
  `ubicacion` varchar(45) NOT NULL,
  PRIMARY KEY (`idDevice`),
  KEY `device_usuario_idx` (`idUsuario`),
  CONSTRAINT `device_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` VALUES (1,'192.168.0.108','Casa',1,12512,'Peña Bética'),(2,'150.123.2.15','Hospital',2,1235465468,''),(3,'192.168.0.108','Cas2a',1,1123456789,'asdasdasd');
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor`
--

DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor` (
  `idSensor` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `idDevice` int NOT NULL,
  `ubicacion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idSensor`),
  KEY `sensor_device_idx` (`idDevice`),
  CONSTRAINT `sensor_device` FOREIGN KEY (`idDevice`) REFERENCES `device` (`idDevice`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor`
--

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` VALUES (1,'Temp y Hum','h11',1,'Casa'),(2,'Particulas','pm25',2,'Trabajo'),(3,'Tem123p y Hum','h121',2,'Casdasa');
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_dht11_value`
--

DROP TABLE IF EXISTS `sensor_dht11_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_dht11_value` (
  `idSensor_DHT11_value` int NOT NULL AUTO_INCREMENT,
  `idSensor` int NOT NULL,
  `Temperatura` float NOT NULL,
  `Humedad` float NOT NULL,
  `accuracy` float NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`idSensor_DHT11_value`),
  KEY `sensor_DHT11_value_sensor_idx` (`idSensor`),
  CONSTRAINT `sensor_DHT11_value_sensor` FOREIGN KEY (`idSensor`) REFERENCES `sensor` (`idSensor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_dht11_value`
--

LOCK TABLES `sensor_dht11_value` WRITE;
/*!40000 ALTER TABLE `sensor_dht11_value` DISABLE KEYS */;
INSERT INTO `sensor_dht11_value` VALUES (1,2,33,56,87,95780),(2,2,123,123,123,123);
/*!40000 ALTER TABLE `sensor_dht11_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensor_particula_value`
--

DROP TABLE IF EXISTS `sensor_particula_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensor_particula_value` (
  `idSensor_particula_value` int NOT NULL AUTO_INCREMENT,
  `idSensor` int NOT NULL,
  `Particulas_1` float NOT NULL,
  `Particulas_25` float NOT NULL,
  `Particulas_10` float NOT NULL,
  `accuracy` float NOT NULL,
  `timestamp` bigint DEFAULT NULL,
  PRIMARY KEY (`idSensor_particula_value`),
  KEY `sensor_particula_value_sensor_idx` (`idSensor`),
  CONSTRAINT `sensor_particula_value_sensor` FOREIGN KEY (`idSensor`) REFERENCES `sensor` (`idSensor`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensor_particula_value`
--

LOCK TABLES `sensor_particula_value` WRITE;
/*!40000 ALTER TABLE `sensor_particula_value` DISABLE KEYS */;
INSERT INTO `sensor_particula_value` VALUES (1,2,13000,23000,54000,100,3257),(2,2,132,223,524,21,123123123123);
/*!40000 ALTER TABLE `sensor_particula_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `dni` varchar(45) DEFAULT NULL,
  `fechaNacimiento` bigint DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'usu','Eduardo','Jimenez','45807502M',2154315354),(2,'gerente','Antonio','Puerto','46089703P',5646513354),(3,'usu','Alvaro','Aguayo','41284501A',6546357465),(4,'usasdu','Eduarasddo','Jimeasdnez','12345678',121212);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-15 18:09:28
