-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 19, 2023 at 03:34 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mystic_grills`
--

-- --------------------------------------------------------

--
-- Table structure for table `menuitem`
--

CREATE TABLE `menuitem` (
  `menuItemId` int(11) NOT NULL,
  `menuItemName` varchar(30) NOT NULL,
  `menuItemDescription` varchar(100) NOT NULL,
  `menuItemPrice` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `menuitem`
--

INSERT INTO `menuitem` (`menuItemId`, `menuItemName`, `menuItemDescription`, `menuItemPrice`) VALUES
(1, 'Ayam', 'enak', 200),
(2, 'bakso', 'enak', 1500);

-- --------------------------------------------------------

--
-- Table structure for table `orderitem`
--

CREATE TABLE `orderitem` (
  `orderId` int(11) NOT NULL,
  `menuItemId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderitem`
--

INSERT INTO `orderitem` (`orderId`, `menuItemId`, `quantity`) VALUES
(5, 2, 25),
(6, 2, 2),
(7, 2, 1),
(9, 1, 3),
(9, 2, 4),
(10, 1, 1),
(11, 1, 1),
(12, 2, 7),
(13, 1, 3),
(14, 1, 2),
(15, 1, 1),
(16, 2, 1),
(17, 2, 15),
(18, 1, 1),
(18, 2, 1),
(19, 2, 2),
(19, 1, 2),
(20, 2, 1),
(21, 2, 1),
(25, 1, 2),
(26, 2, 5),
(26, 1, 1),
(27, 2, 2),
(28, 2, 1),
(29, 1, 5),
(29, 2, 2),
(30, 1, 12),
(30, 2, 4),
(31, 1, 5),
(31, 2, 2),
(32, 2, 20),
(33, 2, 1),
(34, 2, 1),
(35, 2, 41),
(37, 2, 1),
(38, 1, 1),
(40, 2, 1),
(41, 1, 10),
(2, 1, 1),
(2, 1, 1),
(3, 1, 5),
(4, 1, 1),
(5, 1, 2),
(8, 2, 1),
(6, 1, 1),
(2, 2, 1),
(1, 1, 1),
(1, 2, 1),
(22, 1, 1),
(39, 2, 1),
(42, 2, 1),
(46, 1, 1),
(47, 1, 1),
(3, 2, 1),
(48, 1, 13),
(49, 1, 1),
(49, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `orderStatus` varchar(20) NOT NULL,
  `orderDate` date NOT NULL,
  `orderTotal` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderId`, `userId`, `orderStatus`, `orderDate`, `orderTotal`) VALUES
(1, 1, 'Pending', '2023-12-13', 0),
(2, 1, 'Pending', '2023-12-16', 0),
(3, 1, 'Pending', '2023-12-16', 0),
(4, 1, 'Pending', '2023-12-16', 0),
(5, 1, 'Pending', '2023-12-16', 0),
(6, 1, 'Prepared', '2023-12-16', 0),
(7, 1, 'Prepared', '2023-12-16', 0),
(8, 1, 'Served', '2023-12-16', 0),
(9, 1, 'Prepared', '2023-12-16', 0),
(10, 1, 'Pending', '2023-12-16', 0),
(11, 1, 'Pending', '2023-12-16', 0),
(12, 1, 'Pending', '2023-12-16', 0),
(13, 1, 'Pending', '2023-12-16', 0),
(14, 1, 'Prepared', '2023-12-16', 0),
(15, 1, 'Pending', '2023-12-16', 0),
(16, 1, 'Pending', '2023-12-16', 0),
(17, 1, 'Pending', '2023-12-16', 0),
(18, 1, 'Pending', '2023-12-16', 1700),
(19, 1, 'Pending', '2023-12-16', 3400),
(20, 1, 'Pending', '2023-12-16', 1500),
(21, 1, 'Pending', '2023-12-16', 1500),
(22, 1, 'Pending', '2023-12-16', 0),
(23, 1, 'Pending', '2023-12-16', 0),
(24, 1, 'Pending', '2023-12-16', 0),
(25, 1, 'Pending', '2023-12-16', 200),
(26, 1, 'Pending', '2023-12-16', 7700),
(27, 1, 'Pending', '2023-12-16', 3000),
(28, 1, 'Pending', '2023-12-16', 1500),
(29, 1, 'Pending', '2023-12-16', 16000),
(30, 1, 'Pending', '2023-12-16', 5200),
(31, 1, 'Pending', '2023-12-19', 4000),
(32, 1, 'Pending', '2023-12-19', 30000),
(33, 1, 'Pending', '2023-12-19', 1500),
(34, 1, 'Pending', '2023-12-19', 1500),
(35, 1, 'Pending', '2023-12-19', 61500),
(36, 1, 'Pending', '2023-12-19', 1500),
(37, 1, 'Pending', '2023-12-19', 1500),
(38, 1, 'Pending', '2023-12-19', 200),
(39, 1, 'Pending', '2023-12-19', 200),
(40, 1, 'Pending', '2023-12-19', 1500),
(41, 1, 'Pending', '2023-12-19', 200),
(42, 1, 'Pending', '2023-12-19', 1500),
(43, 1, 'Pending', '2023-12-19', 1500),
(44, 1, 'Pending', '2023-12-19', 1500),
(45, 1, 'Pending', '2023-12-19', 1500),
(46, 1, 'Pending', '2023-12-19', 200),
(47, 1, 'Pending', '2023-12-19', 200),
(48, 1, 'Pending', '2023-12-19', 2600),
(49, 1, 'Pending', '2023-12-19', 1700);

-- --------------------------------------------------------

--
-- Table structure for table `receipt`
--

CREATE TABLE `receipt` (
  `receiptId` int(11) NOT NULL,
  `orderId` int(11) NOT NULL,
  `receiptPaymentAmount` double NOT NULL,
  `receiptPaymentDate` date NOT NULL,
  `receiptPaymentType` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `userRole` varchar(10) NOT NULL,
  `userName` varchar(30) NOT NULL,
  `userEmail` varchar(30) NOT NULL,
  `userPassword` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `userRole`, `userName`, `userEmail`, `userPassword`) VALUES
(1, 'Customer', 'asd', 'cust', '123123'),
(2, 'Chef', 'def', 'chef', '123123'),
(3, 'Waiter', 'Waiter', 'wait', '123123'),
(4, 'Cashier', 'Cashier', 'cash', '123123'),
(5, 'Admin', 'Admin', 'adm', '123123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `menuitem`
--
ALTER TABLE `menuitem`
  ADD PRIMARY KEY (`menuItemId`);

--
-- Indexes for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD KEY `fk_menu_item_ids` (`menuItemId`),
  ADD KEY `orderId` (`orderId`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderId`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `receipt`
--
ALTER TABLE `receipt`
  ADD PRIMARY KEY (`receiptId`),
  ADD KEY `fk_order_ids` (`orderId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `menuitem`
--
ALTER TABLE `menuitem`
  MODIFY `menuItemId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT for table `receipt`
--
ALTER TABLE `receipt`
  MODIFY `receiptId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `fk_menu_item_ids` FOREIGN KEY (`menuItemId`) REFERENCES `menuitem` (`menuItemId`),
  ADD CONSTRAINT `fk_order_id` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK_userId` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `receipt`
--
ALTER TABLE `receipt`
  ADD CONSTRAINT `fk_order_ids` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
