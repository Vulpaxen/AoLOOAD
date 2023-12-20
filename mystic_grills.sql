-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 20, 2023 at 05:04 PM
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

CREATE DATABASE IF NOT EXISTS mystic_grills;
USE mystic_grills;

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
(4, 'Chicken Steak (Single)', 'Delicious juicy fried chicken with gravy', 25),
(5, 'Chicken Steak (Double)', 'Delicious 2 pieces of juicy fried chicken with gravy', 48.5),
(6, 'Sirloin Steak (Single)', 'Cut of sirloin piece grilled to perfection', 50),
(7, 'Sirloin Steak (Double)', 'Twice the cut of sirloin piece grilled to perfection', 97.5),
(8, 'Chicken Cordon Bleu', 'Deep fried chicken cordon bleu with smoked ham and cheese', 35.5),
(9, 'French Fries', 'Cuts of potatoes deep fried until golden brown and crisp', 10),
(10, 'Rice', 'Aromatic fluffy jasmine rice', 5),
(11, 'Sweet Iced Tea', 'Homebrewed sweetened iced tea', 3.5);

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
(51, 4, 1),
(51, 6, 1),
(51, 9, 2),
(51, 10, 2),
(51, 11, 3),
(52, 4, 1),
(52, 9, 1),
(52, 11, 1),
(53, 9, 1),
(54, 10, 1),
(54, 7, 1),
(54, 11, 1),
(55, 5, 1),
(55, 7, 1),
(55, 11, 1),
(56, 8, 2),
(56, 9, 1),
(57, 11, 2),
(57, 10, 1),
(58, 5, 1),
(58, 11, 1),
(59, 5, 1),
(59, 11, 1),
(60, 9, 3),
(60, 7, 1),
(61, 10, 2),
(61, 11, 3),
(62, 5, 1),
(62, 6, 2),
(59, 4, 1),
(63, 6, 1),
(63, 11, 1),
(64, 9, 2),
(64, 10, 1),
(65, 5, 3),
(66, 5, 1),
(66, 10, 1),
(66, 7, 1),
(67, 9, 1),
(67, 11, 1),
(67, 5, 2),
(68, 7, 2);

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
(51, 1, 'Pending', '2023-12-20', 105.5),
(52, 1, 'Paid', '2023-12-20', 38.5),
(53, 1, 'Prepared', '2023-12-20', 10),
(54, 1, 'Served', '2023-12-20', 106),
(55, 8, 'Pending', '2023-12-20', 149.5),
(56, 8, 'Prepared', '2023-12-20', 81),
(57, 8, 'Paid', '2023-12-20', 12),
(58, 8, 'Paid', '2023-12-20', 52),
(59, 9, 'Paid', '2023-12-20', 57),
(60, 9, 'Prepared', '2023-12-20', 127.5),
(61, 9, 'Paid', '2023-12-20', 20.5),
(62, 9, 'Paid', '2023-12-20', 98.5),
(63, 8, 'Pending', '2023-12-20', 53.5),
(64, 9, 'Paid', '2023-12-20', 25),
(65, 9, 'Served', '2023-12-20', 145.5),
(66, 8, 'Served', '2023-12-20', 151),
(67, 8, 'Paid', '2023-12-20', 110.5),
(68, 8, 'Paid', '2023-12-20', 195);

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

--
-- Dumping data for table `receipt`
--

INSERT INTO `receipt` (`receiptId`, `orderId`, `receiptPaymentAmount`, `receiptPaymentDate`, `receiptPaymentType`) VALUES
(3, 59, 76, '2023-12-20', 'Cash'),
(4, 57, 12, '2023-12-20', 'Debit'),
(5, 61, 20.5, '2023-12-20', 'Credit'),
(11, 68, 195, '2023-12-20', 'Credit');

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
(1, 'Customer', 'Raien', 'customer@gmail.com', '123123'),
(2, 'Chef', 'Isna', 'chef@gmail.com', '123123'),
(3, 'Waiter', 'Budi', 'waiter@gmail.com', '123123'),
(4, 'Cashier', 'Yanto', 'cashier@gmail.com', '123123'),
(5, 'Admin', 'Admin', 'admin@gmail.com', '123123'),
(8, 'Customer', 'Adit', 'adit@gmail.com', 'adit123'),
(9, 'Customer', 'Kuncoro', 'kuncoro@gmail.com', 'kuncoro123');

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
  MODIFY `menuItemId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=69;

--
-- AUTO_INCREMENT for table `receipt`
--
ALTER TABLE `receipt`
  MODIFY `receiptId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `fk_menu_item_ids` FOREIGN KEY (`menuItemId`) REFERENCES `menuitem` (`menuItemId`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_order_id` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`) ON DELETE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK_userId` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `receipt`
--
ALTER TABLE `receipt`
  ADD CONSTRAINT `fk_order_ids` FOREIGN KEY (`orderId`) REFERENCES `orders` (`orderId`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
