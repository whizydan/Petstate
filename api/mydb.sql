CREATE TABLE `responses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `MerchantRequestID` varchar(23) NOT NULL,
  `CheckoutRequestID` varchar(50) NOT NULL,
  `ResponseCode` varchar(50) NOT NULL,
  `ResponseDescription` varchar(256) NOT NULL,
  `amount` varchar(10) NOT NULL,
  `receiptnumber` varchar(20) NOT NULL,
  `balance` text,
  `date` varchar(15) NOT NULL,
  `phonenumber` varchar(13) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1